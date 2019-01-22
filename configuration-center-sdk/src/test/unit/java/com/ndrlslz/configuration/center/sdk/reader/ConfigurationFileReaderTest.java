package com.ndrlslz.configuration.center.sdk.reader;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
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
    private static final String FILE_NAME = "configuration-center-failover/test/configurations_read.properties";
    private static final Path FAILOVER_PATH = Paths.get("configuration-center-failover");
    private ConfigurationFileReader configurationFileReader = new ConfigurationFileReader();

    @After
    public void tearDown() throws IOException {
        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }
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