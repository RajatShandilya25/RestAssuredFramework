package Res;

import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;


/**
 * @author Rajat Shandilya
 * @time 10/04/21 11:25 PM
 */
public class Test2
{
    String baseUri = "https://reqres.in/api/";


    @Test(priority = 1)
    public void GET_UserList()
    {
        Response response = given()
        .when()
                .get(baseUri +"users?page=2")
        .then()
                .statusCode(200)
                .body(
                        "page", equalTo(2),
                "per_page", equalTo(6),
                        "data.id", hasItems(7, 8, 9, 10),
                        "data.size()", equalTo(6)
                )
        .extract().response();

        // Validating and printing response using ResponseSpecification object
       List<Integer> idList = response.path("data.id");
       for(Integer it: idList)
       {
           System.out.println("data.id --> " +it);
       }
    }

    @Test(priority = 2)
    public void POST_createUser()
    {
        // Header as hashmap
        HashMap<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");



        // Body as hashmap
//        {
//            "name": "morpheus",
//                "job": "leader"
//        }
        HashMap<String, String> body = new HashMap<>();
        body.put("name", "Rajat Shandilya");
        body.put("job", "SDET");


       Response response =  given()
                .headers(header)
                .body(body)
        .when()
                .get(baseUri +"users")
        .then()
                .statusCode(201)
               .body("id", isA(String.class)) // Validate if id is in String format
                .extract().response();


        // Validating and printing response using JsonPath object object
        JsonPath jsonPath = new JsonPath(response.toString());
        System.out.println("Name -> " +jsonPath.getString("name"));
        Assert.assertEquals(jsonPath.getString("name"), body.get("name"));

    }

    @Test(priority = 3)
    public void POST_createUser2()
    {
        File createUserPayload = new File("/Users/praveensharma/IdeaProjects/RestAssured/src/test/Resources/createUser.json");
        // Header as hashmap
        HashMap<String, String> header = new HashMap<>();
        header.put("Accept", "*/*");

        Response response =  given()
                .headers(header)
                .body(createUserPayload)          // Body as json file
                .when()
                .get(baseUri +"users")
                .then()
                .statusCode(201)
                .body("id", isA(String.class)) // Validate if id is in String format
                .extract().response();


        // Validating and printing response using JsonPath object object
        JsonPath jsonPath = new JsonPath(response.toString());
        System.out.println("Name -> " +jsonPath.getString("name"));
        Assert.assertEquals(jsonPath.getString("name"), response.path("name"));


    }
}