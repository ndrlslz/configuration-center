package com.ndrlslz.configuration.center.spring.test;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.spring.app.TestApplication;
import io.restassured.RestAssured;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.String.format;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public abstract class IntegrationTestBase implements InitializingBean {
    static final String APPLICATION = "customer-api";
    static final String ENVIRONMENT = "dev";
    private static TestingServer testingServer;
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
        testingServer.start();

        configurationCenterClient = new ConfigurationCenterClient.Builder()
                .connectionString(testingServer.getConnectString())
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .build();

        initZookeeperTestData();
    }

    @After
    public void tearDown() throws Exception {
        configurationCenterClient.deleteApplication(APPLICATION);
        initZookeeperTestData();
    }

    private static void initZookeeperTestData() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(APPLICATION);
        configurationCenterClient.createEnvironment(APPLICATION, ENVIRONMENT);
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "name", "Tom");
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "age", "23");
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "address", "TianFu Street");
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "javaer", "true");
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "email", "tom@gmail.com");
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "app", "customer-api");
    }

    @AfterClass
    public static void afterClass() throws Exception {
        configurationCenterClient.close();
        testingServer.close();
    }
}
