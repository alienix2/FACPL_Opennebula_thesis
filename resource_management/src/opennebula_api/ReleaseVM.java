package opennebula_api;

import java.util.List;

import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

public class ReleaseVM extends OpenNebulaActionBase {

	public ReleaseVM(OpenNebulaActionContext ONActionContext) {
		super(ONActionContext);
	}

	@Override
	public void eval(List<Object> args) {
		ONActionContext.getLogger().info("Powering off (Releasing) 1 VM of [host, temp]: " + "[" + args.get(0) + " " + args.get(2) + "]");
		List<VMDescriptor> suspendList = 
				ONActionContext.getVMsInfo().getRunningVMsByHostTemplate((String)args.get(0), (String)args.get(2));
		if (suspendList.isEmpty()) {
			ONActionContext.getLogger().severe("No VM found");
            return;
        }
		logResponse(
				new VirtualMachine(Integer.parseInt(suspendList.get(0).getVmId()), ONActionContext.getClient())
				.poweroff());
	}
}
