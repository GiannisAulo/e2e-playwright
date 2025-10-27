package services;

import config.EnvDataConfig;
import config.TestDataConfig;
import utils.enums.Actors;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static utils.Utils.findQuarter;

public class RestCommonValidations {
    Utils utils;
    EnvDataConfig envDataConfig;
    ExtractableResponse response;
    public RestCommonRequests requests;
    TestDataConfig testDataConfig = new TestDataConfig();

    public final String KEY = "Key";
    public final String VALUE = "Value";
    public final String FIELD_NAME = "Field";
    public final String LIST_NAME = "ListName";

    public RestCommonValidations(Utils utils) {
        this.utils = utils;
        envDataConfig = new EnvDataConfig();
        requests = new RestCommonRequests(utils);
        response = utils.context().getResponse();
    }

    /**
     * Method to verify that the returned Status Code is equal to an expected.
     *
     * @param expectedStatusCode The expected status code
     */
    public RestCommonValidations verifyStatusCode(int expectedStatusCode) {
        Assert.assertEquals((long) response.statusCode(), (long) expectedStatusCode);
        return this;
    }

    public RestCommonValidations verifyResponseIsEmpty() {
        Assert.assertEquals(response.asPrettyString(), "");
        return this;
    }

    public RestCommonValidations verifyValueOfGivenParamOnResponse(Properties param) {
        verifyValuesOnResponse(param, Actors.ECAuthAdmin);
        return this;
    }

    public RestCommonValidations verifyValueOfGivenParamOnResponse(Properties param, Actors actors) {
        verifyValuesOnResponse(param, actors);
        return this;
    }

    private RestCommonValidations verifyValuesOnResponse(Properties param, Actors actors) {

        List<String> keys = TestDataConfig.multipleOrSingleTestData(param, KEY);
        List<String> expectedValues = TestDataConfig.multipleOrSingleTestData(param, VALUE);

        for (int i = 0; i < keys.size(); i++) {
            String expectedValue = expectedValues.get(i);
            if (keys.get(i).equals("reportDate.year") || keys.get(i).equals("reportDate.quarter")) {
                Map<String, String> reportDate = findQuarter(expectedValue);
                expectedValue = keys.get(i).contains("year") ? reportDate.get("year") : reportDate.get("quarter");
            }
            String actualValue = response.path(keys.get(i)) == null ? "null" : response.path(keys.get(i)).toString();
            if (actualValue.contains("["))
                actualValue = actualValue.substring(actualValue.indexOf("[") + 1, actualValue.indexOf("]"));
            if (expectedValue.equals("<today>")) {
                expectedValue = utils.findTheCurrentDateTime("yyyy-MM-dd");
                actualValue = actualValue.substring(0, actualValue.indexOf("T"));
            }
            if (expectedValue.equals("<not null>")) {
                Assert.assertNotNull(actualValue, " The " + keys.get(i) + "should be not null");
            } else
                Assert.assertEquals(actualValue, expectedValue, "Incorrect response value for " + keys.get(i));
        }
        return this;
    }

    //This method verifies the values in the search response based on keys and expected values
    // provided in the param. It also includes handling for dynamic values such as dates and
    // specific actor-related values (e.g., declarantEori, importer).
    public RestCommonValidations verifySearchResponseValues(Properties param) {

        List<String> keys = TestDataConfig.multipleOrSingleTestData(param, KEY);
        List<String> expectedValues = TestDataConfig.multipleOrSingleTestData(param, VALUE);

        for (int i = 0; i < keys.size(); i++) {
            String expectedValue = expectedValues.get(i);
            String actualValue = response.path(keys.get(i)) == null ? "null" : response.path(keys.get(i)).toString();
            if (keys.get(i).equals("reportDate.year") || keys.get(i).equals("reportDate.quarter") || expectedValue.equals("current") || expectedValue.equals("previous - 2")) {
                Map<String, String> reportDate = findQuarter(expectedValue);
                String year = reportDate.get("year");
                String quarter = reportDate.get("quarter");
                expectedValue = year + " - " + quarter;
            }
            if (actualValue.contains("["))
                actualValue = actualValue.substring(actualValue.indexOf("[") + 1, actualValue.indexOf("]"));
            if (expectedValue.equals("yyyy/MM/dd")) {
                expectedValue = utils.findTheCurrentDateTime("yyyy-MM-dd");
                Assert.assertEquals(actualValue, expectedValue, "Wrong date -> actual date = " + actualValue + " , expected date = " + expectedValue + " ");
            } else if (expectedValue.equals("<not null>")) {
                Assert.assertNotNull(actualValue, " The " + keys.get(i) + "should be not null");
            } else
                Assert.assertEquals(actualValue, expectedValue, "Incorrect response value for " + keys.get(i));
        }
        return this;
    }

    /*Verifies whether a key from json response exists or not
     */
    public void verifyValueExistanceOfResponse(String key, String keyValue, boolean expectedExistance) {
        boolean existance = false;
        JsonPath responseJson = new JsonPath(response.asString());
        int responseSize = responseJson.getInt("data.size()");
        for (int i = 0; i < responseSize; i++) {
            String keyType = responseJson.get("data[" + i + "]." + key + " ");
            if (keyType.equals(keyValue)) {
                existance = true;
                break;
            }
        }
        Assert.assertEquals(existance, expectedExistance, "Expected existance is not true");
        System.out.println("Expected existance is valid");
    }

    //Similar to the previous method but allows specifying a custom JSON path.
    // It verifies if the given key-value pair exists within the specified path in the response,
    // ensuring its presence (or absence) matches the expectation.
    public void verifyValueExistanceOfResponseUsingPath(String path, String key, Object keyValue, boolean expectedExistance) {
        boolean existance = false;
        JsonPath responseJson = new JsonPath(response.asString());
        int responseSize = responseJson.getInt(path + ".size()");
        for (int i = 0; i < responseSize; i++) {
            Object keyType = responseJson.get(path + "[" + i + "]." + key);

            if (keyType != null && keyType.equals(keyValue)) {
                existance = true;
                break;
            }
        }
        Assert.assertEquals(existance, expectedExistance, "Expected existence is not true");
        System.out.println("Expected existence is valid for value: " + keyValue);
    }

    //Verifies that the values associated with the given keys in the API response are empty lists.
// Extracts the lists and asserts that they contain no elements.
    public void verifyValuesOfResponseUsingListEmptyElements(Properties param) {
        List<String> keys = TestDataConfig.multipleOrSingleTestData(param, KEY);

        for (String key : keys) {
            List<Object> actualList = response.path(key);
            Assert.assertTrue(actualList.isEmpty(), "The list is not empty for key: " + key);
        }
    }

    public void verifyTheKeyHasNoEmptyValue(Properties params) {
        String key = params.getProperty(KEY);
        Assert.assertNotNull(response.path(key), "Value should not be null.");
        System.out.println(response.asPrettyString());
    }

    /**
     * Method to verify that a single element from the response is equal to an expected.
     *
     * @param testData Properties pulled by getTestData() method with the element as the variable's name and its expected value.
     */
    public RestCommonValidations verifyResponseBodySingleElement(Properties testData) {
        //Sort properties
        Map<String, String> elements = new HashMap<>();
        Map<String, String> finalElements = elements;
        testData.forEach((key, value) -> {
            finalElements.put(key.toString().toLowerCase(Locale.ROOT), value.toString());
        });
        // Sort map by keys in Java 8 and above
        elements = elements.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        //Read sorted map with prefixed properties
        JSONObject jsonObject = new JSONObject(response.asPrettyString());
        Iterator<Map.Entry<String, String>> itr = elements.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            System.out.println("JSON Object: " + jsonObject);
            System.out.println("Expected: " + entry.getKey() + " = " + entry.getValue());
            System.out.println("Actual  : " + jsonObject.get(entry.getKey()).toString());
            Assert.assertEquals(jsonObject.get(entry.getKey()).toString(), entry.getValue());
        }
        return this;
    }

    /**
     * This method can be used for every Response that returns and ArrayList
     * which is like {"entryList":[{fieldName:value}]}
     * it can verify Both String and Integer values
     *
     * @param params
     * @param keyValue
     * @param exists
     * @return
     */
    public RestCommonValidations verifyResponseValueInList(Properties params, Object keyValue, boolean exists) {
        String listName = params.getProperty(LIST_NAME);
        String fieldName = params.getProperty(FIELD_NAME);
        JsonPath js = new JsonPath(response.asString());
        boolean found = false;
        int size = js.getInt("" + listName + ".size()");
        for (int i = 0; i < size; i++) {
            Object entry = js.get("" + listName + "[" + i + "]." + fieldName + "");
            if (keyValue instanceof Integer && entry instanceof Integer) {
                if (((Integer) keyValue).equals((Integer) entry)) {
                    found = true;
                    break;
                }
            } else if (keyValue instanceof String && entry instanceof String) {
                if (((String) keyValue).equals((String) entry)) {
                    found = true;
                    break;
                }
            }
        }
        if (exists)
            Assert.assertTrue(found, "Value " + keyValue + " is not contained in table Response");
        else
            Assert.assertFalse(found, "Value " + keyValue + " is contained in table Response");
        return this;
    }


    /**
     * Verifies if the EORI is displayed in response
     *
     * @param actor
     * @param exists
     * @return
     */

    //This method checks whether the EORI value for a given actor is present or not
    // in the response and verifies if its visibility matches the expected value (exists or not).
    public RestCommonValidations verifyResponseContainsEORIValue(Actors actor, boolean exists) {
        String text = envDataConfig.getEORI(actor.toString());
        JsonPath js = new JsonPath(response.asString());
        boolean found = false;
        int listSize = js.getInt("data.size()");
        for (int i = 0; i < listSize; i++) {
            String eori = js.get("data[" + i + "].eori");
            if (eori.equals(text)) {
                found = true;
                break;
            }
        }
        Assert.assertEquals(found, exists, "Incorrect visibility for value : " + text);
        return this;
    }

    /**
     * Verifies contains text in response
     *
     * @param params
     * @return
     */
    public RestCommonValidations verifyResponseText(Properties params) {
        String expectedText = params.getProperty(VALUE);
        verifyResponseContainsExpectedText(expectedText);
        return this;
    }

    public RestCommonValidations verifyResponseText(String expectedText) {
        verifyResponseContainsExpectedText(expectedText);
        return this;
    }

    private void verifyResponseContainsExpectedText(String expectedText) {
        Assert.assertTrue(response.asString().contains(expectedText));
    }

    /**
     * Method to verify that the response contains the expected count of the parent group of elements.
     *
     * @param expectedCount that defines expectedCount
     */
    public RestCommonValidations verifyResponseCount(int expectedCount) {
        int actualCount = 0;
        if (response.asString().startsWith("[")) {
            JSONArray jsonArray = new JSONArray(response.asString());
            actualCount = jsonArray.length();
        } else {
            actualCount = 1;
        }
        Assert.assertEquals(actualCount, expectedCount);
        return this;
    }

    //This method validates if the values in the API response corresponding to specified keys match the expected values.
    // It handles the response dynamically by checking both single and multiple values,
    // then ensures that the values are correct and in the expected order.
    public void verifyValuesOfGraphResponseUsingList(Properties param) {
        List<String> keys = TestDataConfig.multipleOrSingleTestData(param, "KEY");
        List<String> expectedValues = TestDataConfig.multipleOrSingleTestData(param, "VALUE");

        List<String> actualValues = new ArrayList<>();

        for (String key : keys) {
            String actualValue = response.path(key) == null ? "null" : response.path(key).toString();
            if (actualValue.contains("[")) {
                actualValue = actualValue.substring(actualValue.indexOf("[") + 1, actualValue.indexOf("]"));
            }
            actualValues.add(actualValue);
        }

        Collections.sort(expectedValues);
        Collections.sort(actualValues);
        Assert.assertEquals(actualValues, expectedValues, "Incorrect response");
    }

    public void verifyValuesOfGraphResponse(Properties param) {

        // Fetch keys and expected values from properties using the test case ID
        String keysStr = param.getProperty("KEY");
        String valuesStr = param.getProperty("VALUE");

        // Check if the keys and values properties exist
        if (keysStr == null || valuesStr == null) {
            throw new IllegalArgumentException("Keys or values properties not found for test case ID: ");
        }

        // Split the keys and values into lists
        List<String> keys = Arrays.asList(keysStr.split(","));
        List<String> expectedValues = Arrays.asList(valuesStr.split(","));

        // Verify each key-value pair in the response
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String expectedValue = expectedValues.get(i);

            // Get the actual value from the response
            String actualValue = response.path(key) == null ? "null" : response.path(key).toString();

            // Handle null values
            if ("null".equals(expectedValue)) {
                expectedValue = null;
            }

            // Assert that the actual value matches the expected value
            Assert.assertEquals(actualValue, expectedValue, "Incorrect response value for " + key);
        }
    }

    //This method validates whether specific key-value pairs in the API response
// match expected values provided in a Properties object.
    public void verifyValuesOfRibbonResponseUsingList(Properties param) {
        // Print out all keys in the properties object for debugging
        System.out.println("Keys in properties:");
        param.keySet().forEach(System.out::println);


        // Fetch keys and expected values from properties using the test case ID
        String keysStr = param.getProperty("KEY");
        String valuesStr = param.getProperty("VALUE");

        // Split the keys and values into lists
        List<String> keys = Arrays.asList(keysStr.split(","));
        List<String> expectedValues = Arrays.asList(valuesStr.split(","));

        // Verify each key-value pair in the response
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String expectedValue = expectedValues.get(i);

            // Get the actual value from the response
            String actualValue = response.path(key) == null ? "null" : response.path(key).toString();

            // Assert that the actual value matches the expected value
            Assert.assertEquals(actualValue, expectedValue, "Incorrect response value for " + key);
        }
    }

    //It extracts key-value pairs from the response, normalizes them, and checks if
    // each expected value (split by ,@#) is present in the corresponding actual value.
    // If any expected value is missing, the test fails; otherwise, it logs successful matches.
    public void verifyExistanceOfValuesInResponse(Properties param) {
        List<String> keys = TestDataConfig.multipleOrSingleTestData(param, KEY);
        List<String> expectedValues = TestDataConfig.multipleOrSingleTestData(param, VALUE);
        List<String> actualValues = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object actualValue = response.path(key);
            if (actualValue instanceof Map || actualValue instanceof List) {
                actualValues.add(actualValue.toString());
            } else {
                actualValues.add(actualValue != null ? actualValue.toString() : "");
            }
        }
        Collections.sort(expectedValues);
        Collections.sort(actualValues);
        for (int i = 0; i < actualValues.size(); i++) {
            String actualValue = actualValues.get(i);
            String expectedValue = expectedValues.get(i);
            String[] expectedWords = expectedValue.split(",@# ");
            for (String word : expectedWords) {
                if (!actualValue.contains(word)) {
                    Assert.fail("Expected value '" + word + "' not found in actual value: " + actualValue);
                }
                System.out.println("Expected value '" + word + "' found in actual value.");
            }
        }
    }

    //This method checks whether a specified array in the API response is empty.
    public void verifySpecificArrayIsEmpty(String arrayPath) {
        List<?> dataArray = response.path(arrayPath);
        Assert.assertNotNull(dataArray, "The array '" + arrayPath + "' should exist in the response.");
        Assert.assertTrue(dataArray.isEmpty(), "The array '" + arrayPath + "' should be empty.");
    }

    private void validateErrorSize(List<String> keys, int skippedErrorCount) {
        String regex = "errors\\[(\\d+)]";
        Pattern pattern = Pattern.compile(regex);
        int expectedSize = keys.stream().map(pattern::matcher).filter(Matcher::find)
                .mapToInt(matcher -> Integer.parseInt(matcher.group(1)) + 1)
                .max()
                .orElse(-1);
        expectedSize -= skippedErrorCount / 2;
        ArrayList<?> actualErrorsList = response.body().jsonPath().get("errors");
        int actualSize = actualErrorsList.size();
        System.out.println("Actual size errors: " + actualSize);
        System.out.println("Expected size errors: " + expectedSize);
        Assert.assertEquals(actualSize, expectedSize, "Wrong amount of validation errors");
    }
}
