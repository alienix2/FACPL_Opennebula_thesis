package opennebula_shell;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ShutdownVM extends ShellCommandTemplate {

	public ShutdownVM(Logger logger) {
		super(logger);
	}
	
	public void runCommand(List<Object> args) throws IOException, InterruptedException {
		String shutdownList = VMsInfo.getVMsInfoByHostTemplate((String)args.get(0), (String)args.get(1), (int)args.get(2));
		
		logger.info("Shutting down VM(s): " + "[" + shutdownList + "]");
		process = new ProcessBuilder()
				.command("bash", "-c", "onevm shutdown \"" + shutdownList + "\"").start();
		
	}
}
