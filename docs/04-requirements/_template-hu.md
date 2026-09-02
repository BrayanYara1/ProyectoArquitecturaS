# User Stories Documentation - Salud Activa Project

This document contains the user story backlog for the MVP, following the technical documentation standard.

---

# HU-IAM-001: User Authentication (Login)

> **ID convention:** `HU-IAM-001` (IAM: Identity and Access Management)

## Story
**As** an application user
**I want** to enter my credentials (email and password)
**So that** I can access my personal medical data and manage my appointments securely.

## Acceptance criteria
- [ ] **AC1:** Given that the user is not logged in, when they enter a valid email and password, then the system must redirect them to the "HomeFragment" and securely store the JWT.
- [ ] **AC2:** Given that the user is already logged in, when they open the application, then the system must validate the token and bypass the Login screen automatically.
- [ ] **AC3:** **[Error Scenario]** Given that the user enters an incorrect password, when they press "Login", then the system must display the message "Invalid credentials" and remain on the Login screen.

## Technical notes
**Responsible service(s):** Backend (Auth routes), `AuthRepository.kt` (Android)
**Endpoint(s) implemented:** `POST /api/auth/login`
**Required permissions:** N/A (Public)

## Estimation and priority
| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | High |
| Target sprint | Sprint 1 |
| Dependencies | N/A |

---

# HU-SCHED-001: Medical Appointment Request

> **ID convention:** `HU-SCHED-001` (SCHED: Scheduling)

## Story
**As** a patient
**I want** to select a specialty and an available date
**So that** I can schedule a medical appointment without needing to call by phone.

## Acceptance criteria
- [ ] **AC1:** Given that the user is in "SolicitarTurnoFragment", when they select a specialty, doctor, and date, then they should view a summary of the appointment before confirming.
- [ ] **AC2:** Given that the user confirms the appointment, when there is an internet connection, then the system must synchronize the appointment with the server immediately.
- [ ] **AC3:** **[Offline Mode]** Given that the user has no connection, when they confirm the appointment, then the system must save it locally (Room) and mark it for later synchronization via `SyncWorker`.

## Technical notes
**Responsible service(s):** Backend (Turnos routes), `TurnoRepository.kt`
**Endpoint(s) implemented:** `POST /api/turnos`, `GET /api/turnos`
**Events generated:** Local reminder notification via `ReminderManager.kt`
**Required permissions:** Role: USER (JWT)

## Estimation and priority
| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 1 |
| Dependencies | HU-IAM-001 |

---

# HU-MED-001: Medication Registration and Scanning

> **ID convention:** `HU-MED-001` (MED: Medications)

## Story
**As** a patient under treatment
**I want** to scan a medication's barcode or enter it manually
**So that** I can keep track of my medicine inventory and receive intake alerts.

## Acceptance criteria
- [ ] **AC1:** Given that the user uses the camera, when they scan a valid barcode, then the system must auto-complete the name and presentation of the medication using `MedicineScanner.kt`.
- [ ] **AC2:** Given that the medication has been registered, when the configured time arrives, then the system must trigger a high-priority notification via `NotificationHelper.kt`.

## Technical notes
**Responsible service(s):** Android App (Local storage)
**Endpoint(s) implemented:** `POST /api/medicamentos` (Optional for backup)
**Required permissions:** CAMERA, POST_NOTIFICATIONS

## Estimation and priority
| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Medium |
| Target sprint | Sprint 2 |
| Dependencies | HU-IAM-001 |

---

# HU-DOC-001: Medical Records Management (PDF)

> **ID convention:** `HU-DOC-001` (DOC: Documents)

## Story
**As** a patient
**I want** to upload and view my medical study results in PDF or image format
**So that** I have all my clinical history digitized and accessible in one place.

## Acceptance criteria
- [ ] **AC1:** Given that the user selects a file, when they press "Upload", then the system must process the document and show a thumbnail in the study list.
- [ ] **AC2:** Given that the study is saved, when the user clicks on it, then the system must open the internal PDF viewer (`PdfGenerator.kt` / Viewers).

## Technical notes
**Responsible service(s):** `EstudioRepository.kt`, `ImageStorageManager.kt`
**Endpoint(s) implemented:** N/A (Local/Cloud storage)
**Required permissions:** READ_EXTERNAL_STORAGE / READ_MEDIA_VISUAL_USER_SELECTED

## Estimation and priority
| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Medium |
| Target sprint | Sprint 2 |
| Dependencies | HU-IAM-001 |