package com.ndrlslz.configuration.center.sdk.writer;

import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationFileWriterTest {
    private static final String FILE_NAME = "configuration-center-sdk/src/test/unit/resources/configurations_write.properties";
    private ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter();

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }

    @Test
    public void shouldWritePropertiesToFile() throws IOException {
        ZookeeperStorage.set("key", "value");

        configurationFileWriter.write(Paths.get(FILE_NAME));

        Properties properties = read();
        assertThat(properties.getProperty("key"), is("value"));
    }

    private Properties read() throws IOException {
        Properties properties = new Properties();

        InputStream inputStream = Files.newInputStream(Paths.get(FILE_NAME));
        properties.load(inputStream);

        return properties;

    }
}