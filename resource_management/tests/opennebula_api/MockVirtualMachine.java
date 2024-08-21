package opennebula_api;

import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

public class MockVirtualMachine extends VirtualMachine {

    private String state;
    private String id;
    private String name;
    private String lcmState;
    private String xmlData;

    public MockVirtualMachine(String id, String name, String state, String lcmState, String xmlData) {
        super(-1, null);
        this.id = id;
        this.name = name;
        this.state = state;
        this.lcmState = lcmState;
        this.xmlData = xmlData;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String stateStr() {
        return state;
    }
    
    @Override
    public String lcmStateStr() {
        return lcmState;
    }
    
    @Override
    public OneResponse info() {
        return new OneResponse(true, xmlData);
    } 
}