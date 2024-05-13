# Step 1: Build the Spring Boot application with Maven and Corretto
FROM maven:3.8.6-amazoncorretto-17 AS build

# Step 2: Set the working directory and copy the source files
WORKDIR /app
COPY . /app

# Step 3: Build the application
RUN mvn clean package -DskipTests

# Step 4: Use a lightweight Amazon Corretto image for the runtime environment
FROM amazoncorretto:17-alpine-jdk

# Step 5: Set the working directory for the runtime container
WORKDIR /app

# Step 6: Copy the JAR file from the build stage to the runtime stage
COPY --from=build /app/target/livelocation-0.0.1-SNAPSHOT.jar app.jar

# Step 7: Expose the Spring Boot port (default 8080)
EXPOSE 8080

# Step 8: Define the entry point to start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]