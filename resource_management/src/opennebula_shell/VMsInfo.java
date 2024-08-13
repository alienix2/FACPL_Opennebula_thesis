package opennebula_shell;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import utilities.FileLoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMsInfo {
    
	protected static final Logger logger = FileLoggerFactory.make("commandsOutput.txt");
	private static List<Map<String, String>> vmInfoList;

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
        	System.out.println(line);
            String[] fields = line.split(",");
            String vmId = fields[0].trim();
            String vmName = fields[3].trim();
            String host = fields[7].trim();

            String templateId = getInfoForVM(vmId, "TEMPLATE_ID");
                
            // Store VM information in a map
            Map<String, String> vmInfo = new HashMap<>();
            vmInfo.put("vmId", vmId);
            vmInfo.put("vmName", vmName);
            vmInfo.put("templateId", templateId);
            vmInfo.put("host", host);
                
            vmInfoList.add(vmInfo);
        }
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
	
    public static String getVMsInfoByHostTemplate(String host, String templateId, int number) throws IOException, InterruptedException {
    	updateVmsInfo();
    	System.out.println(vmInfoList.toString());
    	return String.join(", ", vmInfoList.stream()
				.filter(x -> x.get("host").equals(host))
				.filter(x -> x.get("templateId").equals(templateId))
				.map(x -> x.get("vmId"))
				.limit(number)
				.collect(Collectors.toList()));
    }
}
