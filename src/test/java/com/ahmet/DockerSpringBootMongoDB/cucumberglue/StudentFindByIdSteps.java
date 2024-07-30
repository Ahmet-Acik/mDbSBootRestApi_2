package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for Cucumber tests related to finding a student by ID.
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class StudentFindByIdSteps {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    /**
     * Populates the database with a student having the given data.
     *
     * @param studentData a list of maps containing student data
     */
    @Given("a student with the following data exists:")
    public void aStudentWithTheFollowingDataExists(List<Map<String, String>> studentData) {
        Map<String, String> data = studentData.get(0);
        Student student = new Student();
        student.setId(data.get("id"));
        student.setName(data.get("name"));
        student.setAge(Integer.parseInt(data.get("age")));
        student.setEmail(data.get("email"));
        studentRepository.save(student);
    }

    /**
     * Ensures that no student with the specified ID exists in the database.
     *
     * @param id the ID of the student to delete
     */
    @Given("no student with ID {string} exists")
    public void noStudentWithIdExists(String id) {
        studentRepository.deleteById(id);
    }

    /**
     * Asserts that the response contains the student with the specified ID.
     *
     * @param id the ID of the student to check in the response
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    @Then("the response should contain the student with ID {string}")
    public void theResponseShouldContainTheStudentWithId(String id) throws JsonProcessingException {
        String responseBody = testContext.getResponse().getBody();
        Student student = objectMapper.readValue(responseBody, Student.class);
        assertEquals(id, student.getId());
    }
}