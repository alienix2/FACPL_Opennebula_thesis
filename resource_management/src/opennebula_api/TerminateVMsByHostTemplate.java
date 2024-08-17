package opennebula_api;

import java.util.List;
import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;

public class TerminateVMsByHostTemplate extends ApiCallTemplate {

	public TerminateVMsByHostTemplate(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) {
		logger.info("Terminating 1 VM of [host, temp]: " + "[" + args.get(0) + " " + args.get(1) + "]");
		List<VirtualMachine> terminateList = vmsInfo
				.getRunningVMsByHostTemplate((String)args.get(0), 
											(String)args.get(1));
		terminateList.forEach(x -> x.toString());
		terminateList.forEach(vm -> responseList.add(vm.terminate()));
	}
}
