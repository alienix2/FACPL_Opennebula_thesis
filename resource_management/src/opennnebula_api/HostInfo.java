package opennnebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utilities.FileLoggerFactory;
import utilities.XmlUtils;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

public class HostInfo {
	
	private static Client oneClient;
	private static Logger logger;
    private static int availableCpu = -1;
    private static long availableMem = -1;

    public static void initializeClient(String auth, String endpoint) throws ClientConfigurationException {
        if (oneClient == null) {
            oneClient = new Client(auth, endpoint);
            logger = FileLoggerFactory.make("hostInfoLog.txt");
        }
    }
    
    public static void setLogger(Logger logger) {
    	HostInfo.logger = logger;
    }
    
    public static void initializeClient() throws ClientConfigurationException {
    	oneClient = new Client("oneadmin:Panzerotto", "http://localhost:2633/RPC2");
    }

    private static void updateHostResources(int hostId) throws SAXException, IOException, ParserConfigurationException {
        // Retrieve the host object
        Host host = new Host(hostId, oneClient);

        // Fetch the host info
        OneResponse info = host.info();

        if (info.isError()) {
            logger.severe("Error retrieving host information: " + info.getErrorMessage());
            return;
        }

        // Parse the XML response
        String xmlData = info.getMessage();
		Document document = XmlUtils.parseXml(xmlData);

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

    public static int getAvailableCpu(int hostId) throws SAXException, IOException, ParserConfigurationException {
    	updateHostResources(hostId);
        return availableCpu;
    }
    
    public static long getAvailableMem(int hostId) throws SAXException, IOException, ParserConfigurationException{
    	updateHostResources(hostId);
        return availableMem;
    }
}
