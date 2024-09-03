package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opennebula.client.Client;

class HostInfoTest {

	private static final String TEST_HOST_ID = "123";
	private Client validMockClient;
	private Client invalidMockClient;
	private HostInfo hostInfo;
    private static final String VALID_XML_RESPONSE = "<HOST><TOTAL_CPU>1600</TOTAL_CPU><CPU_USAGE>400</CPU_USAGE><TOTAL_MEM>2048</TOTAL_MEM><MEM_USAGE>1024</MEM_USAGE></HOST>";
    private static final String INVALID_XML_RESPONSE = "<HOST><TOTAL_CPU></TOTAL_CPU><CPU_USAGE></CPU_USAGE><TOTAL_MEM></TOTAL_MEM><MEM_USAGE></MEM_USAGE></HOST>";
    
    @BeforeEach
    void setUp() throws Exception {
        invalidMockClient = new MockClientFalse(INVALID_XML_RESPONSE);
        validMockClient = new MockClientTrue(VALID_XML_RESPONSE);
    }
    
    @Test
    void testGetAvailableCpuValidResponse() throws Exception {
        hostInfo = HostInfo.withClient(validMockClient);
        int availableCpu = hostInfo.getAvailableCpu(TEST_HOST_ID);

        assertEquals(1200, availableCpu); // 1600 - 400
    }
    
    @Test
    void testGetAvailableMemValidResponse() throws Exception {
        hostInfo = HostInfo.withClient(validMockClient);
        long availableMem = hostInfo.getAvailableMem(TEST_HOST_ID);

        assertEquals(1024, availableMem); // 2048 - 1024
    }
    
    @Test
    void testGetAvailableCpuWithInvalidResponse() throws Exception {
        hostInfo = HostInfo.withClient(invalidMockClient);
        int availableCpu = hostInfo.getAvailableCpu(TEST_HOST_ID);

        assertEquals(-1, availableCpu);
    }
    
    @Test
    void testGetAvailableMemoryWithInvalidResponse() throws Exception {
        hostInfo = HostInfo.withClient(invalidMockClient);
        int availableMem = hostInfo.getAvailableCpu(TEST_HOST_ID);

        assertEquals(-1, availableMem);
    }
}
