package opennebula_api;

import java.util.ArrayList;
import java.util.List;

public class MockVirtualMachineService implements VirtualMachineService {

    private List<VMDescriptor> virtualMachinesInfo = new ArrayList<>();
    private List<VMDescriptor> runningVirtualMachineInfo = new ArrayList<>();

    // Setters for injecting test data
    public void setVirtualMachinesInfo(List<VMDescriptor> virtualMachinesInfo) {
        this.virtualMachinesInfo = virtualMachinesInfo;
    }

    public void setRunningVirtualMachineInfo(List<VMDescriptor> runningVirtualMachineInfo) {
        this.runningVirtualMachineInfo = runningVirtualMachineInfo;
    }

    @Override
    public List<VMDescriptor> getVirtualMachinesInfo() {
        return virtualMachinesInfo;
    }

    @Override
    public List<VMDescriptor> getRunningVirtualMachineInfo() {
        return runningVirtualMachineInfo;
    }
}
