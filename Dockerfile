FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Install Gradle
RUN apk add --no-cache gradle

# Copy build files
COPY build.gradle settings.gradle ./

# Copy source code
COPY src/ src/

# Build the application
RUN gradle build -x test

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar

# Add health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Add JVM options for better container performance
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]