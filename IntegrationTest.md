### Step-by-Step Guide for Integration Testing

#### 1. **Create a Separate Directory for Integration Tests**

Create a directory `src/test/java/com/ahmet/DockerSpringBootMongoDB/integration` for integration tests.

#### 2. **Test Configuration**

Create a test configuration class to define any additional beans needed for tests.

```java
//// src/test/java/com/ahmet/DockerSpringBootMongoDB/integration/TestConfig.java
//package com.ahmet.DockerSpringBootMongoDB.integration;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ComponentScan(basePackages = "com.ahmet.DockerSpringBootMongoDB")
//@EnableAutoConfiguration
//public class TestConfig {
//    @Bean
//    public TestRestTemplate restTemplate() {
//        return new TestRestTemplate();
//    }

```

#### 3. **Integration Test for Student Service**

Create an integration test class for the student service.

```java
//// src/test/java/com/ahmet/DockerSpringBootMongoDB/integration/StudentServiceIntegrationTest.java
//package com.ahmet.DockerSpringBootMongoDB.integration;
//
//import com.ahmet.DockerSpringBootMongoDB.Application;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest(classes = {Application.class, TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//public class StudentServiceIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void testFindAllStudents() {
//        ResponseEntity<String> response = restTemplate.getForEntity("/students/all", String.class);
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//        assertThat(response.getBody()).contains("John Doe", "Jane Smith");
//    }
//
//    @Test
//    public void testCreateStudent() {
//        String studentJson = "{\"name\":\"New Student\",\"age\":23,\"email\":\"new.student@example.com\"}";
//        ResponseEntity<String> response = restTemplate.postForEntity("/students", studentJson, String.class);
//        assertThat(response.getStatusCodeValue()).isEqualTo(201);
//        assertThat(response.getBody()).contains("A new student is successfully created with ID:");
//    }
//}
```

#### 4. **Running Integration Tests**

- **Using Maven**:
  ```sh
  mvn verify
  ```

- **Using IntelliJ IDEA**:
  Right-click on the test class or method and select `Run`.

- **Continuous Integration (CI)**:
  Configure your CI pipeline (e.g., GitHub Actions, Jenkins) to run integration tests as part of the build process.

### Summary of Tests to Include

1. **CRUD Operations**:
    - Create, Read, Update, Delete operations for student records.

2. **Error Handling**:
    - Test scenarios where invalid data is provided.
    - Test scenarios where non-existent resources are accessed.

3. **Service Integration**:
    - Test interactions with external services (mocked).

4. **Edge Cases**:
    - Test edge cases like empty databases, large datasets, etc.

### Plan

1. **Add `maven-failsafe-plugin` for integration tests**:
    - Configure the plugin to run tests in the `integration` directory.
    - Define the naming convention for integration tests (e.g., `*IT.java`).

2. **Configure `maven-surefire-plugin` for unit and Cucumber tests**:
    - Define the naming convention for Cucumber tests (e.g., `*CucumberTest.java`).

3. **Provide Maven commands to run specific tests**:
    - Integration tests only.
    - Cucumber tests only.
    - Both integration and Cucumber tests.

### Updated `pom.xml`

[//]: # (   <dependencies>)

[//]: # (        <!-- Dependencies here -->)

[//]: # (    </dependencies>)

[//]: # (    <build>)

[//]: # (        <pluginManagement>)

[//]: # (            <plugins>)

[//]: # (                <plugin>)

[//]: # (                    <groupId>org.apache.maven.plugins</groupId>)

[//]: # (                    <artifactId>maven-surefire-plugin</artifactId>)

[//]: # (                    <version>${maven.surefire.version}</version>)

[//]: # (                    <configuration>)

[//]: # (                        <includes>)

[//]: # (                            <include>**/*IT.java</include>)

[//]: # (                            <include>**/CucumberTestRunner.java</include>)

[//]: # (                        </includes>)

[//]: # (                    </configuration>)

[//]: # (                </plugin>)

[//]: # (                <plugin>)

[//]: # (                    <groupId>org.apache.maven.plugins</groupId>)

[//]: # (                    <artifactId>maven-failsafe-plugin</artifactId>)

[//]: # (                    <version>${maven.surefire.version}</version>)

[//]: # (                    <executions>)

[//]: # (                        <execution>)

[//]: # (                            <goals>)

[//]: # (                                <goal>integration-test</goal>)

[//]: # (                                <goal>verify</goal>)

[//]: # (                            </goals>)

[//]: # (                            <configuration>)

[//]: # (                                <includes>)

[//]: # (                                    <include>**/integration/*IT.java</include>)

[//]: # (                                </includes>)

[//]: # (                            </configuration>)

[//]: # (                        </execution>)

[//]: # (                    </executions>)

[//]: # (                </plugin>)

[//]: # (            </plugins>)

[//]: # (        </pluginManagement>)

[//]: # (        <plugins>)

[//]: # (            <plugin>)

[//]: # (                <groupId>org.springframework.boot</groupId>)

[//]: # (                <artifactId>spring-boot-maven-plugin</artifactId>)

[//]: # (                <configuration>)

[//]: # (                    <excludes>)

[//]: # (                        <exclude>)

[//]: # (                            <groupId>org.projectlombok</groupId>)

[//]: # (                            <artifactId>lombok</artifactId>)

[//]: # (                        </exclude>)

[//]: # (                    </excludes>)

[//]: # (                </configuration>)

[//]: # (            </plugin>)

[//]: # (        </plugins>)

[//]: # (    </build>)

[//]: # (</project>)

### Maven Commands

1. **Run Integration Tests Only**:
   ```sh
   mvn failsafe:integration-test
   ```

2. **Run Cucumber Tests Only**:
   ```sh
   mvn test -Dtest=*CucumberTest
   ```

3. **Run Both Integration and Cucumber Tests**:
   ```sh
   mvn verify
   ```