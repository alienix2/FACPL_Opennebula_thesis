package opennebula_api;
import java.util.List;

import org.opennebula.client.OneResponse;

import it.unifi.facpl.lib.interfaces.IPepAction;

public abstract class OpenNebulaActionBase implements IPepAction{
    protected final OpenNebulaActionContext ONActionContext;

    public OpenNebulaActionBase(OpenNebulaActionContext ONActionContext) {
        this.ONActionContext = ONActionContext;
    }
    
    public abstract void eval(List<Object> args);
    
    // Common functionality for all actions can go here
    protected void logResponse(OneResponse response) {
        if (response.isError()) {
            ONActionContext.getLogger().severe(response.getErrorMessage());
        } else {
        	ONActionContext.getLogger().info(response.getMessage());
        }
    }
}