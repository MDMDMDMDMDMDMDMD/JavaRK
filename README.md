# Todo List CRUD Application

A simple Todo List CRUD application built with Java Spring Boot, Hibernate, and PostgreSQL.

## Technologies Used

- Java 21
- Spring Boot 3.2.3
- Hibernate/JPA
- PostgreSQL 15
- Gradle
- Docker & Docker Compose

## Project Structure

- `src/main/java/com/example/todoj/model`: Contains the Todo entity
- `src/main/java/com/example/todoj/repository`: Contains the TodoRepository interface
- `src/main/java/com/example/todoj/service`: Contains the TodoService class
- `src/main/java/com/example/todoj/controller`: Contains the TodoController class

## API Endpoints

- `GET /api/todos`: Get all todos
- `GET /api/todos/{id}`: Get a specific todo by ID
- `POST /api/todos`: Create a new todo
- `PUT /api/todos/{id}`: Update an existing todo
- `DELETE /api/todos/{id}`: Delete a todo

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