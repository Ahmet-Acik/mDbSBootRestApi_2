package com.ahmet.DockerSpringBootMongoDB.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a Student entity in the MongoDB database.
 *
 * This class is annotated with @Document to indicate it's a MongoDB document and
 * includes various fields like id, name, email, etc., with appropriate annotations
 * for indexing and JSON serialization behavior.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {

    /**
     * The unique identifier for the student.
     */
    @Id
    private String id;

    /**
     * The name of the student.
     * This field is indexed and cannot be blank.
     */
    @Indexed()
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * The email of the student.
     * This field is indexed uniquely and must be a valid email format.
     */
    @Indexed(unique = true)
    @Email(message = "Email is not valid")
    private String email; // Assuming email is unique in the system

    /**
     * The address of the student is a Class.
     */
    private Address address;

    /**
     * The age of the student.
     * Must be a positive number.
     */
    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    /**
     * The list of courses the student is enrolled in.
     */
    private List<String> courses;

    /**
     * Indicates if the student is a full-time student.
     */
    private Boolean fullTime;

    /**
     * The Grade Point Average of the student.
     */
    private Double gpa;
    private LocalDateTime graduationDate;
    private LocalDateTime registerDate;

    /**
     * Custom constructor to create a Student instance without an id.
     *
     * @param name Name of the student.
     * @param email Email of the student, must be unique.
     * @param address Address of the student.
     * @param age Age of the student.
     * @param courses List of courses the student is enrolled in.
     * @param fullTime Boolean indicating if the student is a full-time student.
     * @param gpa Grade Point Average of the student.
     * @param graduationDate Expected graduation date of the student.
     * @param registerDate Registration date of the student.
     */
    public Student(String name, String email, Address address, Integer age, List<String> courses, Boolean fullTime, Double gpa, LocalDateTime graduationDate, LocalDateTime registerDate) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.age = age;
        this.courses = courses;
        this.fullTime = fullTime;
        this.gpa = gpa;
        this.graduationDate = graduationDate;
        this.registerDate = registerDate;
    }
}
