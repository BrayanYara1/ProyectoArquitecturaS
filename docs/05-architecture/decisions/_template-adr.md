# Technical Documentation: Salud Activa Project

This document provides a unified technical overview of the Salud Activa ecosystem, covering strategic decisions, architectural design, functional requirements, and operational standards.

---

## 1. Architectural Decision Record: ADR-001

**Decision Title:** Documentation Language
**ID:** ADR-001
**Date:** 2024-01-10
**Status:** Accepted
**Authors:** Maria Garcia — Tech Lead

### Context
The global software industry operates in English. Mixing languages across project artifacts (such as Spanish documentation versus English code) creates a cognitive translation boundary that slows onboarding and increases naming errors. A single, clear rule established from day one prevents inconsistencies between technical design and implementation.

### Decision
We decided to use English for all documentation and code. This policy applies to:
*   Code: Variable names, functions, and classes.
*   Database: Table and column names.
*   Git: Commits following the Conventional Commits standard and branch names.
*   Documentation: All Markdown files and technical guides.
*   API: OpenAPI contracts and endpoint descriptions.
*   Logs: Internal system logs and monitoring metrics.

### Consequences
*   Positive: A single language across all artifacts eliminates the translation boundary and aligns the project with global industry standards.
*   Negative: Team members less confident in English may need initial support or a domain glossary for specific business terms.

---

## 2. System Architecture Overview

### Adopted Architectural Style
**Style:** Modular Monolith based on Clean Architecture and MVVM.
**Justification:** This approach ensures a strict separation between business logic and delivery mechanisms (UI, Database, API). It allows the project to scale without rewriting core business rules.

### Architectural Principles
*   **API-First:** Design the API contract before implementing the service.
*   **Offline-First:** The Android app must remain functional without connectivity via local Room caching and background synchronization.
*   **Clean Architecture:** Business logic must remain independent of UI or Database frameworks.
*   **Observability:** JSON structured logging and CloudWatch metrics are mandatory.

### Service Catalog
*   **Android App:** Responsible for User Interface, local logic, and offline synchronization. It uses Room for the local database and communicates via REST and Firebase Cloud Messaging (FCM).
*   **Backend API:** Responsible for authentication, appointment logic, and Personal Identifiable Information (PII) management. It uses Node.js and Express with MongoDB and PostgreSQL.
*   **Infrastructure:** Managed as Infrastructure as Code (IaC) using Terraform for AWS ECS Fargate, RDS, and Application Load Balancers.

---

## 3. Functional Requirements: User Stories

### HU-IAM-001: User Authentication (Login)
**Story:** As an application user, I want to enter my credentials so that I can access my medical data securely.
*   Acceptance Criteria 1: Redirect to HomeFragment on success and store the JWT securely.
*   Acceptance Criteria 2: Auto-login if a valid token exists in EncryptedSharedPreferences.
*   Acceptance Criteria 3: Show an "Invalid credentials" error on failed attempts.

### HU-SCHED-001: Medical Appointment Request
**Story:** As a patient, I want to select a specialty and date so that I can schedule a medical appointment online.
*   Acceptance Criteria 1: Display a summary for confirmation before final submission.
*   Acceptance Criteria 2: Synchronize with the server immediately if the device is online.
*   Acceptance Criteria 3: Save locally and mark for background sync if the device is offline.

### HU-MED-001: Medication Registration and Scanning
**Story:** As a patient, I want to scan barcodes or enter medications manually so that I receive intake alerts.
*   Acceptance Criteria 1: Auto-populate fields using the MedicineScanner utility.
*   Acceptance Criteria 2: Trigger high-priority notifications via the NotificationHelper.

---

## 4. Non-Functional Requirements (NFR)

### Performance Goals
*   Critical Endpoints Latency: P95 less than 300ms under a load of 100 requests per second.
*   Android App Cold Start: Less than 2 seconds on mid-range devices.
*   Service Startup Time: Less than 20 seconds for AWS Fargate cold starts.

### Availability Standards
*   Monthly SLO: 99.9% availability for production.
*   Maintenance Window: Sundays from 2:00 AM to 4:00 AM.
*   Health Checks: Standardized GET /api/status for liveness monitoring.

### Security Requirements
*   Authentication: Mandatory JWT in the Authorization header with a 1-hour expiry.
*   Transmission: HTTPS mandatory in production via TLS 1.2 or higher.
*   Android Protections: Encrypted local storage for tokens and Biometric protection for access.

### Maintainability Metrics
*   Test Coverage: Minimum 80% coverage in business logic layers.
*   CI Build Time: Less than 6 minutes for the full pipeline.
*   Onboarding: Local environment setup in less than 1 hour.

---

## 5. Hexagonal Architecture Guide

### The Concept
Hexagonal architecture (Ports and Adapters) ensures the business domain is completely independent of surrounding technology. 
*   **Domain:** The core hexagon containing Entities, Value Objects, and Ports (interfaces).
*   **Application:** Use cases that orchestrate the domain logic.
*   **Infrastructure:** Adapters including HTTP controllers, database implementations, and API clients.

### Dependency Rule
Dependencies always point inward: **Infrastructure -> Application -> Domain**. The Domain layer must never import anything from external layers or frameworks like Retrofit, Room, or Express.

---

## 6. Disaster Recovery (DR)

### Recovery Scenarios
*   **Backend Instance Failure:** Recovery time objective (RTO) is less than 2 minutes. The recovery point objective (RPO) is zero.
*   **Database Failure:** Recovery time objective is less than 5 minutes via failover. The recovery point objective window is less than 5 seconds.
*   **Regional Disaster:** Recovery time objective is less than 4 hours for a full AWS redeploy. The recovery point objective window is less than 1 hour.

---

## 7. ADR Template for Future Decisions

Use this structure for documenting new architectural decisions in the `records/` directory.

### ADR-NNN — [Decision Title]

*   **ID:** ADR-NNN
*   **Date:** YYYY-MM-DD
*   **Status:** Proposed | Accepted | Rejected | Superseded by ADR-NNN
*   **Authors:** [names]

#### Context
Describe the situation that requires a decision. Why is this important? What are the technical or business constraints?

#### Decision
Provide a clear description of the chosen path.
*   **Justification:** Explain why this option is the best fit compared to others.

#### Evaluated Alternatives
*   Alternative A (Chosen): List pros and cons.
*   Alternative B: List pros, cons, and why it was discarded.

#### Consequences
*   **Positive:** Benefits gained from this decision.
*   **Negative:** Costs, trade-offs, or new limitations.
*   **Impact:** List affected services and documents to be updated.

#### Risks and Mitigation
*   Identify potential risks, their probability/impact, and how to mitigate them.
