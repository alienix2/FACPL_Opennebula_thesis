package utilities;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class StringBuilderLogHandler extends Handler {
	
	private final StringBuilder logBuilder = new StringBuilder();
	
	@Override
    public void publish(LogRecord record) {
        if (record.getLevel() == Level.SEVERE) {
            logBuilder.append("SEVERE: ");
        } else if (record.getLevel() == Level.WARNING) {
            logBuilder.append("WARNING: ");
        } else if (record.getLevel() == Level.INFO) {
            logBuilder.append("INFO: ");
        } else {
            logBuilder.append("DEBUG: ");
        }
        logBuilder.append(record.getMessage()).append("\n");
    }

	public String getLogBuilder() {
		return logBuilder.toString();
	}

	@Override
	public void close() throws SecurityException {}

	@Override
	public void flush() {}
}
