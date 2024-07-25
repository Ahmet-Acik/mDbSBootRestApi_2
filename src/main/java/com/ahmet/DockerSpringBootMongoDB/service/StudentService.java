package com.ahmet.DockerSpringBootMongoDB.service;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    String save(Student student);

    List<Student> getStudentStartWith(String name);

    ResponseEntity<List<Student>> findAll();

    public Student findById(String id);

    void deleteById(String id);

    List<Student> getByPersonAge(Integer minAge, Integer maxAge);

    public Student updateStudent(String id, Student student);

    public Student partiallyUpdateStudent(String id, Student student);

    boolean existsById(String id);

    public Optional<Student> findByIdOptional(String id);

    public Optional<Student> updateStudentDetails(String id, Student student);

}

