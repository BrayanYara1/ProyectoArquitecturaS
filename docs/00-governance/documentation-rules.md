# Documentation Rules & Architectural Norms

> These rules determine how documentation, repositories, microservices, and user interfaces are written, organized, and named in this project.
> Non-compliance blocks PR merges and code reviews.

---

# Salud Activa - Sprint Management Framework

Este documento define la metodología de trabajo para el desarrollo de la App Android, el Backend y la infraestructura en AWS.

---

## Sprint Configuration

| Field | Value |
| :--- | :--- |
| **Duration** | 2 weeks |
| **Sprint start** | Monday |
| **Sprint end** | Friday of week 2 |
| **Current sprint** | Sprint 12 — August 17 to August 28, 2026 |
| **Estimated capacity** | 50 story points per sprint |

---

## Agile Ceremonies

- **Sprint Planning:** Monday of week 1 (09:00 AM). Max 2h. Update **GitHub Issues**.
- **Daily Stand-up:** Every day (09:30 AM). Max 15 min. (Yesterday? Today? Blockers?).
- **Backlog Refinement:** Wednesday of week 2 (11:00 AM). Max 1h. Detail next stories.
- **Sprint Review:** Friday of week 2 (03:00 PM). Max 1h. Demo App Android & API.
- **Sprint Retrospective:** Friday of week 2 (04:15 PM). Max 45 min. Improvements.

---

## Estimation & Velocity

### Story Point Scale
| Points | Meaning | Examples in Salud Activa |
| :--- | :---: | :--- |
| **1** | Trivial | Update dependencies, fix Material 3 colors, text changes. |
| **2** | Small | New simple Node.js endpoint, new field in a Fragment. |
| **3** | Medium | New flow (Request Appointment), Terraform scripts. |
| **5** | Large | FCM integration, Room migrations, AWS ECS setup. |
| **8** | V. Large | Complete Chat system. **(MUST be split)**. |
| **13** | Epic | Infrastructure migration or app redesign. **(MUST be split)**. |

---

## Definition of Ready (DoR)

*Before moving a User Story to "Ready for Sprint":*
- [ ] **Format:** "As [role], I want [action], so that [benefit]".
- [ ] **Criteria:** At least 2 "Given / When / Then" scenarios.
- [ ] **Dependencies:** API contracts (OpenAPI) defined; AWS resources identified.
- [ ] **UI/UX:** Material 3 mockups available and approved.
- [ ] **Estimation:** Story points assigned by the whole team.

---

## Definition of Done (DoD)

*Before closing a User Story:*
- [ ] **Code:** Peer-reviewed, follows Linting, and meets all AC.
- [ ] **Tests:** Unit tests pass (JUnit/Jest); no coverage regression.
- [ ] **Integration:** Verified with FCM, Room, and MongoDB.
- [ ] **Deployment:** Green CI/CD pipeline (GitHub Actions); deployed to Staging in AWS.
- [ ] **Documentation:** `README.md` updated; ADR created if architecture changed.

---

## 4.5.4 Prohibited Forms & Architectural Naming Norms

> **Expressed Restrictions (Section 4.5.4):**
> The following naming conventions and patterns are strictly prohibited in the project:

| Prohibited Form | Reason & Rule | Compliance in Salud Activa |
| :--- | :--- | :--- |
| `ms-<domain>`, `svc-<domain>` | Repository prefix must be project abbreviation (`abbr`), not component type | Repositories use project abbreviation prefix (`ProyectoDistribuidos2026`, `ProyectoArquitecturaS`). |
| Single centralized migration repository | Each database owns its own schema structure and migrations (Rule 7.2) | Migrations and database schemas are decentralized per domain service. |
| `<domain>_schema` inside shared database | Separate schemas in a single database do not fulfill Database-per-Domain (Rule 7.1) | Each domain (`Auth`, `Turnos`, `Medicamentos`, `Estudios`, `Chat`) uses its own database instance, connection, and volume. |
| Portals named by role or profile | UI is organized by domain or channel, not by user role | User interfaces are organized by channel: Mobile App (`app`) and Web Portal (`portal` / `web`). |
| `<abbr>-<domain>-front` | Domain interface is named by channel: `portal` or `app` | Interfaces use channel naming (`app` for Android, `portal` / `web` for Web). |

---

## Workflow Board

| Column | Meaning |
| :--- | :--- |
| **Backlog** | Pending refinement. Raw issues. |
| **Ready** | Meets **DoR**. Ready for current/next sprint. |
| **In Progress** | Active coding in Kotlin, Node.js, or Terraform. |
| **In Review** | PR open. Waiting for review and CI feedback. |
| **Done** | Meets **DoD**. Deployed and verified. |
