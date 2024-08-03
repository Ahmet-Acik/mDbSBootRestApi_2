package com.ahmet.DockerSpringBootMongoDB.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
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
 * Represents a student entity in the MongoDB database.
 * This class is annotated with Lombok annotations for boilerplate code reduction
 * and Spring Data MongoDB annotations for mapping to the database.
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
     * Indexed for faster search.
     */
    @Indexed
    private String name;

    /**
     * The email of the student.
     * Indexed and must be unique.
     */
    @Indexed(unique = true)
    private String email;

    /**
     * The address of the student.
     */
    private Address address;

    /**
     * The age of the student.
     */
    private Integer age;

    /**
     * The list of courses the student is enrolled in.
     */
    private List<String> courses;

    /**
     * Indicates if the student is enrolled full-time.
     */
    private Boolean fullTime;

    /**
     * The GPA of the student.
     */
    private Double gpa;

    /**
     * The graduation date of the student.
     */
    private LocalDateTime graduationDate;

    /**
     * The registration date of the student.
     */
    private LocalDateTime registerDate;

    /**
     * Constructs a new Student with the specified details.
     *
     * @param name           the name of the student
     * @param email          the email of the student
     * @param address        the address of the student
     * @param age            the age of the student
     * @param courses        the list of courses the student is enrolled in
     * @param fullTime       indicates if the student is enrolled full-time
     * @param gpa            the GPA of the student
     * @param graduationDate the graduation date of the student
     * @param registerDate   the registration date of the student
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

    public Student(String name, String email, Integer age, Boolean fullTime, Double gpa) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.fullTime = fullTime;
        this.gpa = gpa;
    }
}