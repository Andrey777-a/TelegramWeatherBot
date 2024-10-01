# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /app
COPY ./src ./src
COPY mvnw pom.xml lombok.config ./
RUN mvn clean package

# Stage 2: Create a minimal runtime image
FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/target/*.jar ./application.jar