package opennebula_api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opennebula.client.Client;

import opennebula_api.HostInfo;

class HostInfoTest {

	private static final int TEST_HOST_ID = 123;
	private Client validMockClient;
	private Client invalidMockClient;
	private HostInfo hostInfo;
    private static final String VALID_XML_RESPONSE = "<HOST><TOTAL_CPU>1600</TOTAL_CPU><CPU_USAGE>400</CPU_USAGE><TOTAL_MEM>2048</TOTAL_MEM><MEM_USAGE>1024</MEM_USAGE></HOST>";
    private static final String INVALID_XML_RESPONSE = "<HOST><TOTAL_CPU></TOTAL_CPU><CPU_USAGE></CPU_USAGE><TOTAL_MEM></TOTAL_MEM><MEM_USAGE></MEM_USAGE></HOST>";
    
    @BeforeEach
    void setUp() throws Exception {
        invalidMockClient = new MockClient("fake-auth", "http://fake-endpoint:123/random", INVALID_XML_RESPONSE, null);
        validMockClient = new MockClient("fake-auth", "http://fake-endpoint:123/random", VALID_XML_RESPONSE, null);
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

        assertEquals(0, availableCpu);
    }
    
    @Test
    void testGetAvailableMemoryWithInvalidResponse() throws Exception {
        hostInfo = HostInfo.withClient(invalidMockClient);
        int availableMem = hostInfo.getAvailableCpu(TEST_HOST_ID);

        assertEquals(0, availableMem);
    }
}
