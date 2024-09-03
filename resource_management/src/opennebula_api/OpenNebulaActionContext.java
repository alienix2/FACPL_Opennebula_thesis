package opennebula_api;

import java.util.logging.Logger;

import org.opennebula.client.Client;

public class OpenNebulaActionContext {	
	private final Client oneClient;
    private final Logger logger;
    private final VMsInfo vmsInfo;

    public OpenNebulaActionContext(Client oneClient, Logger logger) {
        this.oneClient = oneClient;
        this.logger = logger;
        this.vmsInfo = VMsInfo.withCustomLogger(new OpenNebulaVMService(oneClient), logger);
    }
    
    public OpenNebulaActionContext(Client oneClient) {
        this.oneClient = oneClient;
        logger = Logger.getLogger(VMsInfo.class.getName());
        this.vmsInfo = VMsInfo.withDefaultLogger(new OpenNebulaVMService(oneClient));
    }

    public Client getClient() {
        return oneClient;
    }

    public Logger getLogger() {
        return logger;
    }

    public VMsInfo getVMsInfo() {
        return vmsInfo;
    }
}
