# Smart Clinic Management System - Architecture Documentation

## Section 1: Architecture Summary

The **Smart Clinic Management System** is a three-tier web application developed using **Spring Boot**, designed to provide both server-rendered dashboards and scalable RESTful APIs. It incorporates **Spring MVC with Thymeleaf** for admin and doctor interfaces, and **REST APIs** for modules like appointments and patient records.

The application is architected with a clear separation of concerns across three layers:

- **Presentation Tier**: Includes Thymeleaf templates for web-based dashboards and RESTful endpoints for client-side consumers such as mobile apps or frontend frameworks.
- **Application Tier**: Implements the business logic using Spring Boot components such as controllers, services, and model classes.
- **Data Tier**: Uses two databases:
  - **MySQL** for structured, relational data such as users, roles, appointments, and patient records (accessed via Spring Data JPA).
  - **MongoDB** for unstructured, document-based data such as prescriptions (accessed via Spring Data MongoDB).

All controller requests are processed through a centralized **Service Layer**, which applies business logic and interacts with the appropriate **Repository Layer**. This modular structure enhances maintainability, testability, and scalability. The system is designed to be cloud-ready, with easy integration into CI/CD pipelines and support for containerized deployment using Docker and Kubernetes.

---

## Section 2: Flow of Data and Control (Step-by-Step)

1. **User Interface Layer**  
   End users interact with the system through:
   - **Thymeleaf-based dashboards** (e.g., `AdminDashboard`, `DoctorDashboard`) rendered on the server.
   - **REST API clients**, such as patient portals or mobile apps, which communicate via HTTP and consume JSON responses.

2. **Controller Layer**  
   Based on the request type:
   - **Thymeleaf Controllers** return rendered HTML views for browser-based interactions.
   - **REST Controllers** process incoming JSON requests, invoke services, and return structured JSON responses.

3. **Service Layer**  
   Controllers delegate request processing to services, which:
   - Enforce business rules and validations.
   - Coordinate multi-entity workflows (e.g., validating availability before creating an appointment).
   - Decouple controller logic from data access concerns.

4. **Repository Layer**  
   The service layer interacts with repositories, which abstract persistence logic:
   - **JPA Repositories** handle operations for structured entities (patients, doctors, admins).
   - **MongoDB Repositories** manage document-based data like prescriptions.

5. **Database Access**  
   Each repository interfaces with a corresponding database:
   - **MySQL** ensures integrity and normalization for structured, transactional data.
   - **MongoDB** provides flexible schema handling for prescription documents and similar records.

6. **Model Binding**  
   Retrieved database entries are mapped to Java objects:
   - **JPA Entities** (annotated with `@Entity`) for relational records.
   - **MongoDB Documents** (annotated with `@Document`) for BSON/JSON documents.

7. **Response Rendering**  
   - In MVC workflows, model objects are passed to Thymeleaf templates to render dynamic web pages.
   - In REST workflows, model objects or DTOs are serialized to JSON and returned as part of the HTTP response to client applications.

---
