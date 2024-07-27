// src/test/java/com/ahmet/DockerSpringBootMongoDB/smoke/StudentServiceSmokeTest.java
package com.ahmet.DockerSpringBootMongoDB.smoke;

import com.ahmet.DockerSpringBootMongoDB.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StudentServiceSmokeTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testApplicationUp() {

            ResponseEntity<String> healthResponse = restTemplate.getForEntity("/actuator/health", String.class);
            assertEquals(HttpStatus.OK, healthResponse.getStatusCode());
    }

    @Test
    public void testFindAllStudents() {
        ResponseEntity<String> response = restTemplate.getForEntity("/students/all", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}