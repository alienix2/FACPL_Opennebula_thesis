package opennebula_shell;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import it.unifi.facpl.lib.interfaces.IPepAction;

public abstract class ShellCommandTemplate implements IPepAction{
	
	protected final Logger logger;
    private final ProcessHandler processHandler;
    protected Process process;
    
    public ShellCommandTemplate(Logger logger) {
    	this.logger = logger;
    	processHandler = new ProcessHandler(logger);
    }
    
    public void eval(List<Object> args) throws Throwable {
		runCommand(args);
		logCommand();
	}
	
	public abstract void runCommand(List<Object> args) throws IOException, InterruptedException;
	
	private final void logCommand() throws IOException, InterruptedException{
		processHandler.handle(process);
		int exitCode = process.waitFor();
        logger.info("Exit code: " + exitCode);
	}
}
