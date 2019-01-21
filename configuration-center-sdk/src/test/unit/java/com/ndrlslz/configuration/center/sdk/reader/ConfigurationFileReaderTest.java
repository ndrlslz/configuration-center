package com.ndrlslz.configuration.center.sdk.reader;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationFileReaderTest {
    private static final String FILE_NAME = "configuration-center-sdk/src/test/unit/resources/configurations_read.properties";
    private ConfigurationFileReader configurationFileReader = new ConfigurationFileReader();

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }

    @Test
    public void shouldReadFile() throws IOException {
        Path path = Paths.get(FILE_NAME);
        Files.deleteIfExists(path);
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        Files.write(path, "key=value".getBytes());

        Properties properties = configurationFileReader.read(path);

        assertThat(properties.getProperty("key"), is("value"));
    }
}