package com.ahmet.DockerSpringBootMongoDB.integration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for integration tests.
 * This class sets up the necessary beans and configurations for running integration tests.
 */
@Configuration
@ComponentScan(basePackages = "com.ahmet.DockerSpringBootMongoDB")
@EnableAutoConfiguration
public class TestConfig {

    // Additional beans and configurations can be added here if needed

}