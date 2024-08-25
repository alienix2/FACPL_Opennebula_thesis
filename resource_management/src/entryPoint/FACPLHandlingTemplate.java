package entryPoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import utilities.CodeExecutor;
import utilities.FileLoggerFactory;
import utilities.OpenNebulaFACPLClassSetup;

public abstract class FACPLHandlingTemplate {

    protected Logger logger;
    protected CodeExecutor executor;
    protected String javaFilesDir;

    public FACPLHandlingTemplate(String logFilePath, String javaFilesDir) throws IOException {
        this.logger = FileLoggerFactory.make(logFilePath);
        this.executor = new CodeExecutor(javaFilesDir, logger);
        this.javaFilesDir = javaFilesDir;
    }
    
    public FACPLHandlingTemplate(Logger logger, String javaFilesDir) throws IOException {
        this.logger = logger;
        this.executor = new CodeExecutor(javaFilesDir, logger);
        this.javaFilesDir = javaFilesDir;
    }

    public final void execute(String[] args) throws Exception {
        try {
            List<String> fileLocations = Arrays.asList(args[0]);

            setup(fileLocations);
            compile();
            postProcess();

        } catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    protected void setup(List<String> fileLocations) throws Exception {
        OpenNebulaFACPLClassSetup setup = new OpenNebulaFACPLClassSetup(logger, fileLocations);
        setup.setup("opennebula_context_actions", javaFilesDir);
    }

    protected void compile() throws Exception {
        boolean success = executor.compileJavaFiles();
        if (success) {
            logger.info("Compilation successful.");
        } else {
            logger.severe("Compilation failed.");
            throw new RuntimeException("Compilation failed");
        }
    }

    protected abstract void postProcess() throws Exception; // This will be implemented by subclasses
}

