package utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLoggerFactory {
	
	public static Logger make(String fileName) {
		Throwable t = new Throwable();
		StackTraceElement directCaller = t.getStackTrace()[1];
		Logger logger = Logger.getLogger(directCaller.getClassName());
		try {
            FileHandler fileHandler = new FileHandler(fileName, true);
            fileHandler.setFormatter(new CreatorClassFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger handler.", e);
        }
		return logger;
	}
}
