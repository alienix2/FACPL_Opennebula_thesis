package opennebula_api;

import java.util.List;
import java.util.stream.Collectors;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;

public class MockClient extends Client{
	
	private final String mockResponse;
	private final List<MockVirtualMachine> mockVMList;
	
	public MockClient(String auth, String endpoint, String mockResponse, List<MockVirtualMachine> mockVMList) throws ClientConfigurationException {
		super(auth, endpoint);
        this.mockResponse = mockResponse;
        this.mockVMList = mockVMList;
    }
	
	@Override
	public OneResponse call(String action, Object... args) {
		
		if (action.equals("host.info")){	//HostInfoTest
			return new OneResponse(true, mockResponse);
		}
		
		if (action.equals("vmpool.info")) {	//VMsInfoTest
			String vmPoolResponse = "<VM_POOL>" + String.join("", 
					mockVMList.stream()
					.map(x -> x.info()
					.getMessage()).collect(Collectors.toList())) + "</VM_POOL>";
			return new OneResponse(true, vmPoolResponse);
		}
		
		if (action.equals("vm.info")) {
			String vmResponse = mockVMList.stream()
									.filter(x -> x.getId().equals(args[0]+""))
									.map(x -> x.info().getMessage())
									.findFirst()
									.get()
									.toString();
			return new OneResponse(true, vmResponse);
		}
		
		if (action.equals("template.instantiate")) {
			return new OneResponse(true, "100");
		}
		
		if (action.equals("vm.deploy")) {
			return new OneResponse(true, "Success message");
		}
		
		System.out.println(action);
		
		return null;
	}
}
