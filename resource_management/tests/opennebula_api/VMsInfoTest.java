package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VMsInfoTest {

    private final MockVirtualMachineService mockService = new MockVirtualMachineService();

    @Test
    void testGetRunningVMsByHostTemplate() {
    	mockService.setRunningVirtualMachineInfo(Arrays.asList(
                new VMDescriptor("1", "VM1", "template1", "host1", "ACTIVE", "RUNNING"),
                new VMDescriptor("2", "VM2", "template2", "host2", "ACTIVE", "RUNNING"),
                new VMDescriptor("3", "VM3", "template2", "host1", "ACTIVE", "RUNNING")
    			));

        VMsInfo vmsInfo = VMsInfo.withDefaultLogger(mockService);

        List<VMDescriptor> result = vmsInfo.getRunningVMsByHostTemplate("host1", "template2");

        assertEquals(1, result.size());
        assertEquals("VM3", result.get(0).getVmName());
        
        result = vmsInfo.getRunningVMsByHostTemplate("host1", "template1");

        assertEquals(1, result.size());
        assertEquals("VM1", result.get(0).getVmName());
    }

    @Test
    void testGetRunningVMsByHost() {
    	mockService.setRunningVirtualMachineInfo(Arrays.asList(
                new VMDescriptor("1", "VM1", "template1", "host1", "ACTIVE", "RUNNING"),
                new VMDescriptor("2", "VM2", "template2", "host2", "ACTIVE", "RUNNING"),
                new VMDescriptor("5", "VM5", "template1", "host1", "ACTIVE", "RUNNING")
    			));

        VMsInfo vmsInfo = VMsInfo.withDefaultLogger(mockService);

        List<VMDescriptor> result = vmsInfo.getRunningVMsByHost("host1");

        assertEquals(2, result.size());
        assertEquals("VM1", result.get(0).getVmName());
        assertEquals("VM5", result.get(1).getVmName());
        
        result = vmsInfo.getRunningVMsByHost("host2");

        assertEquals(1, result.size());
        assertEquals("VM2", result.get(0).getVmName());
    }
    
    @Test
    void testGetRunningVMByName() {
    	mockService.setRunningVirtualMachineInfo(Arrays.asList(
                new VMDescriptor("1", "VM1", "template1", "host1", "ACTIVE", "RUNNING"),
                new VMDescriptor("2", "VM2", "template1", "host2", "ACTIVE", "RUNNING")
    			));

        VMsInfo vmsInfo = VMsInfo.withDefaultLogger(mockService);

        VMDescriptor result = vmsInfo.getRunningVMByName("VM1");

        assertEquals("VM1", result.getVmName());
    }

    @Test
    void testCountRunningVMsByHost() {
    	mockService.setRunningVirtualMachineInfo(Arrays.asList(
                new VMDescriptor("1", "VM1", "template1", "host1", "ACTIVE", "RUNNING"),
                new VMDescriptor("2", "VM2", "template1", "host2", "ACTIVE", "RUNNING"),
                new VMDescriptor("3", "VM3", "template2", "host1", "ACTIVE", "RUNNING")
    			));

        VMsInfo vmsInfo = VMsInfo.withDefaultLogger(mockService);

        Long count = vmsInfo.countRunningVMsByHost("host1");

        assertEquals(2, count);
    }
    @Test
    void testEmptyResults() {
        mockService.setRunningVirtualMachineInfo(Collections.emptyList());

        VMsInfo vmsInfo = VMsInfo.withDefaultLogger(mockService);

        List<VMDescriptor> resultList = vmsInfo.getRunningVMsByHostTemplate("host1", "template1");
        assertTrue(resultList.isEmpty());
        
        Long count = vmsInfo.countRunningVMsByHost("host1");
        assertEquals(0, count);
        
        VMDescriptor result = vmsInfo.getRunningVMByName("VM1");
        assertNull(result);
    }
}
