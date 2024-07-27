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

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {Application.class, TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ControllerStudent_IT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student sampleStudent;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
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

    @Test
    public void testCreateStudent() throws Exception {
        String expectedResponse = "Expected response string";
        doReturn(expectedResponse).when(studentService).save(any(Student.class));

        String studentJson = objectMapper.writeValueAsString(sampleStudent);

        ResultActions response = mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson));

        response.andExpect(status().isCreated())
                .andExpect(content().json("{\"message\": \"A new student is successfully created with ID: Expected response string\"}"));
    }

    @Test
    public void testSaveStudent() throws Exception {
        String expectedSaveResponse = "Student saved successfully!";
        doReturn(expectedSaveResponse).when(studentService).save(any(Student.class));
        String studentJson = objectMapper.writeValueAsString(sampleStudent);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\": \"A new student is successfully created with ID: Student saved successfully!\"}"));
    }

    @Test
    public void testAnotherEndpoint() throws Exception {
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));
        ResultActions response = mockMvc.perform(get("/students/1")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sampleStudent)));
    }

    @Test
    public void testFindAll() throws Exception {
        List<Student> students = Collections.singletonList(sampleStudent);
        ResponseEntity<List<Student>> responseEntity = new ResponseEntity<>(students, HttpStatus.OK);
        given(studentService.findAll()).willReturn(responseEntity);
        mockMvc.perform(get("/students/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    public void testFindById() throws Exception {
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sampleStudent)));
    }

    @Test
    public void testGetStudentStartWith() throws Exception {
        List<Student> students = Collections.singletonList(sampleStudent);
        given(studentService.getStudentStartWith("John")).willReturn(students);
        mockMvc.perform(get("/students?name=John"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    public void testGetByPersonAge() throws Exception {
        List<Student> students = Collections.singletonList(sampleStudent);
        given(studentService.getByPersonAge(18, 22)).willReturn(students);
        mockMvc.perform(get("/students/age?minAge=18&maxAge=22"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(students)));
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student();
        student.setName("Updated Student");
        student.setEmail("updated@student.com");
        student.setAge(21);

        when(studentService.findByIdOptional(anyString())).thenReturn(Optional.of(student));
        when(studentService.updateStudent(anyString(), any(Student.class))).thenReturn(student);

        ResponseEntity<?> response = studentController.updateStudent("1", student);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void updateStudent_whenStudentExists_updatesStudentSuccessfully() throws Exception {
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));
        given(studentService.updateStudent(eq("1"), any(Student.class))).willReturn(sampleStudent);

        UpdateStudentResponse expectedResponse = new UpdateStudentResponse("Student updated successfully with ID: 1", sampleStudent);
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);

        String actualResponse = mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStudent)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println("Actual response: " + actualResponse);

        assertTrue(actualResponse.contains("\"message\":\"Student updated successfully with ID: 1\""));
    }

    @Test
    public void testPartiallyUpdateStudent() throws Exception {
        given(studentService.findByIdOptional("1")).willReturn(Optional.of(sampleStudent));
        given(studentService.partiallyUpdateStudent(eq("1"), any(Student.class))).willReturn(sampleStudent);

        mockMvc.perform(patch("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStudent)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new PartialUpdateStudentResponse("Student partially updated successfully with ID: 1", sampleStudent))));
    }

    @Test
    public void testDeleteById() throws Exception {
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateStudent_withInvalidData() {
        Student invalidStudent = new Student();
        invalidStudent.setName(""); // Invalid name
        invalidStudent.setEmail("invalid-email"); // Invalid email
        invalidStudent.setAge(-1); // Invalid age

        ResponseEntity<?> response = studentController.updateStudent("1", invalidStudent);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Bad Request"));
    }

    @Test
    public void testUpdateStudent_nonExistentStudent() {
        Student student = new Student();
        student.setName("Updated Student");
        student.setEmail("updated@student.com");
        student.setAge(21);

        when(studentService.findByIdOptional(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = studentController.updateStudent("1", student);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Student not found"));
    }
}