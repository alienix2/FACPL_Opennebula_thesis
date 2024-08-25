package entryPoint;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import utilities.StringBuilderLogHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class ApplyPolicyTest {

    @TempDir
    Path tempDir;

    private ApplyPolicy applyPolicy;
    private StringBuilderLogHandler logHandler;
    private Logger logger;

    @BeforeEach
    void setUp() throws IOException {
        logHandler = new StringBuilderLogHandler();
        logger = Logger.getLogger(ApplyPolicy.class.getName());
        logger.addHandler(logHandler);

        Path javaFilesDir = tempDir.resolve("javaFiles");
        Files.createDirectory(javaFilesDir);

        Path javaFile = javaFilesDir.resolve("ValidClass.java");
        String javaCode = "public class ValidClass { public static void main(String[] args) {} }";
        Files.write(javaFile, javaCode.getBytes());

        applyPolicy = new ApplyPolicy(logger, javaFilesDir.toString());
    }

    @Test
    void testExecuteSuccess() throws Exception {
        Path validFile = tempDir.resolve("testFile.txt");
        Files.createFile(validFile);

        applyPolicy.execute(new String[] { validFile.toString() });

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Policies applied, listening to requests!"),
                "Expected log message not found. Log output: " + logOutput);
    }

    @Test
    void testExecuteFailure() throws Exception {
        applyPolicy = new ApplyPolicy(logger, "invalidDir");
        assertThrows(Exception.class, () -> applyPolicy.execute(new String[] {"file"}));

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("An error occurred:"));
    }
}
