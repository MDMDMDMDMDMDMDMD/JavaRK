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
ENTRYPOINT ["java","-jar","/app.jar"]