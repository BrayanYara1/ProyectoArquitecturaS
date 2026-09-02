## How to use this matrix

text
Requirement → HU → Test Case → Implementation → Service

If a requirement has no HU: it is not planned
If a HU has no test case: it has no completeness criterion
If a test case has no implementation: there is test technical debt
If there is code without an HU: possible gold-plating or bug introduced without a story


---

## FR → HU → Test → Service matrix

| FR ID | FR Description | HU(s) | Tests that verify it | Service | Status |
|-------|---------------|-------|---------------------|---------|--------|
| FR-001 | System allows user registration and login | HU-001 | Authentication tests | Authentication module | 🟡 In progress |
| FR-002 | System allows account verification | HU-002 | Account verification tests | Authentication module | 🟡 In progress |
| FR-003 | System allows biometric authentication | HU-003 | Biometric authentication tests | Authentication module | 🟡 In progress |
| FR-004 | System allows users to consult medical appointments | HU-004 | Appointment consultation tests | Appointment management module | 🟡 In progress |
| FR-005 | System allows users to view appointment details | HU-005 | Appointment detail tests | Appointment management module | 🟡 In progress |
| FR-006 | System allows users to request new medical appointments | HU-006 | Appointment request tests | Appointment management module | 🟡 In progress |
| FR-007 | System allows medication tracking | HU-007 | Medication tracking tests | Health and medication module | 🟡 In progress |
| FR-008 | System allows users to record symptoms | HU-008 | Symptom registration tests | Health and medication module | 🟡 In progress |
| FR-009 | System allows users to view health statistics | HU-009 | Health statistics tests | Health and medication module | 🟡 In progress |
| FR-010 | System allows users to store and view medical studies | HU-010 | Medical studies tests | Medical studies module | 🟡 In progress |
| FR-011 | System allows the possible scanning of medical studies using ML Kit | HU-011 | ML Kit scanning tests | Medical studies module | 🔴 Pending |
| FR-012 | System provides an integrated chat system | HU-012 | Chat functionality tests | Communication module | 🟡 In progress |
| FR-013 | System provides an achievements system | HU-013 | Achievement functionality tests | Gamification module | 🟡 In progress |
| FR-014 | System keeps locally available information to support offline operation | HU-014 | Offline operation and synchronization tests | Offline / synchronization module | 🟡 In progress |

---

## NFR → Validation matrix

| NFR ID | Description | How it is validated | Tool | Status |
|--------|-------------|---------------------|------|--------|
| NFR-001 | Interface provides consistent loading and empty states | Verify loading and empty states in the application UI | Facebook Shimmer / UI Testing | 🟡 In progress |
| NFR-002 | Application supports devices from SDK 24 and is optimized for Android 15 (SDK 35) | Test application compatibility on the supported Android versions | Android SDK / UI Testing | 🟡 In progress |
| NFR-003 | Sensitive data uses protection mechanisms such as Security Crypto and biometric authentication | Verify protected storage and biometric authentication | Security Crypto / Biometric API | 🟡 In progress |
| NFR-004 | Local-data functionalities remain available without connection when information is stored in Room | Disable network connection and verify access to locally stored information | Room / OfflineCacheManager | 🟡 In progress |
| NFR-005 | Architecture maintains separation of responsibilities using Clean Architecture, MVVM and Repository Pattern | Review architecture and code organization | Code Review | 🟡 In progress |
| NFR-006 | Application uses Crashlytics and Analytics for diagnostics and analysis | Verify error reporting and analytics events | Firebase Crashlytics / Analytics | 🟡 In progress |
| NFR-007 | Shimmer and Lottie contribute to a smoother loading and navigation experience | Verify loading states and animations during navigation | Facebook Shimmer / Lottie | 🟡 In progress |

---

## Inverse traceability: HU → FR

| HU | Title | FR(s) it implements | Sprint |
|----|-------|---------------------|--------|
| HU-001 | User registration and login | FR-001 | Phase 1 |
| HU-002 | Account verification | FR-002 | Phase 1 |
| HU-003 | Biometric authentication | FR-003 | Phase 1 |
| HU-004 | Medical appointment consultation | FR-004 | Phase 2 |
| HU-005 | Appointment detail | FR-005 | Phase 2 |
| HU-006 | Medical appointment request | FR-006 | Phase 2 |
| HU-007 | Medication tracking | FR-007 | Phase 3 |
| HU-008 | Symptom registration | FR-008 | Phase 3 |
| HU-009 | Health statistics | FR-009 | Phase 3 |
| HU-010 | Medical studies management | FR-010 | Phase 4 |
| HU-011 | Medical studies scanning | FR-011 | Phase 4 |
| HU-012 | Integrated chat | FR-012 | Phase 5 |
| HU-013 | Achievements system | FR-013 | Phase 5 |
| HU-014 | Offline information management | FR-014 | Phase 6 |

---

## Status legend

| Status | Meaning |
|--------|---------|
| ✅ Done | Implemented, tested, and in production |
| 🟡 In progress | Under development in the current sprint |
| 🔴 Pending | In the backlog, not started |
| ⏸️ Blocked | Has an external blocker |
| ❌ Cancelled | Removed from scope |

---

## Identified gaps (requirements without coverage)

> This section is updated automatically or manually when reviewing the matrix.
> A gap is: an FR without an HU, or an HU without a test, or a test without implementation.

| Gap type | Description | Required action | Owner | Date |
|----------|-------------|----------------|-------|------|
| HU without formal test | The proposed HUs do not yet have formally defined test cases in the PDR | Define and document acceptance tests | QA / Dev | [date] |
| Test without implementation evidence | The PDR does not provide evidence of executed tests | Implement and execute the corresponding tests | QA / Dev | [date] |
| FR-011 definition | Medical study scanning using ML Kit is described as a possible functionality | Confirm whether it is part of the final scope | Product Owner / Dev | [date] |
| Offline synchronization | The synchronization strategy for OfflineCacheManager is not fully defined | Define synchronization and conflict-resolution strategy | Dev | [date] |

---

## How to maintain this matrix

1. When an HU is created: add the row in the FR → HU → Test → Service section
2. When a test is written: note the test in the "Tests that verify it" column
3. When an HU is completed: change the status to ✅
4. At each Sprint Planning: review gaps and assign actions

---

## Correlations

- User Stories → 04-requirements/user-stories.md
- Non-Functional Requirements → 04-requirements/non-functional.md
- Testing strategy → 11-quality/testing-strategy.md
- DoD that determines when an HU is Done → 00-governance/definition-of-done.md