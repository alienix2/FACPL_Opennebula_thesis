package utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CopyStrategyTest {

    @TempDir
    Path tempDir;

    @Test
    public void testCopyFile() throws IOException {
        Path sourceFile = tempDir.resolve("source.txt");
        Path targetFile = tempDir.resolve("target.txt");
        byte[] content = "Test content".getBytes();
        Files.write(sourceFile, content);

        FolderContentStrategy copyStrategy = new CopyStrategy();

        copyStrategy.processFile(sourceFile.toString(), targetFile.toString());

        assertTrue(Files.exists(targetFile));
        byte[] targetContent = Files.readAllBytes(targetFile);
        assertArrayEquals(content, targetContent);
    }
    
    @Test
    public void testCopyNonExistentFile() {
        Path sourceFile = tempDir.resolve("nonexistent.txt");
        Path targetFile = tempDir.resolve("target.txt");

        FolderContentStrategy copyStrategy = new CopyStrategy();

        assertThrows(IOException.class, () -> {
            copyStrategy.processFile(sourceFile.toString(), targetFile.toString());
        });
    }
}
