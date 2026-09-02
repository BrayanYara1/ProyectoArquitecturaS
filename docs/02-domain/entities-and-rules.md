# Entities, Value Objects, and Business Rules

> **What to fill in here:** The building blocks of the domain following the DDD tactical model.
> This document translates domain knowledge (obtained in Event Storming) into code models.

> **Stack note:** The concepts of Entity, Value Object, and Aggregate are language-independent.
> Code examples (classes, interfaces, decorators) are written in pseudo-TypeScript
> to illustrate the idea. To see the implementation in your technology:
> [`_stacks/node-typescript.md`](../_stacks/node-typescript.md) ·
> [`_stacks/java-spring.md`](../_stacks/java-spring.md) ·
> [`_stacks/python-fastapi.md`](../_stacks/python-fastapi.md) ·
> [`_stacks/go.md`](../_stacks/go.md)

---

## Tactical DDD concepts

### Entity

An *Entity* is defined by its identity. For GestionTurnosApp, examples include a User, an Appointment, a Medication record, a Symptom record, a Medical Study, a Chat and an Achievement.

### Value Object (VO)

A *Value Object* is defined by its attributes and has no identity of its own. It is immutable. Examples in this domain include Email, AppointmentDateTime, MedicationSchedule and MedicalStudyFile.

### Aggregate

An *Aggregate* groups related entities and Value Objects that must remain consistent as a unit. The Aggregate Root is the entry point for operations involving that aggregate.

### Business Rules

Business rules are constraints that the domain must satisfy. The PDR provides the functional modules and architecture of the system, but it does not define all detailed workflows. Therefore, only rules supported directly by the PDR or necessary data consistency rules are documented below.

---

# System entities

## Entity: User

*Context:* Identity and Access Management

*Description:* Represents a person with an account who can securely access GestionTurnosApp and use its personal health management features.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique user identifier | Yes | Unique identifier |
| email | Email | User email address used for account access | Yes | Must use a valid email format |
| credentials | String | Account authentication information | Yes | Must be handled securely |
| accountVerified | Boolean | Indicates whether account verification has been completed | Yes | Default verification state is defined by the authentication implementation |
| biometricEnabled | Boolean | Indicates whether biometric access is enabled | No | Requires device biometric capability |
| createdAt | DateTime | Account creation date | Yes | Set when the account is created |
| updatedAt | DateTime | Last account modification date | Yes | Updated when account data changes |

*Lifecycle / States:*

text
REGISTERED ──(verify account)──▶️ VERIFIED
     │
     └──(account remains unverified)──▶️ REGISTERED


| State | Description | Allowed transitions |
|---|---|---|
| REGISTERED | Account has been created. | → VERIFIED |
| VERIFIED | Account verification has been completed. | No additional transition is defined in the PDR. |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-USER-001: Unique user identity
  - Rule: Every User must have a unique identifier.
  - Violation: Two different users cannot represent the same entity identity.
  - Implementation: Generate and persist a unique ID.

INV-USER-002: Valid email
  - Rule: The user's email must use a valid email format.
  - Violation: Invalid email data cannot be accepted as account data.
  - Implementation: Validate through the Email Value Object.

INV-USER-003: Secure access
  - Rule: Sensitive access information must be handled securely.
  - Violation: Credentials or sensitive information must not be stored as plain, unprotected data.
  - Implementation: Use the security mechanisms defined in the application architecture.

INV-USER-004: Biometric access requires device support
  - Rule: Biometric authentication can only be used through the Android Biometric API when available.
  - Violation: The application cannot require unsupported biometric capabilities.
  - Implementation: Validate availability before enabling biometric authentication.


---

## Entity: Appointment

*Context:* Appointment Management

*Description:* Represents a medical appointment that the user can list, view in detail or request through the application.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique appointment identifier | Yes | Unique identifier |
| userId | UUID | Identifier of the user associated with the appointment | Yes | Must reference an existing user |
| dateTime | AppointmentDateTime | Date and time associated with the appointment | Yes | Must contain a valid date and time |
| details | String | Appointment detail information | No | Content depends on available appointment information |
| createdAt | DateTime | Creation date | Yes | Set when created |
| updatedAt | DateTime | Last modification date | Yes | Updated when appointment data changes |

*Lifecycle / States:*

text
REQUESTED ──(appointment information becomes available)──▶️ MANAGED


> The PDR confirms that users can request, list and view appointment details, but it does not define a complete appointment status workflow. Therefore, no additional states such as confirmed, completed or cancelled are asserted here.

| State | Description | Allowed transitions |
|---|---|---|
| REQUESTED | A request for a new medical appointment has been made. | → MANAGED when appointment information is available |
| MANAGED | Appointment is available for listing and detail management. | No further lifecycle transitions are defined in the PDR. |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-APPT-001: Unique appointment identity
  - Rule: Every Appointment must have a unique identifier.
  - Violation: Different appointments cannot share the same entity identity.

INV-APPT-002: Appointment belongs to a user
  - Rule: An Appointment must be associated with a User.
  - Violation: An appointment without an associated user cannot be managed as personal appointment information.

INV-APPT-003: Valid appointment date and time
  - Rule: When date and time information exists, it must be represented as a valid AppointmentDateTime value.
  - Violation: Invalid date or time information cannot be stored.


---

## Entity: Medication

*Context:* Personal Health Management

*Description:* Represents a medication registered by the user for personal medication tracking.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique medication tracking identifier | Yes | Unique identifier |
| userId | UUID | Identifier of the user who tracks the medication | Yes | Must reference an existing user |
| name | String | Name of the medication | Yes | Must not be empty |
| schedule | MedicationSchedule | Schedule used for medication tracking | No | Valid when present |
| createdAt | DateTime | Creation date | Yes | Set when created |
| updatedAt | DateTime | Last modification date | Yes | Updated when medication data changes |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-MED-001: Medication belongs to a user
  - Rule: A medication tracking record must be associated with a User.

INV-MED-002: Medication name is required
  - Rule: A registered medication must have a non-empty name.

INV-MED-003: Valid schedule
  - Rule: When a medication schedule is registered, it must satisfy the MedicationSchedule validation rules.


> The PDR confirms medication tracking but does not define dosage, frequency, start date, end date, or medication status. Those attributes are intentionally not asserted as mandatory domain requirements.

---

## Entity: Symptom

*Context:* Personal Health Management

*Description:* Represents a symptom registered by the user as part of personal health tracking.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique symptom record identifier | Yes | Unique identifier |
| userId | UUID | Identifier of the user who registered the symptom | Yes | Must reference an existing user |
| description | String | Description of the registered symptom | Yes | Must not be empty |
| recordedAt | DateTime | Date and time when the symptom was registered | Yes | Must be a valid date and time |
| createdAt | DateTime | Creation date | Yes | Set when created |
| updatedAt | DateTime | Last modification date | Yes | Updated when data changes |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-SYM-001: Symptom belongs to a user
  - Rule: A symptom record must be associated with a User.

INV-SYM-002: Symptom description is required
  - Rule: A symptom record must contain a non-empty description.

INV-SYM-003: Valid registration time
  - Rule: recordedAt must contain a valid date and time.


---

## Entity: MedicalStudy

*Context:* Medical Studies Management

*Description:* Represents a medical study or result stored and visualized by the user.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique medical study identifier | Yes | Unique identifier |
| userId | UUID | Identifier of the user associated with the study | Yes | Must reference an existing user |
| file | MedicalStudyFile | File or stored resource representing the study | Yes | Must contain valid file reference information |
| recognizedText | String | Text recognized from the study when text recognition is used | No | Optional because ML Kit support is described as possible |
| createdAt | DateTime | Storage creation date | Yes | Set when stored |
| updatedAt | DateTime | Last modification date | Yes | Updated when data changes |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-STUDY-001: Medical study belongs to a user
  - Rule: A stored medical study must be associated with a User.

INV-STUDY-002: Study file is required
  - Rule: A MedicalStudy must reference a valid stored file or resource.

INV-STUDY-003: Text recognition is optional
  - Rule: recognizedText is only present when text recognition is applied successfully.
  - Violation: The absence of recognized text does not invalidate a medical study.


> The PDR identifies the management of potentially large medical files as a risk. It does not define maximum file size or permitted file formats.

---

## Entity: Chat

*Context:* Communication

*Description:* Represents an integrated chat used for communication within the application.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique chat identifier | Yes | Unique identifier |
| userId | UUID | Identifier of the user associated with the chat | Yes | Must reference an existing user |
| createdAt | DateTime | Chat creation date | Yes | Set when created |
| updatedAt | DateTime | Last modification date | Yes | Updated when the chat changes |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-CHAT-001: Chat has unique identity
  - Rule: Every Chat must have a unique identifier.

INV-CHAT-002: Chat is associated with a user
  - Rule: A personal chat context must be associated with a User.


> The PDR only specifies an integrated chat system. Participants, message structure and conversation lifecycle are not defined in the source and are therefore not modeled as detailed requirements.

---

## Entity: Achievement

*Context:* Gamification

*Description:* Represents an achievement used to encourage continued use of the application.

*Attributes:*

| Attribute | Type | Description | Required | Rules |
|---|---|---|---|---|
| id | UUID | Unique achievement identifier | Yes | Unique identifier |
| userId | UUID | Identifier of the user associated with the achievement | Yes | Must reference an existing user |
| name | String | Name of the achievement | Yes | Must not be empty |
| createdAt | DateTime | Achievement creation or assignment date | Yes | Set when created or assigned |
| updatedAt | DateTime | Last modification date | Yes | Updated when data changes |

*Invariants (Business rules that MUST ALWAYS hold):*

text
INV-ACH-001: Achievement has unique identity
  - Rule: Every Achievement must have a unique identifier.

INV-ACH-002: Achievement belongs to a user
  - Rule: A user achievement must be associated with a User.

INV-ACH-003: Achievement name is required
  - Rule: An achievement must have a non-empty name.


> The PDR does not define achievement conditions, points, levels or reward rules.

---

# System Value Objects

## Value Object: Email

*Description:* Represents a validated email address associated with a user account.

*Attributes:*

| Attribute | Type | Description |
|---|---|---|
| value | String | Email address value |

*Validation rules:*

text
- Email must have a valid format.
- Email must not be empty.
- The value should be normalized consistently before comparison.


---

## Value Object: AppointmentDateTime

*Description:* Represents the date and time information associated with a medical appointment.

*Attributes:*

| Attribute | Type | Description |
|---|---|---|
| value | DateTime | Appointment date and time |

*Validation rules:*

text
- The value must represent a valid date and time.
- The PDR does not define rules about minimum notice, working hours or allowed appointment dates.


---

## Value Object: MedicationSchedule

*Description:* Represents scheduling information used for medication tracking when such information is registered.

*Attributes:*

| Attribute | Type | Description |
|---|---|---|
| value | String | Medication schedule information |

*Validation rules:*

text
- When present, the schedule value must not be empty.
- The PDR does not define a fixed schedule format, dosage or frequency model.


---

## Value Object: MedicalStudyFile

*Description:* Represents the file or resource information used to store and access a medical study.

*Attributes:*

| Attribute | Type | Description |
|---|---|---|
| path | String | Local or managed storage reference |
| fileName | String | Name of the stored resource |

*Validation rules:*

text
- path must not be empty.
- fileName must not be empty.
- The PDR does not define permitted file formats or maximum file sizes.


---

# System Aggregates

## Aggregate: User Account

*Aggregate Root:* User

*Internal entities:*

- None explicitly defined by the PDR.

*Value Objects:*

- Email

*Aggregate invariants:*

text
AGGR-INV-USER-001: The User must always have a unique identity.
AGGR-INV-USER-002: The User email must remain valid.
AGGR-INV-USER-003: Sensitive account information must be handled securely.


*Why do these objects form an aggregate?*

> User identity and account access information must remain consistent because authentication, account verification and biometric access all operate around the same user account.

---

## Aggregate: Appointment

*Aggregate Root:* Appointment

*Internal entities:*

- None explicitly defined by the PDR.

*Value Objects:*

- AppointmentDateTime

*Aggregate invariants:*

text
AGGR-INV-APPT-001: The Appointment must have a unique identity.
AGGR-INV-APPT-002: The Appointment must be associated with a User.
AGGR-INV-APPT-003: Appointment date and time information must be valid when present.


*Why do these objects form an aggregate?*

> Appointment data must remain consistent as one unit because listing, viewing details and managing an appointment refer to the same identified medical appointment.

---

## Aggregate: Personal Health Record

*Aggregate Root:* User

*Internal entities:*

- Medication — represents medication tracking information associated with the user.
- Symptom — represents health symptoms registered by the user.

*Value Objects:*

- MedicationSchedule

*Aggregate invariants:*

text
AGGR-INV-HEALTH-001: Every health tracking record must be associated with a User.
AGGR-INV-HEALTH-002: Medication records must contain a name.
AGGR-INV-HEALTH-003: Symptom records must contain a description and valid registration time.


*Why do these objects form an aggregate?*

> Medication and symptom tracking are both part of the user's personal health management. The PDR groups these capabilities within the same health and medication module. This aggregate expresses their association with the user's health information without defining unsupported clinical relationships.

---

## Aggregate: Medical Study

*Aggregate Root:* MedicalStudy

*Internal entities:*

- None explicitly defined by the PDR.

*Value Objects:*

- MedicalStudyFile

*Aggregate invariants:*

text
AGGR-INV-STUDY-001: The MedicalStudy must have a unique identity.
AGGR-INV-STUDY-002: The MedicalStudy must be associated with a User.
AGGR-INV-STUDY-003: The MedicalStudy must reference a valid stored file or resource.


*Why do these objects form an aggregate?*

> A medical study and its file reference must remain consistent because the study cannot be stored or visualized without the resource that represents it.

---

# Summary table of tactical building blocks

| Name | Type | Bounded Context | Aggregate Root? |
|---|---|---|---|
| User | Entity | Identity and Access Management | Yes — User Account |
| Appointment | Entity | Appointment Management | Yes |
| Medication | Entity | Personal Health Management | No — inside Personal Health Record |
| Symptom | Entity | Personal Health Management | No — inside Personal Health Record |
| MedicalStudy | Entity | Medical Studies Management | Yes |
| Chat | Entity | Communication | Yes |
| Achievement | Entity | Gamification | Yes |
| Email | Value Object | Identity and Access Management | N/A |
| AppointmentDateTime | Value Object | Appointment Management | N/A |
| MedicationSchedule | Value Object | Personal Health Management | N/A |
| MedicalStudyFile | Value Object | Medical Studies Management | N/A |
| OfflineSynchronizationService | Domain Service | Offline Data Management | N/A |

---

# Domain Services

## Domain Service: OfflineSynchronizationService

*Context:* Offline Data Management

*Description:* Coordinates the availability and synchronization of local and remote application data to support resilient operation when connectivity is unavailable.

*Responsibilities:*

text
- Coordinate local persistence using the application's data layer.
- Support synchronization between local and remote data when connectivity is available.
- Protect application functionality from direct dependency on constant connectivity.


> The PDR identifies Room Database, Retrofit and OfflineCacheManager as the mechanisms supporting local persistence, remote access and offline operation. It also identifies synchronization complexity as a potential risk when remote business logic changes.

## Domain Service: HealthStatisticsService

*Context:* Personal Health Management

*Description:* Supports the visualization of health information and statistics from information recorded by the user.

*Responsibilities:*

text
- Prepare available health information for statistical visualization.
- Provide data used by charts and health statistics screens.


> The PDR confirms health statistics and chart visualization, but it does not define exact formulas or medical calculations. Therefore, no specific statistical algorithm is documented.

---

# Correlation with code

The PDR defines an Android application using Clean Architecture, MVVM, Repository Pattern and Hilt. The exact package and file names for domain entities are not specified in the PDR. The following structure is therefore a proposed correlation based on the documented architecture and must be aligned with the actual project code.

| Domain artifact | Proposed package / folder in code | Proposed file |
|---|---|---|
| Aggregate Root User | domain/auth/ | User.kt |
| Aggregate Root Appointment | domain/appointment/ | Appointment.kt |
| Entity Medication | domain/health/ | Medication.kt |
| Entity Symptom | domain/health/ | Symptom.kt |
| Aggregate Root MedicalStudy | domain/medicalstudy/ | MedicalStudy.kt |
| Entity Chat | domain/chat/ | Chat.kt |
| Entity Achievement | domain/gamification/ | Achievement.kt |
| Value Object Email | domain/shared/valueobjects/ | Email.kt |
| Value Object AppointmentDateTime | domain/appointment/valueobjects/ | AppointmentDateTime.kt |
| Domain Service OfflineSynchronizationService | domain/offline/services/ | OfflineSynchronizationService.kt |

> See the system architecture documentation for the final correlation between domain models, repositories, data sources and Android implementation.