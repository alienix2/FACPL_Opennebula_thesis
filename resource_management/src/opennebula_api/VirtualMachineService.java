package opennebula_api;

import java.util.List;

public interface VirtualMachineService {
	List<VMDescriptor> getVirtualMachinesInfo();
	List<VMDescriptor> getRunningVirtualMachineInfo();
}
