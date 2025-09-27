# DevOps Project - Student Management System

A Spring Boot application for managing student records with automated tasks for student creation and database cleanup. Built for university DevOps coursework.

## Features

- **Student Management**: CRUD operations for student records
- **Automated Student Creation**: Scheduled task to create students from JSON file every 4 minutes
- **Automated Database Cleanup**: Scheduled task to remove inactive students every 2 minutes
- **RESTful API**: JSON-based REST endpoints for all operations
- **H2 Database**: In-memory database for development and testing
- **Spring Boot Actuator**: Health checks and monitoring endpoints
- **Comprehensive Testing**: Unit and integration tests included

## Technologies Used

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Lombok** (code generation)
- **Maven** (build tool)
- **JUnit 5** (testing)

## API Endpoints

### Student Operations

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/students` | Get all students | - | `200 OK` with student list |
| `POST` | `/api/students` | Create a new student | Student JSON | `201 Created` with created student |
| `DELETE` | `/api/students/{id}` | Delete student by ID | - | `204 No Content` |

### Student JSON Format

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "course": "Computer Science",
  "active": true
}
```

### Response Format

**Success Response (GET /api/students):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "course": "Computer Science",
    "active": true
  }
]
```

**Error Responses:**
- `400 Bad Request`: Invalid student data
- `404 Not Found`: Student not found
- `409 Conflict`: Email already exists
- `500 Internal Server Error`: Unexpected server error

### Health Check

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/actuator/health` | Application health status |

## Database Schema

### Student Entity

| Field | Type | Constraints |
|-------|------|-------------|
| `id` | UUID | Primary Key, Auto-generated |
| `name` | String | Not null |
| `email` | String | Not null, Unique |
| `course` | String | Not null |
| `active` | Boolean | Not null |

## Scheduled Tasks

### Student Creation Task
- **Frequency**: Every 4 minutes (240,000ms)
- **Purpose**: Automatically creates students from `static/students.json` file
- **Behavior**: Skips students that already exist (based on email uniqueness)
- **Location**: `StudentCreationTask.java:25`

### Database Cleanup Task
- **Frequency**: Every 2 minutes (120,000ms)
- **Purpose**: Automatically removes inactive students from database
- **Location**: `DatabaseCleanupTask.java:16`

## Prerequisites

- **Java 21** or later
- **Maven 3.6+**

## Running the Application

### 1. Clone the repository
```bash
git clone <repository-url>
cd devops_project
```

### 2. Build the application
```bash
mvn clean compile
```

### 3. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Alternative: Run as JAR
```bash
mvn clean package
java -jar target/devops-0.0.1-SNAPSHOT.jar
```

## Docker Deployment

### Using Docker Compose (Recommended):
```bash
docker-compose up -d
```

### Manual Docker Build:
```bash
docker build -t devops-spring-boot-app .
docker run -p 8080:8080 devops-spring-boot-app
```

The Dockerfile uses multi-stage build with Maven for compilation and Alpine JRE for runtime.

## Running Tests

### Run all tests
```bash
mvn test
```

### Run specific test classes
```bash
# Unit tests
mvn test -Dtest=StudentServiceTest
mvn test -Dtest=StudentControllerTest
mvn test -Dtest=StudentCreationTaskTest
mvn test -Dtest=DatabaseCleanupTaskTest

# Integration tests
mvn test -Dtest=StudentControllerIntegrationTest
```

### Test Coverage
The project includes tests:
- **Unit Tests**: Service layer, Controller layer, Scheduled tasks
- **Integration Tests**: End-to-end API testing

## Database Access

The application uses H2 in-memory database. To access the H2 console:

1. Start the application
2. Navigate to `http://localhost:8080/h2-console`
3. Use the following connection settings:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **User Name**: `sa`
   - **Password**: (leave empty)

## Configuration

### Default Behavior
- **Database**: H2 in-memory (auto-configured)
- **Port**: 8080 (Spring Boot default)
- **Logging**: SLF4J with Logback
- **Actuator**: Enabled for health checks

## API Usage Examples

### Create a Student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Johnson",
    "email": "alice.johnson@university.edu",
    "course": "Mathematics",
    "active": true
  }'
```

### Get All Students
```bash
curl http://localhost:8080/api/students
```

### Delete a Student
```bash
curl -X DELETE http://localhost:8080/api/students/{student-id}
```

### Check Application Health
```bash
curl http://localhost:8080/actuator/health
```

## Development

### Project Structure
```
src/
├── main/java/com/krisztavasas/devops/
│   ├── controller/     # REST controllers
│   ├── entity/         # JPA entities
│   ├── repository/     # Data repositories
│   ├── service/        # Business logic
│   ├── task/           # Scheduled tasks
│   └── DevopsApplication.java
├── main/resources/
│   └── application.properties
└── test/java/          # Test classes
```

## Monitoring

The application provides several monitoring capabilities:
- **Actuator Health Endpoint**: `/actuator/health`
- **Comprehensive Logging**: All operations are logged with SLF4J
- **Scheduled Task Monitoring**: Student creation and cleanup tasks log their execution
- **Exception Handling**: Proper error responses and logging for all failures