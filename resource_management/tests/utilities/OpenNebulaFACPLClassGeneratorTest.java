package utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class OpenNebulaFACPLClassGeneratorTest {

    @TempDir
    Path tempDir;

    private StringBuilderLogHandler logHandler;
    private OpenNebulaFACPLClassGenerator generator;
    private MockFileMerger fileMerger;

    @BeforeEach
    void setUp() throws IOException {
        logHandler = new StringBuilderLogHandler();
        Logger logger = Logger.getLogger(OpenNebulaFACPLClassGenerator.class.getName());
        logger.addHandler(logHandler);
        logger.setLevel(Level.ALL); // Ensure all levels are captured

        Path mockInputFile = tempDir.resolve("mockInputFile.fpl");
        Files.createFile(mockInputFile);

        fileMerger = new MockFileMerger(mockInputFile.toString());
        generator = new OpenNebulaFACPLClassGenerator(fileMerger, logger);
    }

    @Test
    void testGenerateClassesSuccess() throws Exception {
        Path outputFolder = tempDir.resolve("output");

        Files.createDirectory(outputFolder);

        generator.generateClasses(outputFolder.toString());

        assertTrue(Files.exists(outputFolder));
        assertTrue(logHandler.getLogBuilder().contains("FACPL class generation completed."));
    }

    @Test
    void testGenerateClassesFailure() throws Exception {
        Path invalidInputFile = tempDir.resolve("invalidInputFile.fpl");
        fileMerger = new MockFileMerger(invalidInputFile.toString());
        generator = new OpenNebulaFACPLClassGenerator(fileMerger, Logger.getLogger(OpenNebulaFACPLClassGenerator.class.getName()));

        Exception exception = assertThrows(Exception.class, () -> {
            generator.generateClasses(tempDir.toString());
        });

        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("java.io.FileNotFoundException:"));
        assertTrue(logHandler.getLogBuilder().contains("java.io.FileNotFoundException:"));
    }
}