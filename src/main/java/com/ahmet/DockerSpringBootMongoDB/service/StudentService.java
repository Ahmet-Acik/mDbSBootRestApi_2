package com.ahmet.DockerSpringBootMongoDB.service;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    String save(Student student);
    List<Student> getStudentStartWith(String name);
    ResponseEntity<List<Student>> findAll();
    Student findById(String id);
    void deleteById(String id);
    List<Student> getByPersonAge(Integer minAge, Integer maxAge);
    Student updateStudent(String id, Student student);
    Optional<Student> updateStudentDetails(String id, Student student);
    Student partiallyUpdateStudent(String id, Student student);
    boolean existsById(String id);
    Optional<Student> findByIdOptional(String id);
    void createStudents(int count);
    void deleteAllStudents();
    void initializeStudents();


}

