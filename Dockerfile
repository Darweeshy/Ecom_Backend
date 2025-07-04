# Stage 1: Build the application with Maven using Java 21
FROM maven:3-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

# Fully skip compiling and running tests
RUN mvn clean package -Dmaven.test.skip=true


# Stage 2: Create the final, smaller image using Java 21
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
