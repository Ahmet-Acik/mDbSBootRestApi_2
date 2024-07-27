package com.ahmet.DockerSpringBootMongoDB.integration;

import com.ahmet.DockerSpringBootMongoDB.Application;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Student Controller using RestAssured.
 */
@SpringBootTest(classes = {Application.class, TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestAssuredControllerStudent_IT {

    private static final Logger logger = LoggerFactory.getLogger(RestAssuredControllerStudent_IT.class);

    private String studentId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/students";

        String studentJson = "{\"name\":\"Sample Student\",\"email\":\"sample@student.com\",\"age\":20,\"address\":{\"street\":\"123 Main St\",\"city\":\"Sample City\",\"postcode\":12345},\"courses\":[\"Math\",\"Science\"],\"fullTime\":true,\"gpa\":3.5,\"graduationDate\":\"2024-05-13T00:00:00\",\"registerDate\":\"2023-10-01T00:00:00\"}";
        Response response = given().contentType(ContentType.JSON)
                .body(studentJson)
                .when().post()
                .then()
                .log().all() // Log the response for debugging
                .extract().response();

        if (response.statusCode() == 201 && response.getContentType().contains("application/json")) {
            String responseMessage = response.path("message");
            // Use a regular expression to extract the ID from the message
            Pattern pattern = Pattern.compile("ID: ([a-f0-9\\-]+)");
            Matcher matcher = pattern.matcher(responseMessage);
            if (matcher.find()) {
                studentId = matcher.group(1);
            } else {
                System.out.println("Student ID not found in response message. Response body: " + response.asString());
                throw new IllegalStateException("Student ID not found in response message.");
            }
        } else {
            System.out.println("Unexpected response status code: " + response.statusCode() + " or content type: " + response.getContentType());
            throw new IllegalStateException("Unexpected response status code: " + response.statusCode() + " or content type: " + response.getContentType());
        }
    }

    @AfterEach
    public void tearDown() {
        if (studentId != null) {
            Response deleteResponse = given().pathParam("id", studentId)
                    .when().delete("/{id}")
                    .then()
                    .log().all() // Log the request and response for debugging
                    .extract().response();

            if (deleteResponse.statusCode() != 204) {
                System.out.println("Failed to delete student with ID: " + studentId + ". Status code: " + deleteResponse.statusCode());
            }
        } else {
            System.out.println("Student ID is null, cannot delete student.");
        }
    }

    @Test
    public void createStudent_shouldReturn201() {
        String studentJson = "{\"name\":\"Created Student\",\"email\":\"Created@student.com\",\"age\":20}";
        Response response = given().contentType(ContentType.JSON)
                .body(studentJson)
                .when().post()
                .then().statusCode(201)
                .body("message", containsString("ID:"))
                .log().all()
                .extract().response();

        String responseMessage = response.path("message");
        Pattern pattern = Pattern.compile("ID: ([a-f0-9\\-]+)");
        Matcher matcher = pattern.matcher(responseMessage);
        assertTrue(matcher.find(), "ID should be present in the response message");

        String responseID = matcher.group(1);
        assertNotNull(responseID, "ID should not be null");
        assertFalse(responseID.isEmpty(), "ID should not be empty");
    }

    @Test
    public void deleteStudentById_shouldReturn204() {
        given().pathParam("id", studentId)
                .when().delete("/{id}")
                .then().statusCode(204);
    }

    @Test
    public void partiallyUpdateStudent_shouldReturn200() {
        String partialUpdateJson = "{\"email\":\"updated.partial@update.com\"}";
        given().contentType(ContentType.JSON)
                .body(partialUpdateJson)
                .pathParam("id", studentId)
                .when().patch("/{id}")
                .then().log().all()
                .statusCode(200)
                .body("student.email", equalTo("updated.partial@update.com"));
    }

    @Test
    public void findStudentById_shouldReturn200() {
        given().pathParam("id", studentId)
                .when().get("/{id}")
                .then().statusCode(200)
                .body("id", equalTo(studentId));
    }

    @Test
    public void updateStudentName_shouldReturn200() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String updatedStudentJson = "{\"id\":\"" + studentId + "\", \"name\":\"Updated Name Only\", \"email\":\"sample@student.com\", \"age\":20,\"address\":{\"street\":\"123 Main St\",\"city\":\"Sample City\",\"postcode\":12345},\"courses\":[\"Math\",\"Science\"],\"fullTime\":true,\"gpa\":3.5,\"graduationDate\":\"2024-05-13T00:00:00\",\"registerDate\":\"2023-10-01T00:00:00\"}";
        logger.info("Executing updateStudentName_shouldReturn200 with studentId: {} and payload: {}", studentId, updatedStudentJson);

        Response response = given().contentType(ContentType.JSON)
                .body(updatedStudentJson)
                .when()
                .put("/{id}", studentId);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Received status code: {} for updateStudentName_shouldReturn200 with studentId: {}. Response body: {}", statusCode, studentId, responseBody);
        assertEquals(200, statusCode, "Expected status code 200 but received: " + statusCode + ". Response body: " + responseBody);
    }

    @Test
    public void findAllStudents_shouldReturn200() {
        when().get("/all")
                .then().statusCode(200)
                .body("$", hasSize(greaterThan(0)));
    }

    // Unhappy Path Tests

    @Test
    public void deleteNonExistentStudent_shouldReturn404() {
        given().contentType(ContentType.JSON)
                .when()
                .delete("/nonexistentId")
                .then().statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    public void updateStudent_withInvalidData_shouldReturn400() {
        String invalidUpdatedStudentJson = "{\"id\":\"" + studentId + "\", \"name\":\"\", \"email\":\"invalidemail\", \"age\":-1}";
        given().contentType(ContentType.JSON)
                .body(invalidUpdatedStudentJson)
                .when()
                .put("/" + studentId)
                .then().statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    public void createStudent_withInvalidData_shouldReturn400() {
        String[][] testCases = {
                {"", "20", "john@example.com", "Missing required field: Name is required"},
                {"John", "-1", "john@example.com", "Missing required field: Age must be a positive number"},
                {"John", "20", "invalid-email", "Missing required field: Email is not valid"}
        };

        for (String[] testCase : testCases) {
            String name = testCase[0];
            String age = testCase[1];
            String email = testCase[2];
            String expectedErrorMessage = testCase[3];

            String invalidStudentJson = String.format("{\"name\":\"%s\", \"age\":%s, \"email\":\"%s\"}", name, age, email);
            logger.info("Executing createStudent_withInvalidData_shouldReturn400 with payload: {}", invalidStudentJson);

            Response response = given().contentType(ContentType.JSON)
                    .body(invalidStudentJson)
                    .when()
                    .post()
                    .then().log().all()
                    .statusCode(400)
                    .body("message", equalTo(expectedErrorMessage))
                    .extract().response();

            logger.info("Received response: {}", response.asString());
        }
    }
    /*
    ### Plan

1. **Log Request and Response**: Add logging to capture the request and response details for each test.
2. **Verify Status Code and Response Body**: Ensure the status code and response body are correctly verified.

### Updated Test Class

```java
package com.ahmet.DockerSpringBootMongoDB.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredControllerStudentTest {

    private static final Logger logger = LoggerFactory.getLogger(RestAssuredControllerStudentTest.class); // create private static logger

    @Test
    public void createStudent_withInvalidData_shouldReturn400() {
        String invalidStudentJson = "{\"name\":\"\", \"email\":\"invalidemail\", \"age\":-1}";
        logger.info("Executing createStudent_withInvalidData_shouldReturn400 with payload: {}", invalidStudentJson);   // add logger.info (“Executing ……with payload:{}” invalidSudentJson) for request.

        Response response = given().contentType(ContentType.JSON)
                .body(invalidStudentJson)
                .when()
                .post("/")
                .then().log().all()
                .statusCode(400)
                .body("message", notNullValue())
                .extract().response();

        logger.info("Received response: {}", response.asString()); add logger.info("Received response: {}", response.asString()); for response
    }

    @Test
    public void partiallyUpdateStudent_withInvalidData_shouldReturn400() {
        String invalidPartialUpdateJson = "{\"email\":\"invalidemail\"}";
        logger.info("Executing partiallyUpdateStudent_withInvalidData_shouldReturn400 with payload: {}", invalidPartialUpdateJson);

        Response response = given().contentType(ContentType.JSON)
                .body(invalidPartialUpdateJson)
                .when()
                .patch("/" + studentId)
                .then().log().all()
                .statusCode(400)
                .body("message", notNullValue())
                .extract().response();

        logger.info("Received response: {}", response.asString());
    }

    @Test
    public void findNonExistentStudentById_shouldReturn404() {
        logger.info("Executing findNonExistentStudentById_shouldReturn404 with non-existent ID");

        Response response = given().pathParam("id", "non-existent-id")
                .when()
                .get("/{id}")
                .then().log().all()
                .statusCode(404)
                .body("message", equalTo("Student not found"))
                .extract().response();

        logger.info("Received response: {}", response.asString());
    }
}
```

Plan
Analyze the Test Failures: Identify the root cause of each test failure.
Fix the Issues: Update the tests and possibly the controller to ensure the correct status codes and responses are returned.

Analysis and Fixes
Test createStudent_withInvalidData_shouldReturn400:
Issue: Expected status code 400 but received 404.
Fix: Ensure the controller returns a 400 status code for invalid data.

Test findNonExistentStudentById_shouldReturn404:
Issue: No content-type was defined in the response.
Fix: Ensure the controller sets the content-type for 404 responses.

Test partiallyUpdateStudent_withInvalidData_shouldReturn400:
Issue: Expected status code 400 but received 200.
Fix: Ensure the controller returns a 400 status code for invalid data during partial updates.
     */
}
