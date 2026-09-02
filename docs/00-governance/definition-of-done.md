# Definition of Done (DoD) - Salud Activa

This checklist is mandatory for closing any User Story or Issue. It ensures that the software increment is of high quality and production-ready.

## Mandatory Checklist

### Code
- [x] **Acceptance Criteria:** The code implements all acceptance criteria defined in the User Story.
- [x] **Peer Review:** The code has been reviewed and approved by at least 1 team member (PR approval).
- [x] **Standards:** The code follows project standards (Linting for Kotlin and Node.js passed).
- [x] **No Hidden Debt:** No technical debt has been introduced without registering it in `docs/15-project-control/tech-backlog.md`.

### Tests
- [x] **Unit Tests:** Unit tests are written for new business logic (JUnit/Mockito for Android, Jest for Backend).
- [x] **Coverage:** Test coverage does not decrease compared to the project baseline.
- [x] **CI Pipeline:** All tests pass both locally and in GitHub Actions.
- [x] **Manual Check:** Acceptance criteria have been manually verified on both an emulator and a physical device.

### Integration
- [x] **Integration Tests:** Changes do not break other services (FCM notifications, Room persistence).
- [x] **API Contract:** If there were API changes, the OpenAPI/Swagger contract in `docs/07-api/contracts/` has been updated.
- [x] **Data Model:** If the data model changed, the documentation for the MongoDB or Room schema has been updated.
- [x] **AWS/Terraform:** If infrastructure changes were made, the `.tf` files have been validated and applied to the test environment.

### Deployment
- [x] **Conflict Free:** The code is mergeable into the main branch (no conflicts).
- [x] **Green CI/CD:** The automated deployment pipeline to AWS ECS/Fargate is green.
- [x] **Staging:** The code has been successfully deployed to the staging/test environment.
- [x] **Smoke Test:** Critical functionalities (Login, Request Appointment) work correctly in staging.

### Documentation
- [x] **README:** The service's `README.md` (App or Backend) has been updated if the public interface changed.
- [x] **ADR:** If a significant technical decision was made, an Architecture Decision Record (ADR) has been created or updated.

---

## Allowed Exceptions

Any exception must be explicitly approved by the Tech Lead:
- **E2E tests omitted:** Due to temporary environment limitations (the risk must be documented).
- **Deferred documentation:** For emergency delivery (requires the immediate creation of a ticket in `tech-backlog.md`).

---

## What is NOT a Done criterion

- **"The code is on my machine":** It must be in the remote repository.
- **"It works on my local environment":** It must work in the AWS staging environment.
- **"The PM/PO approved it":** That is the product acceptance, not the technical fulfillment of this DoD.
