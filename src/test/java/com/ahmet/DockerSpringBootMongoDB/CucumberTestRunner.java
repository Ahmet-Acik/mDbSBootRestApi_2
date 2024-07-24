package com.ahmet.DockerSpringBootMongoDB;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        plugin = {"pretty"},
        glue = {"com.ahmet.DockerSpringBootMongoDB.cucumberglue"})
public class CucumberTestRunner {
}
