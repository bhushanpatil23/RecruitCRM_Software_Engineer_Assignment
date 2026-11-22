# Candidate Management System

A Spring Boot REST API for managing recruitment candidate records. The application supports candidate CRUD operations, partial updates, dynamic filtering, pagination, sorting, request validation, and PostgreSQL persistence.

## Tech Stack

- Java 21
- Spring Boot 3.5.7
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Jakarta Bean Validation
- ModelMapper
- Springdoc OpenAPI / Swagger UI
- JUnit 5, Mockito, and Spring MockMvc

## Prerequisites

- Java 21
- PostgreSQL
- Maven 3.9+ or the included Maven Wrapper
- PowerShell or another terminal

## Database Setup

Create the database in PostgreSQL:

```sql
CREATE DATABASE recruitcrm;
```

The default local datasource URL is:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/recruitcrm
```

Configure your PostgreSQL username and password in `src/main/resources/application.properties`, or provide them through environment variables.

Example using PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/recruitcrm"
$env:SPRING_DATASOURCE_USERNAME="your_database_user"
$env:SPRING_DATASOURCE_PASSWORD="your_database_password"
```

Do not commit real database credentials.

### Database configuration notes

The current local configuration uses:

```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.flyway.enabled=false
```

The application loads demonstration records from `src/main/resources/data.sql`. Hibernate's `update` setting updates the schema without intentionally dropping existing tables when the application stops. Flyway migration scripts are not currently configured; add versioned migrations and review the schema strategy before production deployment.

## Build, Test, and Run

Run these commands from the project root on Windows.

Run the test suite:

```powershell
.\mvnw.cmd clean test
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

To build a JAR:

```powershell
.\mvnw.cmd clean package
```

Then run the generated JAR from the `target` directory:

```powershell
java -jar .\target\candidate-management-0.0.1-SNAPSHOT.jar
```

The application runs at `http://localhost:8080`.

## API Documentation

With the application running:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## API Endpoints

Base path: `/candidates`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/candidates/create` | Create a candidate |
| `GET` | `/candidates/{id}` | Retrieve a candidate by ID |
| `GET` | `/candidates/all` | Retrieve candidates with pagination and sorting |
| `GET` | `/candidates/filter` | Search and filter candidates |
| `PUT` | `/candidates/{id}` | Update a candidate |
| `PATCH` | `/candidates/{id}` | Partially update a candidate |
| `DELETE` | `/candidates/{id}` | Delete a candidate |

### Pagination and sorting

The `/all` and `/filter` endpoints support Spring Data pagination parameters:

- `page`: zero-based page number
- `size`: number of records per page
- `sort`: property and direction

Example:

```text
GET /candidates/all?page=0&size=10&sort=firstName,asc
```

The default page size is 20. The service enforces a maximum page size of 100 and validates supported sort fields.

### Candidate filtering

The `/filter` endpoint supports these optional query parameters:

- `firstName`
- `lastName`
- `email`
- `skills`
- `currentCompany`
- `currentLocation`
- `preferredLocation`
- `experienceMin`
- `experienceMax`
- `salaryMin`
- `salaryMax`
- `noticePeriodMax`

Example:

```text
GET /candidates/filter?skills=Java&experienceMin=3&experienceMax=8&page=0&size=10
```

String filters use partial, case-insensitive matching. Numeric filters support minimum and maximum values.

### Example: Create a candidate

Endpoint: `POST /candidates/create`

Request body:

```json
{
  "firstName": "Aarav",
  "lastName": "Sharma",
  "email": "aarav@example.com",
  "phoneNumber": "9876543210",
  "skills": "Java, Spring Boot, PostgreSQL",
  "experience": 3.5,
  "currentCompany": "TCS",
  "currentLocation": "Pune",
  "preferredLocation": "Bangalore",
  "expectedSalary": 900000,
  "noticePeriodInDays": 30
}
```

### Example: Partial update

Endpoint: `PATCH /candidates/1`

Request body:

```json
{
  "experience": 5,
  "currentCompany": "Infosys"
}
```

### Delete a candidate

Endpoint: `DELETE /candidates/1`

A successful deletion returns HTTP `204 No Content`.

## Response Format

Successful responses use the following structure:

```json
{
  "timestamp": "2026-11-22T12:00:00",
  "data": {},
  "error": null
}
```

Error responses include an error object with a status and message. Validation errors may also include details in `subErrors`.

## Project Documentation

- `TECHNICAL_DESIGN.md` — application architecture, database design, API details, implementation decisions, testing, and limitations.
- `AI_USAGE.md` — how AI tools were used during development and how their suggestions were reviewed.
