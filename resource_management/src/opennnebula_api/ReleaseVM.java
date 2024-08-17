package opennnebula_api;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;
import org.xml.sax.SAXException;

public class ReleaseVM extends ApiCallTemplate {

	public ReleaseVM(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) throws InterruptedException, SAXException, ParserConfigurationException, IOException {
		logger.info("Shutting down (releasing) the VM: " + args.get(1));
		responseList.add(VMsInfo.withClientAndLogger(oneClient, logger)
					.getRunningVMByName((String)args.get(1)).poweroff());
	}
}
