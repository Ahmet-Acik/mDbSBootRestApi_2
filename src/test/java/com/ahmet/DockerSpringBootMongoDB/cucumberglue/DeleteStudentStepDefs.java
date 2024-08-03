package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Step definitions for Cucumber tests related to deleting a student.
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class DeleteStudentStepDefs {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TestContext testContext;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Sends a DELETE request to the specified URL.
     * If the student is not found, sets the response status to 404 with a custom message.
     *
     * @param url the URL to send the DELETE request to
     */
    @When("I send a DELETE request to {string}")
    public void i_send_a_DELETE_request_to(String url) {
        String absoluteUrl = "http://localhost:8080" + url; // Ensure the URL is absolute
        try {
            ResponseEntity<String> response = restTemplate.exchange(absoluteUrl, HttpMethod.DELETE, null, String.class);
            testContext.setResponse(response);
            log.info("Received response: {}", response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Student not found for URL: {}", absoluteUrl);
            testContext.setResponse(ResponseEntity.status(404).body("{\"message\":\"Student not found\",\"student\":null}"));
        } catch (Exception e) {
            log.error("Error during DELETE request", e);
            throw e;
        }
    }

}