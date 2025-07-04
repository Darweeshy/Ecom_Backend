# Stage 1: Build the application with Maven using Java 21
FROM maven:3-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy all files into the container
COPY . .

# Package the application, skip tests for faster build
RUN mvn clean package -DskipTests


# Stage 2: Create the final, smaller image using Java 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory for the runtime container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render uses port 8080 by default
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
