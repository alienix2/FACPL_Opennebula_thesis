package opennebula_api;

import java.util.Iterator;
import java.util.List;

import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

public class MockVirtualMachinePool extends VirtualMachinePool {

    private List<VirtualMachine> virtualMachines;
    
	public MockVirtualMachinePool(List<VirtualMachine> virtualMachines) {
		super(null);
		this.virtualMachines = virtualMachines;
	}
	
	@Override
	public OneResponse info() {
		return null;
	}
	
	@Override
	public Iterator<VirtualMachine> iterator() {
		return virtualMachines.iterator();
	}

}
