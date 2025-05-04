# Todo List CRUD Application

A simple Todo List CRUD application built with Java Spring Boot, Hibernate, and PostgreSQL, with JWT authentication integration.

## Fixed Issues

### 1. Circular Dependency Resolution
The application was experiencing circular dependency issues between `JwtAuthenticationFilter` and `RestTemplate`. This has been fixed by:
- Removing the `@Component` annotation from `JwtAuthenticationFilter`
- Creating the filter as a bean in `WebSecurityConfig`
- Restructuring the dependency injection flow

### 2. Docker Stability Improvements
- Added health checks to all services
- Improved container restart policies from `always` to `on-failure:5`
- Added proper service dependency conditions
- Optimized JVM settings for containerized environments
- Added PostgreSQL readiness checks

### 3. Application Monitoring
- Added Spring Boot Actuator for health monitoring
- Created custom health check endpoint
- Configured proper database connection pool settings

## Technologies Used

- Java 21
- Spring Boot 3.2.3
- Hibernate/JPA
- PostgreSQL 15
- JWT Authentication
- Gradle
- Docker & Docker Compose

## Project Structure

- `src/main/java/com/example/todoj/model`: Contains the Todo entity
- `src/main/java/com/example/todoj/repository`: Contains the TodoRepository interface
- `src/main/java/com/example/todoj/service`: Contains the TodoService class
- `src/main/java/com/example/todoj/controller`: Contains the TodoController and authentication-related controllers
- `src/main/java/com/example/todoj/security`: Contains security configuration and JWT utilities

## API Endpoints

### Todo Endpoints (Authenticated)

- `GET /api/todos`: Get all todos
- `GET /api/todos/{id}`: Get a specific todo by ID
- `POST /api/todos`: Create a new todo
- `PUT /api/todos/{id}`: Update an existing todo
- `DELETE /api/todos/{id}`: Delete a todo

### Authentication Endpoints

- `GET /api/auth-status`: Check authentication status
- `GET /api/public/info`: Public endpoint that doesn't require authentication

## Authentication Flow

1. **User Authentication**: Users authenticate with the Auth Service (separate microservice) to obtain a JWT token.
2. **Token Usage**: The JWT token must be included in the Authorization header for all protected endpoints:
   ```
   Authorization: Bearer <jwt_token>
   ```
3. **Token Validation**: The Todo application validates tokens in two ways:
   - Local validation using the JwtUtils class (validates token structure and expiration)
   - Remote validation by calling the Auth Service's validation endpoint
4. **Authorization**: Once validated, the user is granted access to the protected resources based on their role.

## Security Configuration

The application uses Spring Security with JWT authentication:

- Public endpoints are accessible without authentication
- All other endpoints require a valid JWT token
- JWT tokens are validated both locally and through the Auth Service
- Token extraction and validation are handled by JwtAuthenticationFilter and JwtUtils

## Running the Application

### Using Docker Compose

1. Make sure you have Docker and Docker Compose installed
2. Clone the repository
3. Navigate to the project directory
4. Run the following command:

```bash
docker-compose up --build
```

The application will be available at http://localhost:8080

### Running Locally

1. Make sure you have Java 21 and PostgreSQL installed
2. Update the `application.properties` file with your PostgreSQL configuration
3. Run the following commands:

```bash
./gradlew build
./gradlew bootRun
```

## Sample Todo JSON

```json
{
  "title": "Complete project",
  "description": "Finish the Todo application",
  "completed": false
}
```
