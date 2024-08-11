FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Add the application's jar to the container
COPY target/DockerSpringBootMongoDB-0.0.1-SNAPSHOT.jar app.jar

# Add health check
HEALTHCHECK --interval=5s --timeout=3s --retries=12 CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]