package entryPoint;

import java.io.IOException;
import java.util.logging.Logger;

public class ApplyPolicy extends FACPLHandlingTemplate {

    public ApplyPolicy(String logFilePath, String javaFilesDir) throws IOException {
        super(logFilePath, javaFilesDir);
    }
    
    public ApplyPolicy(Logger logger, String javaFilesDir) throws IOException {
        super(logger, javaFilesDir);
    }

    @Override
    protected void postProcess() {
        logger.info("Policies applied, listening to requests!");
    }
}
