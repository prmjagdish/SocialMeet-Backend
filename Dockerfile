# Step 1: Use an official Maven image to build the app
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the JAR file (skip tests for speed)
RUN mvn clean package -DskipTests

# Step 2: Use a smaller JDK image to run the app
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
