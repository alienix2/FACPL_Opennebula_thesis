package utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileLoggerFactoryTest {

	private static final String LOG_FILE = "test.log";

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(LOG_FILE));
    }

    @Test
    void testLoggerCreationAndLogging() throws IOException {
        Logger logger = FileLoggerFactory.make(LOG_FILE);

        logger.info("Test log message");
        closeHandlers(logger);

        Path logFilePath = Paths.get(LOG_FILE);
        assertTrue(Files.exists(logFilePath));

        String logContent = new String(Files.readAllBytes(logFilePath), StandardCharsets.UTF_8);
        assertTrue(logContent.contains("Test log message"));
    }

    @Test
    void testCustomFormatterAndLevel() throws IOException {
        Logger logger = FileLoggerFactory.make(LOG_FILE, new SimpleFormatter(), Level.WARNING);

        logger.info("This should not be logged");
        logger.warning("This should be logged");
        closeHandlers(logger);

        Path logFilePath = Paths.get(LOG_FILE);
        String logContent = new String(Files.readAllBytes(logFilePath), StandardCharsets.UTF_8);

        assertFalse(logContent.contains("This should not be logged"));
        assertTrue(logContent.contains("This should be logged"));
    }

    private void closeHandlers(Logger logger) {
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
            handler.close();
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(LOG_FILE));
    }

}
