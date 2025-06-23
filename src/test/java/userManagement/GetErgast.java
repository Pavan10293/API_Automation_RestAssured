package userManagement;

import core.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class GetErgast extends BaseTest {

    @Test(groups = "RegressionSuite")
    public void validateStatusCodeWithPathParameterInRequest() {

        Response response = given()
                .pathParam("raceSeason", 2017)
                .when()
                .get("https://ergast.com/api/f1/{raceSeason}/circuits.json");

        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, 200);
        System.out.println(response.body().asString());


    }

}
