package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * TestContext is a Spring component that holds the context for Cucumber tests.
 * It includes the RestTemplate for making HTTP requests and stores the response and student ID.
 */
@Slf4j
@Component
public class TestContext {

    private RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> response;
    private String studentId;

    /**
     * Gets the RestTemplate instance.
     *
     * @return the RestTemplate instance
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * Sets the RestTemplate instance.
     *
     * @param restTemplate the RestTemplate instance to set
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Gets the HTTP response.
     *
     * @return the HTTP response
     */
    public ResponseEntity<String> getResponse() {
        return response;
    }

    /**
     * Sets the HTTP response.
     *
     * @param response the HTTP response to set
     */
    public void setResponse(ResponseEntity<String> response) {
        this.response = response;
    }

    /**
     * Sets the student ID.
     *
     * @param studentId the student ID to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the student ID.
     *
     * @return the student ID
     */
    public String getStudentId() {
        return studentId;
    }
}