package entryPoint;

import java.io.IOException;

public class ApplyPolicy extends FACPLHandlingTemplate {

    public ApplyPolicy(String logFilePath, String javaFilesDir) throws IOException {
        super(logFilePath, javaFilesDir);
    }

    @Override
    protected void postProcess() {
        logger.info("Policies applied, listening to requests!");
    }
}
