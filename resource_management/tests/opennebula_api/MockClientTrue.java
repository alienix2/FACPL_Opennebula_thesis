package opennebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;

public class MockClientTrue extends Client{
	
	private String callNumber;
	
	public MockClientTrue(String callNumber) throws ClientConfigurationException {
		super(null, null);
		this.callNumber = callNumber;
    }
	
	@Override
	public OneResponse call(String action, Object... args) {
		System.out.println("called");
		return new OneResponse(true, callNumber);
	}
}
