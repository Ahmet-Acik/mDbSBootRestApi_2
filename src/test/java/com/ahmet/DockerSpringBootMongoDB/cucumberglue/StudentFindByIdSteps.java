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

    @Given("no student with ID {string} exists")
    public void noStudentWithIdExists(String id) {
        studentRepository.deleteById(id);
    }

    @Then("the response should contain the student with ID {string}")
    public void theResponseShouldContainTheStudentWithId(String id) throws JsonProcessingException {
        String responseBody = testContext.getResponse().getBody();
        Student student = objectMapper.readValue(responseBody, Student.class);
        assertEquals(id, student.getId());
    }
}