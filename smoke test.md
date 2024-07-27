### Step-by-Step Guide for Smoke Testing

#### 1. **Define Smoke Tests**
- Identify critical functionalities to be tested.
- Create a separate directory for smoke tests.

#### 2. **Create a Separate Directory for Smoke Tests**
- Create a directory `src/test/java/com/ahmet/DockerSpringBootMongoDB/smoke` for smoke tests.

#### 3. **Implement Smoke Tests**
- Create smoke test classes for critical functionalities.

Example:
```java
//package com.ahmet.DockerSpringBootMongoDB.smoke;
//
//import com.ahmet.DockerSpringBootMongoDB.Application;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//public class StudentServiceSmokeTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void testApplicationUp() {
//        ResponseEntity<String> healthResponse = restTemplate.getForEntity("/actuator/health", String.class);
//        assertEquals(HttpStatus.OK, healthResponse.getStatusCode());
//    }
//
//    @Test
//    public void testFindAllStudents() {
//        ResponseEntity<String> response = restTemplate.getForEntity("/students/all", String.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//}
```

#### 4. **Configure Maven for Smoke Tests**
- Update the `pom.xml` to include the configuration for smoke tests.


[//]: # (<build>)

[//]: # (    <pluginManagement>)

[//]: # (        <plugins>)

[//]: # (            <plugin>)

[//]: # (                <groupId>org.apache.maven.plugins</groupId>)

[//]: # (                <artifactId>maven-surefire-plugin</artifactId>)

[//]: # (                <version>${maven.surefire.version}</version>)

[//]: # (                <configuration>)

[//]: # (                    <includes>)

[//]: # (                        <include>**/*IT.java</include>)

[//]: # (                        <include>**/CucumberTestRunner.java</include>)

[//]: # (                    </includes>)

[//]: # (                </configuration>)

[//]: # (            </plugin>)

[//]: # (            <plugin>)

[//]: # (                <groupId>org.apache.maven.plugins</groupId>)

[//]: # (                <artifactId>maven-failsafe-plugin</artifactId>)

[//]: # (                <version>${maven.surefire.version}</version>)

[//]: # (                <executions>)

[//]: # (                    <execution>)

[//]: # (                        <goals>)

[//]: # (                            <goal>integration-test</goal>)

[//]: # (                            <goal>verify</goal>)

[//]: # (                        </goals>)

[//]: # (                        <configuration>)

[//]: # (                            <includes>)

[//]: # (                                <include>**/integration/*IT.java</include>)

[//]: # (                            </includes>)

[//]: # (                        </configuration>)

[//]: # (                    </execution>)

[//]: # (                </executions>)

[//]: # (            </plugin>)

[//]: # (        </plugins>)

[//]: # (    </pluginManagement>)

[//]: # (    <plugins>)

[//]: # (        <plugin>)

[//]: # (            <groupId>org.springframework.boot</groupId>)

[//]: # (            <artifactId>spring-boot-maven-plugin</artifactId>)

[//]: # (            <configuration>)

[//]: # (                <excludes>)

[//]: # (                    <exclude>)

[//]: # (                        <groupId>org.projectlombok</groupId>)

[//]: # (                        <artifactId>lombok</artifactId>)

[//]: # (                    </exclude>)

[//]: # (                </excludes>)

[//]: # (            </configuration>)

[//]: # (        </plugin>)

[//]: # (    </plugins>)

[//]: # (</build>)

[//]: # ()
[//]: # (<profiles>)

[//]: # (    <profile>)

[//]: # (        <id>smoke-tests</id>)

[//]: # (        <build>)

[//]: # (            <plugins>)

[//]: # (                <plugin>)

[//]: # (                    <groupId>org.apache.maven.plugins</groupId>)

[//]: # (                    <artifactId>maven-surefire-plugin</artifactId>)

[//]: # (                    <version>${maven.surefire.version}</version>)

[//]: # (                    <configuration>)

[//]: # (                        <includes>)

[//]: # (                            <include>**/smoke/*SmokeTest.java</include>)

[//]: # (                        </includes>)

[//]: # (                    </configuration>)

[//]: # (                </plugin>)

[//]: # (            </plugins>)

[//]: # (        </build>)

[//]: # (    </profile>)

[//]: # (</profiles>)


### Command to Run Smoke Tests

To run the smoke tests, use the following Maven command:

```sh
mvn test -Psmoke-tests
```

### Difference Between `-P` and `-D` in Maven

- `-P`: This option is used to activate a Maven profile. Profiles are defined in the `pom.xml` and can be used to customize the build for different environments or purposes (e.g., smoke tests, integration tests, production builds).

- `-D`: This option is used to set a system property. System properties can be used to pass configuration values to the build process or to the application being built.

### Example Usage

- `-Psmoke-tests`: Activates the `smoke-tests` profile defined in the `pom.xml`.
- `-Dproperty=value`: Sets a system property named `property` with the value `value`.

### Example Commands

```sh
# Activating a profile
mvn test -Psmoke-tests

# Setting a system property
mvn test -Dproperty=value
```