package opennnebula_api;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.xml.sax.SAXException;

public class TerminateVMByName extends ApiCallTemplate {

	public TerminateVMByName(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
		logger.info("Terminating VM with name: " + args.get(1) + "from host: " + args.get(0));
		responseList.add(VMsInfo.withClientAndLogger(oneClient, logger)
				.getRunningVMByName((String)args.get(1)).terminate());
	}
}
