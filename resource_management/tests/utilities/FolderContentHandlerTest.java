package utilities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

public class FolderContentHandlerTest {

    @TempDir
    Path tempDir;

    private Path sourceDir;
    private Path targetDir;
    private FolderContentHandler folderContentHandler;
    private StringBuilderLogHandler logHandler;

    @BeforeEach
    void setUp() throws IOException {
        sourceDir = tempDir.resolve("source");
        targetDir = tempDir.resolve("target");
        Files.createDirectory(sourceDir);
        Files.createDirectory(targetDir);
        
        logHandler = new StringBuilderLogHandler();
        Logger logger = Logger.getLogger(FolderContentHandler.class.getName());
        logger.addHandler(logHandler);
        folderContentHandler = new FolderContentHandler(logger);
    }

    @Test
    void testCopyStrategy() throws IOException {
        Path file1 = sourceDir.resolve("file1.txt");
        Files.write(file1, "content".getBytes());

        FolderContentStrategy copyStrategy = new CopyStrategy();
        folderContentHandler.processFolderContents(sourceDir.toString(), targetDir.toString(), copyStrategy);

        Path copiedFile = targetDir.resolve("file1.txt");
        assertTrue(Files.exists(copiedFile));
        assertArrayEquals(Files.readAllBytes(file1), Files.readAllBytes(copiedFile));

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("INFO: Copied file:"));
    }

    @Test
    void testMoveStrategy() throws IOException {
        Path file1 = sourceDir.resolve("file1.txt");
        Files.write(file1, "content".getBytes());

        FolderContentStrategy moveStrategy = new MoveStrategy();
        folderContentHandler.processFolderContents(sourceDir.toString(), targetDir.toString(), moveStrategy);

        Path movedFile = targetDir.resolve("file1.txt");
        assertFalse(Files.exists(file1)); // File should be moved
        assertTrue(Files.exists(movedFile)); // File should be in targetDir
        assertArrayEquals("content".getBytes(), Files.readAllBytes(movedFile));

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("INFO: Moved file:"));
    }

    @Test
    void testInvalidSourceDirectory() {
        Path invalidSourceDir = tempDir.resolve("invalid_source");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            folderContentHandler.processFolderContents(invalidSourceDir.toString(), targetDir.toString(), new CopyStrategy());
        });

        assertTrue(exception.getMessage().contains("Source path is not a directory"));
    }

    @Test
    void testInvalidTargetDirectory() throws IOException {
        Path invalidTargetDir = tempDir.resolve("invalid_target");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            folderContentHandler.processFolderContents(sourceDir.toString(), invalidTargetDir.toString(), new CopyStrategy());
        });

        assertTrue(exception.getMessage().contains("Target path is not a directory"));
    }

    @Test
    void testEmptyDirectories() throws IOException {
        Path emptySourceDir = tempDir.resolve("empty_source");
        Path emptyTargetDir = tempDir.resolve("empty_target");
        Files.createDirectory(emptySourceDir);
        Files.createDirectory(emptyTargetDir);

        FolderContentStrategy copyStrategy = new CopyStrategy();
        folderContentHandler.processFolderContents(emptySourceDir.toString(), emptyTargetDir.toString(), copyStrategy);

        assertFalse(Files.list(emptyTargetDir).findFirst().isPresent());
    }

    @Test
    void testLogging() throws IOException {
        Path file1 = sourceDir.resolve("file1.txt");
        Files.write(file1, "content".getBytes());

        FolderContentStrategy copyStrategy = new CopyStrategy();
        folderContentHandler.processFolderContents(sourceDir.toString(), targetDir.toString(), copyStrategy);

        String logOutput = logHandler.getLogBuilder();
        assertTrue(logOutput.contains("INFO: Copied file:"));
    }
}