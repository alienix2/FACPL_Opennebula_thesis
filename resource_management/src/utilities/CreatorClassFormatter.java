package utilities;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CreatorClassFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        // Format to include method name and other details
        return String.format("%s %s.%s: %s%n",
            record.getLevel(),
            //record.getLoggerName(),
            record.getSourceClassName(),
            record.getSourceMethodName(),
            record.getMessage()
        );
    }
}
