package com.ahmet.DockerSpringBootMongoDB.controller;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.dto.PartialUpdateStudentResponse;
import com.ahmet.DockerSpringBootMongoDB.dto.UpdateStudentResponse;
import com.ahmet.DockerSpringBootMongoDB.exception.MissingFieldException;
import com.ahmet.DockerSpringBootMongoDB.exception.ResourceNotFoundException;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.ahmet.DockerSpringBootMongoDB.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for handling student-related operations.
 * This controller provides endpoints for CRUD operations on students.
 */


@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<Map<String, String>> save(@Valid @RequestBody Student student) {
        String result = studentService.save(student);
        Map<String, String> response = new HashMap<>();
        response.put("message", "A new student is successfully created with ID: " + result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errors);
    }

    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMissingFieldExceptions(MissingFieldException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();
        if (!message.startsWith("Missing required field: ")) {
            message = "Missing required field: " + message;
        }
        error.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\":\"" + ex.getMessage() + "\"}");
    }

    @GetMapping("/all")
    @Operation(summary = "Find all students in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found students"),
            @ApiResponse(responseCode = "204", description = "No students found")
    })
    public ResponseEntity<List<Student>> findAll() {
        ResponseEntity<List<Student>> response = studentService.findAll();
        if (response.getBody() == null || response.getBody().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return response;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the student"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })

    public ResponseEntity<Student> findById(@PathVariable String id) {
        Optional<Student> student = studentService.findByIdOptional(id);
        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(null));
    }


    @GetMapping
    @Operation(summary = "Find students starting with a given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found students"),
            @ApiResponse(responseCode = "204", description = "No students found")
    })
    public ResponseEntity<List<Student>> getStudentStartWith(@RequestParam("name") String name) {
        List<Student> students = studentService.getStudentStartWith(name);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(students);
        }
    }

    @GetMapping("/age")
    @Operation(summary = "Find students by age range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found students"),
            @ApiResponse(responseCode = "204", description = "No students found")
    })
    public ResponseEntity<List<Student>> getByPersonAge(@RequestParam("minAge") int minAge, @RequestParam("maxAge") int maxAge) {
        List<Student> students = studentService.getByPersonAge(minAge, maxAge);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(students);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student partially updated successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<PartialUpdateStudentResponse> partiallyUpdateStudent(@PathVariable String id, @RequestBody Student student) {
        Optional<Student> existingStudent = studentService.findByIdOptional(id);
        if (existingStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PartialUpdateStudentResponse("Student not found", null));
        }

        Student updatedStudent = studentService.partiallyUpdateStudent(id, student);
        PartialUpdateStudentResponse response = new PartialUpdateStudentResponse("Student partially updated successfully with ID: " + id, updatedStudent);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        if (!studentService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Student not found\"}");
        }
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @RequestBody Student student) {
        logger.info("Updating student with ID: {}", id);

        // Validate the student object
        if (student.getName() == null || student.getName().isEmpty() || student.getAge() == null || student.getAge() < 0 || student.getEmail() == null || !student.getEmail().contains("@")) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Bad Request\"}");
        }

        // Check if the student exists
        Optional<Student> existingStudent = studentService.findByIdOptional(id);
        if (existingStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("{\"message\":\"Student not found\"}");
        }

        // Update the student details
        student.setId(id); // Ensure the student's ID is set to the path variable
        studentService.updateStudentDetails(id, student); // Corrected method call
        UpdateStudentResponse response = new UpdateStudentResponse("Student updated successfully with ID: " + id, student);
        logger.info("Student updated successfully with ID: {}", id);
        return ResponseEntity.ok(response); // You might want to return the updated student or a custom response
    }
}

