package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class FileMerger {
    private final List<String> filePaths;
    private final Logger logger;

    public FileMerger(List<String> filePaths) {
        this.filePaths = filePaths;
        this.logger = Logger.getLogger(FileMerger.class.getName());
    }

    public FileMerger(List<String> filePaths, Logger logger) {
        this.filePaths = filePaths;
        this.logger = logger;
    }

    public String mergeFiles(String outputFolder) throws IOException {
        Path outputFile = Paths.get(outputFolder, "system.fpl");
        try {
            List<Path> pathList = filePaths.stream().map(Paths::get).collect(Collectors.toList());
            appendFiles(pathList, outputFile);
            logger.info("Files merged successfully to " + outputFile.toString());
        } catch (IOException e) {
            logger.severe("Failed to merge files: " + e.getMessage());
            throw e;
        }
        return outputFile.toString();
    }

    private void appendFiles(List<Path> sourceFiles, Path destinationFile) throws IOException {
        Files.write(destinationFile, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        for (Path sourceFile : sourceFiles) {
            byte[] content = Files.readAllBytes(sourceFile);
            Files.write(destinationFile, content, StandardOpenOption.APPEND);
        }
    }
}