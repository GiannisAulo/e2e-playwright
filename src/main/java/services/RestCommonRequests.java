package services;

import config.EnvDataConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.factory.StaticContextFactory;
import utils.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;


@SuppressWarnings("rawtypes")
public class RestCommonRequests {

    Utils utils;
    EnvDataConfig envDataConfig;

    ExtractableResponse response;
    private Map<String, String> storedReportDate;

    public RestCommonRequests(Utils utils) {
        this.utils = utils;
        envDataConfig = new EnvDataConfig();
    }

    public static void waitForAction(Integer time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            e.printStackTrace();
        }
    }

    public ExtractableResponse getResponse(String endpoint, String referer) {

        RestAssured.baseURI = StaticContextFactory.api;
        try {
            response = RestAssured.given()
                    .header("Referer", StaticContextDataFactory.referer)
                    .when()
                    .get(endpoint)
                    .then().extract();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Get response of listed registries, based on parameters
     *
     * @param endpoint the endpoint of service
     * @return the response
     */
    public ExtractableResponse getResponse(Properties params, String endpoint, String referer) {
        RestAssured.baseURI = StaticContextFactory.api;
        try {
            response = RestAssured.given()
                    .queryParam("pageNumber", params.getProperty("PageNumber"))
                    .queryParam("pageSize", params.getProperty("PageSize"))
                    .queryParam("sort", params.getProperty("Sort"))
                    .when()
                    .get(endpoint)
                    .then().extract();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Get response of listed registries, based on given filter
     *
     * @param endpoint the endpoint of service
     * @return the response
     */
    public ExtractableResponse getResponseByFilter(Properties params, String filter, String endpoint, String referer) {
        RestAssured.baseURI = StaticContextDataFactory.api;
        try {
            response = RestAssured.given()
                    .queryParam("pageNumber", params.getProperty("PageNumber"))
                    .queryParam("pageSize", params.getProperty("PageSize"))
                    .queryParam("sort", params.getProperty("Sort"))
                    .queryParam(filter, params.getProperty("Value"))
                    .when()
                    .get(endpoint)
                    .then().extract();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public <T> ExtractableResponse post(String endpoint, T requestBody, String referer) {
        RestAssured.baseURI = StaticContextDataFactory.api;
        try {
            if (envDataConfig.getUUMDSMode() || envDataConfig.getCCN2Mode()) {
                response = RestAssured.given()
                        .header("Referer", StaticContextDataFactory.referer)
                        .header("X-XSRF-TOKEN", StaticContextDataFactory.x_xsrf_Token)
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post(endpoint)
                        .then().log().status().extract();
            } else {
                response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post(endpoint)
                        .then().log().status().extract();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public <T> ExtractableResponse update(String endpoint, T requestBody, String referer) {
        RestAssured.baseURI = StaticContextDataFactory.api;
        try {
            if (envDataConfig.getUUMDSMode() || envDataConfig.getCCN2Mode()) {
                response = RestAssured.given()
                        .header("X-XSRF-TOKEN", StaticContextDataFactory.x_xsrf_Token)
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .put(endpoint)
                        .then().extract();
            } else {
                response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .put(endpoint)
                        .then().extract();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public ExtractableResponse delete(String endpoint, String whatToDelete, String referer) {
        RestAssured.baseURI = StaticContextDataFactory.api;
        endpoint = endpoint + whatToDelete;
        try {
            if (envDataConfig.getUUMDSMode() || envDataConfig.getCCN2Mode()) {
                response = RestAssured.given()
                        .header("X-XSRF-TOKEN", StaticContextDataFactory.x_xsrf_Token)
                        .contentType(ContentType.JSON)
                        .when()
                        .delete(endpoint)
                        .then().extract();
            } else {
                response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .when()
                        .delete(endpoint)
                        .then().extract();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String readLineByLine(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public ExtractableResponse modifyRequestBodyAndPostEachObject(JSONArray jsonArray, String endpoint, String referer) {
        ExtractableResponse extractableResponse = null;
        int count = jsonArray.length(); // get totalCount of all jsonObjects
        for (int i = 0; i < count; i++) {   // iterate through jsonArray
            JSONObject jsonObject = jsonArray.getJSONObject(i);  // get jsonObject @ i position
            extractableResponse = post(endpoint, jsonObject.toString(), referer);

        }
        return extractableResponse;
    }

    public ExtractableResponse modifyRequestBodyAndImporterAndPostEachObject(JSONArray jsonArray, String endpoint, String referer) {
        ExtractableResponse extractableResponse = null;
        int count = jsonArray.length(); // get totalCount of all jsonObjects
        for (int i = 0; i < count; i++) {   // iterate through jsonArray
            JSONObject jsonObject = jsonArray.getJSONObject(i);  // get jsonObject @ i position
            if (jsonObject.getJSONObject("economicOperatorsCriteria").has("importerId")) {
                jsonObject.getJSONObject("economicOperatorsCriteria").put("importerId", envDataConfig.getImporterIdentifier("ImporterIdentifier"));
            }
            extractableResponse = post(endpoint, jsonObject.toString(), referer);
        }
        return extractableResponse;

    }
}
