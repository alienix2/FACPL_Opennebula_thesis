package opennebula_api;

import java.util.List;
import java.util.logging.Logger;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;

public class FreezeVM extends OpenNebulaActionBase {

	public FreezeVM(OpenNebulaActionContext ONActionContext) {
		super(ONActionContext);
	}

	@Override
	public void eval(List<Object> args) {
		ONActionContext.getLogger().info("Suspending (Freezing) 1 VM of [host, temp]: " + "[" + args.get(0) + " " + args.get(2) + "]");
		List<VMDescriptor> suspendList = 
				ONActionContext.getVMsInfo().getRunningVMsByHostTemplate((String)args.get(0), (String)args.get(2));
		if (suspendList.isEmpty()) {
			ONActionContext.getLogger().severe("No VM found");
            return;
        }
		logResponse(
				new VirtualMachine(Integer.parseInt(suspendList.get(0).getVmId()), ONActionContext.getClient())
				.suspend());
	}
}
