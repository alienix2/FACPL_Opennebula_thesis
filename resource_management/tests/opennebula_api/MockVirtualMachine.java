package opennebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

public class MockVirtualMachine extends VirtualMachine {

    private final String response;
    private String state = "undefined";

    public MockVirtualMachine(int id, Client mockClient, String response) {
        super(id, mockClient);
        this.id = id;
        this.response = response;
    }

    @Override
    public OneResponse info() {
        return new OneResponse(true, response);
    }
    
    @Override
    public OneResponse terminate() {
    	state = "termiated";
        return new OneResponse(true, state);
    }
    
    @Override
    public OneResponse suspend() {
    	state = "suspended";
        return new OneResponse(true, state);
    }
    
    @Override
    public OneResponse poweroff() {
    	state = "poweroff";
        return new OneResponse(true, state);
    }
    
    @Override
    public OneResponse deploy(int hostId) {
    	state = "deployed";
    	return new OneResponse(true, state);
    }
    
    public String getState() {
    	return state;
    }
    
}