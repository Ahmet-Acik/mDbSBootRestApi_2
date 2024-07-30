package com.ahmet.DockerSpringBootMongoDB;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CucumberTestRunner is the test runner class for Cucumber tests.
 * It configures the Spring Boot context and specifies the Cucumber options.
 */
@SpringBootTest(classes = {Application.class, CucumberTestRunner.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"}, // Path to the feature files
        glue = {"com.ahmet.DockerSpringBootMongoDB.cucumberglue"}, // Package containing the step definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"} // Plugins for generating reports
)
public class CucumberTestRunner {
}