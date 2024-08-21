package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opennebula.client.ClientConfigurationException;

public class OpenNebulaActionBaseTest {

	private StringBuilderLogHandler mockLogHandler;
	private Logger mockLogger;
	
	@BeforeEach
	void setUp() throws ClientConfigurationException {
		mockLogger = Logger.getLogger(OpenNebulaActionBaseTest.class.getName());
		mockLogHandler = new StringBuilderLogHandler();
		mockLogger.addHandler(mockLogHandler);
	}
	
    @Test
    public void testLogResponseSuccess() {
        // Set up mock context and response
        MockOpenNebulaActionContext mockContext = new MockOpenNebulaActionContext(null, mockLogger);
        MockOneResponse mockResponse = new MockOneResponse(false, "Operation successful", null);

        // Instantiate the testable class
        TestAction action = new TestAction(mockContext);

        // Call the method under test
        action.logResponse(mockResponse);

        assertEquals("INFO: Operation successful\n", mockLogHandler.getLogBuilder());
    }

    @Test
    public void testLogResponseError() {
        // Set up mock context and response
        MockOpenNebulaActionContext mockContext = new MockOpenNebulaActionContext(null, mockLogger);
        MockOneResponse mockResponse = new MockOneResponse(true, null, "Operation failed");

        // Instantiate the testable class
        TestAction action = new TestAction(mockContext);

        // Call the method under test
        action.logResponse(mockResponse);

        assertEquals("SEVERE: Operation failed\n", mockLogHandler.getLogBuilder());
    }
}