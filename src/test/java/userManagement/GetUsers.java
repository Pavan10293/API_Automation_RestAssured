package userManagement;

import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.JsonReader;
import utils.PropertyReader;
import utils.SoftAssertionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class GetUsers {

    SoftAssertionUtil softAssertion = new SoftAssertionUtil();

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

    @Test
    public void automateSingleHeader() {

        given()
            .header("Content-Type", "application/json")
        .when()
             .get("https://reqres.in/api/users?page=2")
         .then()
             .statusCode(200);

    }

    @Test
    public void automateMultipleHeaders() {

        given()
             .header("Content-Type", "application/json")
             .header("x-api-key", "reqres-free-v1")
       .when()
             .get("https://reqres.in/api/users/2")
        .then()
             .statusCode(200);

    }

    @Test
    public void automateMultipleHeadersUsingMap() {

        Map<String, String> headers = new HashMap<>();

        headers.put("", "");
        headers.put("", "");

        given().
            headers(headers);

    }

    @Test
    public void fetchResponseHeadersAndValidate() {

        Response response = given()
                            .when()
                                .get("https://reqres.in/api/users?page=2")
                            .then()
                                .extract().response();

        Headers headers = response.getHeaders();
        for(Header eachHeader : headers) {
            System.out.println(eachHeader.getName()+" : "+eachHeader.getValue());
        }

        for(Header eachHeader : headers) {
            if(eachHeader.getName().equals("Server")) {
                System.out.println(eachHeader.getName() + " : " + eachHeader.getValue());
                assertEquals(eachHeader.getValue(), "cloudflare");
            }
        }

    }

    @Test
    public void testUseCookies() {

        given()
            .cookie("test", "testing")
            .cookie("test2", "testing2")
        .when()
             .get("https://reqres.in/api/users?page=2")
        .then()
             .statusCode(200)
             .body("response", equalTo("expected_value"));

        //Using Cookie Builder
        Cookie myCookie = new Cookie.Builder("", "").build();

        given()
            .cookie(myCookie)
        .when()
             .get("https://reqres.in/api/users?page=2")
        .then()
              .statusCode(200)
              .body("response", equalTo("expected_value"));

        //Cookies an also be passed as a Map  of <String, String> like it's done for headers above in automateMultipleHeadersUsingMap().
    }

    @Test
    public void testFetchResponseCookies() {

        Response response = given()
                            .when()
                                .get("https://reqres.in/api/users?page=2")
                            .then()
                                .extract().response();

        Map<String, String> responseCookies = response.getCookies();
        assertThat(responseCookies, hasKey("JSESSIONID"));
        assertThat(responseCookies, hasKey("ABCDEF123456"));

        Cookies responseCookies1 = response.getDetailedCookies();
        assertEquals(responseCookies1.getValue("JSESSIONID"), "ABCDEF123456");

    }

    @Test (groups = {"SmokeSuite", "RegressionSuite"})
    public void automateDeleteRequest() {

        Response response = given()
                                .header("x-api-key", "reqres-free-v1")
                            .when()
                                .delete("https://reqres.in/api/users/2");

        assertEquals(response.getStatusCode(), StatusCode.NO_CONTENT.code);

    }

    @Test (groups = "RegressionSuite")
    public void validateWithDataFromPropertiesFile() {

        String serverAddress = PropertyReader.propertyReader("config.properties", "server");

        System.out.println("Server address from properties file :- " + serverAddress);

        Response response = given()
                                .queryParam("page", 2)
                            .when()
                                 .get(serverAddress+"users");

        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, StatusCode.SUCCESS.code);
    }


    @Test
    public void validateWithDataFromPropertiesFileAndTestData() throws IOException, ParseException {

        String serverAddress = PropertyReader.propertyReader("config.properties", "server");
        String endPoint = JsonReader.getTestData("endpoint");
        String endPointURL = serverAddress+endPoint;

        System.out.println("Server address from properties file :- " + serverAddress);
        System.out.println("URL :- " + endPointURL);

        Response response = given()
                .queryParam("page", 2)
                .when()
                .get(endPointURL);

        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, StatusCode.SUCCESS.code);
    }

    @Test
    public void hardAssertion() {
        System.out.println("Hard Assert 1");
        Assert.assertTrue(false);
        System.out.println("Hard Assert 2"); //This line won't get executed as the above line throws exception as it's a hard assert.
    }

    //NOTE: Hard assertion cannot / is not preferred to be used when there are ,multiple assertions in a test script.
    //Eg. asserting various values of a response body.

    @Test
    public void softAssertion() {

        System.out.println("Soft Assert 1");
        softAssertion.assertTrue(false, "");
        softAssertion.assertTrue(true, "");
        System.out.println("Soft Assert 2");

        //Where soft assertions are used, to make the test script indicate which assertion failed, assertAll() should be called
        softAssertion.assertAll();
    }

    @DataProvider(name="testData")
    public Object[][] testData() {
        return new Object[][]{
                {"1", "John"},
                {"2", "Jane"},
                {"3", "Bob"}
        };
    }

    @Test(dataProvider = "testData")
    @Parameters({"id", "name"})
    public void testEndPoint(String id, String name) {

        given()
             .header("x-api-key", "reqres-free-v1")
             .queryParam("id", id)
             .queryParam("name", name)
        .when()
             .get("https://reqres.in/api/users")
        .then()
             .statusCode(200);

    }

    @Test
    public void Test() throws IOException, ParseException {
        JsonReader.getJsonArrayDataAtAnIndex("languages", 2);
    }

    private static FileInputStream fileInputStream(String requestBodyFileName) {
        FileInputStream fileInputStream;
        try {
             fileInputStream = new FileInputStream(new File(System.getProperty("user.dir") +
                    "/resources/TestData/"+requestBodyFileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileInputStream;
    }

    @Test
    public void validatePostRequestWithRequestBodyFromJsonFile() throws IOException {

        Response response = given()
                                .header("x-api-key", "reqres-free-v1")
                                .header("Content-Type", "application/json")
                                .body(IOUtils.toString(fileInputStream("postRequestBody.json")))
                            .when()
                                .post("https://reqres.in/api/users");

        System.out.println(response.getBody().asString());
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
    }


    @Test
    public void validatePatchRequestWithRequestBodyFromJsonFile() throws IOException {

        Response response = given()
                                .header("x-api-key", "reqres-free-v1")
                                .header("Content-Type", "application/json")
                                .body(IOUtils.toString(fileInputStream("patchRequestBody.json")))
                            .when()
                                .patch("https://reqres.in/api/users/2");

        System.out.println(response.getBody().asString());
        assertEquals(response.getStatusCode(), StatusCode.SUCCESS.code);
    }

    @Test
    public void validatePutRequestWithRequestBodyFromJsonFile() throws IOException {

        Response response = given()
                                .header("x-api-key", "reqres-free-v1")
                                .header("Content-Type", "application/json")
                                .body(IOUtils.toString(fileInputStream("putRequestBody.json")))
                            .when()
                                .put("https://reqres.in/api/users/2");

        System.out.println(response.getBody().asString());
        assertEquals(response.getStatusCode(), StatusCode.SUCCESS.code);
    }


}
