package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.core.client.ConfigurationCenterClient;
import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import com.ndrlslz.configuration.center.core.model.Pagination;
import io.restassured.RestAssured;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static java.lang.String.format;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTestBase implements InitializingBean {
    private static TestingServer testingServer;

    @Autowired
    ConfigurationCenterClient configurationCenterClient;

    @LocalServerPort
    private String port;

    String baseURI;

    @Override
    public void afterPropertiesSet() {
        baseURI = format("http://localhost:%s", port);
        RestAssured.baseURI = baseURI;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        testingServer = new TestingServer(10080);
        testingServer.start();
    }

    @AfterClass
    public static void afterClass() throws IOException {
        testingServer.close();
    }

    @After
    public void tearDown() throws ConfigurationCenterException {
        Pagination pagination = new Pagination.Builder()
                .withSize(Integer.MAX_VALUE)
                .withNumber(0).build();

        configurationCenterClient
                .getApplications(pagination)
                .getContent()
                .forEach(app -> {
                    try {
                        configurationCenterClient.deleteApplication(app);
                    } catch (ConfigurationCenterException ignored) {
                    }
                });
    }
}