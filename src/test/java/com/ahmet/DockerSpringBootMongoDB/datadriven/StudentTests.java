package com.ahmet.DockerSpringBootMongoDB.datadriven;

import com.ahmet.DockerSpringBootMongoDB.utils.CsvUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Arrays;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentTests {

    private static String studentId;
    private static final String UPDATE_CSV_PATH = "src/test/resources/test-data/update-student.csv";
    private static final String GET_CSV_PATH = "src/test/resources/test-data/get-student.csv";
    private static final String DELETE_CSV_PATH = "src/test/resources/test-data/delete-student.csv";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data/create-student.csv", numLinesToSkip = 1)
    void createStudentTest(String name, String email, int age) {
        String studentJson = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"age\":%d}", name, email, age);

        Response response = given().contentType(ContentType.JSON)
                .body(studentJson)
                .when().post("/students")
                .then()
                .statusCode(201)
                .extract().response();

        studentId = response.jsonPath().getString("message").split(": ")[1];

        // Update the CSV files with the new student ID
        List<String[]> updateData = Arrays.asList(
                new String[]{"id", "name", "email", "age"},
                new String[]{studentId, name, email, String.valueOf(age)}
        );
        CsvUtil.updateCsvFile(UPDATE_CSV_PATH, updateData);

        List<String[]> getData = Arrays.asList(
                new String[]{"id"},
                new String[]{studentId}
        );
        CsvUtil.updateCsvFile(GET_CSV_PATH, getData);

        List<String[]> deleteData = Arrays.asList(
                new String[]{"id"},
                new String[]{studentId}
        );
        CsvUtil.updateCsvFile(DELETE_CSV_PATH, deleteData);

        // Additional assertions
        assertEquals(201, response.getStatusCode());
    }

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data/get-student.csv", numLinesToSkip = 1)
    void getStudentTest(String id) {
        Response response = given().pathParam("id", id)
                .when().get("/students/{id}")
                .then()
                .statusCode(200)
                .extract().response();

        // Additional assertions
        assertEquals(200, response.getStatusCode());
    }

    @Order(3)
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data/update-student.csv", numLinesToSkip = 1)
    void updateStudentTest(String id, String name, String email, int age) {
        String studentJson = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"age\":%d}", name, email, age);

        Response response = given().contentType(ContentType.JSON)
                .body(studentJson)
                .when().put("/students/{id}", id)
                .then()
                .statusCode(200)
                .extract().response();

        // Additional assertions
        assertEquals(200, response.getStatusCode());
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data/delete-student.csv", numLinesToSkip = 1)
    void deleteStudentTest(String id) {
        Response response = given().pathParam("id", id)
                .when().delete("/students/{id}")
                .then()
                .statusCode(204)
                .extract().response();

        // Additional assertions
        assertEquals(204, response.getStatusCode());
    }
}