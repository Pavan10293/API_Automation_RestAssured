package userManagement;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetUsers {

    @Test
    public void getUsersData() {

        given()
        .when()
             .get("https://reqres.in/api/users?page=2")
        .then()
             .statusCode(200);

    }

    @Test
    public void validateGetResponseBody() {

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        given()
        .when()
             .get("/todos/1")
        .then()
                .statusCode(200)
                .body(not(emptyString()))
                .body("title", equalTo("delectus aut autem"))
                .body("userId", equalTo(1));

    }

    @Test
    public void validateResponseHasItemsAndHasSizeMatchers() {

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = given()
                            .when()
                                .get("/posts")
                            .then()
                                .extract().response();

//        System.out.println("Response body :" +
//                response.body().print();

        //Use Hamcrest to check that the response body contains specific items.
        assertThat(response.jsonPath().getList("title"), hasItems("qui est esse", "eveniet quod temporibus"));
        assertThat(response.jsonPath().getList(""), hasSize(100));
    }

}
