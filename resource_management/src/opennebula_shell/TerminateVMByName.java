package opennebula_shell;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class TerminateVMByName extends ShellCommandTemplate {

	public TerminateVMByName(Logger logger) {
		super(logger);
	}

	public void runCommand(List<Object> args) throws IOException, InterruptedException {	
		logger.info("Terminating VM with name: " + args.get(1) + " from host: " + args.get(0));
		String vmId = VMsInfo.getRunningVMIdByName((String)args.get(1));
		
		process = new ProcessBuilder()
				.command("bash", "-c", "onevm terminate " + vmId ).start();
		
	}
}
