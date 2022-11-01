package de.teampb.soco;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import javax.json.Json;
import javax.json.JsonObject;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testPersonEinzelladenGeht() {
        given()
          .when().get("/person/1")
          .then()
             .statusCode(200);
    }

    @Test
    public void testPersonenLadenGeht() {
        given()
          .when().get("/person")
          .then()
             .statusCode(200);
    }

    @Test
    public void testPersonenSpeichernGeht() {

        JsonObject build = Json.createObjectBuilder().add("name", "Johnny").build();

        given().body(build.toString()).header("Content-Type", "application/json")
          .when().post("/person")
          .then()
             .statusCode(200);
    }

}