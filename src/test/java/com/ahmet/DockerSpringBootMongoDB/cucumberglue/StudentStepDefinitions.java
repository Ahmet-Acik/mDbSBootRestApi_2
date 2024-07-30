package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for Cucumber tests related to student operations.
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class StudentStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    private Student student;

    private Map<String, String> studentIds = new HashMap<>();

    /**
     * Verifies that the student service is running by checking the health endpoint.
     */
    @Given("the student service is running")
    public void theStudentServiceIsRunning() {
        ResponseEntity<String> healthResponse = restTemplate.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, healthResponse.getStatusCode());
    }

    /**
     * Initializes a student object with the provided data.
     *
     * @param studentData a list of maps containing student data
     */
    @Given("the following student data:")
    public void theFollowingStudentData(List<Map<String, String>> studentData) {
        Map<String, String> data = studentData.get(0);
        student = new Student();
        student.setName(data.get("name"));
        student.setAge(Integer.parseInt(data.get("age")));
        student.setEmail(data.get("email"));
    }

    /**
     * Sends a POST request to the specified URL with the student data.
     *
     * @param url the URL to send the POST request to
     */
    @When("I send a POST request to {string} with the student data")
    public void iSendAPostRequestToWithTheStudentData(String url) {
        try {
            String json = objectMapper.writeValueAsString(student);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            testContext.setResponse(response);

            // Extract student ID from response and store it
            String responseBody = response.getBody();
            String studentId = extractIdFromResponse(responseBody);
            studentIds.put(student.getName(), studentId);
        } catch (HttpClientErrorException e) {
            ResponseEntity<String> response = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
            testContext.setResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Asserts that the response contains the specified message.
     *
     * @param message the expected message in the response body
     */
    @Then("the response should contain a message {string}")
    public void theResponseShouldContainAMessage(String message) {
        String responseBody = testContext.getResponse().getBody();
        String expectedMessage = message.replace("<id>", extractIdFromResponse(responseBody));
        assertThat(responseBody, containsString(expectedMessage));
    }

    /**
     * Extracts the student ID from the response body.
     *
     * @param responseBody the response body as a string
     * @return the extracted student ID
     */
    private String extractIdFromResponse(String responseBody) {
        try {
            Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);
            String message = responseMap.get("message");
            return message.substring(message.lastIndexOf(":") + 2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Asserts that the response contains the specified error messages.
     *
     * @param errorMessages a list of maps containing the expected error messages
     */
    @And("the response should contain the following error messages:")
    public void theResponseShouldContainTheFollowingErrorMessages(List<Map<String, String>> errorMessages) {
        String responseBody = testContext.getResponse().getBody();
        for (Map<String, String> errorMessage : errorMessages) {
            assertThat(responseBody, containsString(errorMessage.get("error_message")));
        }
    }

    /**
     * Creates students with the provided data and stores their IDs.
     *
     * @param studentsData a list of maps containing student data
     */
    @Given("the following students are created:")
    public void theFollowingStudentsAreCreated(List<Map<String, String>> studentsData) {
        for (Map<String, String> data : studentsData) {
            Student student = new Student();
            student.setName(data.get("name"));
            student.setAge(Integer.parseInt(data.get("age")));
            student.setEmail(data.get("email"));

            try {
                String json = objectMapper.writeValueAsString(student);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> request = new HttpEntity<>(json, headers);
                ResponseEntity<String> response = restTemplate.postForEntity("/students", request, String.class);
                testContext.setResponse(response);

                // Extract student ID from response and store it
                String responseBody = response.getBody();
                String studentId = extractIdFromResponse(responseBody);
                studentIds.put(student.getName(), studentId);
            } catch (HttpClientErrorException e) {
                ResponseEntity<String> response = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
                testContext.setResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stores the student ID for the given student name.
     *
     * @param name the name of the student
     */
    @Given("the student ID is stored for {string}")
    public void theStudentIdIsStoredFor(String name) {
        testContext.setStudentId(studentIds.get(name));
    }

    /**
     * Sends a DELETE request to the specified URL.
     *
     * @param url the URL to send the DELETE request to
     */
    @When("I send one DELETE request to {string}")
    public void iSendOneDeleteRequestTo(String url) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url.replace("<id>", testContext.getStudentId()), HttpMethod.DELETE, null, String.class);
            testContext.setResponse(response);
        } catch (HttpClientErrorException e) {
            ResponseEntity<String> response = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
            testContext.setResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}