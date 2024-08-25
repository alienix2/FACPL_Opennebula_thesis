package utilities;

import java.io.IOException;

interface FolderContentStrategy {
    void processFile(String source, String target) throws IOException;
    String getOperationName();
}
