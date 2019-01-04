package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import org.junit.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;

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
                .body("links.find { it.ref = 'self' }.href", is(baseURI + "/applications?size=10&page=0"))
                .body("metadata.totalElements", is(3))
                .body("metadata.totalPages", is(1))
                .body("data.size()", is(3))
                .body("data.type", hasItem("APPLICATION"))
                .body("data.attributes.name", hasItems("customer-api", "product-api", "order-api"))
                .body("data.relationships.environments", hasItems(
                        "/applications/customer-api/environments",
                        "/applications/product-api/environments",
                        "/applications/order-api/environments"));

    }
}
