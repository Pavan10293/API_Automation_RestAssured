package userManagement;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

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

    @Test
    public void validateListContainsInOrder() {

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        Response response = given()
                            .when()
                                .get("/comments?postId=1")
                            .then()
                                .extract().response();

        String[] expectedEmailsArray = new String[]{"Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz"};
//        List<String> expectedEmailsList = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmailsArray));
//        assertThat(response.jsonPath().getList("email"), contains(expectedEmailsList.toArray()));
    }

    @Test
    public void testGetUsersWithQueryParametersAndValidateAllFieldsOfAnEntity() {

        RestAssured.baseURI = "https://reqres.in/api";

        Response response = given()
                                .queryParam("page", 2)
                            .when()
                                .get("/users");

        System.out.println(response.jsonPath().getMap("data[0]"));

        //is() matcher is pretty much similar to equalTo() matcher.
        response.then().body("data[0].id", is(7));
        response.then().body("data[0].email", is("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));

    }

    @Test
    public void validateStatusCodeWithPathParameterInRequest() {

        Response response = given()
                                .pathParam("raceSeason", 2017)
                            .when()
                                .get("https://ergast.com/api/f1/{raceSeason}/circuits.json");

        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, 200);
        System.out.println(response.body().asString());


    }

    @Test
    public void validatePostRequestWithFormParam() {

        Response response = given()
                               .header("x-api-key", "reqres-free-v1")
                               .contentType("application/x-www-form-urlencoded")
                               .formParam("name", "John Doe")
                               .formParam("job", "Developer")
                            .when()
                               .post("https://reqres.in/users")
                            .then()
                                .extract()
                                .response();

          response.then().statusCode(201);

//        response.then().body("name", equalTo("John Doe"));
//        response.then().body("job", equalTo("Developer"));
    }

}
