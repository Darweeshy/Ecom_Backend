# Stage 1: Build the application with Maven using Java 21
FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final, smaller image using Java 21
FROM eclipse-temurin:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render automatically uses port 8080, but it's good practice to declare it
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]