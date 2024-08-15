package opennnebula_api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.xml.sax.SAXException;

import it.unifi.facpl.lib.interfaces.IPepAction;

public abstract class ApiCallTemplate implements IPepAction{
	
	protected Client oneClient;
	protected List<OneResponse> responseList = new ArrayList<>();
	protected final Logger logger;
	protected VMsInfo vmsInfo;
    
    public ApiCallTemplate(Client oneClient, Logger logger) {
    	this.logger = logger;
    	this.oneClient = oneClient;
    }
    
    public void eval(List<Object> args) throws ClientConfigurationException, InterruptedException, SAXException, ParserConfigurationException, IOException {
    	setupVMs();
    	callApi(args);
    	logResponse();
	}
	
    private final void setupVMs() throws ClientConfigurationException {
    	vmsInfo = VMsInfo.withClientAndLogger(oneClient, logger);
    }
    
	public abstract void callApi(List<Object> args) throws InterruptedException, SAXException, ParserConfigurationException, IOException;

	private void logResponse() {
		responseList.forEach(res -> {
			if(res.isError()) {
				logger.severe(res.getErrorMessage());
			} 
			else{
				logger.info(res.getMessage());
			}
		});
		responseList.clear();
	}
}
