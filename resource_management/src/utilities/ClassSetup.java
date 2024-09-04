package utilities;

import org.apache.commons.configuration2.ex.ConfigurationException;

public interface ClassSetup {
    void setup(String contextFileLocation, String javaFilesDir) throws ConfigurationException;
}
