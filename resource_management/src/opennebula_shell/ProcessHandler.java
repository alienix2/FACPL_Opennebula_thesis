package opennebula_shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.logging.Logger;

public class ProcessHandler {
	
    private final Logger logger;
	
	public ProcessHandler(Logger logger) {
		this.logger = logger;
	}
	
	public void handle(Process process) throws IOException {
        try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            String line;
            while ((line = outputReader.readLine()) != null) {
                logger.info(line);
            }
            while ((line = errorReader.readLine()) != null) {
                logger.severe(line);
            }
        }
    }
}
