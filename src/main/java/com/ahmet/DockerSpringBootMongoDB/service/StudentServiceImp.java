package com.ahmet.DockerSpringBootMongoDB.service;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.exception.MissingFieldException;
import com.ahmet.DockerSpringBootMongoDB.exception.ResourceNotFoundException;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Service implementation for managing students.
 * Provides functionality for CRUD operations on student entities.
 */
@Service
public class StudentServiceImp implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Saves a student entity to the database.
     *
     * @param student The student entity to save.
     * @return The ID of the saved student.
     */
    @Override
    public String save(Student student) {
        // Perform validation
        if (student.getName() == null || student.getName().isEmpty()) {
            throw new MissingFieldException("Name is required");
        }
        if (student.getAge() == null || student.getAge() <= 0) {
            throw new MissingFieldException("Age must be a positive number");
        }
        if (student.getEmail() == null || !student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new MissingFieldException("Email is not valid");
        }
        return studentRepository.save(student).getId();
    }
    /**
     * Retrieves a list of students whose names start with the specified prefix.
     *
     * @param name The prefix to match student names against.
     * @return A list of matching students.
     */
    @Override
    public List<Student> getStudentStartWith(String name) {
        return studentRepository.findByNameStartsWith(name);
    }

    /**
     * Retrieves all students from the database.
     *
     * @return A list of all students.
     */
    @Override
    public ResponseEntity<List<Student>> findAll() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Finds a student by their ID.
     *
     * @param id The ID of the student to find.
     * @return The found student entity.
     * @throws ResourceNotFoundException if no student is found with the given ID.
     */
    @Override
    public Student findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    /**
     * Deletes a student by their ID.
     *
     * @param id The ID of the student to delete.
     */
    @Override
    public void deleteById(String id) {
        studentRepository.deleteById(id);
    }

    /**
     * Retrieves a list of students within a specified age range.
     *
     * @param minAge The minimum age of students to retrieve.
     * @param maxAge The maximum age of students to retrieve.
     * @return A list of students within the specified age range.
     */
    @Override
    public List<Student> getByPersonAge(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    /**
     * Updates a student with new information.
     *
     * @param id      The ID of the student to update.
     * @param student The new information for the student.
     * @return The updated student entity.
     * @throws ResourceNotFoundException if no student is found with the given ID.
     */
    @Override
    public Student updateStudent(String id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        checkForMissingFields(student);
        updateFields(existingStudent, student);
        studentRepository.save(existingStudent);
        return existingStudent;
    }

    /**
     * Updates non-null fields of a student.
     *
     * @param id      The ID of the student to update.
     * @param student The student data to update with.
     * @return An Optional containing the updated student, or empty if not found.
     */

    @Override
    public Optional<Student> updateStudentDetails(String id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            // Copy non-null properties from `student` to `existingStudent`
            String[] nullPropertyNames = getNullPropertyNames(student);
            BeanUtils.copyProperties(student, existingStudent, nullPropertyNames);
            return Optional.of(studentRepository.save(existingStudent));
        }).orElse(Optional.empty());
    }

    /**
     * Utility method to get names of null properties of an object.
     *
     * @param source The source object to check.
     * @return An array of null property names.
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * Partially updates a student with new information.
     *
     * @param id      The ID of the student to update.
     * @param student The new information for the student.
     * @return The partially updated student entity.
     * @throws ResourceNotFoundException if no student is found with the given ID.
     */
    @Override
    public Student partiallyUpdateStudent(String id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        updateFields(existingStudent, student);
        studentRepository.save(existingStudent);
        return existingStudent;
    }

    /**
     * Checks if a student exists by their ID.
     *
     * @param id The ID of the student to check.
     * @return true if the student exists, false otherwise.
     */
    @Override
    public boolean existsById(String id) {
        return studentRepository.existsById(id);
    }

    /**
     * Checks for missing fields in a student entity and throws an exception if any are found.
     *
     * @param student The student entity to check.
     * @throws MissingFieldException if any fields are missing.
     */
    private void checkForMissingFields(Student student) {
        Field[] fields = Student.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(student) == null) {
                    throw new MissingFieldException(field.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
    }

    /**
     * Updates the fields of an existing student with values from a new student, if they are not null.
     *
     * @param existingStudent The existing student to update.
     * @param newStudent      The new student data to update from.
     */
    private void updateFields(Student existingStudent, Student newStudent) {
        Field[] fields = Student.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(newStudent);
                if (newValue != null) {
                    field.set(existingStudent, newValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing field: " + field.getName() + ". " + e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Error setting field: " + field.getName() + " with value from new student. " + e.getMessage(), e);
            }
        }
    }

    /**
     * Finds a student by their ID, returning an Optional.
     *
     * @param id The ID of the student to find.
     * @return An Optional containing the found student, or empty if not found.
     */
    public Optional<Student> findByIdOptional(String id) {
        return studentRepository.findById(id);
    }
}