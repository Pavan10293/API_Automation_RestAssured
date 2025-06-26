package userManagement;

import core.StatusCode;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojo.CityRequest;
import pojo.PostRequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class PostUsers {

    @Test
    public void validatePostRequestWithRequestBodyFromPojoListObject() throws IOException {

        List<String> listOfLanguages = new ArrayList<String>();
        listOfLanguages.add("Java");
        listOfLanguages.add("Python");

        CityRequest cityReq = new CityRequest();
        cityReq.setName("Bangalore");
        cityReq.setTemperature("30");

        CityRequest cityReq2 = new CityRequest();
        cityReq2.setName("Delhi");
        cityReq2.setTemperature("30");

        List<CityRequest> listOfCities = new ArrayList<CityRequest>();
        listOfCities.add(cityReq);
        listOfCities.add(cityReq2);

        PostRequestBody postRequestBody = new PostRequestBody();
        postRequestBody.setName("Morpheus");
        postRequestBody.setJob("Leader");
        postRequestBody.setLanguages(listOfLanguages);
        postRequestBody.setCityRequestBody(listOfCities);

        Response response = given()
                .header("x-api-key", "reqres-free-v1")
                .header("Content-Type", "application/json")
                .body(postRequestBody)
                .when()
                .post("https://reqres.in/api/users");

        System.out.println(response.getBody().asString());
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
    }

}
