package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opennebula.client.ClientConfigurationException;
import org.xml.sax.SAXException;

import opennebula_api.CreateVM;

class CreateVMTest {

	private CreateVM vmCreator;
	private StringBuilderLogHandler mockLogHandler;
	
	@BeforeEach
	void setUp() throws ClientConfigurationException {
		Logger mockLogger = Logger.getLogger(ApiCallTemplateTest.class.getName());
		mockLogHandler = new StringBuilderLogHandler();
		mockLogger.addHandler(mockLogHandler);
		MockClient mockClient = new MockClient(null, null, null, null);
		vmCreator = new CreateVM(mockClient, mockLogger);	
	}
	
	@Test
	void testEvalSuccess() throws ClientConfigurationException, InterruptedException, SAXException, ParserConfigurationException, IOException {
		Object[] argsArray = new Object[] {0, "param1", 2};
		List<Object> args = Arrays.asList(argsArray);
		vmCreator.eval(args);
		assertEquals("INFO: Starting VM: [2, param1]\n"
					+ "INFO: 100\n"
					+ "INFO: Success message\n", mockLogHandler.getLogBuilder());
	}
}
