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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    /**
     * Creates a new student in the database.
     * @param student The student to be created.
     * @return A ResponseEntity with a message including the new student's ID.
     */
    @PostMapping
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<String> save(@Valid @RequestBody Student student) {
        String result = studentService.save(student);
        String message = String.format("{\"message\": \"A new student is successfully created with ID: %s\"}", result);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingFieldExceptions(MissingFieldException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    /**
     * Retrieves all students from the database.
     * @return A ResponseEntity containing a list of all students.
     */
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

    /**
     * Finds a student by their ID.
     * @param id The ID of the student to find.
     * @return A ResponseEntity containing the found student or a 404 status if not found.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the student"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<Student> findById(@PathVariable String id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok(student);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * Finds students whose names start with a given prefix.
     * @param name The prefix to match against student names.
     * @return A ResponseEntity containing a list of matching students or a 204 status if none found.
     */
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

    /**
     * Finds students within a specified age range.
     * @param minAge The minimum age of students to find.
     * @param maxAge The maximum age of students to find.
     * @return A ResponseEntity containing a list of students within the age range or a 204 status if none found.
     */
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

    /**
     * Partially updates a student by their ID with the provided student information.
     * @param id The ID of the student to update.
     * @param student The student information to update.
     * @return A ResponseEntity containing the response of the partial update operation.
     */
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

    /**
     * Deletes a student by their ID.
     * @param id The ID of the student to delete.
     * @return A ResponseEntity with a 204 status code if the deletion was successful.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (!studentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing student with the provided student information.
     * @param id The ID of the student to update.
     * @param student The new student information.
     * @return A ResponseEntity containing the response of the update operation.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody Student student) {
        student.setId(id); // Ensure the student's ID is set to the path variable
        studentService.updateStudentDetails(id, student); // Corrected method call
        UpdateStudentResponse response = new UpdateStudentResponse("Student updated successfully with ID: " + id, student);
        return ResponseEntity.ok(response); // You might want to return the updated student or a custom response
    }
}
