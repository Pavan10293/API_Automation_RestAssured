package utils;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

        return jsonObject;
//        Object obj = new org.json.simple.parser.JSONParser().parse(jsonString);
//        return (JSONObject) obj;
    }

    public static JSONArray getJsonArray(String key) throws IOException, ParseException {
        JSONObject jsonObject = getJsonData();
        JSONArray jsonArray = (JSONArray) jsonObject.get(key);
        return jsonArray;
    }

    public static Object getJsonArrayDataAtAnIndex(String key, int index) throws IOException, ParseException {
        JSONArray languages = getJsonArray(key);
        return languages.get(index);
    }
}
