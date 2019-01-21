package com.ndrlslz.configuration.center.sdk.writer;

import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.lang.System.lineSeparator;

public class ConfigurationFileWriter {
    private static final String PROPERTY_FORMAT = "%s=%s";

    public void write(Path path) throws IOException {
        Files.deleteIfExists(path);
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        String properties = readProperties();

        Files.write(path, properties.getBytes());
    }

    private String readProperties() {
        StringBuilder propertiesBuilder = new StringBuilder();

        ZookeeperStorage.forEach((key, value) -> propertiesBuilder
                .append(format(PROPERTY_FORMAT, key, value))
                .append(lineSeparator()));

        return propertiesBuilder.toString();
    }
}
