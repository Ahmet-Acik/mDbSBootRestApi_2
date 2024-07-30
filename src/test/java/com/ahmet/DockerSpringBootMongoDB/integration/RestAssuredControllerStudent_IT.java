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

    /**
     * Sets up the test environment before each test.
     * Creates a sample student and extracts its ID from the response.
     */
    @BeforeEach
    public void setup() {
        // Set base URI, port, and base path for RestAssured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/students";

        // JSON string for creating a sample student
        String studentJson = "{\"name\":\"Sample Student\",\"email\":\"sample@student.com\",\"age\":20,\"address\":{\"street\":\"123 Main St\",\"city\":\"Sample City\",\"postcode\":12345},\"courses\":[\"Math\",\"Science\"],\"fullTime\":true,\"gpa\":3.5,\"graduationDate\":\"2024-05-13T00:00:00\",\"registerDate\":\"2023-10-01T00:00:00\"}";

        // Perform a POST request to create a new student
        Response response = given().contentType(ContentType.JSON)
                .body(studentJson)
                .when().post()
                .then()
                .log().all() // Log the response for debugging
                .extract().response();

        // Check if the response status code is 201 and content type is JSON
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

    /**
     * Cleans up the test environment after each test.
     * Deletes the created student by ID.
     */
    @AfterEach
    public void tearDown() {
        // Check if studentId is not null
        if (studentId != null) {
            // Perform a DELETE request to delete the student by ID
            Response deleteResponse = given().pathParam("id", studentId)
                    .when().delete("/{id}")
                    .then()
                    .log().all() // Log the request and response for debugging
                    .extract().response();

            // Check if the response status code is not 204
            if (deleteResponse.statusCode() != 204) {
                System.out.println("Failed to delete student with ID: " + studentId + ". Status code: " + deleteResponse.statusCode());
            }
        } else {
            System.out.println("Student ID is null, cannot delete student.");
        }
    }

    /**
     * Tests creating a student and verifies that the response status is 201.
     */
    @Test
    public void createStudent_shouldReturn201() {
        // JSON string for creating a new student
        String studentJson = "{\"name\":\"Created Student\",\"email\":\"Created@student.com\",\"age\":20}";

        // Perform a POST request to create a new student
        Response response = given().contentType(ContentType.JSON)
                .body(studentJson)
                .when().post()
                .then().statusCode(201) // Assert that the status code is 201
                .body("message", containsString("ID:")) // Assert that the response body contains "ID:"
                .log().all() // Log the response for debugging
                .extract().response();

        // Extract the message from the response
        String responseMessage = response.path("message");
        // Use a regular expression to extract the ID from the message
        Pattern pattern = Pattern.compile("ID: ([a-f0-9\\-]+)");
        Matcher matcher = pattern.matcher(responseMessage);
        assertTrue(matcher.find(), "ID should be present in the response message");

        // Extract the ID from the matcher
        String responseID = matcher.group(1);
        assertNotNull(responseID, "ID should not be null");
        assertFalse(responseID.isEmpty(), "ID should not be empty");
    }

    /**
     * Tests deleting a student by ID and verifies that the response status is 204.
     */
    @Test
    public void deleteStudentById_shouldReturn204() {
        // Perform a DELETE request to delete the student by ID
        given().pathParam("id", studentId)
                .when().delete("/{id}")
                .then().statusCode(204); // Assert that the status code is 204
    }

    /**
     * Tests partially updating a student and verifies that the response status is 200.
     */
    @Test
    public void partiallyUpdateStudent_shouldReturn200() {
        // JSON string for partially updating the student's email
        String partialUpdateJson = "{\"email\":\"updated.partial@update.com\"}";

        // Perform a PATCH request to partially update the student
        given().contentType(ContentType.JSON)
                .body(partialUpdateJson)
                .pathParam("id", studentId)
                .when().patch("/{id}")
                .then().log().all() // Log the response for debugging
                .statusCode(200) // Assert that the status code is 200
                .body("student.email", equalTo("updated.partial@update.com")); // Assert that the email is updated
    }

    /**
     * Tests finding a student by ID and verifies that the response status is 200.
     */
    @Test
    public void findStudentById_shouldReturn200() {
        // Perform a GET request to find the student by ID
        given().pathParam("id", studentId)
                .when().get("/{id}")
                .then().statusCode(200) // Assert that the status code is 200
                .body("id", equalTo(studentId)); // Assert that the ID matches
    }

    /**
     * Tests updating a student's name and verifies that the response status is 200.
     */
    @Test
    public void updateStudentName_shouldReturn200() {
        // Logger for logging information
        Logger logger = LoggerFactory.getLogger(this.getClass());

        // JSON string for updating the student's name
        String updatedStudentJson = "{\"id\":\"" + studentId + "\", \"name\":\"Updated Name Only\", \"email\":\"sample@student.com\", \"age\":20,\"address\":{\"street\":\"123 Main St\",\"city\":\"Sample City\",\"postcode\":12345},\"courses\":[\"Math\",\"Science\"],\"fullTime\":true,\"gpa\":3.5,\"graduationDate\":\"2024-05-13T00:00:00\",\"registerDate\":\"2023-10-01T00:00:00\"}";
        logger.info("Executing updateStudentName_shouldReturn200 with studentId: {} and payload: {}", studentId, updatedStudentJson);

        // Perform a PUT request to update the student's name
        Response response = given().contentType(ContentType.JSON)
                .body(updatedStudentJson)
                .when()
                .put("/{id}", studentId);

        // Extract the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Received status code: {} for updateStudentName_shouldReturn200 with studentId: {}. Response body: {}", statusCode, studentId, responseBody);

        // Assert that the status code is 200
        assertEquals(200, statusCode, "Expected status code 200 but received: " + statusCode + ". Response body: " + responseBody);
    }

    /**
     * Tests finding all students and verifies that the response status is 200.
     */
    @Test
    public void findAllStudents_shouldReturn200() {
        // Perform a GET request to find all students
        when().get("/all")
                .then().statusCode(200) // Assert that the status code is 200
                .body("$", hasSize(greaterThan(0))); // Assert that the response body has a size greater than 0
    }

    // Unhappy Path Tests

    /**
     * Tests deleting a non-existent student and verifies that the response status is 404.
     */
    @Test
    public void deleteNonExistentStudent_shouldReturn404() {
        // Perform a DELETE request to delete a non-existent student
        given().contentType(ContentType.JSON)
                .when()
                .delete("/nonexistentId")
                .then().statusCode(404) // Assert that the status code is 404
                .body("message", notNullValue()); // Assert that the response body contains a message
    }

    /**
     * Tests updating a student with invalid data and verifies that the response status is 400.
     */
    @Test
    public void updateStudent_withInvalidData_shouldReturn400() {
        // JSON string for updating the student with invalid data
        String invalidUpdatedStudentJson = "{\"id\":\"" + studentId + "\", \"name\":\"\", \"email\":\"invalidemail\", \"age\":-1}";

        // Perform a PUT request to update the student with invalid data
        given().contentType(ContentType.JSON)
                .body(invalidUpdatedStudentJson)
                .when()
                .put("/" + studentId)
                .then().statusCode(400) // Assert that the status code is 400
                .body("message", notNullValue()); // Assert that the response body contains a message
    }

    /**
     * Tests creating a student with invalid data and verifies that the response status is 400.
     */
    @Test
    public void createStudent_withInvalidData_shouldReturn400() {
        // Define test cases for invalid student data
        String[][] testCases = {
                {"", "20", "john@example.com", "Missing required field: Name is required"},
                {"John", "-1", "john@example.com", "Missing required field: Age must be a positive number"},
                {"John", "20", "invalid-email", "Missing required field: Email is not valid"}
        };

        // Iterate over each test case
        for (String[] testCase : testCases) {
            String name = testCase[0];
            String age = testCase[1];
            String email = testCase[2];
            String expectedErrorMessage = testCase[3];

            // Arrange: Create a JSON string for the invalid student
            String invalidStudentJson = String.format("{\"name\":\"%s\", \"age\":%s, \"email\":\"%s\"}", name, age, email);
            logger.info("Executing createStudent_withInvalidData_shouldReturn400 with payload: {}", invalidStudentJson);

            // Act: Perform a POST request to create a new student with invalid data
            Response response = given().contentType(ContentType.JSON)
                    .body(invalidStudentJson)
                    .when()
                    .post()
                    .then().log().all() // Log the response for debugging
                    .statusCode(400) // Assert: Verify the response status code is 400 (Bad Request)
                    .body("message", equalTo(expectedErrorMessage)) // Assert: Verify the response body contains the expected error message
                    .extract().response();

            logger.info("Received response: {}", response.asString());
        }
    }
}