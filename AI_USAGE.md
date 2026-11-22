# AI Usage Documentation

## 1. Overview

AI tools were used as development assistants during the Candidate
Management application assignment. They supported requirement analysis,
implementation planning, coding assistance, debugging, test design, and
documentation.

AI-generated suggestions were treated as recommendations. I reviewed the
resulting code, made implementation decisions, and validated the
application through compilation and automated tests.

## 2. AI Tools Used

### ChatGPT

ChatGPT was used to: - Break down the assignment requirements into
development tasks. - Discuss the Spring Boot application architecture
and technology choices. - Understand REST API design, DTOs, validation,
filtering, and exception handling. - Explore dynamic filtering with
Spring Data JPA Specifications. - Investigate development issues and
understand framework errors. - Plan and review the technical design
documentation.

### GitHub Copilot

GitHub Copilot was used as an in-IDE coding assistant during
implementation. It supported code suggestions and completion while
working on application components such as controllers, services, DTOs,
repositories, validation, exception handling, and tests.

The final implementation was reviewed and tested rather than accepted
solely on the basis of AI-generated suggestions.

## 3. Areas Where AI Assistance Was Used

  -----------------------------------------------------------------------
Area                                How AI assistance was used
  ----------------------------------- -----------------------------------
Requirement analysis                Breaking the assignment into API,
validation, filtering, persistence,
testing, and documentation tasks.

Architecture                        Evaluating a layered Spring Boot
application structure and
separation of controller, service,
repository, entity, and DTO
responsibilities.

Dependencies                        Reviewing dependencies for Spring
Web, Spring Data JPA, PostgreSQL,
validation, mapping, API
documentation, and testing.

Candidate APIs                      Supporting implementation of
candidate create, retrieve, update,
partial update, delete, and
filtering operations.

Validation and exceptions           Discussing request validation,
custom exceptions, duplicate-email
handling, and consistent error
responses.

Search and filtering                Understanding optional filters and
dynamic query construction using
JPA Specifications.

Testing and debugging               Supporting test-case design and
investigation of compilation,
runtime, API, and test issues.

Documentation                       Planning and reviewing the
technical design and AI usage
documentation.
  -----------------------------------------------------------------------

## 4. Review and Validation of AI-Assisted Work

AI suggestions were reviewed against the assignment requirements and the
application's implementation.

Validation activities included:

-   **Source review:** Reviewing controllers, services, repositories,
    entities, DTOs, validation rules, and exception handling.
-   **Compilation:** Using the Maven Wrapper to compile and build the
    application.
-   **Automated tests:** Running the Maven test suite. The recorded
    verification result was 26 tests, with 0 failures and 0 errors
    (`BUILD SUCCESS`).
-   **Database startup:** Verifying that the application could connect
    to the configured PostgreSQL database and initialize the candidate
    schema.
-   **API checks:** Exercising candidate endpoints locally through
    Swagger or direct HTTP requests.

The repository does not include a complete Testcontainers integration
suite, exported Postman collection, code-coverage report, or load-test
report. These have therefore not been presented as completed validation
activities.

## 5. Engineering Decisions

AI recommendations were considered in the context of the assignment
rather than adopted automatically.

### Approaches used

-   **Layered monolithic architecture:** A single Spring Boot
    application was selected because it fits the scope of the Candidate
    Management assignment.
-   **DTO separation:** Request, response, patch, and search DTOs are
    used to separate API contracts from persistence entities.
-   **JPA Specifications:** Dynamic filtering is implemented using
    composable query predicates.
-   **Bean Validation:** Validation constraints are used for candidate
    request and search inputs.
-   **Centralized exception handling:** Application exceptions and
    validation errors are handled centrally.
-   **Duplicate-email checks:** Application-level checks are combined
    with a database unique constraint.
-   **Automated tests:** JUnit 5, Mockito, and Spring Boot test support
    are used for service and controller tests.

### Approaches not implemented

The following technologies or capabilities were not included because
they were outside the current assignment scope or were not required by
the implemented application:

-   **Microservices:** The application remains a single deployable
    Spring Boot service.
-   **Kafka:** Event streaming is not required for the current CRUD and
    filtering workflow.
-   **Redis:** A caching layer was not added.
-   **Elasticsearch:** PostgreSQL-based filtering is used for the
    current search requirements.
-   **Spring Security:** Authentication and authorization are not
    implemented in the submitted application.
-   **Production database migrations:** Flyway migration scripts and a
    production migration workflow are not included in the current
    implementation.

These are possible future enhancements, not features of the submitted
application.

## 6. Human Review and Responsibility

I remained responsible for the final implementation and the decisions
made during development. I reviewed the application structure and
behavior, evaluated AI suggestions, addressed issues found during
development, and ran the available automated tests.

I am responsible for understanding the submitted code and being able to
explain, modify, and extend the implementation.

## 7. Conclusion

ChatGPT and GitHub Copilot helped accelerate requirement analysis,
implementation, debugging, testing, and documentation. Their output was
used as development assistance, while the final technical decisions and
submitted implementation remained subject to my review and validation.
