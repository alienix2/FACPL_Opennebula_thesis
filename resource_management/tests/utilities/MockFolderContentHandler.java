package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class MockFolderContentHandler extends FolderContentHandler {

    private final boolean shouldFail;

    public MockFolderContentHandler(Logger logger, boolean shouldFail) {
        super(logger);
        this.shouldFail = shouldFail;
    }

    @Override
    public void processFolderContents(String sourceDir, String targetDir, FolderContentStrategy strategy) throws IOException {
        if (shouldFail) {
            throw new IOException("Simulated failure");
        }

        Path targetPath = Paths.get(targetDir);
        if (Files.notExists(targetPath)) {
            Files.createDirectories(targetPath);
        }
    }
}
