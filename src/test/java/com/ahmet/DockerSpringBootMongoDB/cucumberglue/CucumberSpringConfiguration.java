package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import org.springframework.boot.test.context.SpringBootTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuration class for integrating Cucumber with Spring Boot.
 * This class ensures that Cucumber uses the Spring application context.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "com.ahmet.DockerSpringBootMongoDB")
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}