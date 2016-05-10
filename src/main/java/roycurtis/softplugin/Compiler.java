package roycurtis.softplugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import javax.tools.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static roycurtis.softplugin.SoftPlugin.SOFTLOG;

class Compiler
{
    JavaCompiler javac;

    StandardJavaFileManager fileManager;

    ArrayList<File> sourceFiles;

    Compiler()
    {
        javac = ToolProvider.getSystemJavaCompiler();

        if (javac == null)
            throw new RuntimeException("Could not get javac; is server running on JDK?");

        SOFTLOG.fine("Compiler created");
    }

    void begin()
    {
        discoverFiles();

        int fileCount = sourceFiles.size();

        if (fileCount < 1)
        {
            SOFTLOG.info("No source files found; SoftPlugin will do nothing");
            return;
        }
        else
            SOFTLOG.fine("Discovered source files: " + fileCount);

        fileManager = javac.getStandardFileManager(null, null, Charset.forName("UTF-8"));

        Iterable<? extends JavaFileObject> toCompile = fileManager.getJavaFileObjectsFromFiles(sourceFiles);

        String bukkitJar = Bukkit.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        JavaCompiler.CompilationTask task = javac.getTask(null, fileManager, null,
            Arrays.asList(
                "-d", Config.Paths.cache.getAbsolutePath(),
                "-classpath", bukkitJar
            ),
            null, toCompile);

        task.call();
    }

    private void discoverFiles()
    {
        Path sourcePath = Config.Paths.source.toPath();
        sourceFiles = new ArrayList<>();

        try (Stream<Path> fileWalker = Files.walk(sourcePath))
        {
            fileWalker
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    File file = path.toFile();

                    SOFTLOG.finest("Discovered source file " + path);
                    sourceFiles.add(file);
                });
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not discover source files", e);
        }
    }
}