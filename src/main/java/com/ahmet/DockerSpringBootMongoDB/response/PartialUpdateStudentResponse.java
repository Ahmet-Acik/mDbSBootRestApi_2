package com.ahmet.DockerSpringBootMongoDB.response;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;

public class PartialUpdateStudentResponse {
    private String message;
    private Student student;

    public PartialUpdateStudentResponse(String message, Student student) {
        this.message = message;
        this.student = student;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}