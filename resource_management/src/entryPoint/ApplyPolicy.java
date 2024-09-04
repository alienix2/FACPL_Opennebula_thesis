package entryPoint;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import utilities.BaseCodeExecutor;
import utilities.OpenNebulaFACPLClassSetup;

public class ApplyPolicy extends FACPLHandlingTemplate {

    public ApplyPolicy(String logFilePath, String javaFilesDir) throws IOException {
        super(logFilePath, javaFilesDir);
    }
    
    public ApplyPolicy(Logger logger, String javaFilesDir) throws IOException {
        super(logger, javaFilesDir);
    }
    
	@Override
	protected void initializeConcreteSetupperExecutor(List<String> fileLocations) throws Exception {
		executor = new BaseCodeExecutor(javaFilesDir, logger);
		setupper = new OpenNebulaFACPLClassSetup(logger, fileLocations);
	}

    @Override
    protected void postProcess() {
        logger.info("Policies applied, listening to requests!");
    }
}
