# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app
# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application, skipping tests (tests run in CI/CD)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Security: Run as a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy the built artifact from the builder stage
COPY --from=builder /app/target/predictive-analytics-*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
