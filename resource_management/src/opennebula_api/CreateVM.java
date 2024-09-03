package opennebula_api;

import java.util.List;
import org.opennebula.client.OneResponse;
import org.opennebula.client.template.Template;
import org.opennebula.client.vm.VirtualMachine;

public class CreateVM extends OpenNebulaActionBase {

	public CreateVM(OpenNebulaActionContext ONActionContext) {
		super(ONActionContext);
	}

	@Override
	public void eval(List<Object> args) {
		ONActionContext.getLogger().info("Starting VM: " + "[" + args.get(2) + ", " + args.get(1) + "]");
		Template template = new Template((int) args.get(2), ONActionContext.getClient());
		OneResponse instantiateResponse = template.instantiate((String) args.get(1));
		logResponse(instantiateResponse);
		if (!instantiateResponse.isError()){
			VirtualMachine vm = 
					new VirtualMachine(instantiateResponse.getIntMessage(), ONActionContext.getClient());
			logResponse(vm.deploy((int) args.get(0)));
		}		
	}
}
