# Use an official OpenJDK runtime as a parent image
FROM maven:openjdk:17-jdk-alpine AS build
COPY . .
RUN mvn clean package



FROM openjdk:17.0.1-jdk-slim

# Copy the Maven build output (your jar file) into the container at /app
COPY --from=build /target/stayease-0.0.1-SNAPSHOT.jar stayease.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "stayease.jar"]
