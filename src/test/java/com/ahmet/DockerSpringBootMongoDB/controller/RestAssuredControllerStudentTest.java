package com.ahmet.DockerSpringBootMongoDB.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Student Controller using RestAssured.
 */
public class RestAssuredControllerStudentTest {


    private String studentId;

    /**
     * Setup method to initialize RestAssured base URI, port, and basePath.
     * Creates a student to be used in subsequent tests and extracts the student's ID.
     */
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
    /**
     * Tear down method to delete the student created during setup.
     * Ensures clean state for subsequent tests.
     */
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

    /**
     * Test to verify that creating a student returns a 201 status code
     * and the response contains the student's ID.
     */
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

    /**
     * Test to verify that deleting a student by ID returns a 204 status code.
     */
    @Test
    public void deleteStudentById_shouldReturn204() {
        // Use the studentId from setup instead of hardcoded "1"
        given().pathParam("id", studentId)
                .when().delete("/{id}")
                .then().statusCode(204);
    }

    /**
     * Test to verify that partially updating a student's email returns a 200 status code
     * and the email is updated as expected.
     */
    @Test
    public void partiallyUpdateStudent_shouldReturn200() {
        String partialUpdateJson = "{\"email\":\"updated.partial@update.com\"}";
        given().contentType(ContentType.JSON)
                .body(partialUpdateJson)
                .pathParam("id", studentId) // Ensure this is the correct studentId captured from setup
                .when().patch("/{id}")
                .then().log().all() // Log the response for debugging
                .statusCode(200)
                .body("student.email", equalTo("updated.partial@update.com")); // Adjusted to navigate through the nested structure
    }

    /**
     * Test to verify that retrieving a student by ID returns a 200 status code
     * and the correct student ID in the response.
     */
    @Test
    public void findStudentById_shouldReturn200() {
        // Ensure studentId is not null and correctly used
        given().pathParam("id", studentId)
                .when().get("/{id}")
                .then().statusCode(200)
                .body("id", equalTo(studentId));
    }

    /**
     * Test to verify that updating a student's name returns a 200 status code.
     * Logs the request and response for debugging.
     */
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

    /**
     * Test to verify that retrieving all students returns a 200 status code
     * and the response contains at least one student.
     */
    @Test
    public void findAllStudents_shouldReturn200() {
        // This test does not need to use studentId as it checks for all students
        when().get("/all")
                .then().statusCode(200)
                .body("$", hasSize(greaterThan(0)));
    }
}
