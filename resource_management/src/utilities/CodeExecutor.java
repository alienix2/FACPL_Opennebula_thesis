package utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class CodeExecutor {

    private final String outputDir;
    private final Logger logger;

    public CodeExecutor(String outputDir, Logger logger) {
        this.outputDir = outputDir;
        this.logger = logger;
    }

    public boolean compileJavaFiles() throws IOException {
        logger.info("Starting compilation of Java files in directory: " + outputDir);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> javaFiles = findJavaFiles(Paths.get(outputDir));

        if (javaFiles.isEmpty()) {
            logger.warning("No Java files found in directory: " + outputDir);
            throw new IOException("No Java files found in directory: " + outputDir);
        }

        logger.info("Compiling Java files...");
        int result = compiler.run(null, null, null, javaFiles.toArray(new String[0]));

        boolean success = result == 0;
        if (success) {
            logger.info("Compilation successful.");
        } else {
            logger.severe("Compilation failed with result code: " + result);
            throw new RuntimeException("Compilation failed with result code: " + result);
        }
        return success;
    }

    public void runCompiledClass(String className, String dir) throws Exception {
        logger.info("Running compiled class: " + className);
        File outputDirFile = new File(dir);
        try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { outputDirFile.toURI().toURL() })) {
            Class<?> clazz = classLoader.loadClass(className);
            Method mainMethod = clazz.getMethod("main", String[].class);
            String[] args = {};
            mainMethod.invoke(null, (Object) args);
            logger.info("Successfully ran class: " + className);
        } catch (Exception e) {
            logger.severe("Error running class: " + className);
            throw e;
        }
    }

    private List<String> findJavaFiles(Path directory) throws IOException {
        List<String> javaFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.list(directory)) {
            paths.filter(path -> path.toString().endsWith(".java"))
                 .forEach(path -> javaFiles.add(path.toString()));
        }
        return javaFiles;
    }
}