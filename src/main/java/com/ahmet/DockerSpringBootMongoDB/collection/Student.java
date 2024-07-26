package com.ahmet.DockerSpringBootMongoDB.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {


    @Id
    private String id;

    @Indexed
    private String name;

    @Indexed(unique = true)
    private String email;

    private Address address;
    private Integer age;
    private List<String> courses;
    private Boolean fullTime;
    private Double gpa;
    private LocalDateTime graduationDate;
    private LocalDateTime registerDate;

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
