# Git Conventions

> **Read this document before making your first commit on the project.**

# Salud Activa - Engineering & Management Handbook

This document serves as the single source of truth for the management and technical governance of the **GestionTurnosApp** project. It defines our operational processes, quality benchmarks, and the maintenance standards for our AWS infrastructure and Android/Node.js codebase.

---

## 1. Sprint Management Framework

### Cycle Configuration
| Field | Value |
| :--- | :--- |
| **Duration** | 2 weeks |
| **Sprint Start** | Monday 09:00 AM |
| **Sprint End** | Friday (Week 2) 05:00 PM |
| **Current Sprint** | Sprint 12 — August 17 to August 28, 2026 |
| **Estimated Capacity** | 50 Story Points per sprint |

### Agile Ceremonies
- **Sprint Planning:** Monday (Week 1) — 09:00 AM. Duration: 2h. Focus: Task selection and technical breakdown.
- **Daily Stand-up:** Every day — 09:30 AM. Duration: 15 min. (What did I do? What will I do? Blockers?).
- **Backlog Refinement:** Wednesday (Week 2) — 11:00 AM. Duration: 1h. Detail upcoming Stories and API contracts.
- **Sprint Review:** Friday (Week 2) — 03:00 PM. Duration: 1h. Functional demo (App + Backend).
- **Sprint Retrospective:** Friday (Week 2) — 04:15 PM. Duration: 45 min. Improvement actions with owner and due date.

---

## 2. Estimation & Velocity

### Story Point Scale (Fibonacci)
| Points | Meaning | Examples in Salud Activa |
| :--- | :--- | :--- |
| **1** | Trivial | Text changes, Material 3 colors, updating dependencies. |
| **2** | Small | New simple Node.js endpoint, new field in Room. |
| **3** | Medium | Complete "Request Appointment" flow, basic Terraform scripts. |
| **5** | Large | FCM integration, complex DB migrations, AWS ECS setup. |
| **8** | Very Large | Real-time Chat system. **(Must be split)**. |
| **13** | Epic | Full App redesign or Cloud migration. **(Forbidden to enter the sprint)**. |

### Velocity History
- **Sprint 9:** 42 pts (AWS Infrastructure Setup)
- **Sprint 10:** 55 pts (FCM & UI Modernization)
- **Sprint 11:** 48 pts (Room Refactor & 500 Diagnostics)
- **Average:** **48.3 pts** (Sustainable velocity).

---

## 3. Quality Standards (DoR & DoD)

### Definition of Ready (DoR) - *Before starting*
- [ ] Story follows the format: **As a [role], I want [action], so that [benefit]**.
- [ ] Clear Acceptance Criteria in **Given / When / Then** format.
- [ ] **API Contract defined** (OpenAPI) and UI Mockups approved.
- [ ] Technical dependencies (AWS/Firebase) identified and resolved.

### Definition of Done (DoD) - *Before finishing*
- [ ] **Code:** Peer-reviewed, no hidden technical debt, and approved in a PR.
- [ ] **Tests:** Green unit tests (JUnit/Jest). Stable coverage.
- [ ] **Integration:** Tested with local persistence (Room) and external services (FCM).
- [ ] **Deployment:** Green CI/CD. Deployed to **Staging (AWS)** and verified with Smoke Tests.
- [ ] **Documentation:** README and diagrams updated. ADR created if applicable.

---

## 4. Documentation Strategy

### Language and Conventions
- **Source Code and Technical Documentation:** English.
- **User Interface (UI) and Errors:** Spanish (localized in `strings.xml`).
- **Commits:** Conventional Commits (e.g., `feat(auth): add JWT support`).

### Repository Structure (`/docs`)
- [`00-governance/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/00-governance/): Rules, DoD, DoR.
- [`02-domain/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/02-domain/): Medical business logic.
- [`05-architecture/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/05-architecture/): ADRs and C4 diagrams.
- [`07-api/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/07-api/): OpenAPI contracts.
- [`13-operations/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/13-operations/): AWS and Terraform guides.
- [`15-project-control/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/15-project-control/): Risks and Technical Debt.

---

## 5. Git & Workflow Strategy

### Branching
- **`main`**: Production. Always stable.
- **`dev`**: Continuous Integration. Development base.
- **`feat/[desc]`**: New feature branches.
- **`fix/[desc]`**: Bugfix branches.
- **`chore/[desc]`**: Maintenance or infrastructure tasks.

### Merge Policy
- **Features → Dev:** Squash and Merge (Clean history).
- **Dev → Main:** Merge Commit (Release traceability).
- **PR Size:** Maximum **400 lines**. If larger, split it to facilitate review.

---

## What is NOT acceptable (Anti-patterns)
- **"It works on my machine":** It must work on AWS Staging.
- **Direct commits** to `dev` or `main`.
- **User stories larger than 8 points** in the sprint.
- **Mixing English and Spanish** in code comments.