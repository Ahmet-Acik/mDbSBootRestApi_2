package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Step definitions for Cucumber tests related to finding all students.
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class StudentFindAllSteps {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    /**
     * Sends a GET request to the specified URL.
     *
     * @param url the URL to send the GET request to
     */
    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        testContext.setResponse(response);
    }

    /**
     * Asserts that the response contains a list of students.
     *
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    @Then("the response should contain a list of students")
    public void theResponseShouldContainAListOfStudents() throws JsonProcessingException {
        String responseBody = testContext.getResponse().getBody();
        List<Student> students = objectMapper.readValue(responseBody, new TypeReference<List<Student>>() {});
        assertThat(students, is(not(empty())));
    }

    /**
     * Ensures that the student repository is empty.
     */
    @Given("the student repository is empty")
    public void theStudentRepositoryIsEmpty() {
        studentRepository.deleteAll();
    }
}