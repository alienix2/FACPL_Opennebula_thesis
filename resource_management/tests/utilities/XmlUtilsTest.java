package utilities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.w3c.dom.Document;

public class XmlUtilsTest {

    @Test
    void testParseValidXml() {
        String xmlData = "<VM><ID>1</ID><NAME>TestVM</NAME></VM>";
        
        Document document = XmlUtils.parseXml(xmlData);
        
        assertNotNull(document);
        assertEquals("1", document.getElementsByTagName("ID").item(0).getTextContent());
        assertEquals("TestVM", document.getElementsByTagName("NAME").item(0).getTextContent());
    }

    @Test
    void testParseMalformedXml() {
        String malformedXml = "<VM><ID>1<ID><NAME>TestVM</NAME></VM>";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            XmlUtils.parseXml(malformedXml);
        });
        
        assertTrue(exception.getMessage().contains("Failed to parse XML"));
    }

    @Test
    void testParseEmptyXml() {
        String emptyXml = "";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            XmlUtils.parseXml(emptyXml);
        });

        assertTrue(exception.getMessage().contains("Failed to parse XML"));
    }

    @Test
    void testParseXmlWithMissingElements() {
        String xmlData = "<VM><ID>1</ID></VM>";
        
        Document document = XmlUtils.parseXml(xmlData);
        
        assertNotNull(document);
        assertEquals("1", document.getElementsByTagName("ID").item(0).getTextContent());
        assertNull(document.getElementsByTagName("NAME").item(0));  // Should be null since NAME is missing
    }
}

