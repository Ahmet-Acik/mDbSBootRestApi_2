package com.ahmet.DockerSpringBootMongoDB;

import com.ahmet.DockerSpringBootMongoDB.collection.Address;
import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Main application class for the DockerSpringBootMongoDB project.
 * This class serves as the entry point for the Spring Boot application.
 * It is annotated with @SpringBootApplication to enable auto-configuration, component scan, and extra configuration.
 * It also enables MongoDB repositories through @EnableMongoRepositories.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.ahmet.DockerSpringBootMongoDB.repository")
public class DockerSpringBootMongoDbApplication {

    /**
     * Main method to run the Spring Boot application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(DockerSpringBootMongoDbApplication.class, args);

    }

    /**
     * CommandLineRunner bean to perform actions on application startup.
     * This method checks for existing data in the database and initializes it if not present.
     *
     * @param repository The StudentRepository for database operations.
     * @return A CommandLineRunner bean that checks and initializes data.
     */
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            System.out.println("Checking if data already exists...");

            String email = "flying.dutchman@bikinibottom.com";
            if (!repository.existsByEmail(email)) {
                System.out.println("Initializing data...");

                Student student = getStudent(email);
                repository.insert(student);
                System.out.println("Data initialized.");
            } else {
                System.out.println("Data already exists. Skipping initialization.");
            }
        };
    }

    /**
     * Creates a Student object with predefined data.
     * This method constructs a Student object with a specific email and other hardcoded values.
     *
     * @param email The email address to assign to the new Student object.
     * @return A Student object with predefined data.
     */
    private static Student getStudent(String email) {
        Address address = new Address("123 Main St", "Anytown", 12335);
        return new Student(
                "Flying Dutchman",
                email,
                address,
                12,
                Arrays.asList("History", "Geography", "Navigation"),
                true,
                3.2,
                null,
                LocalDateTime.parse("2024-07-19T08:45:05.546")
        );
    }
}
