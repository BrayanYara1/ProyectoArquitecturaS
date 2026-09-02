# Definition of Ready (DoR) - Salud Activa

Before moving a User Story to "Ready for Sprint," the team must verify compliance with the following points during Refinement sessions.

## DoR Checklist

### Clarity and Value
- [x] **Standard Format:** The story follows the format: **As a [specific role], I want [action], so that [benefit].**
- [x] **Specific Role:** No generic "user" is used. Example: "As an Authenticated Patient" or "As a Clinic Administrator."
- [x] **Verifiable Benefit:** The business value is clear and measurable.

### Acceptance Criteria (AC)
- [x] **Gherkin Format:** There are at least 2 criteria written in **Given / When / Then** format.
- [x] **Scenarios:** The "Happy Path" and at least the main error cases (e.g., handling Error 500 or network loss) are covered.
- [x] **Testable:** It is possible to write an automated test (Unit or UI) for each criterion.
- [x] **Unambiguous:** Terms like "fast" or "intuitive" are avoided. Response times or specific flows are defined.

### Dependencies and Blockers
- [x] **Synchronization:** Dependencies between the Android App and the Backend have been identified.
- [x] **Resolution:** External dependencies (e.g., Firebase FCM access or AWS credentials) are resolved or have an action plan.
- [x] **Prerequisites:** If it depends on another story, that story is already in "Done" status.

### Estimation and Sizing
- [x] **Consensus:** The team has estimated the story in Story Points using Planning Poker.
- [x] **Feasibility:** The story fits within a 2-week sprint.
- [x] **Division:** If the story was estimated at 8+ points, it has been split into smaller tasks.

### Technical Readiness (Critical for Salud Activa)
- [x] **API Contract:** Necessary Node.js endpoints are defined (OpenAPI/Swagger).
- [x] **UI Design:** Material 3 mockups are available and approved.
- [x] **Data Model:** The impact on the database (Room in Android or MongoDB in Backend) has been defined.
- [x] **Environment:** Necessary AWS infrastructure resources are identified.

### Non-Functional Requirements
- [x] **Security:** Specifies if it requires new permissions, JWT validation, or encryption.
- [x] **Observability:** Necessary logs or metrics to monitor the feature in production are defined.

---

## Common Reasons a Story is NOT Ready

| Problem | Action |
| :--- | :--- |
| **Vague requirements** | Schedule a 30-min refinement session with the PO to detail the flow. |
| **Missing API contract** | The Backend team must define the response JSON before starting Android development. |
| **Story too large (> 8 SP)** | Split by components (e.g., Task 1: Backend/DB, Task 2: Android UI). |
| **No AWS/Firebase access** | The DevOps lead must generate credentials before Monday Planning starts. |
| **Incomplete mockups** | Do not start Compose UI development until colors and states are defined. |

---

## DoR vs DoD

| Feature | Definition of Ready (DoR) | Definition of Done (DoD) |
| :--- | :--- | :--- |
| **When does it occur?** | Before starting the story (Refinement/Planning). | Upon finishing the story (Review). |
| **Who verifies?** | The entire team and the Product Owner. | The technical team and the QA/Lead. |
| **Purpose** | Ensure the team can work without blockers. | Ensure the code is high quality and deployable. |

---

## References and Correlations

- **Full DoD:** [`docs/00-governance/definition-of-done.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/00-governance/definition-of-done.md)
- **User Story Template:** [`docs/04-requirements/_template-hu.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/04-requirements/_template-hu.md)
- **Story Backlog:** [`docs/04-requirements/user-stories.md`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/04-requirements/user-stories.md)
