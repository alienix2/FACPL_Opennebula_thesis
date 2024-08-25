package opennebula_api;

import java.util.logging.Logger;
import org.opennebula.client.Client;

public class MockOpenNebulaActionContext extends OpenNebulaActionContext {

    private Logger logger;
    private Client client;
    private VMsInfo vmsInfo; // Add this field to store the mock VMsInfo object

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
        return vmsInfo; // Return the mock VMsInfo object
    }

    // Add a setter to inject the mock VMsInfo object
    public void setVMsInfo(VMsInfo vmsInfo) {
        this.vmsInfo = vmsInfo;
    }
}
