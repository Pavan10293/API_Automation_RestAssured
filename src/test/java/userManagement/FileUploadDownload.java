package userManagement;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FileUploadDownload {

    @Test
    public void fileUploadExample() {
        File file = new File("resources/demo.txt");

        Response response = given().
                                multiPart(file)
                            .when()
                                .post("https://example.com/upload");



    }
}
