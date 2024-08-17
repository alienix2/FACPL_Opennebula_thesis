package opennebula_api;

import java.util.List;
import java.util.logging.Logger;

import org.opennebula.client.Client;

public class ReleaseVM extends ApiCallTemplate {

	public ReleaseVM(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) {
		logger.info("Shutting down (releasing) the VM: " + args.get(1));
		responseList.add(VMsInfo.withClientAndLogger(oneClient, logger)
					.getRunningVMByName((String)args.get(1)).poweroff());
	}
}
