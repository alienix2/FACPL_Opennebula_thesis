package opennebula_api;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.w3c.dom.Document;

import utilities.XmlUtils;

import java.util.ArrayList;
import java.util.List;

public class VMsInfo {
	
    private final Logger logger;
    private final Client oneClient;
    private VirtualMachinePool vmPool;

    private VMsInfo(Client client, Logger logger) {
        this.oneClient = client;
        this.logger = logger;
    }

    // Static factory methods
    public static VMsInfo withDefaultLogger(String auth, String endpoint) {
    	try {
			return new VMsInfo(new Client(auth, endpoint), Logger.getLogger(ApiCallTemplate.class.getName()));
        } catch (ClientConfigurationException e) {
	        throw new RuntimeException("Failed to create VMsInfo", e);		
	    }
    }

    public static VMsInfo withCustomLogger(String auth, String endpoint, Logger logger) {
    	try {
			return new VMsInfo(new Client(auth, endpoint), logger);
        } catch (ClientConfigurationException e) {
	        throw new RuntimeException("Failed to create VMsInfo", e);		
	    }
    }

    public static VMsInfo withClient(Client client) {
        return new VMsInfo(client, Logger.getLogger(ApiCallTemplate.class.getName()));
    }

    public static VMsInfo withClientAndLogger(Client client, Logger logger) {
        return new VMsInfo(client, logger);
    }

    private List<VMDescriptor> updateVmsInfo() {
        vmPool = new VirtualMachinePool(oneClient);
        vmPool.info();

        List<VMDescriptor> vmInfoList = new ArrayList<>();
        for (VirtualMachine vm : vmPool) {
            String xmlData = vm.info().getMessage();
            Document document = XmlUtils.parseXml(xmlData);

            String vmId = vm.getId() + "";
            String vmName = vm.getName();
            String templateId = document.getElementsByTagName("TEMPLATE_ID").item(0).getTextContent();
            String host = document.getElementsByTagName("HOSTNAME").item(0).getTextContent();
            String state = vm.stateStr();

            VMDescriptor vmInfo = new VMDescriptor(vmId, vmName, templateId, host, state, vm);
            vmInfoList.add(vmInfo);
        }

        this.logger.info("Updated VMs info: " + vmInfoList.toString());
		return vmInfoList;
    }

    // Return a list of VMInfo objects
    public List<String> getRunningVMsIdByHostTemplate(String host, String templateId, int number) {
        return updateVmsInfo().stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getTemplateId().equals(templateId))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVmId())
                .limit(number)
                .collect(Collectors.toList());
    }

    // Return a specific VMInfo object by VM name
    public String getRunningVMIdByName(String vmName) {
        return updateVmsInfo().stream()
                .filter(x -> x.getVmName().equals(vmName))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVmId())
                .findFirst()
                .orElse(null);
    }

    // Return a list of VMInfo objects for running VMs on a specific host
    public List<String> getRunningVMsIdByHost(String host) {
        return updateVmsInfo().stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVmId())
                .collect(Collectors.toList());
    }
    
    public Long countRunningVMsByHost(String host) {
        return updateVmsInfo().stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getState().equals("ACTIVE"))
                .count();
    }
    
    public List<VirtualMachine> getRunningVMsByHostTemplate(String host, String templateId) {
        return updateVmsInfo().stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getTemplateId().equals(templateId))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVirtualMachine())
                .collect(Collectors.toList());
    }

    // Return a specific VMInfo object by VM name
    public VirtualMachine getRunningVMByName(String vmName) {
        return updateVmsInfo().stream()
                .filter(x -> x.getVmName().equals(vmName))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVirtualMachine())
                .findFirst()
                .orElse(null);
    }

    // Return a list of VMInfo objects for running VMs on a specific host
    public List<VirtualMachine> getRunningVMsByHost(String host) {
        return updateVmsInfo().stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVirtualMachine())
                .collect(Collectors.toList());
    }
    
//    public List<VirtualMachine> getVMsByHostTemplate(String host, String templateId, int number) throws IOException, SAXException, ParserConfigurationException {
//    	vmPool = new VirtualMachinePool(oneClient);
//    	vmPool.info();
//        List<VirtualMachine> matchedVMs = new ArrayList<>();
//        
//    	for (VirtualMachine vm : vmPool) {
//			String xmlData = vm.info().getMessage();
//    		Document document = XmlUtils.parseXml(xmlData);
//    		if (templateId.equals(document.getElementsByTagName("TEMPLATE_ID").item(0).getTextContent()) 
//    				&& host.equals(document.getElementsByTagName("HOSTNAME").item(0).getTextContent())){
//    			matchedVMs.add(vm);
//    		}
//    	}
//    	
//    	return matchedVMs.isEmpty() 
//    			? Collections.emptyList() : 
//    				matchedVMs.subList(0, Math.min(number, matchedVMs.size()));
//    }
//    
//    public VirtualMachine getVMByName(String vmName) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
//    	vmPool = new VirtualMachinePool(oneClient);
//    	vmPool.info();
//        
//    	for(VirtualMachine vm : vmPool) {
//			if(vm.getName().equals(vmName)) return vm;
//    	}
//    	return null;
//    }
//    
//    public List<VirtualMachine> getRunningVMsByHost(String host) throws SAXException, IOException, ParserConfigurationException {
//    	vmPool = new VirtualMachinePool(oneClient);
//    	vmPool.info();
//        List<VirtualMachine> matchedVMs = new ArrayList<>();
//        
//    	for (VirtualMachine vm : vmPool) {
//			String xmlData = vm.info().getMessage();
//			Document document = XmlUtils.parseXml(xmlData);
//    		if (vm.stateStr().equals("ACTIVE") 
//    				&& host.equals(document.getElementsByTagName("HOSTNAME").item(0).getTextContent())){
//    			matchedVMs.add(vm);
//    		}
//    	}
//    	
//    	return matchedVMs.isEmpty() ? Collections.emptyList() : matchedVMs;
//    }
//    
//    public long countRunningVMsByHost(String host) throws SAXException, IOException, ParserConfigurationException {
//    	vmPool = new VirtualMachinePool(oneClient);
//    	vmPool.info();
//        List<VirtualMachine> matchedVMs = new ArrayList<>();
//        
//    	for (VirtualMachine vm : vmPool) {
//			String xmlData = vm.info().getMessage();
//			Document document = XmlUtils.parseXml(xmlData);
//    		if (vm.stateStr().equals("ACTIVE") 
//    				&& host.equals(document.getElementsByTagName("HOSTNAME").item(0).getTextContent())){
//    			matchedVMs.add(vm);
//    		}
//    	}
//    	return matchedVMs.size();
//    }
}
