package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class OpenNebulaVMServiceTest {

    @Test
    void testGetVirtualMachinesInfo() {
        String xmlData1 = "<VM><ID>1</ID><NAME>VM1</NAME><TEMPLATE_ID>100</TEMPLATE_ID><HID>1</HID><STATE>ACTIVE</STATE></VM>";
        String xmlData2 = "<VM><ID>2</ID><NAME>VM2</NAME><TEMPLATE_ID>101</TEMPLATE_ID><HID>2</HID><STATE>STOPPED</STATE></VM>";

        MockVirtualMachine vm1 = new MockVirtualMachine("1", "VM1", "ACTIVE", "RUNNING", xmlData1);
        MockVirtualMachine vm2 = new MockVirtualMachine("2", "VM2", "STOPPED", "RUNNING", xmlData2);
        MockVirtualMachine vm3 = new MockVirtualMachine("3", "VM3", "ACTIVE", "PROLOG", xmlData1);

        MockVirtualMachinePool vmPool = new MockVirtualMachinePool(Arrays.asList(vm1, vm2, vm3));

        OpenNebulaVMService service = new OpenNebulaVMService(vmPool);
        List<VMDescriptor> vmDescriptors = service.getVirtualMachinesInfo();

        assertEquals(3, vmDescriptors.size());
        assertEquals("1", vmDescriptors.get(0).getVmId());
        assertEquals("VM1", vmDescriptors.get(0).getVmName());

        assertEquals("2", vmDescriptors.get(1).getVmId());
        assertEquals("VM2", vmDescriptors.get(1).getVmName());
        
        assertEquals("3", vmDescriptors.get(2).getVmId());
        assertEquals("VM3", vmDescriptors.get(2).getVmName());

        assertEquals("3", vmDescriptors.get(2).getVmId());
        assertEquals("VM3", vmDescriptors.get(2).getVmName());
    }

    @Test
    void testGetRunningVirtualMachineInfo() {
        String xmlData1 = "<VM><ID>1</ID><NAME>VM1</NAME><TEMPLATE_ID>100</TEMPLATE_ID><HID>1</HID><STATE>ACTIVE</STATE></VM>";
        String xmlData2 = "<VM><ID>2</ID><NAME>VM2</NAME><TEMPLATE_ID>101</TEMPLATE_ID><HID>2</HID><STATE>STOPPED</STATE></VM>";

        MockVirtualMachine vm1 = new MockVirtualMachine("1", "VM1", "ACTIVE", "RUNNING", xmlData1);
        MockVirtualMachine vm2 = new MockVirtualMachine("2", "VM2", "STOPPED", "RUNNING", xmlData2);
        MockVirtualMachine vm3 = new MockVirtualMachine("3", "VM3", "ACTIVE", "PROLOG", xmlData1);


        MockVirtualMachinePool vmPool = new MockVirtualMachinePool(Arrays.asList(vm1, vm2, vm3));

        OpenNebulaVMService service = new OpenNebulaVMService(vmPool);
        List<VMDescriptor> runningVMs = service.getRunningVirtualMachineInfo();

        assertEquals(1, runningVMs.size());
        assertEquals("1", runningVMs.get(0).getVmId());
        assertEquals("ACTIVE", runningVMs.get(0).getState());
    }
}
