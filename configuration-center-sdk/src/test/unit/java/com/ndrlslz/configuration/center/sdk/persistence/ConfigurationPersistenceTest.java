package com.ndrlslz.configuration.center.sdk.persistence;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.ndrlslz.configuration.center.sdk.storage.ZookeeperStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ndrlslz.configuration.center.sdk.failover.ConfigurationFailover.MEMORY_CACHE_PATH;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigurationPersistenceTest {
    private static final Path FAILOVER_PATH = Paths.get("configuration-center-failover");
    private ConfigurationPersistence configurationPersistence;

    @Before
    public void setUp() throws Exception {
        makeSureStartThreadImmediately();
        configurationPersistence = new ConfigurationPersistence();

        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }
    }

    @Test
    public void shouldPersistConfigurations() throws InterruptedException, IOException {
        ZookeeperStorage.set("key", "value");
        ZookeeperStorage.set("configuration-center.connectionString", "localhost:2181");
        ZookeeperStorage.set("spring.application.name", "customer-api");
        ZookeeperStorage.set("logging.level.", "debug");

        configurationPersistence.run();

        TimeUnit.SECONDS.sleep(2);
        Path path = Paths.get(MEMORY_CACHE_PATH);
        List<String> lines = Files.readAllLines(path);

        assertThat(Files.exists(path), is(true));
        assertThat(lines.size(), is(4));
        assertThat(lines, hasItems("key=value",
                "configuration-center.connectionString=localhost:2181",
                "spring.application.name=customer-api",
                "logging.level.=debug"));
    }

    private static void makeSureStartThreadImmediately() throws Exception {
        Field field = ConfigurationPersistence.class.getDeclaredField("INITIAL_DELAY");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, 0);
    }
}