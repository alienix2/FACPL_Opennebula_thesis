package opennebula_api;

import java.util.List;

import org.opennebula.client.vm.VirtualMachine;

public class FreezeVMMultiple extends OpenNebulaActionBase {

	public FreezeVMMultiple(OpenNebulaActionContext ONActionContext) {
		super(ONActionContext);
	}
	
	@Override
	public void eval(List<Object> args) {
		int vmNumber = Integer.parseInt((String)args.get(1));
		ONActionContext.getLogger().info("Stopping (Freezing) " + args.get(1) + " VM of [host template]: " + "[" + args.get(0) + " " + args.get(2) + "]");
		List<VMDescriptor> suspendList = 
				ONActionContext.getVMsInfo().getRunningVMsByHostTemplate((String)args.get(0), (String)args.get(2));
		if (suspendList.size() < vmNumber) {
			ONActionContext.getLogger().severe("Not enough VMs found, stopping the most VMs possible");
        }
		suspendList.stream().limit(vmNumber).forEach(vm -> {logResponse(
				new VirtualMachine(Integer.parseInt(vm.getVmId()), ONActionContext.getClient())
				.stop());
		});

	}
}
