package com.ndrlslz.configuration.center.spring.test;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

public class ApplicationIntegrationFailoverTest extends IntegrationFailoverTestBase {
    @Test
    public void shouldListenPropertyOnceConnectZookeeper() throws Exception {
        when()
                .get("/coder")
                .then()
                .statusCode(200)
                .body("address", is("TianFu Street"))
                .body("name", is("Tom"))
                .body("javaer", is(true))
                .body("age", is(24))
                .body("emailAddress", is("tom@gmail.com"))
                .body("app", is("customer-api"));

        testingServer.restart();

        createConfigurationCenterClient();
        createZookeeperData();

        TimeUnit.SECONDS.sleep(1);

        when()
                .get("/coder")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("name", is("Nick"))
                .body("emailAddress", is("tom@gmail.com"));
    }

    private void createZookeeperData() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(APPLICATION);
        configurationCenterClient.createEnvironment(APPLICATION, ENVIRONMENT);
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "name", "Nick");
        configurationCenterClient.createProperty(APPLICATION, ENVIRONMENT, "emailAddress", "tom@gmail.com");
    }
}
