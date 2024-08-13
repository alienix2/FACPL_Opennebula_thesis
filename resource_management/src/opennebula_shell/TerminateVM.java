package opennebula_shell;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class TerminateVM extends ShellCommandTemplate {

	public TerminateVM(Logger logger) {
		super(logger);
	}

	public void runCommand(List<Object> args) throws IOException, InterruptedException {	
		String stopList = VMsInfo.getVMsInfoByHostTemplate((String)args.get(0), (String)args.get(1), (int)args.get(2));
		
		logger.info("Terminating VM(s): " + "[" + stopList + "]");
		process = new ProcessBuilder()
				.command("bash", "-c", "onevm terminate \"" + stopList + "\"").start();
		
	}
}
