# Technical Documentation: Salud Activa Project

This document provides a unified technical overview of the **Salud Activa** ecosystem, encompassing the Android application, Node.js backend, and AWS infrastructure.

---

## 1. User Stories (HU)

### HU-IAM-001: User Authentication (Login)
> **ID convention:** `HU-IAM-001` (IAM: Identity and Access Management)

**Story**
**As** an application user  
**I want** to enter my credentials (email and password)  
**So that** I can access my personal medical data and manage my appointments securely.

**Acceptance Criteria**
- [ ] **AC1:** Given that the user is not logged in, when they enter a valid email and password, then the system must redirect them to the "HomeFragment" and securely store the JWT.
- [ ] **AC2:** Given that the user is already logged in, when they open the application, then the system must validate the token and bypass the Login screen automatically.
- [ ] **AC3:** **[Error Scenario]** Given that the user enters an incorrect password, when they press "Login", then the system must display the message "Invalid credentials" and remain on the Login screen.

**Technical Notes**
- **Responsible service(s):** Backend (Auth routes), `AuthRepository.kt` (Android).
- **Endpoint(s) implemented:** `POST /api/auth/login`.
- **Required permissions:** N/A (Public access).

---

### HU-SCHED-001: Medical Appointment Request
> **ID convention:** `HU-SCHED-001` (SCHED: Scheduling)

**Story**
**As** a patient  
**I want** to select a specialty and an available date  
**So that** I can schedule a medical appointment without needing to call by phone.

**Acceptance Criteria**
- [ ] **AC1:** Given that the user is in "SolicitarTurnoFragment", when they select a specialty, doctor, and date, then they should view a summary of the appointment before confirming.
- [ ] **AC2:** Given that the user confirms the appointment, when there is an internet connection, then the system must synchronize the appointment with the server immediately.
- [ ] **AC3:** **[Offline Mode]** Given that the user has no connection, when they confirm the appointment, then the system must save it locally (Room) and mark it for later synchronization via `SyncWorker`.

**Technical Notes**
- **Responsible service(s):** Backend (Turnos routes), `TurnoRepository.kt`.
- **Endpoint(s) implemented:** `POST /api/turnos`, `GET /api/turnos`.
- **Events generated:** Local reminder notification via `ReminderManager.kt`.
- **Required permissions:** Role: `USER` (Valid JWT required).

---

### HU-MED-001: Medication Registration and Scanning
> **ID convention:** `HU-MED-001` (MED: Medications)

**Story**
**As** a patient under treatment  
**I want** to scan a medication's barcode or enter it manually  
**So that** I can keep track of my medicine inventory and receive intake alerts.

**Acceptance Criteria**
- [ ] **AC1:** Given that the user uses the camera, when they scan a valid barcode, then the system must auto-complete the name and presentation of the medication using `MedicineScanner.kt`.
- [ ] **AC2:** Given that the medication has been registered, when the configured time arrives, then the system must trigger a high-priority notification via `NotificationHelper.kt`.

**Technical Notes**
- **Responsible service(s):** Android App (Local storage).
- **Required permissions:** `CAMERA`, `POST_NOTIFICATIONS`.

---

### HU-DOC-001: Medical Records Management (PDF)
> **ID convention:** `HU-DOC-001` (DOC: Documents)

**Story**
**As** a patient  
**I want** to upload and view my medical study results in PDF or image format  
**So that** I have all my clinical history digitized and accessible in one place.

**Acceptance Criteria**
- [ ] **AC1:** Given that the user selects a file, when they press "Upload", then the system must process the document and show a thumbnail in the study list.
- [ ] **AC2:** Given that the study is saved, when the user clicks on it, then the system must open the internal PDF viewer (`PdfGenerator.kt`).

---

## 2. Non-Functional Requirements (NFR)

### NFR-001: Performance
| Attribute | Metric | Test condition |
|-----------|--------|---------------|
| P95 Latency — Critical Endpoints | < 300ms | Under 100 RPS load |
| P99 Latency — Critical Endpoints | < 500ms | Under 200 RPS load |
| P95 Latency — Android App (Cold Start) | < 2s | Mid-range device (4GB RAM) |

### NFR-002: Availability
| Environment | SLO | Maintenance window | Max downtime/month |
|------------|-----|-------------------|-------------------|
| Production | 99.9% | Sundays 2am-4am (GMT-5) | 44 minutes |
| Staging | 95% | No restriction | 36 hours |

### NFR-004: Security
- **Authentication:** Mandatory **JWT** in the `Authorization: Bearer <token>` header (1-hour expiry).
- **Encryption:** HTTPS mandatory in production (TLS 1.2+). PII encrypted at rest.
- **Android:** `EncryptedSharedPreferences` for local tokens and Biometric protection enabled.

### NFR-006: Maintainability
- **Test Coverage:** ≥ 80% in business logic (ViewModels and Repositories).
- **CI/CD:** Average build time < 6 minutes using GitHub Actions.
- **Complexity:** Cyclomatic complexity ≤ 10 per function.

---

## 3. Hexagonal Architecture Guide

### Overview
Hexagonal architecture (Ports & Adapters) ensures the **business domain is completely independent** of surrounding technology. Databases, UI frameworks, and external APIs are interchangeable details.

### Folder Structure


### The Dependency Rule
**Dependencies point inward: Infrastructure → Application → Domain.**
The Domain layer must never import anything from the Application or Infrastructure layers.

---

## 4. Disaster Recovery (DR)

| Scenario | RTO (Recovery Time) | RPO (Data Loss Window) |
|---------|---------------------|------------------------|
| Backend Failure | < 2 minutes (ECS Auto-restart) | 0 (Stateless) |
| DB Failure | < 5 minutes (Failover) | < 5 seconds |
| Regional Disaster | < 4 hours | < 1 hour |

---

## 5. Priority Matrix

| Item | Priority | Validated in CI? | Owner |
|------|----------|-----------------|-------|
| Security (NFR-004) | P1 (High) | Yes (SAST) | Security Lead |
| Appointments (HU-SCHED) | P1 (High) | Yes (Integration) | Dev Team |
| Performance (NFR-001) | P2 (Medium) | Yes (k6) | Tech Lead |
| Records (HU-DOC) | P2 (Medium) | Manual | QA Team |