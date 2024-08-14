package opennebula_shell;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SuspendVM extends ShellCommandTemplate {

	public SuspendVM(Logger logger) {
		super(logger);
	}

	public void runCommand(List<Object> args) throws IOException, InterruptedException {
		String suspendList = VMsInfo.getVMsIdByHostTemplate((String)args.get(0), (String)args.get(2), (int)args.get(1));
		
		logger.info("Suspending VM(s): " + "[" + suspendList + "]");
		process = new ProcessBuilder()
				.command("bash", "-c", "onevm suspend \"" + suspendList + "\"").start();
		
	}
}
