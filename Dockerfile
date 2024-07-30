# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Add the application's jar to the container
COPY target/DockerSpringBootMongoDB-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

ENTRYPOINT ["java", "-jar", "app.jar"]