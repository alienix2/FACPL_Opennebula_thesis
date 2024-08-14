package opennnebula_api;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import utilities.XmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class VMsInfo {

    private static Logger logger = Logger.getLogger(VMsInfo.class.getName());
    private static Client oneClient;
    private static List<VMDescriptor> vmInfoList;
    private static VirtualMachinePool vmPool;

    public static void setLogger(Logger logger) {
        VMsInfo.logger = logger;
    }

    public static void initializeClient(String auth, String endpoint) throws ClientConfigurationException {
        if (oneClient == null) {
            oneClient = new Client(auth, endpoint);
        }
    }

    public static void initializeClient() throws ClientConfigurationException {
        oneClient = new Client("oneadmin:Panzerotto", "http://localhost:2633/RPC2");
    }

    public static void updateVmsInfo() throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        vmInfoList = new ArrayList<>();
        vmPool = new VirtualMachinePool(oneClient);
        vmPool.info();

        for (VirtualMachine vm : vmPool) {
            String xmlData = vm.info().getMessage();
            Document document = XmlUtils.parseXml(xmlData);

            // Get VM ID, Name, Template ID, Host, and State
            String vmId = vm.getId() + "";
            String vmName = vm.getName();
            String templateId = document.getElementsByTagName("TEMPLATE_ID").item(0).getTextContent();
            String host = document.getElementsByTagName("HOSTNAME").item(0).getTextContent();
            String state = vm.stateStr();

            VMDescriptor vmInfo = new VMDescriptor(vmId, vmName, templateId, host, state, vm);
            vmInfoList.add(vmInfo);
        }
        logger.info("The current VMs info are: " + vmInfoList.toString());
    }

    // Return a list of VMInfo objects
    public static List<String> getVMsIdByHostTemplate(String host, String templateId, int number) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getTemplateId().equals(templateId))
                .map(x -> x.getVmId())
                .limit(number)
                .collect(Collectors.toList());
    }

    // Return a specific VMInfo object by VM name
    public static String getRunningVMIdByName(String vmName) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getVmName().equals(vmName))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVmId())
                .findFirst()
                .orElse(null);
    }

    // Return a list of VMInfo objects for running VMs on a specific host
    public static List<String> getRunningVMsIdByHost(String host) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVmId())
                .collect(Collectors.toList());
    }
    
    public static long countRunningVMsByHost(String host) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
    	updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getState().equals("runn"))
                .count();
    }
    
    public static List<VirtualMachine> getVMsByHostTemplate(String host, String templateId, int number) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getTemplateId().equals(templateId))
                .map(x -> x.getVirtualMachine())
                .limit(number)
                .collect(Collectors.toList());
    }

    // Return a specific VMInfo object by VM name
    public static VirtualMachine getRunningVMByName(String vmName) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getVmName().equals(vmName))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVirtualMachine())
                .findFirst()
                .orElse(null);
    }

    // Return a list of VMInfo objects for running VMs on a specific host
    public static List<VirtualMachine> getRunningVMsByHost(String host) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.getHost().equals(host))
                .filter(x -> x.getState().equals("ACTIVE"))
                .map(x -> x.getVirtualMachine())
                .collect(Collectors.toList());
    }
    
//    public static List<VirtualMachine> getVMsByHostTemplate(String host, String templateId, int number) throws IOException, SAXException, ParserConfigurationException {
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
//    public static VirtualMachine getVMByName(String vmName) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
//    	vmPool = new VirtualMachinePool(oneClient);
//    	vmPool.info();
//        
//    	for(VirtualMachine vm : vmPool) {
//			if(vm.getName().equals(vmName)) return vm;
//    	}
//    	return null;
//    }
//    
//    public static List<VirtualMachine> getRunningVMsByHost(String host) throws SAXException, IOException, ParserConfigurationException {
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
//    public static long countRunningVMsByHost(String host) throws SAXException, IOException, ParserConfigurationException {
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
