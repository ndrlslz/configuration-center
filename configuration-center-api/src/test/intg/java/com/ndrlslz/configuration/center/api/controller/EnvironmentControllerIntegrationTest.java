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

public class EnvironmentControllerIntegrationTest extends ControllerIntegrationTestBase {
    private static final String CUSTOMER_API = "customer-api";

    @Test
    public void shouldGetEnvironments() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "uat");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "test");
        configurationCenterClient.createEnvironment(CUSTOMER_API, "prod");

        when()
                .get("/applications/customer-api/environments")
                .then()
                .statusCode(200)
                .body("links.find { it.ref = 'self' }.href", is(baseURI + "/applications/customer-api/environments?size=10&page=0"))
                .body("links.find { it.ref = 'first' }.href", is(baseURI + "/applications/customer-api/environments?size=10&page=0"))
                .body("links.find { it.ref = 'last' }.href", is(baseURI + "/applications/customer-api/environments?size=10&page=0"))
                .body("metadata.totalElements", is(4))
                .body("metadata.totalPages", is(1))
                .body("data.size()", is(4))
                .body("data.type", hasItem("environment"))
                .body("data.attributes.name", hasItems("dev", "uat", "test", "prod"))
                .body("data.relationships.properties", hasItems(
                        "/applications/customer-api/environments/dev/properties",
                        "/applications/customer-api/environments/uat/properties",
                        "/applications/customer-api/environments/test/properties",
                        "/applications/customer-api/environments/prod/properties"));
    }

    @Test
    public void shouldCreateEnvironment() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication(CUSTOMER_API);

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_environment.json"))
                .when()
                .post("/applications/customer-api/environments")
                .then()
                .statusCode(200)
                .body("data.type", is("environment"))
                .body("data.attributes.name", is("dev"))
                .body("data.relationships.properties", is("/applications/customer-api/environments/dev/properties"));

        when()
                .get("/applications/customer-api/environments")
                .then()
                .statusCode(200)
                .body("data.size()", is(1))
                .body("data.attributes.name", hasItem("dev"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreateEnvironmentGivenWrongType() throws IOException {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_environment_with_wrong_type.json"))
                .when()
                .post("/applications/customer-api/environments")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("status", is(400))
                .body("message", containsString("type not match"));
    }

    @Test
    public void shouldThrowConflictWhenCreateEnvironmentGivenEnvironmentAlreadyExists() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_environment.json"))
                .when()
                .post("/applications/customer-api/environments")
                .then()
                .statusCode(409)
                .body("status", is(409))
                .body("error", is("Conflict"))
                .body("message", containsString("NodeExists"))
                .body("exception", containsString("NodeExistsException"));
    }

    @Test
    public void shouldDeleteEnvironment() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, "dev");

        when()
                .delete("/applications/customer-api/environments/dev")
                .then()
                .statusCode(200);
    }

    @Test
    public void shouldThrowNotFoundWhenDeleteEnvironmentGivenEnvironmentNotExists() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);

        when()
                .delete("/applications/customer-api/environments/dev")
                .then()
                .statusCode(404)
                .body("error", is("Not Found"))
                .body("status", is(404))
                .body("message", containsString("NoNode"))
                .body("exception", containsString("NoNodeException"));
    }

    private File requestFile(String name) throws IOException {
        return new ClassPathResource("request/environment/" + name).getFile();
    }
}