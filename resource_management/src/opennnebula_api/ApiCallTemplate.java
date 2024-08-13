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
    private final String credentials;
    private final String endpoint;
    
    public ApiCallTemplate(String credentials, String endpoint, Logger logger) {
    	this.credentials = credentials;
    	this.endpoint = endpoint;
    	this.logger = logger;
    }
    
    public void eval(List<Object> args) throws Throwable {
    	setupHost();
    	callApi(args);
    	logResponse();
	}
	
    private final void setupHost() throws ClientConfigurationException {
    	oneClient = new Client(credentials, endpoint);
    }
    
	public abstract void callApi(List<Object> args) throws IOException, InterruptedException, SAXException, ParserConfigurationException;

	public void logResponse() {
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
