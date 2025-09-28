# DevOps Project - Student Management System

Spring Boot application for managing student records with automated tasks. Built for university DevOps coursework.

## Features
- REST API for student CRUD operations
- Automated student creation (every 4 minutes)
- Automated database cleanup (every 2 minutes)
- H2 in-memory database
- Health monitoring with Spring Actuator

## Tech Stack
- Java 21, Spring Boot 3.5.6, H2 Database, Maven

## Quick Start

### Local Development
```bash
git clone <repository-url>
cd devops_project
mvn spring-boot:run
```
Access: http://localhost:8080

### Running with Docker
```bash
docker-compose up -d
```

### Infrastructure Deployment
```bash
cd terraform
./run.sh
```
Uses **Terraform** to deploy complete logging/monitoring stack (Elasticsearch, Kibana, Grafana, Prometheus). See `terraform/` directory for detailed setup.

### Tests
```bash
mvn test
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/students` | Get all students |
| `POST` | `/api/students` | Create student |
| `DELETE` | `/api/students/{id}` | Delete student |
| `GET` | `/actuator/health` | Health check |

### Student JSON Format
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "course": "Computer Science",
  "active": true
}
```

## CI/CD Pipeline

**GitHub Actions** workflow:
1. **Test & Analysis** - Maven build, tests, SonarCloud analysis
2. **Docker Build & Push** - Build and push to GitHub Container Registry

**Triggers**: Push to master (full pipeline), PRs (testing only)

## Database Access
H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa` (no password)