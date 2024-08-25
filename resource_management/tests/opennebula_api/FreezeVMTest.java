package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class FreezeVMTest {

    private FreezeVM freezeVM;
    private MockOpenNebulaActionContext mockContext;
    private StringBuilderLogHandler logHandler;
    private MockVirtualMachineService mockVirtualMachineService;
    private VMsInfo vmsInfo;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize a mock client and logger
        Logger logger = Logger.getLogger("TestLogger");
        logHandler = new StringBuilderLogHandler();
        logger.addHandler(logHandler);
        MockClientTrue mockClient = new MockClientTrue("123");

        mockContext = new MockOpenNebulaActionContext(mockClient, logger);
        mockVirtualMachineService = new MockVirtualMachineService();
        vmsInfo = VMsInfo.withCustomLogger(mockVirtualMachineService, logger);
        mockContext.setVMsInfo(vmsInfo);

        freezeVM = new FreezeVM(mockContext);
    }

    @Test
    public void testEval_VmSuspended() {
        List<VMDescriptor> runningVMs = new ArrayList<>();
        runningVMs.add(new VMDescriptor("123", "TestVM", "101", "host_1", "ACTIVE", "RUNNING"));
        mockVirtualMachineService.setRunningVirtualMachineInfo(runningVMs);

        List<Object> args = new ArrayList<>();
        args.add("host_1");
        args.add(null);
        args.add("101");

        // Call eval method
        freezeVM.eval(args);

        // Assert that the log contains information about the suspension
        String log = logHandler.getLogBuilder();
        System.out.println(log);
        assertTrue(log.contains("Suspending (Freezing) 1 VM of [host, template]: [host_1 101]"));
        assertTrue(log.contains("INFO: 123"));
    }
    
    @Test
    public void testEval_NoVmFound() {
        // Ensure no running VMs are in the mockVirtualMachineService
        mockVirtualMachineService.setRunningVirtualMachineInfo(new ArrayList<>()); // Empty list

        // Create input arguments: [host, null, template]
        List<Object> args = new ArrayList<>();
        args.add("host_1");
        args.add(null);
        args.add("101");

        // Call eval method
        freezeVM.eval(args);

        // Check the log for "No VM found" message
        String log = logHandler.getLogBuilder();
        assertTrue(log.contains("SEVERE: No VM found"));
    }
}