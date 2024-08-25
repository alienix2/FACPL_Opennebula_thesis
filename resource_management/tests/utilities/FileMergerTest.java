package utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileMergerTest {
    private static final String TEMP_DIR = "temp_test_dir";
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Paths.get(TEMP_DIR);
        Files.createDirectories(tempDir);
    }

    @Test
    void testMergeFilesSuccessfully() throws IOException {
        Path file1 = tempDir.resolve("file1.txt");
        Path file2 = tempDir.resolve("file2.txt");
        Files.write(file1, "Content of file 1\n".getBytes());
        Files.write(file2, "Content of file 2".getBytes());

        List<String> filePaths = Arrays.asList(file1.toString(), file2.toString());
        FileMerger fileMerger = new FileMerger(filePaths);

        String resultFilePath = fileMerger.mergeFiles(TEMP_DIR);

        Path resultFile = Paths.get(resultFilePath);
        assertTrue(Files.exists(resultFile), "Merged file should exist");

        String resultContent = new String(Files.readAllBytes(resultFile));
        assertEquals("Content of file 1\nContent of file 2", resultContent);
    }

    @Test
    void testMergeWithEmptyFileList() throws IOException {
        List<String> filePaths = Arrays.asList();
        FileMerger fileMerger = new FileMerger(filePaths);

        String resultFilePath = fileMerger.mergeFiles(TEMP_DIR);

        Path resultFile = Paths.get(resultFilePath);
        assertTrue(Files.exists(resultFile));
        assertEquals(0, Files.size(resultFile));
    }

    @Test
    void testMergeWithNonExistentFiles() throws IOException {
        // Prepare a list with non-existent file paths
        List<String> filePaths = Arrays.asList("non_existent_file1.txt", "non_existent_file2.txt");
        FileMerger fileMerger = new FileMerger(filePaths);

        // Execute merge and expect an exception
        IOException exception = assertThrows(IOException.class, () -> fileMerger.mergeFiles(TEMP_DIR));
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("non_existent_file1.txt"));
    }
    
    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
             .sorted((p1, p2) -> p2.compareTo(p1))
             .forEach(path -> {
                 try {
                     Files.deleteIfExists(path);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             });
    }
}
