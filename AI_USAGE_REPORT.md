# AI Usage Report

# Project

URL Shortener Service

---

# Purpose

Artificial Intelligence (AI) was used as an engineering companion throughout the development process. Rather than generating a complete solution, AI was primarily used to accelerate understanding, explore design alternatives, validate implementation ideas, and review code during development.

The overall architecture, implementation decisions, debugging, testing, and final code integration were performed manually.

---

# AI Tool Used

- ChatGPT (OpenAI)

---

# How AI Was Used

## 1. Understanding the Problem Statement

Before writing any code, AI was used to better understand the assignment requirements and convert them into an implementation plan.

Example prompts:

> "Break this URL shortener assignment into logical implementation phases."

> "Explain what the assignment is really evaluating from a backend engineering perspective."

This helped establish a clear development roadmap before implementation.

---

## 2. Project Ramp-Up

Instead of immediately writing code, AI was used to understand the problem domain and expected architecture.

Example prompts:

> "Explain how a production URL Shortener works."

> "Compare TinyURL and Bitly architectures."

> "Explain the advantages of layered architecture in Spring Boot."

These discussions helped in selecting an appropriate project structure.

---

## 3. Architecture Discussions

AI was used as a brainstorming partner to evaluate different architectural approaches.

Topics discussed included:

- Layered Architecture
- Controller-Service-Repository pattern
- SOLID principles
- DTO design
- Entity modelling
- REST API design

The final architecture was selected after reviewing the assignment requirements and considering implementation simplicity.

---

## 4. Code Review Companion

During development, AI was used similarly to how an engineer might use a peer reviewer.

Example prompts:

> "Review this entity class for production readiness."

> "Does this service class violate any SOLID principles?"

> "Can this repository be improved?"

Suggestions were reviewed individually before being accepted.

Several recommendations were intentionally rejected after manual evaluation.

---

## 5. Learning While Building

AI was frequently used to understand concepts encountered during implementation rather than simply requesting code.

Example prompts:

> "Why is SecureRandom preferred over Random?"

> "Why should duplicate URLs return the same short code?"

> "Explain why constructor injection is preferred."

> "What are the trade-offs between random Base62 generation and hashing?"

These discussions improved understanding of the design decisions.

---

## 6. Debugging Assistance

AI was used to investigate issues encountered during development.

Examples include:

- Docker Compose configuration
- MySQL authentication
- Spring Boot Docker integration
- Maven dependency conflicts
- Unit test failures
- HTTP status handling

Rather than copying fixes directly, each suggestion was reproduced, tested, and verified locally before being incorporated.

---

## 7. Testing Support

AI helped generate ideas for unit tests and edge cases.

Example prompts:

> "What edge cases should be tested for a URL Shortener?"

> "Review this UrlServiceTest for missing scenarios."

Every generated test was executed locally and modified where necessary to match the application's actual behaviour.

---

## 8. Documentation

AI assisted in improving project documentation.

This included discussions around:

- README structure
- API documentation
- Architecture explanation
- Design decisions
- Trade-offs
- Assumptions
- Future improvements

The final documentation was manually reviewed and edited before submission.

---

# Manual Engineering Decisions

The following decisions were made manually after evaluating different alternatives:

- Choosing random Base62 short-code generation.
- Returning the existing short URL for duplicate URLs.
- Supporting optional custom aliases.
- Designing the layered architecture.
- Selecting Spring Data JPA and MySQL.
- Configuring Docker Compose.
- Implementing REST APIs.
- Writing business logic.
- Debugging runtime issues.
- Implementing unit and controller tests.
- Final code refactoring and cleanup.

---

# AI Suggestions That Were Modified or Rejected

Not every AI suggestion was accepted.

Examples include:

- Removing an index suggestion on a TEXT column after identifying MySQL limitations.
- Simplifying certain abstractions to avoid unnecessary complexity for the scope of the assignment.
- Adjusting generated test cases after validating actual application behaviour.
- Refining exception handling and logging to better match REST API best practices.

This ensured the final implementation remained appropriate for the assignment rather than blindly following generated suggestions.

---

# Validation Process

Every significant change was verified through one or more of the following:

- Successful compilation
- Unit testing
- Controller testing
- Manual API verification using Swagger UI
- Database verification
- Docker Compose execution
- Runtime debugging

Only validated changes were retained in the final implementation.

---

# Reflection

AI significantly accelerated learning, brainstorming, and code review, allowing more time to focus on engineering decisions and implementation quality.

Throughout the project, AI functioned as a technical companion rather than an autonomous developer. Every architectural decision, implementation choice, code modification, debugging step, and final verification was performed manually before submission.