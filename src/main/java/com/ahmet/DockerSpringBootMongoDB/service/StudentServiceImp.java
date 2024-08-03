package com.ahmet.DockerSpringBootMongoDB.service;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.exception.MissingFieldException;
import com.ahmet.DockerSpringBootMongoDB.exception.ResourceNotFoundException;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.github.javafaker.Faker;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class StudentServiceImp implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImp.class);

    @PostConstruct
    public void initializeStudents() {
        long count = studentRepository.count();
        System.out.println("Number of students in the database: " + count);

        if (count < 5500) {
            createStudents((int)(5500 -count));
        }

        count = studentRepository.count();
        System.out.println("Number of students after initialization: " + count);
    }

    @Override
    public void createStudents(int count) {
        List<Student> students = generateStudents(count);
        studentRepository.saveAll(students);
    }

    @Override
    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }

    private List<Student> generateStudents(int count) {
        Faker faker = new Faker();
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String name = firstName + " " + lastName + String.format("%04d", i);
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@example.com";
            int age = 20 + (i % 10);
            boolean isActive = i % 2 == 0;
            double gpa = 2.0 + (i % 3);

            Student student = new Student(name, email, age, isActive, gpa);
            students.add(student);
        }
        return students;
    }

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

    @Override
    public List<Student> getStudentStartWith(String name) {
        return studentRepository.findByNameStartsWith(name);
    }

    @Override
    public ResponseEntity<List<Student>> findAll() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @Override
    public Student findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    @Override
    public void deleteById(String id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getByPersonAge(Integer minAge, Integer maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    public Student updateStudent(String id, Student student) {
        logger.info("Updating student with ID: {}", id);
        try {
            Student existingStudent = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
            checkForMissingFields(student);
            updateFields(existingStudent, student);
            studentRepository.save(existingStudent);
            logger.info("Student updated successfully with ID: {}", id);
            return existingStudent;
        } catch (ResourceNotFoundException e) {
            logger.error("Student not found with ID: {}", id, e);
            throw e;
        } catch (MissingFieldException e) {
            logger.error("Missing field in student data: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating student with ID: {}", id, e);
            throw new RuntimeException("Failed to update student", e);
        }
    }


    @Override
    public Optional<Student> updateStudentDetails(String id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            // Copy non-null properties from `student` to `existingStudent`
            String[] nullPropertyNames = getNullPropertyNames(student);
            BeanUtils.copyProperties(student, existingStudent, nullPropertyNames);
            return Optional.of(studentRepository.save(existingStudent));
        }).orElse(Optional.empty());
    }

    @Override
    public Student partiallyUpdateStudent(String id, Student student) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        updateFields(existingStudent, student);
        studentRepository.save(existingStudent);
        return existingStudent;
    }

    @Override
    public boolean existsById(String id) {
        return studentRepository.existsById(id);
    }

    @Override
    public Optional<Student> findByIdOptional(String id) {
        return studentRepository.findById(id);
    }

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

    private void updateFields(Student existingStudent, Student newStudent) {
        Field[] fields = Student.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(newStudent);
                if (newValue != null) {
                    logger.info("Updating field: {} with value: {}", field.getName(), newValue);
                    field.set(existingStudent, newValue);
                }
            } catch (IllegalAccessException e) {
                logger.error("Error accessing field: {}. {}", field.getName(), e.getMessage(), e);
                throw new RuntimeException("Error accessing field: " + field.getName() + ". " + e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                logger.error("Error setting field: {} with value from new student. {}", field.getName(), e.getMessage(), e);
                throw new RuntimeException("Error setting field: " + field.getName() + " with value from new student. " + e.getMessage(), e);
            }
        }
    }

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

}