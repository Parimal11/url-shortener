# 🔗 URL Shortener Service

## Overview

This project is a production-inspired URL Shortener Service built as part of the **Paytm Backend Engineer Take-Home Assessment**.

The service allows users to:

- Generate short URLs from long URLs
- Create custom aliases
- Redirect short URLs to their original destinations
- Prevent duplicate URL mappings
- Validate incoming requests
- Handle failures gracefully through global exception handling

The project follows a layered architecture using Spring Boot, Spring Data JPA, MySQL and Docker Compose while emphasizing clean code, SOLID principles, testability, and maintainability.

---

# Features

- URL shortening
- Custom aliases
- HTTP 301 redirection
- Duplicate URL detection (idempotent behavior)
- Base62 short-code generation
- Collision detection
- Request validation
- Global exception handling
- OpenAPI / Swagger documentation
- Dockerized MySQL
- Unit and Controller Tests

---

# Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Build Tool | Maven |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Validation | Jakarta Bean Validation |
| API Documentation | SpringDoc OpenAPI |
| Testing | JUnit 5, Mockito, MockMvc |
| Containerization | Docker Compose |

---

# High Level Architecture

```
                    Client / Swagger
                           │
                           ▼
                  REST Controller Layer
                           │
                           ▼
                   Business Service Layer
                           │
            ┌──────────────┴──────────────┐
            ▼                             ▼
   URL Validator                Short Code Generator
            │                             │
            └──────────────┬──────────────┘
                           ▼
                   Repository Layer
                           │
                           ▼
                         MySQL
```

---

# Project Structure

```
src
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
├── util
├── config
└── test
```

---

# Database Schema

## url_mapping

| Column | Description |
|---------|-------------|
| id | Primary Key |
| original_url | Original URL |
| short_code | Generated / Custom Alias |
| custom_alias | Indicates custom alias |
| created_at | Creation Timestamp |

---

# Running the Project

## Prerequisites

- Java 17+
- Maven
- Docker Desktop

---

## Clone Repository

```bash
git clone https://github.com/Parimal11/url-shortener.git

cd url-shortener
```

---

## Run Application

```bash
mvn spring-boot:run
```

Spring Boot automatically starts the MySQL container using Docker Compose.

---

## Swagger

```
http://localhost:8080/swagger-ui/index.html
```

---

# API Endpoints

## Create Short URL

### POST /shorten

Request

```json
{
    "url":"https://www.google.com"
}
```

Response

```json
{
    "shortCode":"T6HznT",
    "shortUrl":"http://localhost:8080/T6HznT"
}
```

---

## Create Custom Alias

```json
{
    "url":"https://openai.com",
    "alias":"openai"
}
```

Response

```json
{
    "shortCode":"openai",
    "shortUrl":"http://localhost:8080/openai"
}
```

---

## Redirect

```
GET /{code}
```

Returns

```
301 Moved Permanently
Location: <Original URL>
```

---

# Error Handling

| Scenario | Status |
|----------|--------|
| Invalid URL | 400 |
| Alias Already Exists | 409 |
| Unknown Short Code | 404 |

---

# Testing

The following automated tests are included.

- UrlValidatorTest
- ShortCodeGeneratorTest
- UrlServiceTest
- UrlControllerTest

Run

```bash
mvn test
```

---

### API Validation Harness

A Python-based validation harness is included under the `tools/` directory.

It performs:

- Functional API testing
- Edge-case validation
- Response verification
- Performance measurement
- Markdown report generation

Run the Java test suite:

```bash
mvn test
```

Run the validation harness:

```bash
cd tools
python test_harness.py
```

# Docker Support

The application uses Spring Boot Docker Compose integration.

```
Docker Desktop
        │
Docker Compose
        │
MySQL Container
        │
Spring Boot
```

---

# Design Decisions

## 1. Duplicate URL Handling

### Decision

If the same URL is submitted multiple times without specifying a custom alias, the existing short URL is returned.

### Reason

- Prevent duplicate database records
- Maintain idempotent behavior
- Improve storage efficiency

### Alternative Considered

Always generate a new short URL.

### Why Rejected

Would create redundant mappings for identical URLs without providing additional value.

---

## 2. Short Code Generation

### Decision

Random Base62 six-character codes.

### Reason

- Compact URLs
- Easy to read
- Large key space
- Simple implementation

### Alternative Considered

- UUID
- MD5/SHA Hashing
- Snowflake IDs

### Why Rejected

These approaches either generate unnecessarily long URLs or introduce additional complexity beyond the assignment requirements.

---

## 3. Collision Handling

### Decision

Generated codes are checked for uniqueness before persistence.

### Reason

Although collisions are unlikely, the implementation guarantees uniqueness by regenerating the code until an unused value is found.

---

## 4. Persistence Layer

### Decision

MySQL with Spring Data JPA.

### Reason

Provides reliable persistence while keeping the implementation straightforward.

---

## 5. Layered Architecture

The project follows

```
Controller
↓

Service
↓

Repository
↓

Database
```

This separates HTTP concerns, business logic, and persistence, making the application easier to test and maintain.

---

# Assumptions

The implementation makes the following assumptions:

- Only HTTP and HTTPS URLs are considered valid.
- Custom aliases must be unique.
- Generated short codes are six-character Base62 strings.
- Duplicate URLs return the existing mapping.
- Authentication is outside the scope of the assignment.
- URL expiration is outside the scope of the assignment.
- Analytics are outside the scope of the assignment.

---

# Trade-offs

| Decision | Benefit | Limitation |
|----------|---------|------------|
| Random Base62 | Simple and compact | Requires collision check |
| MySQL Only | Simple architecture | No caching layer |
| Duplicate URL Reuse | Idempotent behavior | Cannot generate multiple codes for same URL |
| Single Service | Easier to understand | Not horizontally decomposed |

---

# Edge Cases Covered

✔ Invalid URL

✔ Empty URL

✔ Missing URL

✔ Unsupported Protocol (FTP)

✔ Duplicate URL

✔ Duplicate Alias

✔ Invalid Alias Format

✔ SQL Injection Payload

✔ XSS Payload

✔ Unknown Short Code

✔ Generated Short Code Collision

✔ HTTP Redirect

---

# Limitations

The following production features were intentionally left out to keep the implementation aligned with the scope of the assignment.

- URL Expiration
- Click Analytics
- Redis Caching
- Rate Limiting
- Authentication
- User Management
- Distributed ID Generation
- Event Streaming
- Multi-region Deployment

---

# Future Improvements

Given additional development time, the following enhancements would be added:

- Redis caching for faster redirects
- Click analytics dashboard
- URL expiration support
- Kafka-based event streaming
- Rate limiting
- User authentication
- Distributed short-code generation
- Horizontal scaling
- Prometheus & Grafana monitoring
- AWS deployment (EC2, RDS, ALB)

---

# AI Usage

This project was developed with assistance from AI as a development aid.

AI was used for:

- Architecture brainstorming
- Code review suggestions
- Documentation refinement
- Test case brainstorming

Every generated suggestion was manually reviewed, validated, modified where necessary, and tested before being incorporated into the final implementation.

A detailed explanation of AI usage is available in the [AI Usage Report](AI_USAGE_REPORT.md).

---

# Build Verification

The project has been verified using:

```bash
mvn clean test

mvn spring-boot:run
```

cd tools
python test_harness.py

The application successfully:

- Starts MySQL using Docker Compose
- Creates the required database schema
- Passes all automated tests
- Exposes Swagger documentation
- Successfully shortens and redirects URLs

---

# Author

Parimal Deshmukh

Developed as part of the Paytm Backend Engineer Take-Home Assessment.
