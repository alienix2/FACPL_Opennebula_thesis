package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utilities.StringBuilderLogHandler;

public class ReleaseVMTest {

    private ReleaseVM releaseVM;
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

        releaseVM = new ReleaseVM(mockContext);
    }

    @Test
    public void testEval_VMReleased() {
        // Simulate a running VM by adding it to the mockVirtualMachineService
    	List<VMDescriptor> runningVMs = new ArrayList<>();
        runningVMs.add(new VMDescriptor("123", "TestVM", "101", "host_1", "ACTIVE", "RUNNING"));
        mockVirtualMachineService.setRunningVirtualMachineInfo(runningVMs);

        List<Object> args = Arrays.asList(null, "TestVM");

        // Call eval method
        releaseVM.eval(args);

        String log = logHandler.getLogBuilder();
        System.out.println(log);
        assertTrue(log.contains("Stopping (Releasing) the VM: TestVM"));
        assertTrue(log.contains("INFO: 123"));
    }

    @Test
    public void testEval_NoVmFound() {
        mockVirtualMachineService.setRunningVirtualMachineInfo(new ArrayList<>()); // Empty list
        List<Object> args = Arrays.asList(null, "TestVM");

        releaseVM.eval(args);

        String log = logHandler.getLogBuilder();
        assertTrue(log.contains("SEVERE: No VM found"), "Log should contain the 'No VM found' message");
    }
}