# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build output (your jar file) into the container at /app
COPY target/stayease-0.0.1-SNAPSHOT.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
