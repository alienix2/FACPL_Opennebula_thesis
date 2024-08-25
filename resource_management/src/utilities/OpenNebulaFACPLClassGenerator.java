package utilities;

import java.util.logging.Logger;

import generator.FacplGenerator;

public class OpenNebulaFACPLClassGenerator {
	
	private FileMerger fileMerger;
    private Logger logger;
	
	public OpenNebulaFACPLClassGenerator(FileMerger fileMerger) {
		this.fileMerger = fileMerger;
		this.logger = Logger.getLogger(OpenNebulaFACPLClassGenerator.class.getName());
	}
	
    public OpenNebulaFACPLClassGenerator(FileMerger fileMerger, Logger logger) {
        this.fileMerger = fileMerger;
        this.logger = logger;
    }
	
	public void generateClasses(String outputFolder) throws Exception {
		String inputFile = fileMerger.mergeFiles(outputFolder).toString();
		String[] args = {inputFile, outputFolder};
        try {
            FacplGenerator.java_compile(args);
            logger.info("FACPL class generation completed.");
            
        } catch (Exception e) {
            logger.severe("Error generating classes: " + e.getMessage());
            throw e;
        }
	}
}
