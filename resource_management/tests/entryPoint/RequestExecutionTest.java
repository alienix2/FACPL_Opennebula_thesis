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

public class RequestExecutionTest {

    @TempDir
    Path tempDir;

    private RequestExecution requestExecution;
    private StringBuilderLogHandler logHandler;
    private Logger logger;

    @BeforeEach
    void setUp() throws IOException {
        logHandler = new StringBuilderLogHandler();
        logger = Logger.getLogger(RequestExecution.class.getName());
        logger.addHandler(logHandler);

        Path javaFilesDir = tempDir.resolve("javaFiles");
        Files.createDirectory(javaFilesDir);

        Path javaFile = javaFilesDir.resolve("MainFACPL.java");
        String javaCode = "public class MainFACPL { public static void main(String[] args) {} }";
        Files.write(javaFile, javaCode.getBytes());

        requestExecution = new RequestExecution(logger, javaFilesDir.toString());
    }

    @Test
    void testExecuteRequestSuccess() throws Exception {
        Path validFile = tempDir.resolve("testFile.txt");
        Files.createFile(validFile);

        requestExecution.execute(new String[] { validFile.toString() });

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Executing the compiled class for request handling..."));
        assertTrue(logOutput.contains("Request execution completed."));
    }

    @Test
    void testExecuteRequestFailure() throws Exception {
        requestExecution = new RequestExecution(logger, "invalidDir");
        assertThrows(Exception.class, () -> requestExecution.execute(new String[] {"file"}));

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("An error occurred:"));
    }
}

