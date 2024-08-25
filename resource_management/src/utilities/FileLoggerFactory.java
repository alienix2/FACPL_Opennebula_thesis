package utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLoggerFactory {

	public static Logger make(String fileName) {
        return make(fileName, new CreatorClassFormatter(), Level.ALL);
    }

    public static Logger make(String fileName, Formatter formatter, Level level) {
        Throwable t = new Throwable();
        StackTraceElement directCaller = t.getStackTrace()[1];
        String loggerName = directCaller.getClassName() + "-" + fileName;

        // Create or retrieve the logger by name
        Logger logger = Logger.getLogger(loggerName);

        // Avoid adding multiple handlers to the same logger
        if (!isFileHandlerAttached(logger, fileName)) {
            try {
                FileHandler fileHandler = createFileHandler(fileName, formatter, level);
                logger.addHandler(fileHandler);
                logger.setUseParentHandlers(false);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize logger handler.", e);
            }
        }

        return logger;
    }

    private static FileHandler createFileHandler(String fileName, Formatter formatter, Level level) throws IOException {
        FileHandler fileHandler = new FileHandler(fileName, true);
        fileHandler.setFormatter(formatter);
        fileHandler.setLevel(level);
        return fileHandler;
    }

    private static boolean isFileHandlerAttached(Logger logger, String fileName) {
        for (Handler handler : logger.getHandlers()) {
            if (handler instanceof FileHandler) {
                return true;
            }
        }
        return false;
    }
}
