package utils;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;

public class JsonReader {

    public static String getTestData(String key) throws IOException, org.json.simple.parser.ParseException {
        String testDataValue;
        testDataValue = (String) getJsonData().get(key);
        return testDataValue;
    }

    private static JSONObject getJsonData() throws IOException, org.json.simple.parser.ParseException {

        File Filename = new File("resources//TestData//testData.json");
        String jsonString = FileUtils.readFileToString(Filename, "UTF-8");

        Object obj = new org.json.simple.parser.JSONParser().parse(jsonString);
        return (JSONObject) obj;
    }
}
