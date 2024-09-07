package utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CodeExecutorTest {

    @TempDir
    Path tempDir;

    private Logger logger;
    private BaseCodeExecutor codeExecutor;
    private StringBuilderLogHandler logHandler;

    @BeforeEach
    void setUp() {
        logHandler = new StringBuilderLogHandler();
        logger = Logger.getLogger(BaseCodeExecutor.class.getName());
        logger.addHandler(logHandler);
        codeExecutor = new BaseCodeExecutor(tempDir.toString(), logger);
    }

    @Test
    void testCompileJavaFilesSuccess() throws IOException {
        Path javaFile = tempDir.resolve("HelloWorld.java");
        String javaCode = "public class HelloWorld { "
                        + "public static void main(String[] args) { "
                        + "System.out.println(\"Hello, World!\"); "
                        + "} "
                        + "}";
        Files.write(javaFile, javaCode.getBytes());

        assertDoesNotThrow(() -> codeExecutor.compileJavaFiles());

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Compilation successful."));
    }

    @Test
    void testCompileJavaFilesFailure() throws IOException {
        Path javaFile = tempDir.resolve("FaultyClass.java");
        String faultyCode = "public class FaultyClass { "
                            + "public static void main(String[] args) { "
                            + "System.out.println(\"Hello, World!\") "  // Incorrect code
                            + "} ";
        Files.write(javaFile, faultyCode.getBytes());

        assertThrows(RuntimeException.class, () -> codeExecutor.compileJavaFiles());

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Compilation failed with result code:"));
    }

    @Test
    void testRunCompiledClassSuccess() throws Exception {
        Path javaFile = tempDir.resolve("HelloWorld.java");
        String javaCode = "public class HelloWorld { "
                        + "public static void main(String[] args) { "
                        + "System.out.println(\"Hello, World!\"); "
                        + "} "
                        + "}";
        Files.write(javaFile, javaCode.getBytes());

        assertDoesNotThrow(() -> codeExecutor.compileJavaFiles());
        assertDoesNotThrow(() -> codeExecutor.runCompiledClass("HelloWorld", tempDir.toString()));

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Successfully ran class: HelloWorld"));
    }

    @Test
    void testRunCompiledClassFailure() throws Exception {
        assertThrows(Exception.class, () -> codeExecutor.runCompiledClass("NonExistentClass", tempDir.toString()));
        
        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("Error loading or finding main method for class: NonExistentClassNonExistentClass"));
    }
}