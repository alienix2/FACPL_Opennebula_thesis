package utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileLoggerFactoryTest {

    @TempDir
    Path tempDir;

    private Path logFilePath;

    @BeforeEach
    void setUp() throws IOException {
        logFilePath = tempDir.resolve("test.log");
        Files.deleteIfExists(logFilePath);
    }

    @Test
    void testLoggerCreationAndLogging() throws IOException {
        Logger logger = FileLoggerFactory.make(logFilePath.toString());

        logger.info("Test log message");
        closeHandlers(logger);

        assertTrue(Files.exists(logFilePath));

        String logContent = new String(Files.readAllBytes(logFilePath), StandardCharsets.UTF_8);
        assertTrue(logContent.contains("Test log message"));
    }

    @Test
    void testCustomFormatterAndLevel() throws IOException {
        Logger logger = FileLoggerFactory.make(logFilePath.toString(), new SimpleFormatter(), Level.WARNING);

        logger.info("This should not be logged");
        logger.warning("This should be logged");
        closeHandlers(logger);

        String logContent = new String(Files.readAllBytes(logFilePath), StandardCharsets.UTF_8);

        assertFalse(logContent.contains("This should not be logged"));
        assertTrue(logContent.contains("This should be logged"));
    }

    @Test
    void testIOExceptionHandling() throws IOException {
        Path invalidDirPath = tempDir.resolve("invalid_dir");
        Files.createDirectory(invalidDirPath);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Logger logger = FileLoggerFactory.make(invalidDirPath.toString());
            logger.info("This should fail");
        });

        assertTrue(exception.getMessage().contains("Failed to initialize logger handler"));
    }

    private void closeHandlers(Logger logger) {
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
            handler.close();
        }
    }
}
