package com.ahmet.DockerSpringBootMongoDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for the StudentService.
 * This class contains tests that verify the integration of the StudentService with the Spring Boot application.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate; // TestRestTemplate for performing HTTP requests in tests

    /**
     * Tests the health check endpoint to ensure the application is running.
     */
    @Test
    public void testHealthCheck() {
        // Act: Perform a GET request to the health check endpoint
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

        // Assert: Verify the response status is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}