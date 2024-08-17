package opennebula_api;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.Host;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import utilities.XmlUtils;

import java.util.logging.Logger;

public class HostInfo {

    private final Client oneClient;
    private final Logger logger;
    private int availableCpu = -1;
    private long availableMem = -1;

    // Private constructor to enforce the use of static factory methods
    private HostInfo(Client oneClient, Logger logger) {
        this.oneClient = oneClient;
        this.logger = logger;
    }

    // Static factory methods
    public static HostInfo withDefaultLogger(String auth, String endpoint) {
        try {
            return new HostInfo(new Client(auth, endpoint), Logger.getLogger(HostInfo.class.getName()));
        } catch (ClientConfigurationException e) {
            throw new RuntimeException("Failed to create HostInfo", e);
        }
    }

    public static HostInfo withCustomLogger(String auth, String endpoint, Logger logger) {
        try {
            return new HostInfo(new Client(auth, endpoint), logger);
        } catch (ClientConfigurationException e) {
            throw new RuntimeException("Failed to create HostInfo", e);
        }
    }

    public static HostInfo withClient(Client client) {
        return new HostInfo(client, Logger.getLogger(HostInfo.class.getName()));
    }

    public static HostInfo withClientAndLogger(Client client, Logger logger) {
        return new HostInfo(client, logger);
    }

    private void updateHostResources(int hostId) {
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

        int totalCpu = maxCpuList.item(0).getTextContent().equals("") ? 0 : Integer.parseInt(maxCpuList.item(0).getTextContent().trim());
        int usedCpu = usedCpuList.item(0).getTextContent().equals("") ? 0 : Integer.parseInt(usedCpuList.item(0).getTextContent().trim());
        availableCpu = totalCpu - usedCpu;

        long totalMem = maxMemList.item(0).getTextContent().equals("") ? 0 : Long.parseLong(maxMemList.item(0).getTextContent().trim());
        long usedMem = usedMemList.item(0).getTextContent().equals("") ? 0 : Long.parseLong(usedMemList.item(0).getTextContent().trim());
        availableMem = totalMem - usedMem;

        logger.info("Resources available on host: " + hostId + ": [CPU, MEM]: " + "[" + availableCpu + "," + availableMem + "]");
    }

    public int getAvailableCpu(int hostId) {
        updateHostResources(hostId);
        return availableCpu;
    }

    public long getAvailableMem(int hostId) {
        updateHostResources(hostId);
        return availableMem;
    }
}
