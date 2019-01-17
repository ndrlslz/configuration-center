package com.ndrlslz.configuration.center.sdk.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;

public class MemoryCacheFileWriter {
    private static final String PATH = "configuration-center-failover/memory-cache/configurations.properties";

    public void write() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        Path path = Paths.get(PATH);

        Files.deleteIfExists(path);
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        Files.write(path, "123\n".getBytes(), APPEND);
    }

    public static void main(String[] args) throws IOException {
        new MemoryCacheFileWriter().write();
    }
}
