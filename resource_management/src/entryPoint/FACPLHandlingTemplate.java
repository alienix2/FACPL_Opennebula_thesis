package entryPoint;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration2.builder.fluent.Configurations;

import utilities.ClassSetup;
import utilities.CodeExecutorInterface;
import utilities.FileLoggerFactory;

public abstract class FACPLHandlingTemplate {

	private final String CONFIG_FILE = "config.properties";
    protected Logger logger;
    protected CodeExecutorInterface executor;
    protected String javaFilesDir;
    protected ClassSetup setupper;

    public FACPLHandlingTemplate(String logFilePath, String javaFilesDir) throws IOException {
        this.logger = FileLoggerFactory.make(logFilePath);
        this.javaFilesDir = javaFilesDir;
    }
    
    public FACPLHandlingTemplate(Logger logger, String javaFilesDir) throws IOException {
        this.logger = logger;
        this.javaFilesDir = javaFilesDir;
    }

    public final void execute(String[] args) throws Exception {
        try {
            List<String> fileLocations = Arrays.asList(args[0]);
            initializeConcreteSetupperExecutor(fileLocations);
            setup();
            compile();
            postProcess();
        } catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
            throw e;
        }
    }
    
    protected abstract void initializeConcreteSetupperExecutor(List<String> fileLocations) throws Exception;

    protected void setup() throws Exception {
        setupper.setup(new Configurations().properties(CONFIG_FILE).getString("context.file.location"), javaFilesDir);
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

    protected abstract void postProcess() throws Exception;
}