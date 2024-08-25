package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

class MoveStrategy implements FolderContentStrategy {
    @Override
    public void processFile(String source, String target) throws IOException {
        Files.move(Paths.get(source), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public String getOperationName() {
        return "Moved";
    }
}
