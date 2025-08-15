# Step 1: Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Step 2: Runtime stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
