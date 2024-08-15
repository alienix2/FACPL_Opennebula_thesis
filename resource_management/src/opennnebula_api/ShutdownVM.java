package opennnebula_api;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;
import org.xml.sax.SAXException;

public class ShutdownVM extends ApiCallTemplate {

	public ShutdownVM(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) throws InterruptedException, SAXException, ParserConfigurationException, IOException {
		logger.info("Shutting down " + args.get(2) + " VMs of [host, temp]: " + "[" + args.get(0) + " " + args.get(1) + "]");
		List<VirtualMachine> terminateList = 
					VMsInfo.withClientAndLogger(oneClient, logger)
					.getRunningVMsByHostTemplate((String)args.get(0), 
												(String)args.get(1), 
												(int)args.get(2));
		terminateList.forEach(x -> x.toString());
		terminateList.forEach(vm -> responseList.add(vm.poweroff()));
	}
}
