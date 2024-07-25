package com.ahmet.DockerSpringBootMongoDB.cucumberglue;

import com.ahmet.DockerSpringBootMongoDB.collection.Student;
import com.ahmet.DockerSpringBootMongoDB.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class StudentFindByAgeSteps {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    private final String baseUrl = "http://localhost:8080"; // Adjust the port if necessary

    @Given("students with the following data exist:")
    public void studentsWithTheFollowingDataExist(List<Map<String, String>> studentsData) {
        studentRepository.deleteAll();
        studentsData.forEach(data -> {
            Student student = new Student();
            student.setId(data.get("id"));
            student.setName(data.get("name"));
            student.setAge(Integer.parseInt(data.get("age")));
            student.setEmail(data.get("email"));
            studentRepository.save(student);
        });
    }

    @Given("no students exist within age range {int} to {int}")
    public void noStudentsExistWithinAgeRange(int minAge, int maxAge) {
        studentRepository.deleteAll();
    }

    /*    @When("I send a GET request to find by age range \"/students/age?minAge={int}&maxAge={int}\"")
    public void iSendAGetRequestToFindByAgeRange(int minAge, int maxAge) {
       String url = String.format("%s/students/age?minAge=%d&maxAge=%d", baseUrl, minAge, maxAge);
       ResponseEntity<String> response = testContext.getRestTemplate().getForEntity(url, String.class);
       testContext.setResponse(response);

    /*

    io.cucumber.junit.UndefinedStepException: The step 'I send a GET request to find by age range "/students/age?minAge=18&maxAge=22"' is undefined.
    You can implement this step using the snippet(s) below:

  @When("I send a GET request to find by age range {string}")
    public void i_send_a_get_request_to_find_by_age_range(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
}
     */

    /*
    Problem 1: Incorrect Step Definition for GET Request  The step definition for sending a GET request to find students by age range is missing the @When annotation and the method signature does not match the step text exactly.
    Fix: Ensure the @When annotation is correctly applied and the method signature matches the step text.
    Problem 1: Incorrect Step Definition for GET Request  The step definition for sending a GET request to find students by age range is missing the @When annotation and the method signature does not match the step text exactly.
    Fix: Ensure the @When annotation is correctly applied and the method signature matches the step text.
    Problem 3: Missing Step Definition for Students with Data Exist  The step definition for the scenario where students with specific data exist is missing the @Given annotation and the method signature does not match the step text exactly.
    Fix: Ensure the @Given annotation is correctly applied and the method signature matches the step text.
     did not work
     */
    //Fix the step definition to match the step text exactly.
    // @When("I send a GET request to find by age range \"/students/age?minAge={int}&maxAge={int}\"")
    //Adjust the step definition to accept a single String parameter instead of two int parameters.
    //Parse the minAge and maxAge from the String parameter within the method.

    /*After -> "/students/age?minAge=<minAge>&maxAge=<maxAge>"
        String[] ages = ageRange.split("&");
        int minAge = Integer.parseInt(ages[0].split("=")[1]);
        int maxAge = Integer.parseInt(ages[1].split("=")[1]);
    The selected code is responsible for parsing a query string to extract the minimum and maximum age values and then constructing a URL for a GET request to find students within that age range.

    First, the code splits the `ageRange` string by the "&" character to separate the `minAge` and `maxAge` parameters:

    String[] ages = ageRange.split("&");

    Next, it extracts the integer values for `minAge` and `maxAge` by further splitting each part of the `ages` array by the "=" character and parsing the resulting string to an integer:


    int minAge = Integer.parseInt(ages[0].split("=")[1]);
    int maxAge = Integer.parseInt(ages[1].split("=")[1]);

    Finally, the code constructs the URL for the GET request using the extracted `minAge` and `maxAge` values and sends the request using the `RestTemplate`:


    String url = String.format("%s/students/age?minAge=%d&maxAge=%d", baseUrl, minAge, maxAge);
    ResponseEntity<String> response = testContext.getRestTemplate().getForEntity(url, String.class);
    testContext.setResponse(response);

    This approach ensures that the correct URL is formed based on the provided age range, and the response from the GET request is stored in the `testContext` for further validation.
         */
    @When("I send a GET request to find by age range {string}")
    public void iSendAGetRequestToFindByAgeRange(String ageRange) {
        String[] ages = ageRange.split("&");
        int minAge = Integer.parseInt(ages[0].split("=")[1]);
        int maxAge = Integer.parseInt(ages[1].split("=")[1]);
        String url = String.format("%s/students/age?minAge=%d&maxAge=%d", baseUrl, minAge, maxAge);
        ResponseEntity<String> response = testContext.getRestTemplate().getForEntity(url, String.class);
        testContext.setResponse(response);
    }

}

