package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class UpdateStudentStepDefs {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestContext testContext;

    @Autowired
    private StudentRepository studentRepository;

//    @When("I send a PUT request to {string} with the following data:")
//    public void i_send_a_PUT_request_to_with_the_following_data(String url, DataTable dataTable) throws Exception {
//        Student student = new Student();
//        dataTable.asMaps().forEach(row -> {
//            String field = row.get("field");
//            String value = row.get("value");
//            switch (field) {
//                case "name":
//                    student.setName(value);
//                    break;
//                case "age":
//                    student.setAge(Integer.parseInt(value));
//                    break;
//                case "email":
//                    student.setEmail(value);
//                    break;
//                default:
//                    log.warn("Unknown field: {}", field);
//            }
//        });
//
//        String studentJson = new ObjectMapper().writeValueAsString(student);
//        log.info("Sending PUT request to {} with data: {}", url, studentJson);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> request = new HttpEntity<>(studentJson, headers);
//        String absoluteUrl = "http://localhost:8080" + url; // Ensure the URL is absolute
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(absoluteUrl, HttpMethod.PUT, request, String.class);
//            testContext.setResponse(response);
//            log.info("Received response: {}", response.getBody());
//        } catch (Exception e) {
//            log.error("Error during PUT request", e);
//            throw e;
//        }
//
        /*
    Handle HttpClientErrorException: Catch HttpClientErrorException and set the response in the testContext.
Log the error message: Log the error message for debugging purposes.

     */
//    }

    @When("I send a PUT request to {string} with the following data:")
    public void i_send_a_PUT_request_to_with_the_following_data(String url, DataTable dataTable) throws Exception {
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
        log.info("Sending PUT request to {} with data: {}", url, studentJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(studentJson, headers);
        String absoluteUrl = "http://localhost:8080" + url; // Ensure the URL is absolute

        try {
            ResponseEntity<String> response = restTemplate.exchange(absoluteUrl, HttpMethod.PUT, request, String.class);
            testContext.setResponse(response);
            log.info("Received response: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Error during PUT request: {}", e.getResponseBodyAsString());
            testContext.setResponse(ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString()));
        }
    }

}