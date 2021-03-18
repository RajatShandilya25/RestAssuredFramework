package Res;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class Test1
{
    @Test
    public void test()
    {

        /*
        Body
         */
        File file = new File("JSON payload file path");

        //Sending Nested JSON
        /*
        { key1: {
                "key2": "value1",
                "key3": "value3",
                }
        }

        There are two JSON so we'll create two HashMaps
         */

        HashMap<String, String> NestedObject = new HashMap<>();
        NestedObject.put("key2", "value2");
        NestedObject.put("key3", "value3");

        HashMap<String, Object> body = new HashMap<>();
        body.put("key1", NestedObject);



        //Headers
        HashMap<String, String> Headers = new HashMap<>();
        Headers.put("Accept", "*/*");


        Response response =
        given()
//                .header("Accept", "*/*")
                .headers(Headers)
                .body(file)
                .body(body)


                .log().all() // To log request method, headers etc
                .log().ifValidationFails()


        .when()
                .get("https://reqres.in/api/users?page=1")


        .then()
                .log().all() // To log response,headers, cookies etc.
                .log().ifValidationFails()
                .assertThat()
                .statusCode(200)
                .body("page", equalTo(1), // Check specific value
                    "total", equalTo(12),
                    "data.id", hasItems(1, 2, 3, 4), // Check value of all id's
                    "data.size()", equalTo(6), // Check size of data array
                    "data[1].last_name", equalTo("Weaver") )
                .extract().response();


/**
 * Ways of exrtracting response
 * 1. Using response object
 * 2. Using JsonPath object
 */

//1.
        System.out.println(response.asString());
        System.out.println("Emai id --> " +response.path("data"));

//2.

//        JsonPath jsonPath = new JsonPath(response.toString());
//        System.out.println("Email id 2 --> " +jsonPath.getString("$.data"));



    }
}
