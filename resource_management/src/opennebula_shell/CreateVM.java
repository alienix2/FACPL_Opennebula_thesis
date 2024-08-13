package opennebula_shell;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CreateVM extends ShellCommandTemplate {

	public CreateVM(Logger logger) {
		super(logger);
	}

	public void runCommand(List<Object> args) throws IOException {
		logger.info("Starting VM: " + "[" + args.get(2) + ", " + args.get(1) + "]");
		process = new ProcessBuilder().command("bash", "-c", 
				"VM_ID=$(onetemplate instantiate " + args.get(2) + 
				" --name " + args.get(1) + 
				" --hold | cut -d' ' -f3) && onevm deploy $VM_ID " 
				+ args.get(0)).start();
	}
}
