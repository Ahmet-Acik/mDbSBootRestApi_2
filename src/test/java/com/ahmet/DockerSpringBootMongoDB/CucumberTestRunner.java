package com.ahmet.DockerSpringBootMongoDB;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest (classes = {Application.class,
        CucumberTestRunner.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"com.ahmet.DockerSpringBootMongoDB.cucumberglue"},
        plugin = {"pretty", "html:target/cucumber-reports.html"}
)

public class CucumberTestRunner {
}
