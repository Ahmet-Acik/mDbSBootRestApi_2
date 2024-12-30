package mongoStudentsDB;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class MDbSDbSimulation extends Simulation {

    // HTTP Configuration
    private HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080/students")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json");

    // ChainBuilder Definitions
    private static ChainBuilder createStudent = 
        exec(http("Create Student")
            .post("/")
            .body(StringBody("{\"name\":\"John Doe\",\"age\":20,\"email\":\"john.doe@example.com\"}")).asJson()
            .check(status().is(201)));

    private static ChainBuilder getAllStudents = 
        exec(http("Get All Students")
            .get("/all")
            .check(status().is(200)));

    private static ChainBuilder getStudentById = 
        exec(http("Get Student by ID")
            .get("/1")
            .check(status().is(200)));

    private static ChainBuilder getStudentsByName = 
        exec(http("Get Students by Name")
            .get("/?name=John")
            .check(status().is(200)));

    private static ChainBuilder getStudentsByAgeRange = 
        exec(http("Get Students by Age Range")
            .get("/age?minAge=18&maxAge=25")
            .check(status().is(200)));

    private static ChainBuilder partiallyUpdateStudent = 
        exec(http("Partially Update Student")
            .patch("/1")
            .body(StringBody("{\"age\":21}")).asJson()
            .check(status().is(200)));

    private static ChainBuilder deleteStudentById = 
        exec(http("Delete Student by ID")
            .delete("/1")
            .check(status().is(204)));

    private static ChainBuilder updateStudent = 
        exec(http("Update Student")
            .put("/1")
            .body(StringBody("{\"name\":\"John Doe\",\"age\":22,\"email\":\"john.doe@example.com\"}")).asJson()
            .check(status().is(200)));

    // ScenarioBuilder Definition
    private ScenarioBuilder scn = scenario("StudentControllerSimulation")
        .exec(createStudent)
        .pause(2)
        .exec(getAllStudents)
        .pause(2)
        .exec(getStudentById)
        .pause(2)
        .exec(getStudentsByName)
        .pause(2)
        .exec(getStudentsByAgeRange)
        .pause(2)
        .exec(partiallyUpdateStudent)
        .pause(2)
        .exec(deleteStudentById)
        .pause(2)
        .exec(updateStudent);

    // Setup
    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}