package utilities;

import java.io.IOException;

public interface CodeExecutorInterface {
    boolean compileJavaFiles() throws IOException;
    void runCompiledClass(String className, String dir) throws Exception;
}