package com.ahmet.DockerSpringBootMongoDB.integration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.ahmet.DockerSpringBootMongoDB")
@EnableAutoConfiguration
public class TestConfig {

}
