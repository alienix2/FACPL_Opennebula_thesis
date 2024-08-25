package opennebula_api;

import java.util.List;

import org.opennebula.client.vm.VirtualMachine;

public class ReleaseVM extends OpenNebulaActionBase {

	public ReleaseVM(OpenNebulaActionContext ONActionContext) {
		super(ONActionContext);
	}

	@Override
	public void eval(List<Object> args) {
		ONActionContext.getLogger().info("Powering off (Releasing) the VM: " + args.get(1));
		VMDescriptor toRelease = 
				ONActionContext.getVMsInfo().getRunningVMByName((String)args.get(1));
		if (toRelease == null) {
			ONActionContext.getLogger().severe("No VM found");
            return;
        }
		logResponse(
				new VirtualMachine(Integer.parseInt(toRelease.getVmId()), ONActionContext.getClient())
				.poweroff());
	}
}
