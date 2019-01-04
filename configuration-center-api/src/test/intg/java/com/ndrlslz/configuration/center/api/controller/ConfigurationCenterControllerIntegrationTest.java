package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import org.junit.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

public class ConfigurationCenterControllerIntegrationTest extends ControllerTestBase {

    @Test
    public void shouldGetApplications() throws ConfigurationCenterException {
        configurationCenterClient.createApplication("customer-api");
        configurationCenterClient.createApplication("product-api");
        configurationCenterClient.createApplication("order-api");

        when()
                .get("/applications")
                .then()
                .statusCode(200)
                .body("data.size()", is(3))
                .body("data.attributes.name", hasItems("customer-api", "product-api", "order-api"));
    }
}
