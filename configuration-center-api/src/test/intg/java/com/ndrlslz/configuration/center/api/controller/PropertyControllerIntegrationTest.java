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

public class PropertyControllerIntegrationTest extends ControllerIntegrationTestBase {
    private static final String CUSTOMER_API = "customer-api";
    private static final String DEV = "dev";

    @Test
    public void shouldGetProperties() throws ConfigurationCenterException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key1", "value1");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key2", "value2");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key3", "value3");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key4", "value4");
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key5", "value5");

        when()
                .get("/applications/customer-api/environments/dev/properties")
                .then()
                .statusCode(200)
                .log()
                .all()
                .body("links.find { it.ref = 'self' }.href", is(baseURI + "/applications/customer-api/environments/dev/properties?size=10&page=0"))
                .body("links.find { it.ref = 'first' }.href", is(baseURI + "/applications/customer-api/environments/dev/properties?size=10&page=0"))
                .body("links.find { it.ref = 'last' }.href", is(baseURI + "/applications/customer-api/environments/dev/properties?size=10&page=0"))
                .body("metadata.totalElements", is(5))
                .body("metadata.totalPages", is(1))
                .body("data.size()", is(5))
                .body("data.type", hasItem("property"))
                .body("data.attributes.name", hasItems("key1", "key2", "key3", "key4", "key5"))
                .body("data.attributes.value", hasItems("value1", "value2", "value3", "value4", "value5"))
                .body("data.relationships.self", hasItems(
                        "/applications/customer-api/environments/dev/properties/key1",
                        "/applications/customer-api/environments/dev/properties/key2",
                        "/applications/customer-api/environments/dev/properties/key3",
                        "/applications/customer-api/environments/dev/properties/key4",
                        "/applications/customer-api/environments/dev/properties/key5"));
    }

    @Test
    public void shouldCreateProperty() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_property.json"))
                .when()
                .post("/applications/customer-api/environments/dev/properties")
                .then()
                .statusCode(200)
                .body("data.type", is("property"))
                .body("data.attributes.name", is("key"))
                .body("data.attributes.value", is("value"))
                .body("data.attributes.version", is(0))
                .body("data.relationships.self", is("/applications/customer-api/environments/dev/properties/key"));

        when()
                .get("/applications/customer-api/environments/dev/properties")
                .then()
                .statusCode(200)
                .body("data.size()", is(1))
                .body("data.attributes.name", hasItem("key"))
                .body("data.attributes.value", hasItem("value"));
    }

    @Test
    public void shouldThrowBadRequestWhenCreatePropertyGivenWrongType() throws IOException {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_property_with_wrong_type.json"))
                .when()
                .post("/applications/customer-api/environments/dev/properties")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("status", is(400))
                .body("message", containsString("type not match"));
    }

    @Test
    public void shouldThrowConflictWhenCreatePropertyGivenPropertyAlreadyExists() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("create_property.json"))
                .when()
                .post("/applications/customer-api/environments/dev/properties")
                .then()
                .statusCode(409)
                .body("status", is(409))
                .body("error", is("Conflict"))
                .body("message", containsString("NodeExists"))
                .body("exception", containsString("NodeExistsException"));
    }

    @Test
    public void shouldUpdateProperty() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("update_property.json"))
                .when()
                .put("/applications/customer-api/environments/dev/properties/key")
                .then()
                .statusCode(200)
                .body("data.type", is("property"))
                .body("data.attributes.name", is("key"))
                .body("data.attributes.value", is("new_value"))
                .body("data.attributes.version", is(1))
                .body("data.attributes.createTime", notNullValue())
                .body("data.attributes.updateTime", notNullValue())
                .body("data.relationships.self", is("/applications/customer-api/environments/dev/properties/key"));
    }

    @Test
    public void shouldThrowExceptionWhenUpdatePropertyGivenVersionNotMatch() throws ConfigurationCenterException, IOException {
        configurationCenterClient.createApplication(CUSTOMER_API);
        configurationCenterClient.createEnvironment(CUSTOMER_API, DEV);
        configurationCenterClient.createProperty(CUSTOMER_API, DEV, "key", "value");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(requestFile("update_property_with_wrong_version.json"))
                .when()
                .put("/applications/customer-api/environments/dev/properties/key")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("error", is("Bad Request"))
                .body("message", containsString("BadVersion"))
                .body("exception", containsString("BadVersionException"));
    }

    private File requestFile(String name) throws IOException {
        return new ClassPathResource("request/property/" + name).getFile();
    }
}
