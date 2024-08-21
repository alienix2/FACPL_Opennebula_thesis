package opennebula_api;

public class VMDescriptor {
    private String vmId;
    private String vmName;
    private String templateId;
    private String host;
    private String state;
    private String lcmState;

    public VMDescriptor(String vmId, String vmName, String templateId, String host, String state, String lcmState) {
        this.vmId = vmId;
        this.vmName = vmName;
        this.templateId = templateId;
        this.host = host;
        this.state = state;
        this.lcmState = lcmState;
    }

    public String getVmId() {
        return vmId;
    }

    public String getVmName() {
        return vmName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getHostId() {
        return host;
    }

    public String getState() {
        return state;
    }
    
    public String getLcmState() {
    	return lcmState;
    }

	@Override
	public String toString() {
		return "VMDescriptor [vmId=" + vmId + ", vmName=" + vmName + ", templateId=" + templateId + ", host=" + host
				+ ", state=" + state + ", virtualMachine=" + "]\n";
	}

}
