package opennebula_shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utilities.XmlUtils;
import javax.xml.parsers.ParserConfigurationException;

public class HostInfo {
    
	private static Logger logger;
	private static int availableCpu = -1;
    private static long availableMem = -1;
    
    public static void setLogger(Logger logger) {
    	HostInfo.logger = logger;
    }

    // Fetch host information in XML format
    private static Document getHostInfoXML(int hostId) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        ProcessBuilder processBuilder = new ProcessBuilder("onehost", "show", hostId + "", "--xml");
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder xmlOutput = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            xmlOutput.append(line).append("\n");
        }

        process.waitFor();
        return XmlUtils.parseXml(xmlOutput.toString());
    }

    // Update host resources
    private static void updateHostResources(int hostId) throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        Document document = getHostInfoXML(hostId);

        // Extract values directly
        NodeList maxCpuList = document.getElementsByTagName("TOTAL_CPU");
        NodeList usedCpuList = document.getElementsByTagName("CPU_USAGE");
        NodeList maxMemList = document.getElementsByTagName("TOTAL_MEM");
        NodeList usedMemList = document.getElementsByTagName("MEM_USAGE");

        int totalCpu = maxCpuList.getLength() > 0 ? Integer.parseInt(maxCpuList.item(0).getTextContent().trim()) : 0;
        int usedCpu = usedCpuList.getLength() > 0 ? Integer.parseInt(usedCpuList.item(0).getTextContent().trim()) : 0;
        availableCpu = totalCpu - usedCpu;

        long totalMem = maxMemList.getLength() > 0 ? Long.parseLong(maxMemList.item(0).getTextContent().trim()) : 0;
        long usedMem = usedMemList.getLength() > 0 ? Long.parseLong(usedMemList.item(0).getTextContent().trim()) : 0;
        availableMem = totalMem - usedMem;
        
        logger.info("Resources available on host: " + hostId + ": [CPU, MEM]: " + "[" + availableCpu + "," + availableMem + "]" );
    }

    public static int getAvailableCpu(int hostId) throws Exception {
        updateHostResources(hostId);
        return availableCpu;
    }
    
    public static long getAvailableMem(int hostId) throws Exception {
        updateHostResources(hostId);
        return availableMem;
    }
}
