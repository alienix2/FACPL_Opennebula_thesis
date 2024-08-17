package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.vm.VirtualMachine;

import opennebula_api.VMsInfo;

public class VMsInfoTest {

    private MockClient mockClient;
    private List<MockVirtualMachine> mockVirtualMachines;
    private VMsInfo vmsInfo;
    
    @BeforeEach
    void setUp() throws ClientConfigurationException {
        // Initialize the mock client
        mockVirtualMachines = new ArrayList<>();
        // Setup mocked virtual machines
        mockVirtualMachines.add(new MockVirtualMachine(443, mockClient, generateVmXml(443, "localhost", "5", "3")));
        mockVirtualMachines.add(new MockVirtualMachine(444, mockClient, generateVmXml(444, "localhost", "5", "6")));
        mockVirtualMachines.add(new MockVirtualMachine(445, mockClient, generateVmXml(445, "remotehost", "7", "3")));
        mockVirtualMachines.add(new MockVirtualMachine(420, mockClient, generateVmXml(420, "remotehost", "7", "5")));
        mockVirtualMachines.add(new MockVirtualMachine(446, mockClient, generateVmXml(446, "localhost", "5", "3")));
        
		mockClient = new MockClient("fake-auth", "http://fake-endpoint:123/random", "random", mockVirtualMachines);
		vmsInfo = VMsInfo.withClient(mockClient);
        // Set up VMsInfo to use these mocked virtual machines
        
    }
    
    private String generateVmXml(int id, String hostname, String templateId, String state) {
        return String.format("<VM><ID>%d</ID><NAME>nome%d</NAME><STATE>%s</STATE><TEMPLATE><TEMPLATE_ID>%s</TEMPLATE_ID></TEMPLATE><HISTORY_RECORDS><HISTORY><HOSTNAME><![CDATA[%s]]></HOSTNAME></HISTORY></HISTORY_RECORDS></VM>",
                id, id, state, templateId, hostname);
    }

    
    @Test
    void testGetVMsIdByHostTemplateMatch() throws Exception {
        List<String> vmIds = vmsInfo.getRunningVMsIdByHostTemplate("localhost", "5", 3);
        assertEquals(2, vmIds.size());
        assertTrue(vmIds.contains("443"));
        assertTrue(vmIds.contains("446"));
        
        vmIds = vmsInfo.getRunningVMsIdByHostTemplate("localhost", "5", 1);
        assertEquals(1, vmIds.size());
        assertTrue(vmIds.contains("443"));
        
        vmIds = vmsInfo.getRunningVMsIdByHostTemplate("localhost", "10", 3);
        assertEquals(0, vmIds.size());
        
        vmIds = vmsInfo.getRunningVMsIdByHostTemplate("remotehost", "5", 3);
        assertEquals(0, vmIds.size());
        
        vmIds = vmsInfo.getRunningVMsIdByHostTemplate("otherhost", "5", 3);
        assertEquals(0, vmIds.size());
    }
    
    @Test
    void testGetRunningVMIdByName() throws Exception {
        String vmId = vmsInfo.getRunningVMIdByName("nome443");
        assertEquals("443", vmId);

        vmId = vmsInfo.getRunningVMIdByName("nome444");
        assertNull(vmId);
    }
    
    @Test
    void testGetRunningVMsIdByHost() throws Exception {
        List<String> vmIds = vmsInfo.getRunningVMsIdByHost("localhost");
        assertEquals(2, vmIds.size());
        assertTrue(vmIds.contains("443"));
        assertTrue(vmIds.contains("446"));

        vmIds = vmsInfo.getRunningVMsIdByHost("remotehost");
        assertEquals(1, vmIds.size());
        
        vmIds = vmsInfo.getRunningVMsIdByHost("otherhost");
        assertEquals(0, vmIds.size());
    }
    
    @Test
    void testCountRunningVMsByHost() throws Exception {
        long count = vmsInfo.countRunningVMsByHost("localhost");
        assertEquals(2, count);

        count = vmsInfo.countRunningVMsByHost("remotehost");
        assertEquals(1, count);

        count = vmsInfo.countRunningVMsByHost("otherhost");
        assertEquals(0, count);
    }
    
    @Test
    void testGetRunningVMsByHostTemplate() throws Exception {
        List<VirtualMachine> vms = vmsInfo.getRunningVMsByHostTemplate("localhost", "5");
        assertEquals(2, vms.size());
        
        List<String> vmIds = vms.stream().map(VirtualMachine::getId).collect(Collectors.toList());
        assertTrue(vmIds.contains("443"));
        assertTrue(vmIds.contains("446"));
        
        vms = vmsInfo.getRunningVMsByHostTemplate("localhost", "10");
        assertEquals(0, vms.size());
        
        vms = vmsInfo.getRunningVMsByHostTemplate("remotehost", "5");
        assertEquals(0, vms.size());
        
        vms = vmsInfo.getRunningVMsByHostTemplate("otherhost", "5");
        assertEquals(0, vms.size());
    }
    
    @Test
    void testGetRunningVMByName() throws Exception {
        VirtualMachine vm = vmsInfo.getRunningVMByName("nome443");
        assertNotNull(vm);
        assertEquals("443", vm.getId());

        vm = vmsInfo.getRunningVMByName("nome444");
        assertNull(vm);
    }

    @Test
    void testGetRunningVMsByHost() throws Exception {
        List<VirtualMachine> vms = vmsInfo.getRunningVMsByHost("localhost");
        assertEquals(2, vms.size());

        List<String> vmIds = vms.stream().map(VirtualMachine::getId).collect(Collectors.toList());
        assertTrue(vmIds.contains("443"));
        assertTrue(vmIds.contains("446"));

        vms = vmsInfo.getRunningVMsByHost("remotehost");
        assertEquals(1, vms.size());
        assertEquals("445", vms.get(0).getId());

        vms = vmsInfo.getRunningVMsByHost("otherhost");
        assertEquals(0, vms.size());
    }
}
