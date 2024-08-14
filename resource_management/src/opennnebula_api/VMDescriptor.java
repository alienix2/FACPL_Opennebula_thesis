package opennnebula_api;

import org.opennebula.client.vm.VirtualMachine;

public class VMDescriptor {
    private String vmId;
    private String vmName;
    private String templateId;
    private String host;
    private String state;
    private VirtualMachine virtualMachine;

    // Constructor
    public VMDescriptor(String vmId, String vmName, String templateId, String host, String state, VirtualMachine virtualMachine) {
        this.vmId = vmId;
        this.vmName = vmName;
        this.templateId = templateId;
        this.host = host;
        this.state = state;
        this.virtualMachine = virtualMachine;
    }

    // Getters
    public String getVmId() {
        return vmId;
    }

    public String getVmName() {
        return vmName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getHost() {
        return host;
    }

    public String getState() {
        return state;
    }

    public VirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    @Override
    public String toString() {
        return "VMInfo{" +
                "vmId='" + vmId + '\'' +
                ", vmName='" + vmName + '\'' +
                ", templateId='" + templateId + '\'' +
                ", host='" + host + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
