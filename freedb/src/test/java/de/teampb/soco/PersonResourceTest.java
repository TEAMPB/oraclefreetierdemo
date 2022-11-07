package de.teampb.soco;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import javax.json.Json;
import javax.json.JsonObject;

@QuarkusTest
public class PersonResourceTest {

    @Test
    public void testPersonSingleQuery() {
        given()
          .when().get("/person/1")
          .then()
             .statusCode(200);
    }

    @Test
    public void testPersonGetAll() {
        given()
          .when().get("/person")
          .then()
             .statusCode(200);
    }

    @Test
    public void testPersonSave() {

        JsonObject build = Json.createObjectBuilder().add("name", "Johnny").build();

        given().body(build.toString()).header("Content-Type", "application/json")
          .when().post("/person")
          .then()
             .statusCode(200);
    }

    @Test
    public void testPersonSaveBlacklisted() {

        JsonObject build = Json.createObjectBuilder().add("name", "Doe").build();

        given().body(build.toString()).header("Content-Type", "application/json")
          .when().post("/person")
          .then()
             .statusCode(400);
    }

}