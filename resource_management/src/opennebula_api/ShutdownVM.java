package opennebula_api;

import java.util.List;
import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;

public class ShutdownVM extends ApiCallTemplate {

	public ShutdownVM(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) {
		logger.info("Shutting down 1 VM of [host, temp]: " + "[" + args.get(0) + " " + args.get(1) + "]");
		List<VirtualMachine> terminateList = 
					VMsInfo.withClientAndLogger(oneClient, logger)
					.getRunningVMsByHostTemplate((String)args.get(0), 
												(String)args.get(2));
		terminateList.forEach(vm -> responseList.add(vm.poweroff()));
	}
}
