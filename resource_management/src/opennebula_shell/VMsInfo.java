package opennebula_shell;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMsInfo {
    
	private static Logger logger = Logger.getLogger(VMsInfo.class.getName());
	private static List<Map<String, String>> vmInfoList;
	
	public static void setLogger(Logger logger) {
		VMsInfo.logger = logger;
	}

    public static void updateVmsInfo() throws IOException, InterruptedException {
        vmInfoList = new ArrayList<>();
        
        // List all running VMs
        ProcessBuilder listProcessBuilder = new ProcessBuilder("onevm", "list", "--csv");
        Process listProcess = listProcessBuilder.start();

        BufferedReader listReader = new BufferedReader(new InputStreamReader(listProcess.getInputStream()));
        String line;

        // Skip header
        listReader.readLine();
        
        while ((line = listReader.readLine()) != null) {
            String[] fields = line.split(",");
            String vmId = fields[0].trim();
            String vmName = fields[3].trim();
            String state = fields[4].trim();
            String host = fields[7].trim();

            String templateId = getInfoForVM(vmId, "TEMPLATE_ID");
                
            // Store VM information in a map
            Map<String, String> vmInfo = new HashMap<>();
            vmInfo.put("vmId", vmId);
            vmInfo.put("vmName", vmName);
            vmInfo.put("templateId", templateId);
            vmInfo.put("host", host);
            vmInfo.put("state", state);
                
            vmInfoList.add(vmInfo);
        }
        logger.info("The current VMs info are: " + vmInfoList.toString());
    }
    
    public static String getInfoForVM(String vmId, String info) throws IOException, InterruptedException {
        //Get the template ID used to create the VM
        ProcessBuilder showProcessBuilder = new ProcessBuilder("onevm", "show", vmId);
        Process showProcess = showProcessBuilder.start();

        BufferedReader showReader = new BufferedReader(new InputStreamReader(showProcess.getInputStream()));
        String line;
        String vmInfo = null;

        while ((line = showReader.readLine()) != null) {
            if (line.contains(info)) {
                vmInfo = line.split("=")[1].trim().replace("\"", "");
                break;
            }
        }

        showProcess.waitFor();
        return vmInfo;
    }
	
    public static String getVMsIdByHostTemplate(String host, String templateId, int number) throws IOException, InterruptedException {
    	updateVmsInfo();
    	return String.join(", ", vmInfoList.stream()
				.filter(x -> x.get("host").equals(host))
				.filter(x -> x.get("templateId").equals(templateId))
				.map(x -> x.get("vmId"))
				.limit(number)
				.collect(Collectors.toList()));
    }
    
    public static String getRunningVMIdByName(String vmName) throws IOException, InterruptedException {
        updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.get("vmName").equals(vmName))
                .filter(x -> x.get("state").equals("runn"))
                .map(x -> x.get("vmId"))
                .toString();
    }
    
    public static String getRunningVMsIdByHost(String host) throws IOException, InterruptedException {
        updateVmsInfo();
        return String.join(", ", vmInfoList.stream()
                .filter(x -> x.get("host").equals(host))
                .filter(x -> x.get("state").equals("runn"))
                .map(x -> x.get("vmName"))
                .collect(Collectors.toList()));
    }
    
    public static long countRunningVMsByHost(String host) throws IOException, InterruptedException {
    	updateVmsInfo();
        return vmInfoList.stream()
                .filter(x -> x.get("host").equals(host))
                .filter(x -> x.get("state").equals("runn"))
                .count();
    }
}
