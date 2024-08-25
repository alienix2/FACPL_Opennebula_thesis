package utilities;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class OpenNebulaFACPLClassSetup {

    private final Logger logger;

    public OpenNebulaFACPLClassSetup() {
		this.logger = Logger.getLogger(OpenNebulaFACPLClassGenerator.class.getName());
    }

    public OpenNebulaFACPLClassSetup(Logger logger) {
        this.logger = logger;
    }

    public void setup(List<String> fileLocations, String additionalFilesFolder, String outputFolder) {
        logger.info("Starting setup...");

        OpenNebulaFACPLClassGenerator classGenerator = new OpenNebulaFACPLClassGenerator(new FileMerger(fileLocations, logger), logger);
        try {
            classGenerator.generateClasses("tmp/FACPLFiles");
            logger.info("Class generation completed successfully.");
        } catch (Exception e) {
            logger.severe("Failed to generate classes: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            FolderContentHandler folderManager = new FolderContentHandler(logger);
            folderManager.processFolderContents("tmp/FACPLFiles/", outputFolder, new MoveStrategy());
            //folderManager.processFolderContents(additionalFilesFolder, outputFolder, new CopyStrategy());
            logger.info("Folder contents moved successfully.");
        } catch (IOException e) {
            logger.severe("Failed to move folder contents: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
