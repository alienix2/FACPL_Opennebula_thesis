package opennebula_api;

import java.util.logging.Logger;

import org.opennebula.client.Client;

import opennebula_api.OpenNebulaActionContext;
import opennebula_api.VMsInfo;

public class MockOpenNebulaActionContext extends OpenNebulaActionContext {
	
    private Logger logger;
    private Client client;

    public MockOpenNebulaActionContext(Client client, Logger logger) {
        super(client, logger);
        this.client = client;
        this.logger = logger;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public Client getClient() {
    	return client;
    }

    @Override
    public VMsInfo getVMsInfo() {
        return null; // Return a mock or stub implementation if needed.
    }
}