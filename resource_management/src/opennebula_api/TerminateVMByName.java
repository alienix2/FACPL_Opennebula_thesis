package opennebula_api;

import java.util.List;
import java.util.logging.Logger;

import org.opennebula.client.Client;

public class TerminateVMByName extends ApiCallTemplate {

	public TerminateVMByName(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) {
		logger.info("Terminating VM with name: " + args.get(1) + "from host: " + args.get(0));
		responseList.add(VMsInfo.withClientAndLogger(oneClient, logger)
				.getRunningVMByName((String)args.get(1)).terminate());
	}
}
