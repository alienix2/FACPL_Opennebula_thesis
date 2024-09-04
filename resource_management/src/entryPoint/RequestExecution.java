package entryPoint;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import utilities.BaseCodeExecutor;
import utilities.OpenNebulaFACPLClassSetup;

public class RequestExecution extends FACPLHandlingTemplate {

    private static final String COMPILED_CLASS = "MainFACPL";

    public RequestExecution(String logFilePath, String javaFilesDir) throws IOException {
        super(logFilePath, javaFilesDir);
    }
    
    public RequestExecution(Logger logger, String javaFilesDir) throws IOException {
        super(logger, javaFilesDir);
    }

	@Override
	protected void initializeConcreteSetupperExecutor(List<String> fileLocations) throws Exception {
		executor = new BaseCodeExecutor(javaFilesDir, logger);
		setupper = new OpenNebulaFACPLClassSetup(logger, fileLocations);
	}
	
    @Override
    protected void postProcess() throws Exception {
        logger.info("Executing the compiled class for request handling...");
        executor.runCompiledClass(COMPILED_CLASS, javaFilesDir);
        logger.info("Request execution completed.");
    }
}
