package com.ndrlslz.configuration.center.api.controller;

import com.ndrlslz.configuration.center.core.exception.ConfigurationCenterException;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;

public class ConfigurationCenterApplicationControllerIntegrationTest extends ControllerTestBase {

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
                .body("links.find { it.ref = 'first' }.href", is(baseURI + "/applications?size=10&page=0"))
                .body("links.find { it.ref = 'last' }.href", is(baseURI + "/applications?size=10&page=0"))
                .body("metadata.totalElements", is(3))
                .body("metadata.totalPages", is(1))
                .body("data.size()", is(3))
                .body("data.type", hasItem("application"))
                .body("data.attributes.name", hasItems("customer-api", "product-api", "order-api"))
                .body("data.relationships.environments", hasItems(
                        "/applications/customer-api/environments",
                        "/applications/product-api/environments",
                        "/applications/order-api/environments"));
    }

    @Test
    public void shouldCreateApplications() throws IOException {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_application.json"))
                .when()
                .post("/applications")
                .then()
                .statusCode(200)
                .body("data.type", is("application"))
                .body("data.attributes.name", is("customer-api"))
                .body("data.relationships.environments", is("/applications/customer-api/environments"));

        when()
                .get("/applications")
                .then()
                .statusCode(200)
                .body("data.size()", is(1))
                .body("data.attributes.name", hasItem("customer-api"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateApplicationGivenWrongType() throws IOException {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_application_with_wrong_type.json"))
                .when()
                .post("/applications")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("status", is(400))
                .body("message", containsString("type not match"));
    }

    @Test
    public void shouldThrowConflictWhenCreateApplicationGivenApplicationAlreadyExists() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication("customer-api");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_application.json"))
                .when()
                .post("/applications")
                .then()
                .statusCode(409)
                .body("status", is(409))
                .body("error", is("Conflict"))
                .body("message", containsString("NodeExists"))
                .body("exception", containsString("NodeExistsException"));
    }

    @Test
    public void shouldDeleteApplication() throws ConfigurationCenterException {
        configurationCenterClient.createApplication("customer-api");

        when()
                .delete("/applications/customer-api")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldThrowNotFoundWhenDeleteApplicationGivenApplicationNotExists() {
        when()
                .delete("/applications/customer-api")
                .then()
                .statusCode(404)
                .body("error", is("Not Found"))
                .body("status", is(404))
                .body("message", containsString("NoNode"))
                .body("exception", containsString("NoNodeException"));
    }

    private File requestFile(String name) throws IOException {
        return new ClassPathResource("request/application/" + name).getFile();
    }
}