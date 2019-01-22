package com.ndrlslz.configuration.center.sdk;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.ndrlslz.configuration.center.core.client.ZookeeperClient;
import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import com.ndrlslz.configuration.center.sdk.failover.ConfigurationFailover;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.nonNull;

public class IntegrationFailoverTestBase {
    private static final Path FAILOVER_PATH = Paths.get("configuration-center-failover");
    private static final String APPLICATION = "customer-api";
    private static final String ENVIRONMENT = "dev";
    private static TestingServer testingServer;
    ConfigurationTemplate configurationTemplate;

    @BeforeClass
    public static void beforeClass() throws Exception {
        testingServer = new TestingServer(9999);
        testingServer.stop();
        decreaseFirstConnectionTimeoutForFastTest();
    }

    @Before
    public void setUp() throws Exception {
        setupFailoverFlag();

        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }
    }

    @After
    public void tearDown() throws IOException {
        if (nonNull(configurationTemplate)) {
            configurationTemplate.close();
        }

        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }
    }

    void createConfigurationTemplate() {
        configurationTemplate = new ConfigurationTemplate.Builder()
                .connectionString(testingServer.getConnectString())
                .application(APPLICATION)
                .environment(ENVIRONMENT)
                .build();
    }

    private void setupFailoverFlag() throws NoSuchFieldException, IllegalAccessException {
        Field field = ConfigurationFailover.class.getDeclaredField("FAILOVER");
        field.setAccessible(true);

        field.set(null, true);
    }

    private static void decreaseFirstConnectionTimeoutForFastTest() throws Exception {
        Field field = ZookeeperClient.class.getDeclaredField("FIRST_CONNECTION_TIMEOUT_S");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, 1);
    }
}
