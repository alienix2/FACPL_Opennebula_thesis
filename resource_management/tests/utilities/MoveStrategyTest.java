package utilities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MoveStrategyTest {

    @TempDir
    Path tempDir;

    @Test
    public void testMoveFile() throws IOException {
        // Arrange
        Path sourceFile = tempDir.resolve("source.txt");
        Path targetFile = tempDir.resolve("target.txt");
        byte[] content = "Test content".getBytes();
        Files.write(sourceFile, content);

        FolderContentStrategy moveStrategy = new MoveStrategy();

        // Act
        moveStrategy.processFile(sourceFile.toString(), targetFile.toString());

        // Assert
        assertTrue(Files.exists(targetFile));
        assertFalse(Files.exists(sourceFile));
        byte[] targetContent = Files.readAllBytes(targetFile);
        assertArrayEquals(content, targetContent);
    }
    
    @Test
    public void testMoveNonExistentFile() {
        Path sourceFile = tempDir.resolve("nonexistent.txt");
        Path targetFile = tempDir.resolve("target.txt");

        FolderContentStrategy moveStrategy = new MoveStrategy();

        assertThrows(IOException.class, () -> {
            moveStrategy.processFile(sourceFile.toString(), targetFile.toString());
        });
    }
}