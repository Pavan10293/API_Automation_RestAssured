package utils;

import org.testng.asserts.SoftAssert;

public class SoftAssertionUtil {

    private SoftAssert softAssert;

    public SoftAssertionUtil() {
        this.softAssert = new SoftAssert();
    }

    public void assertTrue(boolean condition, String message) {
        try {
            softAssert.assertTrue(condition, message);
        } catch (AssertionError ae) {
            //In Java, it's best practice to avoid catching Errors unless you're writing test tooling or frameworks.
            softAssert.fail(message);
        }
    }

    public void assertFalse(boolean condition, String message) {
        try{
            softAssert.assertFalse(condition, message);
        } catch (AssertionError ae) {
            softAssert.fail(message);
        }
    }

    public void assertEquals(Object actual, Object expected, String message) {
        try{
            softAssert.assertEquals(actual, expected, message);
        } catch (AssertionError ae) {
            softAssert.fail(message);
        }
    }

    public void assertNotEquals(Object actual, Object expected, String message) {
        try{
            softAssert.assertNotEquals(actual, expected, message);
        } catch (AssertionError ae) {
            softAssert.fail(message);
        }
    }

    public void assertAll() {
        softAssert.assertAll();
    }




}
