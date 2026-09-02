# Agile Team Conventions

> Defines how the team works through its development cycles. Agree on and sign off
> with the entire team before the first sprint. Update when the team decides to change something.

---

## Sprint structure

| Field | Value |
| :--- | :--- |
| **Duration** | 2 weeks |
| **Sprint start** | Monday |
| **Sprint end** | Friday of week 2 |
| **Current sprint** | Sprint 12 — August 17 to August 28, 2026 |
| **Estimated capacity** | 50 story points per sprint |

---

## Ceremonies

### Sprint Planning
- **When:** First day of the sprint — Monday at 09:00 AM
- **Duration:** Maximum 2 hours (1h per week of sprint)
- **Who:** Entire team (Android Developers, Backend/DevOps, and Product Owner)
- **Goal:** Select and commit to sprint user stories, break down into technical tasks
- **Output artifact:** Sprint Backlog updated in GitHub Issues

### Daily Stand-up
- **When:** Every day — 09:30 AM
- **Duration:** Maximum 15 minutes
- **Format:**
  1. What did I do yesterday?
  2. What will I do today?
  3. Is anything blocking me?
- **Rule:** Technical discussions happen after the daily, not during it

### Sprint Review
- **When:** Last day of the sprint — Friday at 03:00 PM
- **Duration:** Maximum 1 hour
- **Who:** Team + Product Owner (+ stakeholders if applicable)
- **Goal:** Show what was built (Demo de la App Android y nuevos endpoints del Backend) and collect feedback

### Sprint Retrospective
- **When:** Last day of the sprint — Friday at 04:15 PM (after the review)
- **Duration:** Maximum 45 minutes
- **Format:** "Start / Stop / Continue" (What went well / What to improve / Action commitments)
- **Rule:** Each retro produces at least 1 improvement action with an owner and due date (recorded in the `docs/retrospectives` folder of the repo)

### Backlog Refinement
- **When:** Wednesday of the second week — 11:00 AM
- **Duration:** Maximum 1 hour
- **Goal:** Detail and estimate user stories for the next sprint (prioritizing new features like Chat or AWS optimizations)
- **Exit criterion:** The user story meets the Definition of Ready (DoR)

---

## Estimation

### Scale
| Points | Meaning | Examples in Salud Activa |
| :--- | :--- | :--- |
| 1 | Trivial — hours | Update dependencies, fix Material 3 colors, or small README edits. |
| 2 | Small — 1 day | Create a simple Node.js endpoint or add a new icon/field in a Fragment. |
| 3 | Medium — 2–3 days | Implement a new flow (e.g., "Request Appointment") or a Terraform script. |
| 5 | Large — ~1 week | Full FCM integration, complex Room migrations, or AWS ECS setup. |
| 8 | Very large | A complete Chat system or complex Auth logic. **(Should be split)**. |
| 13 | Epic | Complete infrastructure migration or full app redesign. **(MUST be split)**. |

**Technique:** Planning Poker (via Fibonacci scale)
**Tool:** Planning Poker Online (integrated with GitHub Issues)

### Estimation rule
### Estimation Rules & Guidelines

- **Level Disagreement:** If there is a disagreement of **2+ levels** (e.g., a Backend dev says 3 but the Android dev says 8 due to complex ViewModels or Insets), the team must discuss technical constraints and edge cases before voting again.
- **Decomposition Policy:** If a story is estimated at **8 or 13 points**, it is considered too high-risk for a single person/pair. It **must be split** into smaller sub-tasks (e.g., separating the MongoDB schema design from the Retrofit interface implementation).
- **The "High-Fiber" Rule:** If an infrastructure task (Terraform/AWS) is estimated at 8+, it must be peer-reviewed by the whole team before entering the sprint to ensure no architectural bottlenecks.

---

## Backlog tool

**Tool:** [Jira / Linear / GitHub Projects / Trello]
**Board URL:** [URL]

### Board columns
| Column | Meaning | Specific Criteria for Salud Activa |
| :--- | :--- | :--- |
| **Backlog** | Pending refinement | Raw ideas or issues waiting for technical estimation and detail. |
| **Ready** | Ready to enter Sprint (DoR) | Story has clear requirements, UI mocks in Material 3, and defined API contracts. |
| **In Progress** | Active Development | Coding in Kotlin (Android), Node.js (Backend), or Terraform (Infra). |
| **In Review** | Pull Request / QA | Code is in PR. Requires 1+ peer review and passing CI builds (GitHub Actions). |
| **Done** | Completed (DoD) | Tested on physical device/emulator, documentation updated, and code merged to `main`. |

---

## Team velocity

| Sprint | Story points completed | Notes |
| :--- | :---: | :--- |
| **Sprint 9** | 42 | Implementation of AWS ECS infrastructure and basic JWT Auth. |
| **Sprint 10** | 55 | High productivity: FCM (Push Notifications) and Material 3 UI migration. |
| **Sprint 11** | 48 | Refactoring Room database and fixing Error 500 diagnostics. |
| **Average** | **48.3** | **Sustainable velocity for a full-stack Mobile/Cloud team.** |

---

## Related documents

### Governance & Control Documentation

- **Definition of Ready (DoR)** → [`00-governance/definition-of-ready.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/00-governance/definition-of-ready.md)
    *   *Requirement:* Clear API contracts (Node.js) + Material 3 UI Mocks + Acceptance Criteria.
- **Definition of Done (DoD)** → [`00-governance/definition-of-done.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/00-governance/definition-of-done.md)
    *   *Requirement:* Passed unit tests (JUnit/Mockito) + No memory leaks + Verified AWS deployment.
- **Risk Management** → [`15-project-control/risks.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/15-project-control/risks.md)
    *   *Key Risks:* Database connectivity (Error 500), AWS free tier limits, and App Store guidelines compliance.
- **Technical Debt Backlog** → [`15-project-control/tech-backlog.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/15-project-control/tech-backlog.md)
    *   *Tasks:* Optimize Room queries, refactor Terraform modules, and migrate remaining XML views to Jetpack Compose.