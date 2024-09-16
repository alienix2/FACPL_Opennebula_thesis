package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utilities.StringBuilderLogHandler;

public class FreezeVMMultipleTest {

	private FreezeVMMultiple freezeVMMultiple;
	private MockOpenNebulaActionContext mockContext;
	private StringBuilderLogHandler logHandler;
	private MockVirtualMachineService mockVirtualMachineService;
	private VMsInfo vmsInfo;

	@BeforeEach
	public void setUp() throws Exception {
		Logger logger = Logger.getLogger("TestLogger");
		logHandler = new StringBuilderLogHandler();
		logger.addHandler(logHandler);
		MockClientTrue mockClient = new MockClientTrue("123");

		mockContext = new MockOpenNebulaActionContext(mockClient, logger);
		mockVirtualMachineService = new MockVirtualMachineService();
		vmsInfo = VMsInfo.withCustomLogger(mockVirtualMachineService, logger);
		mockContext.setVMsInfo(vmsInfo);

		freezeVMMultiple = new FreezeVMMultiple(mockContext);
	}

	@Test
	public void testEval_VmsFreeze() {
		List<VMDescriptor> runningVMs = new ArrayList<>();
		runningVMs.add(new VMDescriptor("123", "TestVM", "101", "host1", "ACTIVE", "RUNNING"));
		mockVirtualMachineService.setRunningVirtualMachineInfo(runningVMs);

		List<Object> args = new ArrayList<>();
		args.add("host1");
		args.add("1");
		args.add("101");

		freezeVMMultiple.eval(args);

		String log = logHandler.getLogBuilder();
		System.out.println(log);
		assertTrue(log.contains("Stopping (Freezing) 1 VM of [host template]: [host1 101]"));
		assertTrue(log.contains("INFO: 123"));
	}

	@Test
	public void testEval_NoVmFound() {
		mockVirtualMachineService.setRunningVirtualMachineInfo(new ArrayList<>());

		List<Object> args = new ArrayList<>();
		args.add("host1");
		args.add("1");
		args.add("101");

		freezeVMMultiple.eval(args);

		String log = logHandler.getLogBuilder();
		assertTrue(log.contains("SEVERE: Not enough VMs found, stopping the most VMs possible"));
	}

	@Test
	public void testEvalMultipleVmsFreeze() {
		List<VMDescriptor> runningVMs = new ArrayList<>();
		runningVMs.add(new VMDescriptor("123", "TestVM1", "101", "host1", "ACTIVE", "RUNNING"));
		runningVMs.add(new VMDescriptor("123", "TestVM2", "101", "host1", "ACTIVE", "RUNNING"));
		mockVirtualMachineService.setRunningVirtualMachineInfo(runningVMs);
		List<Object> args = new ArrayList<>();
		args.add("host1");
		args.add("2");
		args.add("101");

		freezeVMMultiple.eval(args);

		String log = logHandler.getLogBuilder();
		System.out.println(log);
		assertTrue(log.contains("INFO: Stopping (Freezing) 2 VM of [host template]: [host1 101]"));
		assertTrue(log.contains("INFO: 123"));
		assertTrue(log.contains("INFO: 123"));
	}
}