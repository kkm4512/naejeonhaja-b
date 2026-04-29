# Multi-stage build for Spring Boot application
FROM gradle:8.5-jdk17 AS build

# Set working directory
WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# Copy source code
COPY src/ src/

# Build the application
RUN gradle build -x test --no-daemon

# Production stage
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Create a non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
