package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opennebula.client.ClientConfigurationException;
import org.xml.sax.SAXException;

class ApiCallTemplateTest {
	
	private MockApiCall apiCall;
	private StringBuilderLogHandler mockLogHandler;

	
	@BeforeEach
	void setUp() throws ClientConfigurationException {
		Logger mockLogger = Logger.getLogger(ApiCallTemplateTest.class.getName());
		mockLogHandler = new StringBuilderLogHandler();
		mockLogger.addHandler(mockLogHandler);
		MockClient mockClient = new MockClient(null, null, null, null);
		apiCall = new MockApiCall(mockClient, mockLogger);	
	}

	@Test
	void testCallApi() throws ClientConfigurationException, InterruptedException, SAXException, ParserConfigurationException, IOException {
		apiCall.eval(null);
		assertEquals("INFO: Client initialized.\n"
				+ "INFO: Success message\n"
				+ "SEVERE: Error message\n", 
				mockLogHandler.getLogBuilder());
	}
}
