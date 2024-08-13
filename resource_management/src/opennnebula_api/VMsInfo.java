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

import utilities.FileLoggerFactory;
import utilities.XmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMsInfo {
    
	protected static final Logger logger = FileLoggerFactory.make("commandsOutput.txt");
	private static Client oneClient;
	private static List<Map<String, String>> vmInfoList;
	private static VirtualMachinePool vmPool;
	
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

            // Get VM ID, Name, Template ID, and Host ID
            String vmId = vm.getId() + "";
            String vmName = vm.getName();
            String templateId = document.getElementsByTagName("TEMPLATE_ID").item(0).getTextContent();
            String host = document.getElementsByTagName("HOSTNAME").item(0).getTextContent();

            Map<String, String> vmInfo = new HashMap<>();
            vmInfo.put("vmId", vmId);
            vmInfo.put("vmName", vmName);
            vmInfo.put("templateId", templateId);
            vmInfo.put("host", host);

            vmInfoList.add(vmInfo);           
        }
    }
    
    public static String getVMsInfoByHostTemplate(String hostId, String templateId, int number) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.get("host").equals(hostId))
                .filter(x -> x.get("templateId").equals(templateId))
                .map(x -> x.get("vmId"))
                .limit(number)
                .collect(Collectors.joining(", "));
    }
    
    public static List<VirtualMachine> getVMsByHostTemplate(String host, String templateId, int number) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
    	vmPool = new VirtualMachinePool(oneClient);
    	vmPool.info();
        List<VirtualMachine> matchedVMs = new ArrayList<>();
        
    	for (VirtualMachine vm : vmPool) {
			String xmlData = vm.info().getMessage();
    		Document document = XmlUtils.parseXml(xmlData);
    		if (templateId.equals(document.getElementsByTagName("TEMPLATE_ID").item(0).getTextContent()) 
    				&& host.equals(document.getElementsByTagName("HOSTNAME").item(0).getTextContent())){
    			matchedVMs.add(vm);
    		}
    	}
    	
    	return matchedVMs.isEmpty() 
    			? Collections.emptyList() : 
    				matchedVMs.subList(0, Math.min(number, matchedVMs.size()));
    }
}
