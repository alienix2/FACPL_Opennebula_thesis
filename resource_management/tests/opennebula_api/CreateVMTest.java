package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.opennebula.client.ClientConfigurationException;
import org.junit.jupiter.api.BeforeEach;

import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;

public class CreateVMTest {

	private StringBuilderLogHandler mockLogHandler;
	private Logger mockLogger;
	
	@BeforeEach
	void setUp() throws ClientConfigurationException {
		mockLogger = Logger.getLogger(CreateVMTest.class.getName());
		mockLogHandler = new StringBuilderLogHandler();
		mockLogger.addHandler(mockLogHandler);
	}
	
    @Test
    public void testEvalSuccess() throws ClientConfigurationException {
    	MockClientTrue mockClient = new MockClientTrue("150");
    	MockOpenNebulaActionContext mockContext = new MockOpenNebulaActionContext(mockClient, mockLogger);
    	
        CreateVM createVMAction = new CreateVM(mockContext);

        List<Object> args = Arrays.asList(0, "VMName", 1);
        createVMAction.eval(args);

        assertTrue(mockLogHandler.getLogBuilder().contains("INFO: Starting VM: [1, VMName]\n"));
        assertTrue(mockLogHandler.getLogBuilder().contains("INFO: 150\n"));
    }

    @Test
    public void testEvalFailure() throws ClientConfigurationException {
    	MockClientFalse mockClient = new MockClientFalse("150");
    	MockOpenNebulaActionContext mockContext = new MockOpenNebulaActionContext(mockClient, mockLogger);
    	
        CreateVM createVMAction = new CreateVM(mockContext);

        List<Object> args = Arrays.asList(0, "VMName", 1);
        createVMAction.eval(args);;
        
        assertTrue(mockLogHandler.getLogBuilder().contains("INFO: Starting VM: [1, VMName]\n"));
        assertTrue(mockLogHandler.getLogBuilder().contains("SEVERE: 150\n"));
    }
}