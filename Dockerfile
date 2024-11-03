# Use an official OpenJDK runtime as a parent image
FROM openjdk:17 AS build
COPY . .
RUN mvn clean package -DskipTests



FROM openjdk:17

# Copy the Maven build output (your jar file) into the container at /app
COPY --from=build /target/stayease-0.0.1-SNAPSHOT.jar stayease.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "stayease.jar"]
