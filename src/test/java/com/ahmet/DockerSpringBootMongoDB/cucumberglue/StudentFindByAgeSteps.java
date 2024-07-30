package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Step definitions for Cucumber tests related to finding students by age.
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class StudentFindByAgeSteps {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    private final String baseUrl = "http://localhost:8080"; // Adjust the port if necessary

    /**
     * Populates the database with the given students.
     *
     * @param studentsData a list of maps containing student data
     */
    @Given("students with the following data exist:")
    public void studentsWithTheFollowingDataExist(List<Map<String, String>> studentsData) {
        studentRepository.deleteAll();
        studentsData.forEach(data -> {
            Student student = new Student();
            student.setId(data.get("id"));
            student.setName(data.get("name"));
            student.setAge(Integer.parseInt(data.get("age")));
            student.setEmail(data.get("email"));
            studentRepository.save(student);
        });
    }

    /**
     * Ensures that no students exist within the specified age range.
     *
     * @param minAge the minimum age
     * @param maxAge the maximum age
     */
    @Given("no students exist within age range {int} to {int}")
    public void noStudentsExistWithinAgeRange(int minAge, int maxAge) {
        studentRepository.deleteAll();
    }

    /**
     * Sends a GET request to find students by age range.
     * Parses the minAge and maxAge from the provided string parameter.
     *
     * @param ageRange the age range in the format "minAge=<minAge>&maxAge=<maxAge>"
     */
    @When("I send a GET request to find by age range {string}")
    public void iSendAGetRequestToFindByAgeRange(String ageRange) {
        String[] ages = ageRange.split("&");
        int minAge = Integer.parseInt(ages[0].split("=")[1]);
        int maxAge = Integer.parseInt(ages[1].split("=")[1]);
        String url = String.format("%s/students/age?minAge=%d&maxAge=%d", baseUrl, minAge, maxAge);
        ResponseEntity<String> response = testContext.getRestTemplate().getForEntity(url, String.class);
        testContext.setResponse(response);
    }
}