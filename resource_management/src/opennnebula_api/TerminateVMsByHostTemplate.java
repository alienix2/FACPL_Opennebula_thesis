package opennnebula_api;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.vm.VirtualMachine;
import org.xml.sax.SAXException;

public class TerminateVMsByHostTemplate extends ApiCallTemplate {

	public TerminateVMsByHostTemplate(String credentials, String endpoint, Logger logger) {
		super(credentials, endpoint, logger);
	}

	@Override
	public void callApi(List<Object> args) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		logger.info("Terminating " + args.get(2) + " VMs of [host, temp]: " + "[" + args.get(0) + " " + args.get(1) + "]");
		List<VirtualMachine> terminateList = VMsInfo
				.getVMsByHostTemplate((String)args.get(0), 
										(String)args.get(1), 
										(int)args.get(2));
		terminateList.forEach(x -> x.toString());
		terminateList.forEach(vm -> responseList.add(vm.terminate()));
	}
}
