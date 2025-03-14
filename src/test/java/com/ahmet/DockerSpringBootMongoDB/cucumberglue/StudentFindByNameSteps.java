package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for Cucumber tests related to finding students by name.
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class StudentFindByNameSteps {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    private final String baseUrl = "http://localhost:8080"; // Adjust the port if necessary

    /**
     * Ensures that no students exist with the given name prefix.
     *
     * @param prefix the name prefix to check
     */
    @Given("no students exist with name prefix {string}")
    public void noStudentsExistWithNamePrefix(String prefix) {
        studentRepository.deleteAll();
    }

    /**
     * Sends a GET request to find students by name.
     *
     * @param url the URL to send the GET request to
     */
    @When("I send a GET request to find by name {string}")
    public void iSendAGetRequestToByName(String url) {
        String absoluteUrl = baseUrl + url;
        ResponseEntity<String> response = testContext.getRestTemplate().getForEntity(absoluteUrl, String.class);
        testContext.setResponse(response);
    }

    /**
     * Asserts that the response status is as expected.
     *
     * @param statusCode the expected HTTP status code
     */
    @Then("the response status for find by name should be {int}")
    public void theResponseStatusForFindByNameShouldBe(int statusCode) {
        ResponseEntity<String> response = testContext.getResponse();
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    /**
     * Asserts that the response contains the expected students.
     *
     * @param expectedStudents a list of maps containing the expected student data
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    @Then("the response should contain the following students:")
    public void theResponseShouldContainTheFollowingStudents(List<Map<String, String>> expectedStudents) throws JsonProcessingException {
        String responseBody = testContext.getResponse().getBody();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Student.class);
        List<Student> actualStudents = objectMapper.readValue(responseBody, listType);

        assertEquals(expectedStudents.size(), actualStudents.size());

        expectedStudents.forEach(expected -> {
            Student actual = actualStudents.stream()
                    .filter(student -> student.getId().equals(expected.get("id")))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Student not found: " + expected.get("id")));

            assertEquals(expected.get("name"), actual.getName());
            assertEquals(expected.get("age"), String.valueOf(actual.getAge())); // Convert age to String for comparison
            assertEquals(expected.get("email"), actual.getEmail());
        });
    }
}