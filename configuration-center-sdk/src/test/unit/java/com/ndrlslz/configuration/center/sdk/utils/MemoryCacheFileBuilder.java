package com.ndrlslz.configuration.center.sdk.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class MemoryCacheFileBuilder {
    public static final Path MEMORY_CACHE_PATH = Paths.get("configuration-center-failover/memory-cache/configuration.properties");
    private HashMap<String, String> map;

    public MemoryCacheFileBuilder() {
        this.map = new HashMap<>();
    }

    public MemoryCacheFileBuilder property(String name, String value) {
        map.put(name, value);
        return this;
    }

    public void create() throws IOException {
        Files.deleteIfExists(MEMORY_CACHE_PATH);
        Files.createDirectories(MEMORY_CACHE_PATH.getParent());
        Files.createFile(MEMORY_CACHE_PATH);

        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(key).append("=").append(value).append(System.lineSeparator()));

        Files.write(MEMORY_CACHE_PATH, stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
    }

}
