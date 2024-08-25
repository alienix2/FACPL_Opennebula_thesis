package utilities;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FolderContentHandler {

    private final Logger logger;

    public FolderContentHandler() {
        this.logger = Logger.getLogger(FolderContentHandler.class.getName());
    }

    public FolderContentHandler(Logger logger) {
        this.logger = logger;
    }

    public void processFolderContents(String sourceDir, String targetDir, FolderContentStrategy strategy) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        Path targetPath = Paths.get(targetDir);
        processFolderContentsInternal(sourcePath, targetPath, strategy);
    }

    private void processFolderContentsInternal(Path sourceDir, Path targetDir, FolderContentStrategy strategy) throws IOException {
        validateDirectories(sourceDir, targetDir);

        try (Stream<Path> paths = Files.walk(sourceDir)) {
            paths.filter(path -> !path.equals(sourceDir))
                 .forEach(source -> {
                    try {
                        Path target = targetDir.resolve(source.getFileName());
                        if (Files.isDirectory(source)) {
                            if (!Files.exists(target)) {
                                Files.createDirectories(target);
                            }
                            processFolderContentsInternal(source, target, strategy);
                        } else {
                            strategy.processFile(source.toString(), target.toString());
                            logger.info(strategy.getOperationName() + " file: " + source + " to " + target);
                        }
                    } catch (IOException e) {
                        logger.severe("Error processing file or directory: " + source + ". " + e.getMessage());
                    }
                });
        }
    }


    private void validateDirectories(Path sourceDir, Path targetDir) {
        if (!Files.isDirectory(sourceDir)) {
            throw new IllegalArgumentException("Source path is not a directory: " + sourceDir);
        }
        if (!Files.isDirectory(targetDir)) {
            throw new IllegalArgumentException("Target path is not a directory: " + targetDir);
        }
    }
}