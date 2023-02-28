package typicodetest;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {
    final String url = "https://jsonplaceholder.typicode.com";
    public static String userId;
    public static String id;

    @Before
    public void setUp() {
        RestAssured.config = new RestAssuredConfig().encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8"));
        RestAssured.baseURI = url;
    }

    @Test
    public void a_createTitle() {
        Map<String, Object> title = new HashMap<>();
        title.put("title", "Titulo 1");
        title.put("body", "este es su descripción");
        title.put("userId", 1);

        JsonPath jsonPath = given().log().all().contentType(ContentType.JSON).body(title)
                .when().post("/posts")
                .then().log().all().assertThat().statusCode(201).and().body("id", notNullValue()
                        , "title", equalTo("Titulo 1")
                        , "body", equalTo("este es su descripción")
                        , "userId", equalTo(1)).extract().jsonPath();

        userId = jsonPath.getString("userId");
        id = jsonPath.getString("id");
    }

    @Test
    public void b_updateTitle() {
        Map<String, Object> title = new HashMap<>();
        title.put("id", id);
        title.put("title", "Titulo modificado");
        title.put("body", "Descripción modificada");
        title.put("userId", 1);

        given().log().all().contentType(ContentType.JSON).pathParam("id", userId)
                .body(title).when().put("/posts/{id}")
                .then().log().all().assertThat().statusCode(200).and().body("title", equalTo("Titulo modificado")
                        , "body", equalTo("Descripción modificada"),
                        "userId", equalTo(1));

    }

    @Test
    public void c_getTitle() {
        given().log().all().contentType(ContentType.JSON).pathParam("id", userId)
                .when().get("/posts/{id}").then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void d_getAllTitles() {
        given().log().all().contentType(ContentType.JSON).when().get("/posts").then().log().all().assertThat().statusCode(200);
    }

    @Test
    public void e_deleteTitle() {
        given().log().all().contentType(ContentType.JSON).pathParam("id", userId)
                .when().delete("/posts/{id}").then().log().all().assertThat().statusCode(200);
    }


}

