package com.ndrlslz.configuration.center.spring.test;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.client.ZookeeperClient;
import com.ndrlslz.configuration.center.core.exception.FirstConnectionTimeoutException;
import com.ndrlslz.configuration.center.sdk.failover.ConfigurationFailover;
import com.ndrlslz.configuration.center.spring.app.TestApplication;
import com.ndrlslz.configuration.center.spring.utils.DisasterRecoveryFileBuilder;
import io.restassured.RestAssured;
import org.apache.curator.test.TestingServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public abstract class IntegrationFailoverTestBase implements InitializingBean {
    private static final Path FAILOVER_PATH = Paths.get("configuration-center-failover");
    static final String APPLICATION = "customer-api";
    static final String ENVIRONMENT = "dev";
    static TestingServer testingServer;
    static ConfigurationCenterClient configurationCenterClient;

    @LocalServerPort
    private String port;

    @Override
    public void afterPropertiesSet() {
        RestAssured.baseURI = format("http://localhost:%s", port);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        testingServer = new TestingServer(6666);
        testingServer.stop();
        decreaseFirstConnectionTimeoutForFastTest();
        setupFailoverFlag();

        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }

        new DisasterRecoveryFileBuilder()
                .property("name", "Tom")
                .property("age", "24")
                .property("address", "TianFu Street")
                .property("javaer", "true")
                .property("email", "tom@gmail.com")
                .property("app", "customer-api")
                .create();
    }

    @Before
    public void setUp() throws Exception {
        testingServer.stop();
    }

    @AfterClass
    public static void afterClass() throws IOException {
        if (Files.exists(FAILOVER_PATH)) {
            MoreFiles.deleteRecursively(FAILOVER_PATH, RecursiveDeleteOption.ALLOW_INSECURE);
        }

        testingServer.close();
    }

    public static void createConfigurationCenterClient() throws FirstConnectionTimeoutException {
        configurationCenterClient = new ConfigurationCenterClient.Builder()
                .connectionString(testingServer.getConnectString())
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .fastFail(true)
                .build();
    }

    private static void decreaseFirstConnectionTimeoutForFastTest() throws Exception {
        Field field = ZookeeperClient.class.getDeclaredField("FIRST_CONNECTION_TIMEOUT_S");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, 2);
    }

    private static void setupFailoverFlag() throws NoSuchFieldException, IllegalAccessException {
        Field field = ConfigurationFailover.class.getDeclaredField("FAILOVER");
        field.setAccessible(true);

        field.set(null, true);
    }
}
