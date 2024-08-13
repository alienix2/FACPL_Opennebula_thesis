package opennnebula_api;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.opennebula.client.template.Template;
import org.opennebula.client.vm.VirtualMachine;

public class CreateVM extends ApiCallTemplate {

	public CreateVM(String credentials, String endpoint, Logger logger) {
		super(credentials, endpoint, logger);
	}

	@Override
	public void callApi(List<Object> args) throws IOException, InterruptedException {
		logger.info("Starting VM: " + "[" + args.get(2) + ", " + args.get(1) + "]");
		Template template = new Template((int) args.get(2), oneClient);
		responseList.add(template.instantiate((String) args.get(1)));
		VirtualMachine vm = 
				new VirtualMachine(Integer
						.parseInt(responseList.get(responseList.size()-1).getMessage()), 
				oneClient);
		responseList.add(vm.deploy((int) args.get(0)));
	}

}
