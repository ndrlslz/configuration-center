package com.ndrlslz.configuration.center.sdk.writer;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationFileWriterTest {
    private static final String FILE_NAME = "configuration-center-failover/test/configurations_read.properties";
    private static final Path FAILOVER_PATH = Paths.get("configuration-center-failover");
    private ConfigurationFileWriter configurationFileWriter = new ConfigurationFileWriter();

    @After
    public void tearDown() throws IOException {
        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }
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