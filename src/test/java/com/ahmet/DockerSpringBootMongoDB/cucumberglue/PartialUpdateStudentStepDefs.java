package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
@Transactional
public class PartialUpdateStudentStepDefs {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestContext testContext;

    @Autowired
    private StudentRepository studentRepository;

    @Given("the following students exist:")
    public void the_following_students_exist(DataTable dataTable) {
        dataTable.asMaps().forEach(row -> {
            Student student = new Student();
            student.setId(row.get("id"));
            student.setName(row.get("name"));
            student.setAge(Integer.parseInt(row.get("age")));
            student.setEmail(row.get("email"));
            studentRepository.save(student);
            log.info("Student created: {}", student);
        });
    }

    @When("I send a PATCH request to {string} with the following data:")
    public void i_send_a_PATCH_request_to_with_the_following_data(String url, DataTable dataTable) throws Exception {
        Student student = new Student();
        dataTable.asMaps().forEach(row -> {
            String field = row.get("field");
            String value = row.get("value");
            switch (field) {
                case "name":
                    student.setName(value);
                    break;
                case "age":
                    student.setAge(Integer.parseInt(value));
                    break;
                case "email":
                    student.setEmail(value);
                    break;
                default:
                    log.warn("Unknown field: {}", field);
            }
        });

        String studentJson = new ObjectMapper().writeValueAsString(student);
        log.info("Sending PATCH request to {} with data: {}", url, studentJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(studentJson, headers);
        String absoluteUrl = "http://localhost:8080" + url; // Ensure the URL is absolute

        // Check if the student exists before sending the PATCH request
        String studentId = url.substring(url.lastIndexOf('/') + 1);
        boolean studentExists = studentRepository.existsById(studentId);
        if (!studentExists) {
            log.info("Student with ID {} does not exist", studentId);
            testContext.setResponse(ResponseEntity.status(404).body("{\"message\":\"Student not found\",\"student\":null}"));
            return;
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(absoluteUrl, HttpMethod.PATCH, request, String.class);
            testContext.setResponse(response);
            log.info("Received response: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error during PATCH request", e);
            throw e;
        }
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        ResponseEntity<String> response = testContext.getResponse();
        assertEquals(expectedStatus, response.getStatusCodeValue());
        log.info("Response status is as expected: {}", expectedStatus);
    }


    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedMessage) {
        ResponseEntity<String> response = testContext.getResponse();
        String responseBody = response.getBody();
        assertThat(responseBody, containsString(expectedMessage));
    }
}