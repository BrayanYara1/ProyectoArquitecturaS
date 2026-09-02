# Domain Map — Bounded Contexts

> **What to fill in here:** The domain map is the central DDD (Domain-Driven Design) artifact.
> It defines the system's boundaries and how they relate to each other.
> Build it first with the team and domain experts in an Event Storming session.

## Before filling in this document: Event Storming

**Event Storming** is a collaborative workshop for modeling the domain before writing code.
It lasts 2–4 hours with the whole team (dev + PO + business expert).

**Materials:** Long wall, 4-color sticky notes, markers.

**Standard colors:**
| Color | Represents | Example |
|-------|-----------|---------|
| 🟠 Orange | **Domain events** (something that happened, past tense) | `AppointmentScheduled`, `PaymentReceived` |
| 🔵 Blue | **Commands** (action that triggers the event) | `ScheduleAppointment`, `ProcessPayment` |
| 🟡 Yellow | **Actors** (who executes the command) | `Patient`, `Doctor`, `Admin` |
| 🩷 Pink | **External systems** or integration points | `Payment Gateway`, `Email SMTP` |

**Session steps:**
1. (30 min) Post all events that occur in the business, in chronological order, on the wall
2. (30 min) Identify which command or actor triggers each event
3. (45 min) Group related events — each group is a candidate Bounded Context
4. (30 min) Draw relationships between Bounded Contexts (who depends on whom)
5. (30 min) Discuss the resulting map and agree on names

**Result:** The session output directly feeds the 3 documents in `02-domain/`:
- Identified events → `domain-events.md`
- Entities and their rules → `entities-and-rules.md`
- Bounded Contexts and their map → this document

---

---

## 1. Domain overview

GestionTurnosApp manages personal health information in one place. Users can manage their medical appointments, track medications, record symptoms, view health statistics and store medical studies. The application also includes communication, achievements and secure access features to support the user's health management experience.

---

## 2. Identified Bounded Contexts

### Bounded Context: Identity and Access Management

| Field | Value |
|---|---|
| *Name* | Identity and Access Management |
| *Responsibility* | Manage user registration, login, account verification and secure access to the application. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | authentication-service |
| *Database* | Room Database / Remote API |
| *Ubiquitous Language* | User, Account, Credentials, Registration, Login, Verification, Biometric Authentication |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| User | Person with an account who can access the application. | Yes — in Health Management the user is a patient. |
| Account | Set of identity and access data associated with a user. | No |
| Credentials | Information used to authenticate a user. | No |
| Verification | Process of confirming an account. | Yes — medical verification has a different meaning. |

---

### Bounded Context: Appointment Management

| Field | Value |
|---|---|
| *Name* | Appointment Management |
| *Responsibility* | Manage the lifecycle of medical appointments, including listing, viewing details and requesting new appointments. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | appointment-service |
| *Database* | Room Database / Remote API |
| *Ubiquitous Language* | Appointment, Request, Schedule, Detail, Patient, Medical Appointment |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| Appointment | A medical appointment managed by the user. | No |
| Request | Action of requesting a new medical appointment. | Yes — a request in another context may mean a system request. |
| Schedule | The date and time information associated with an appointment. | Yes — in Medication Management it may refer to medication schedules. |
| Patient | User who manages medical appointments. | Yes — in Identity the person is referred to as User. |

---

### Bounded Context: Personal Health Management

| Field | Value |
|---|---|
| *Name* | Personal Health Management |
| *Responsibility* | Manage medication tracking, symptom registration and health statistics. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | health-service, medication-service, health-statistics-service |
| *Database* | Room Database |
| *Ubiquitous Language* | Medication, Symptom, Health Record, Statistic, Chart, Tracking |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| Medication | Medicine registered and tracked by the user. | No |
| Symptom | Health condition or sign recorded by the user. | No |
| Tracking | Continuous monitoring of medication or symptoms. | Yes — in another context it may refer to delivery tracking. |
| Statistic | Health information summarized for visualization. | No |

---

### Bounded Context: Medical Studies Management

| Field | Value |
|---|---|
| *Name* | Medical Studies Management |
| *Responsibility* | Store and visualize medical studies and results, with possible text recognition support. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | medical-studies-service |
| *Database* | Room Database / Local File Storage |
| *Ubiquitous Language* | Medical Study, Result, File, Image, Storage, Text Recognition |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| Medical Study | Medical document or result stored by the user. | No |
| Result | Information contained in a medical study. | Yes — in Statistics it may mean a calculated result. |
| File | Digital resource containing a medical study. | No |
| Text Recognition | Extraction or recognition of text from a study. | No |

---

### Bounded Context: Communication

| Field | Value |
|---|---|
| *Name* | Communication |
| *Responsibility* | Provide an integrated chat system for communication within the application. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | chat-service |
| *Database* | Room Database / Remote API |
| *Ubiquitous Language* | Chat, Message, Conversation, Sender, Receiver |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| Chat | Communication channel integrated into the application. | No |
| Message | Information sent through a conversation. | Yes — notification messages have a different purpose. |
| Conversation | Group or sequence of chat messages. | No |

---

### Bounded Context: Gamification

| Field | Value |
|---|---|
| *Name* | Gamification |
| *Responsibility* | Manage achievements that encourage continued use of the application. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | gamification-service |
| *Database* | Room Database |
| *Ubiquitous Language* | Achievement, Progress, Reward, Goal |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| Achievement | Goal or accomplishment obtained by the user. | No |
| Progress | Advancement toward obtaining an achievement. | Yes — health progress has a different meaning. |
| Reward | Recognition associated with an achievement. | No |

---

### Bounded Context: Offline Data Management

| Field | Value |
|---|---|
| *Name* | Offline Data Management |
| *Responsibility* | Maintain local data availability and support application functionality without an internet connection. |
| *Owning team* | GestionTurnosApp Development Team |
| *Microservice(s)* | offline-data-service |
| *Database* | Room Database |
| *Ubiquitous Language* | Offline, Cache, Synchronization, Local Data, Remote Data, DAO |

*Context-specific terms (Ubiquitous Language):*

| Term | Meaning in THIS context | Different in another context? |
|---|---|---|
| Offline | Application operation without an active internet connection. | No |
| Cache | Locally stored data used to maintain availability. | No |
| Synchronization | Process of reconciling local and remote information. | No |
| Local Data | Information persisted on the user's device. | No |

---

3. Context Map

                         ┌──────────────────────────────┐
                         │ Identity and Access          │
                         │ Management                   │
                         │ Domain: Users and access     │
                         └──────────────┬───────────────┘
                                        │ U → D
                                        ▼
┌──────────────────────────────┐  ┌──────────────────────────────┐
│ Appointment Management       │  │ Personal Health Management   │
│ Domain: Medical appointments │  │ Domain: Medications,         │
│                              │  │ symptoms and statistics      │
└──────────────┬───────────────┘  └──────────────┬───────────────┘
               │ U → D                           │ U → D
               │                                  │
               ▼                                  ▼
┌──────────────────────────────┐  ┌──────────────────────────────┐
│ Medical Studies Management   │  │ Gamification                 │
│ Domain: Studies and results  │  │ Domain: Achievements         │
└──────────────────────────────┘  └──────────────────────────────┘
               ▲
               │
               │ U → D
               │
┌──────────────┴───────────────┐
│ Offline Data Management      │
│ Domain: Local persistence    │
│ and synchronization          │
└──────────────────────────────┘

        ┌──────────────────────────────┐
        │ Communication                │
        │ Domain: Integrated chat      │
        └──────────────────────────────┘

### Context relationship types

| Type | Symbol | Description | Example |
|---|---|---|---|
| *Upstream → Downstream* | U → D | Upstream provides information or capabilities that downstream consumes. | Identity → Appointments |
| *Anti-Corruption Layer* | ACL | A context translates an external model to protect its internal model. | Offline Data → Remote APIs |
| *Open Host Service* | OHS | A context exposes a protocol that other contexts can consume. | Remote APIs |
| *Published Language* | PL | Contexts communicate using an explicit shared contract. | REST API contracts |

### Relationships table

| Context A | Relationship | Context B | Communication channel | Contract |
|---|---|---|---|---|
| Identity and Access Management | U → D | Appointment Management | Internal interface | User identity |
| Identity and Access Management | U → D | Personal Health Management | Internal interface | User identity |
| Appointment Management | U → D | Offline Data Management | Repository / local persistence | Appointment data model |
| Personal Health Management | U → D | Offline Data Management | Repository / local persistence | Health data model |
| Medical Studies Management | U → D | Offline Data Management | Local storage | Study data and files |
| Personal Health Management | U → D | Gamification | Internal interface | User activity and progress |
| Offline Data Management | ACL | Remote APIs | Retrofit / REST API | External API contract |
| Medical Studies Management | CONF | ML Kit | SDK | ML Kit model |
| Identity and Access Management | CONF | Android Biometric API | Android API | Biometric API |

---

## 4. Core Domain, Supporting, Generic

### Classification of this project's bounded contexts

| Bounded Context | Type | Justification |
|---|---|---|
| Appointment Management | Core | It manages the main business functionality of requesting, listing and viewing medical appointments. |
| Personal Health Management | Core | It provides the main personal health management capabilities through medication tracking, symptom records and statistics. |
| Medical Studies Management | Supporting | It complements personal health management by storing and visualizing medical studies and results. |
| Offline Data Management | Supporting | It supports the application by providing local persistence and resilient operation without connectivity. |
| Communication | Supporting | Integrated chat complements the application's main health management functionality. |
| Gamification | Supporting | Achievements encourage continued use but are not the primary purpose of the application. |
| Identity and Access Management | Generic | Authentication and secure access are standard capabilities supported by existing Android technologies. |

---

## 5. Modeling decisions

### How were these decisions made?

The domain map was derived from the Preliminary Design Review (PDR) by grouping the application's identified functional modules according to their business responsibility. The PDR identifies authentication, appointment management, medication and health tracking, medical studies, communication and gamification as the main functional areas. Offline support was also modeled as a supporting bounded context because the PDR identifies local persistence and OfflineCacheManager as key elements of the application's architecture.

- *Event Storming session:* Not formally conducted; the initial map was derived from the PDR and must be validated with the project team.
- *Tool used:* PDR document analysis and collaborative team review.
- *Map iterations:* v1 — Initial domain map derived from the PDR.

### Key decisions and discarded alternatives

| Decision | Discarded alternative | Reason |
|---|---|---|
| Separate Appointment Management from Personal Health Management | Use one general Health context | Appointment logic and personal health tracking represent different business responsibilities. |
| Keep Medication, Symptoms and Statistics in Personal Health Management | Create one context for each feature | They are closely related aspects of personal health tracking and can share the same business language. |
| Model Medical Studies separately | Include studies inside Personal Health Management | Medical studies involve document and file storage concerns that are distinct from medication and symptom tracking. |
| Model Offline Data Management as a supporting context | Treat offline behavior as only a technical detail | Offline functionality is a key capability of the application and affects multiple functional areas. |
| Keep Gamification separate | Include achievements inside Personal Health Management | Achievement rules have a different responsibility from health data management. |
| Keep Communication separate | Include chat inside Appointment Management | Chat is an independent communication capability and is not limited to appointment operations. |
| Treat Authentication as Generic | Make authentication the core domain | Authentication is necessary but does not differentiate the main health and appointment management business. |

---

## 6. How to update this map

1. Before adding a new microservice, verify whether it belongs to an existing bounded context.
2. If a context's ubiquitous language is changing, review whether the context should be split.
3. Run an Event Storming session every time the domain changes significantly.
4. The context map MUST be synchronized with the C4 system-level diagram (05-architecture/overview.md).

> *Important correlation:* The bounded contexts in this document → Microservices in 09-microservices/service-catalog.md → C4 diagrams in 08-uml/ → Service separation ADRs in 05-architecture/decisions/