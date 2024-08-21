package opennebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;

public class MockClientFalse extends Client{
	
	private String callNumber;
	
	public MockClientFalse(String callNumber) throws ClientConfigurationException {
		super(null, null);
		this.callNumber = callNumber;
    }
	
	@Override
	public OneResponse call(String action, Object... args) {
		return new OneResponse(false, callNumber);
	}
}
