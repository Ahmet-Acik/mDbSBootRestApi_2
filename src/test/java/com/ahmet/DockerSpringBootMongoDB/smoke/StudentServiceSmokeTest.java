package com.ahmet.DockerSpringBootMongoDB.smoke;

import com.ahmet.DockerSpringBootMongoDB.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Smoke tests for the StudentService.
 * These tests ensure that the application context loads and basic endpoints are accessible.
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentServiceSmokeTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Tests if the application is up by checking the health endpoint.
     */
    @Test
    public void testApplicationUp() {
        ResponseEntity<String> healthResponse = restTemplate.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, healthResponse.getStatusCode());
    }

    /**
     * Tests if the endpoint to find all students is accessible and returns a status of OK.
     */
    @Test
    public void testFindAllStudents() {
        ResponseEntity<String> response = restTemplate.getForEntity("/students/all", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}