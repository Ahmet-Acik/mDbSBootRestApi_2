package com.ahmet.DockerSpringBootMongoDB.integration;

import com.ahmet.DockerSpringBootMongoDB.Application;
import com.ahmet.DockerSpringBootMongoDB.collection.Address;
import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.controller.StudentController;
import com.ahmet.DockerSpringBootMongoDB.dto.PartialUpdateStudentResponse;
import com.ahmet.DockerSpringBootMongoDB.dto.UpdateStudentResponse;
import com.ahmet.DockerSpringBootMongoDB.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.eq;

/**
 * Integration tests for the StudentController.
 */
@ExtendWith(MockitoExtension.class) // Use Mockito extension for JUnit 5
@AutoConfigureMockMvc // Auto-configure MockMvc
@SpringBootTest(classes = {Application.class, TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Spring Boot test configuration
@ActiveProfiles("test") // Use the "test" profile
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Reset context after each test
public class ControllerStudent_IT {

    @Autowired
    private MockMvc mockMvc; // MockMvc for performing HTTP requests in tests

    @MockBean
    private StudentService studentService; // Mock the StudentService

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON conversion

    private Student sampleStudent; // Sample student for testing

    @InjectMocks
    private StudentController studentController; // Inject mocks into StudentController

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        // Arrange: Create a sample address and student for testing
        Address sampleAddress = Address.builder()
                .street("123 Main St")
                .city("Anytown")
                .postcode(123)
                .build();

        sampleStudent = Student.builder()
                .address(sampleAddress)
                .age(20)
                .courses(List.of("Math", "Science"))
                .email("john.doe@example.com")
                .name("John Doe")
                .fullTime(true)
                .gpa(3.5)
                .graduationDate(LocalDateTime.now())
                .id("1")
                .registerDate(LocalDateTime.now())
                .build();
    }

    /**
     * Tests the creation of a student.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testCreateStudent() throws Exception {
        // Arrange: Mock the service to return an expected response
        String expectedResponse = "Expected response string";
        doReturn(expectedResponse).when(studentService).save(any(Student.class));

        // Act: Convert the sample student to JSON and perform a POST request
        String studentJson = objectMapper.writeValueAsString(sampleStudent);
        ResultActions response = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson));

        // Assert: Verify the response status and content
        response.andExpect(status().isCreated())
                .andExpect(content().json("{\"message\": \"A new student is successfully created with ID: Expected response string\"}"));
    }

    /**
     * Tests saving a student.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testSaveStudent() throws Exception {
        // Arrange: Mock the service to return an expected save response
        String expectedSaveResponse = "Student saved successfully!";
        doReturn(expectedSaveResponse).when(studentService).save(any(Student.class));

        // Act: Convert the sample student to JSON and perform a POST request
        String studentJson = objectMapper.writeValueAsString(sampleStudent);
        ResultActions response = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson));

        // Assert: Verify the response status and content
        response.andExpect(status().isCreated())
                .andExpect(content().json("{\"message\": \"A new student is successfully created with ID: Student saved successfully!\"}"));
    }

    /**
     * Tests retrieving a student by ID.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testAnotherEndpoint() throws Exception {
        // Arrange: Mock the service to return the sample student
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));

        // Act: Perform a GET request to retrieve the student by ID
        ResultActions response = mockMvc.perform(get("/students/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert: Verify the response status and content
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sampleStudent)));
    }

    /**
     * Tests retrieving all students.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testFindAll() throws Exception {
        // Arrange: Mock the service to return a list of students
        List<Student> students = Collections.singletonList(sampleStudent);
        ResponseEntity<List<Student>> responseEntity = new ResponseEntity<>(students, HttpStatus.OK);
        given(studentService.findAll()).willReturn(responseEntity);

        // Act: Perform a GET request to retrieve all students
        ResultActions response = mockMvc.perform(get("/students/all"));

        // Assert: Verify the response status and content
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    /**
     * Tests retrieving a student by ID.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testFindById() throws Exception {
        // Arrange: Mock the service to return the sample student
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));

        // Act: Perform a GET request to retrieve the student by ID
        ResultActions response = mockMvc.perform(get("/students/1"));

        // Assert: Verify the response status and content
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sampleStudent)));
    }

    /**
     * Tests retrieving students by name prefix.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetStudentStartWith() throws Exception {
        // Arrange: Mock the service to return a list of students starting with "John"
        List<Student> students = Collections.singletonList(sampleStudent);
        given(studentService.getStudentStartWith("John")).willReturn(students);

        // Act: Perform a GET request to retrieve students by name prefix
        ResultActions response = mockMvc.perform(get("/students?name=John"));

        // Assert: Verify the response status and content
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    /**
     * Tests retrieving students by age range.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testGetByPersonAge() throws Exception {
        // Arrange: Mock the service to return a list of students within the age range
        List<Student> students = Collections.singletonList(sampleStudent);
        given(studentService.getByPersonAge(18, 22)).willReturn(students);

        // Act: Perform a GET request to retrieve students by age range
        ResultActions response = mockMvc.perform(get("/students/age?minAge=18&maxAge=22"));

        // Assert: Verify the response status and content
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    /**
     * Tests updating a student.
     */
    @Test
    public void testUpdateStudent() {
        // Arrange: Create a new student object with updated details
        Student student = new Student();
        student.setName("Updated Student");
        student.setEmail("updated@student.com");
        student.setAge(21);

        // Mock the service to return the updated student
        when(studentService.findByIdOptional(anyString())).thenReturn(Optional.of(student));
        when(studentService.updateStudent(anyString(), any(Student.class))).thenReturn(student);

        // Act: Call the updateStudent method of the controller
        ResponseEntity<?> response = studentController.updateStudent("1", student);

        // Assert: Verify the response status
        assertEquals(OK, response.getStatusCode());
    }

    /**
     * Tests updating a student when the student exists.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void updateStudent_whenStudentExists_updatesStudentSuccessfully() throws Exception {
        // Arrange: Mock the service to return the sample student and the updated student
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));
        given(studentService.updateStudent(eq("1"), any(Student.class))).willReturn(sampleStudent);

        // Create an expected response object
        UpdateStudentResponse expectedResponse = new UpdateStudentResponse("Student updated successfully with ID: 1", sampleStudent);
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);

        // Act: Perform a PUT request to update the student
        String actualResponse = mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStudent)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Assert: Verify the response content
        assertTrue(actualResponse.contains("\"message\":\"Student updated successfully with ID: 1\""));
    }

    /**
     * Tests partially updating a student.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testPartiallyUpdateStudent() throws Exception {
        // Arrange: Mock the service to return the sample student and the partially updated student
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));
        given(studentService.partiallyUpdateStudent(eq("1"), any(Student.class))).willReturn(sampleStudent);

        // Act: Perform a PATCH request to partially update the student
        ResultActions response = mockMvc.perform(patch("/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleStudent)));

        // Assert: Verify the response status and content
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PartialUpdateStudentResponse("Student partially updated successfully with ID: 1", sampleStudent))));
    }

    /**
     * Tests deleting a student by ID.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testDeleteById() throws Exception {
        // Act: Perform a DELETE request to delete the student by ID
        ResultActions response = mockMvc.perform(delete("/students/1"));

        // Assert: Verify the response status
        response.andExpect(status().isNotFound());
    }

    /**
     * Tests updating a student with invalid data.
     */
    @Test
    public void testUpdateStudent_withInvalidData() {
        // Arrange: Create an invalid student object
        Student invalidStudent = new Student();
        invalidStudent.setName(""); // Invalid name
        invalidStudent.setEmail("invalid-email"); // Invalid email
        invalidStudent.setAge(-1); // Invalid age

        // Act: Call the updateStudent method of the controller with invalid data
        ResponseEntity<?> response = studentController.updateStudent("1", invalidStudent);

        // Assert: Verify the response status and content
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Bad Request"));
    }


    /**
     * Tests updating a non-existent student.
     */
    @Test
    public void testUpdateStudent_nonExistentStudent() {
        // Arrange: Create a new student object with updated details
        Student student = new Student();
        student.setName("Updated Student");
        student.setEmail("updated@student.com");
        student.setAge(21);

        // Mock the service to return an empty optional
        when(studentService.findByIdOptional(anyString())).thenReturn(Optional.empty());

        // Act: Call the updateStudent method of the controller
        ResponseEntity<?> response = studentController.updateStudent("1", student);

        // Assert: Verify the response status and content
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Student not found"));
    }
}