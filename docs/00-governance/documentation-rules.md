# Documentation Rules

> These rules determine how documentation is written, organized, and maintained in this project.
> Documentation that does not follow these rules may be rejected in code review.

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

### Estimation Rules
- **Disagreement:** If disagreement is 2+ levels (e.g., 3 vs 8), discuss technical debt and logic before re-voting.
- **Decomposition:** Stories with 8+ points must be split into sub-tasks (e.g., Backend logic vs Android UI).

### Velocity History
| Sprint | Points Completed | Notes |
| :--- | :---: | :--- |
| **Sprint 9** | 42 | AWS ECS Setup & basic JWT Auth. |
| **Sprint 10** | 55 | FCM & Material 3 migration. |
| **Sprint 11** | 48 | Room refactor & Error 500 diagnostics. |
| **Average** | **48.3** | **Sustainable velocity for this stack.** |

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

## Documentation & Governance Strategy

- **Language:** Code/Docs in **English**, UI/Errors in **Spanish**.
- **Governance:** [`docs/00-governance/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/00-governance/) (DoR, DoD, Git Rules).
- **Control:** [`docs/15-project-control/`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/15-project-control/) (Risks, Tech Debt).
- **Rule:** Documentation is code. If it's not up to date, the PR is rejected.

---

## Workflow Board

| Column | Meaning |
| :--- | :--- |
| **Backlog** | Pending refinement. Raw issues. |
| **Ready** | Meets **DoR**. Ready for current/next sprint. |
| **In Progress** | Active coding in Kotlin, Node.js, or Terraform. |
| **In Review** | PR open. Waiting for review and CI feedback. |
| **Done** | Meets **DoD**. Deployed and verified. |