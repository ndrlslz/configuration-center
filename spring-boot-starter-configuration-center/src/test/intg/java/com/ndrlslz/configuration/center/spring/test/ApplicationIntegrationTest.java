package com.ndrlslz.configuration.center.spring.test;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import org.junit.Test;


import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

public class ApplicationIntegrationTest extends IntegrationTestBase {
    @Test
    public void shouldGetPropertiesFromZookeeper() {
        when()
                .get("/coder")
                .then()
                .statusCode(200)
                .body("address", is("TianFu Street"))
                .body("name", is("Tom"))
                .body("javaer", is(true))
                .body("age", is(23))
                .body("emailAddress", is("tom@gmail.com"))
                .body("app", is("customer-api"));
    }

    @Test
    public void shouldAutoUpdateProperties() throws ConfigurationCenterException {
        when()
                .get("/coder")
                .then()
                .statusCode(200)
                .body("name", is("Tom"));

        configurationCenterClient.updateProperty(APPLICATION, ENVIRONMENT, "name", "Nick",
                configurationCenterClient.getProperty(APPLICATION, ENVIRONMENT, "name").getVersion());

        when()
                .get("/coder")
                .then()
                .statusCode(200)
                .body("name", is("Nick"));
    }
}
