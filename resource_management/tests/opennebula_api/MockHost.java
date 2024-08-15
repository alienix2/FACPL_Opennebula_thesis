package opennebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;

public class MockHost extends Host {
	
	private final String mockResponse;

    public MockHost(int id, Client client, String mockResponse) {
        super(id, client);
        this.mockResponse = mockResponse;
    }

    @Override
    public OneResponse info() {
        return new OneResponse(false, mockResponse);
    }
}