package utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class OpenNebulaFACPLClassSetupTest {

    @TempDir
    Path tempDir;

    private Logger logger;
    private OpenNebulaFACPLClassSetup setup;
    private StringBuilderLogHandler logHandler;
    private FileMerger fileMerger;

    @BeforeEach
    void setUp() throws IOException {
        logHandler = new StringBuilderLogHandler();
        logger = Logger.getLogger(OpenNebulaFACPLClassSetup.class.getName());
        logger.addHandler(logHandler);

        Path mockInputFile = tempDir.resolve("mockInputFile.fpl");
        Files.createFile(mockInputFile);

        fileMerger = new MockFileMerger(mockInputFile.toString());
        OpenNebulaFACPLClassGenerator classGenerator = new OpenNebulaFACPLClassGenerator(fileMerger, logger);

        setup = new OpenNebulaFACPLClassSetup(logger, classGenerator);
    }

    @Test
    void testSetupSuccess() throws IOException {
        Path outputFolder = tempDir.resolve("output");
        Path additionalFilesFolder = tempDir.resolve("additional");

        Files.createDirectory(outputFolder);
        Files.createDirectory(additionalFilesFolder);

        setup.setup(additionalFilesFolder.toString(), outputFolder.toString());
        
        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Class generation completed successfully."));
        assertTrue(logOutput.contains("Folder contents moved successfully."));
    }

    @Test
    void testSetupFailure_ClassGeneration() throws IOException {
        MockFileMerger faultyFileMerger = new MockFileMerger("non_existent_file.fpl");
        OpenNebulaFACPLClassGenerator faultyGenerator = new OpenNebulaFACPLClassGenerator(faultyFileMerger, logger);
        OpenNebulaFACPLClassSetup faultySetup = new OpenNebulaFACPLClassSetup(logger, faultyGenerator);

        Path outputFolder = tempDir.resolve("output");
        Files.createDirectory(outputFolder);

        faultySetup.setup(tempDir.resolve("additional").toString(), outputFolder.toString());

        String logOutput = logHandler.getLogBuilder();
        
        assertTrue(logOutput.contains("Failed to generate classes:"));
        assertFalse(logOutput.contains("Folder contents moved successfully."));
    }
}