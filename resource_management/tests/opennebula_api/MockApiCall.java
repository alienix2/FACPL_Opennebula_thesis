package opennebula_api;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.xml.sax.SAXException;

import opennebula_api.ApiCallTemplate;

public class MockApiCall extends ApiCallTemplate {
	
	public MockApiCall(Client oneClient, Logger logger) {
		super(oneClient, logger);
	}

	@Override
	public void callApi(List<Object> args)
			throws InterruptedException, SAXException, ParserConfigurationException {
		responseList.add(new OneResponse(true, "Success message"));
        responseList.add(new OneResponse(false, "Error message"));
	}
}
