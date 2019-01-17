package com.ndrlslz.configuration.center.sdk.reader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigurationFileReader {
    public Properties read(Path path) throws IOException {
        Properties properties = new Properties();

        InputStream inputStream = Files.newInputStream(path);
        properties.load(inputStream);

        return properties;
    }
}
