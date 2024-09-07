package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opennebula.client.ClientConfigurationException;

import utilities.StringBuilderLogHandler;

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
        MockOpenNebulaActionContext mockContext = new MockOpenNebulaActionContext(null, mockLogger);
        MockOneResponse mockResponse = new MockOneResponse(false, "Operation successful", null);

        TestAction action = new TestAction(mockContext);

        action.logResponse(mockResponse);

        assertEquals("INFO: Operation successful\n", mockLogHandler.getLogBuilder());
    }

    @Test
    public void testLogResponseError() {
        MockOpenNebulaActionContext mockContext = new MockOpenNebulaActionContext(null, mockLogger);
        MockOneResponse mockResponse = new MockOneResponse(true, null, "Operation failed");

        TestAction action = new TestAction(mockContext);

        action.logResponse(mockResponse);

        assertEquals("SEVERE: Operation failed\n", mockLogHandler.getLogBuilder());
    }
}