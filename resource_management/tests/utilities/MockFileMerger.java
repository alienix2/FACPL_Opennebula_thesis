package utilities;

import java.util.Arrays;

public class MockFileMerger extends FileMerger {
    private final String mergedFilePath;

    public MockFileMerger(String mergedFilePath) {
        super(Arrays.asList()); // Pass an empty list or adjust based on your needs
        this.mergedFilePath = mergedFilePath;
    }

    @Override
    public String mergeFiles(String outputFolder) {
        return mergedFilePath;
    }
}

