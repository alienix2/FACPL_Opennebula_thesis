package utilities;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class OpenNebulaFACPLClassSetup implements ClassSetup{

    private final Logger logger;
    OpenNebulaFACPLClassGenerator classGenerator;

    public OpenNebulaFACPLClassSetup(List<String> fileLocations) {
		this.logger = Logger.getLogger(OpenNebulaFACPLClassGenerator.class.getName());
        this.classGenerator = new OpenNebulaFACPLClassGenerator(new FileMerger(fileLocations, logger), logger);
    }

    public OpenNebulaFACPLClassSetup(Logger logger, List<String> fileLocations) {
        this.logger = logger;
        this.classGenerator = new OpenNebulaFACPLClassGenerator(new FileMerger(fileLocations, logger), logger);
    }
    
    public OpenNebulaFACPLClassSetup(Logger logger, OpenNebulaFACPLClassGenerator classGenerator) {
        this.logger = logger;
        this.classGenerator = classGenerator;
    }

    @Override
    public void setup(String additionalFilesFolder, String outputFolder) {
        logger.info("Starting setup...");

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
            folderManager.processFolderContents(additionalFilesFolder, outputFolder, new CopyStrategy());
            logger.info("Folder contents handled successfully.");
        } catch (IOException e) {
            logger.severe("Failed to move folder contents: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
