# User Stories — Backlog

> *What to fill in here:* The product's User Story backlog. Each HU uses the standard format with Acceptance Criteria in Given/When/Then. Refined (Ready) HUs go to the sprint. Unrefined ones are epics or ideas.

---

## Backlog Status

| *Cut* | *Sprint* | *Total HUs* | *Refined* | *In Progress* | *Completed* |
|---|---|---:|---:|---:|---:|
| Cut 1 | Sprint 1-2 | 8 | 8 | 0 | 0 |
| Cut 2 | Sprint 3-4 | 10 | 10 | 0 | 0 |
| *Total* | *Sprint 1-4* | *18* | *18* | *0* | *0* |

---

## Epics

| *ID* | *Epic* | *Description* |
|---|---|---|
| EP-001 | Authentication and Security | Manage user registration, login, account verification, and authentication mechanisms to protect access to the application. |
| EP-002 | Medical Appointment Management | Allow users to consult, view, and request medical appointments through the application. |
| EP-003 | Health and Medication Management | Allow users to register and consult medications, symptoms, and statistics related to their health information. |
| EP-004 | Medical Study Management | Allow users to store and consult medical studies and support information processing through text scanning. |
| EP-005 | Communication | Provide communication functionality through the integrated chat system. |
| EP-006 | Gamification | Allow users to view achievements associated with the use of the application's functionalities. |
| EP-007 | Offline Operation | Allow users to access locally stored information and manage synchronization when the connection is restored. |

---

# User Stories

## HU-001 — User Registration {#HU-001}

*Epic:* EP-001

> *As* an unregistered user  
> *I want* to register in GestionTurnosApp  
> *so that* I can access personalized application functionalities.

### Acceptance Criteria

gherkin
Scenario 1: Successful registration
  Given that the user is on the registration screen
  And provides all required information correctly
  When the user confirms the registration
  Then the system must create the account
  And must inform the user that the registration was completed successfully.

Scenario 2: Invalid or incomplete information
  Given that the user is on the registration screen
  When the user provides invalid or incomplete information
  Then the system must prevent the registration
  And must display the corresponding validation messages.

Scenario 3: Existing account
  Given that the provided information belongs to an existing account
  When the user attempts to complete the registration
  Then the system must prevent the account from being created
  And must inform the user that the account already exists.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Must Have |
| Target Sprint | Sprint 1 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | None |
| Affected Service(s) | Authentication Service |

---

## HU-002 — Login {#HU-002}

*Epic:* EP-001

> *As* a registered user  
> *I want* to log in to the application  
> *so that* I can securely access my functionalities and data.

### Acceptance Criteria

gherkin
Scenario 1: Successful login
  Given that the user has a registered account
  And provides valid credentials
  When the user confirms the login
  Then the system must authenticate the user
  And must allow access to the application.

Scenario 2: Incorrect credentials
  Given that the user is on the login screen
  When the user provides incorrect credentials
  Then the system must reject access
  And must display a message indicating that the credentials are invalid.

Scenario 3: Required fields incomplete
  Given that one or more required fields are empty
  When the user attempts to log in
  Then the system must prevent the submission
  And must display the corresponding validation messages.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Must Have |
| Target Sprint | Sprint 1 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-001 |
| Affected Service(s) | Authentication Service |

---

## HU-003 — Account Verification {#HU-003}

*Epic:* EP-001

> *As* a registered user  
> *I want* to verify my account  
> *so that* I can confirm my identity and securely access the application.

### Acceptance Criteria

gherkin
Scenario 1: Successful verification
  Given that the user has an account pending verification
  And provides the required information correctly
  When the user confirms the verification
  Then the system must verify the account
  And must allow the user to continue with the access process.

Scenario 2: Invalid verification
  Given that the user attempts to verify the account
  When the user provides incorrect verification information
  Then the system must reject the verification
  And must display a message indicating that the information is invalid.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Must Have |
| Target Sprint | Sprint 1 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-001 |
| Affected Service(s) | Authentication Service |

---

## HU-004 — Biometric Authentication {#HU-004}

*Epic:* EP-001

> *As* a registered user  
> *I want* to use biometric authentication  
> *so that* I can have an additional protection mechanism to access the application.

### Acceptance Criteria

gherkin
Scenario 1: Successful biometric authentication
  Given that the user has biometric authentication configured
  When the user provides a valid biometric characteristic
  Then the system must authenticate the user
  And must allow access to the application.

Scenario 2: Failed biometric authentication
  Given that the user attempts to authenticate using biometrics
  When the biometric characteristic is not recognized
  Then the system must reject the authentication
  And must inform the user that authentication was unsuccessful.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Should Have |
| Target Sprint | Sprint 2 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Authentication / Security |

---

## HU-005 — View Medical Appointments {#HU-005}

*Epic:* EP-002

> *As* an authenticated user  
> *I want* to view my medical appointments  
> *so that* I can know which medical appointments I have scheduled.

### Acceptance Criteria

gherkin
Scenario 1: Viewing registered appointments
  Given that the user has logged in
  And has registered medical appointments
  When the user accesses the appointments module
  Then the system must display the user's appointments
  And must present the available information for each appointment.

Scenario 2: User without appointments
  Given that the user has logged in
  And has no registered appointments
  When the user accesses the appointments module
  Then the system must display an empty state
  And must indicate that there are no registered appointments.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Must Have |
| Target Sprint | Sprint 1 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Appointment Service |

---

## HU-006 — View Appointment Details {#HU-006}

*Epic:* EP-002

> *As* an authenticated user  
> *I want* to view the details of a medical appointment  
> *so that* I can consult the specific information of the scheduled appointment.

### Acceptance Criteria

gherkin
Scenario 1: Viewing appointment details
  Given that the user has a registered appointment
  When the user selects an appointment
  Then the system must display its detailed information
  And must present the available appointment data.

Scenario 2: Appointment unavailable
  Given that the user attempts to view an appointment that is no longer available
  When the user requests its details
  Then the system must inform the user that the information is unavailable.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Must Have |
| Target Sprint | Sprint 1 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-005 |
| Affected Service(s) | Appointment Service |

---

## HU-007 — Request Medical Appointment {#HU-007}

*Epic:* EP-002

> *As* an authenticated user  
> *I want* to request a medical appointment  
> *so that* I can manage my upcoming medical appointments without using other channels.

### Acceptance Criteria

gherkin
Scenario 1: Successful appointment request
  Given that the user is authenticated
  And there are available appointment options
  When the user selects a valid option and confirms the request
  Then the system must register the request
  And must display an operation confirmation.

Scenario 2: Option unavailable
  Given that the user is making an appointment request
  When the selected option is no longer available
  Then the system must prevent the request
  And must inform the user that the selected option is no longer available.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Must Have |
| Target Sprint | Sprint 2 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-005, HU-006 |
| Affected Service(s) | Appointment Service |

---

## HU-008 — Medication Management {#HU-008}

*Epic:* EP-003

> *As* an authenticated user  
> *I want* to register and view my medications  
> *so that* I can keep track of information related to my medication.

### Acceptance Criteria

gherkin
Scenario 1: Successful medication registration
  Given that the user is authenticated
  When the user provides the required medication information
  And confirms the registration
  Then the system must store the information
  And must display the registered medication.

Scenario 2: Incomplete information
  Given that the user is registering a medication
  When the user omits required information
  Then the system must prevent the registration
  And must display the corresponding validation messages.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Must Have |
| Target Sprint | Sprint 2 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Medication Service |

---

## HU-009 — Symptom Registration {#HU-009}

*Epic:* EP-003

> *As* an authenticated user  
> *I want* to register my symptoms  
> *so that* I can maintain a record of information related to my health.

### Acceptance Criteria

gherkin
Scenario 1: Successful symptom registration
  Given that the user is authenticated
  When the user registers a symptom with the required information
  And confirms the registration
  Then the system must store the information
  And must allow the user to consult it later.

Scenario 2: Incomplete information
  Given that the user is registering a symptom
  When the user does not provide the required information
  Then the system must prevent the registration
  And must display the corresponding validation messages.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Must Have |
| Target Sprint | Sprint 3 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Health Service |

---

## HU-010 — View Health Statistics {#HU-010}

*Epic:* EP-003

> *As* an authenticated user  
> *I want* to view statistics related to my health information  
> *so that* I can interpret the recorded information through graphical representations.

### Acceptance Criteria

gherkin
Scenario 1: Viewing health statistics
  Given that the user has registered health information
  When the user accesses the statistics module
  Then the system must display the available statistics
  And must use the graphical representations defined for the information.

Scenario 2: Insufficient data
  Given that the user does not have enough information to generate statistics
  When the user accesses the statistics module
  Then the system must display an empty state
  And must indicate that there is not enough data available.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Should Have |
| Target Sprint | Sprint 3 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-008, HU-009 |
| Affected Service(s) | Health / Statistics Service |

---

## HU-011 — Store Medical Studies {#HU-011}

*Epic:* EP-004

> *As* an authenticated user  
> *I want* to store my medical studies  
> *so that* I can keep my medical information available for future consultations.

### Acceptance Criteria

gherkin
Scenario 1: Successful storage
  Given that the user is authenticated
  When the user selects a compatible medical study
  And confirms the storage operation
  Then the system must store the study
  And must associate it with the user's information.

Scenario 2: Unsupported study
  Given that the user attempts to store a medical study
  When the file does not meet the supported conditions
  Then the system must prevent the storage
  And must inform the user that the file cannot be processed.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Must Have |
| Target Sprint | Sprint 3 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Medical Studies / Storage Service |

---

## HU-012 — View Medical Studies {#HU-012}

*Epic:* EP-004

> *As* an authenticated user  
> *I want* to view my stored medical studies  
> *so that* I can access my medical information whenever I need it.

### Acceptance Criteria

gherkin
Scenario 1: Viewing medical studies
  Given that the user has stored medical studies
  When the user accesses the studies module
  Then the system must display the available studies
  And must allow the user to consult the corresponding information.

Scenario 2: No registered studies
  Given that the user has no stored medical studies
  When the user accesses the studies module
  Then the system must display an empty state
  And must indicate that there are no registered studies.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Must Have |
| Target Sprint | Sprint 3 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-011 |
| Affected Service(s) | Medical Studies Service |

---

## HU-013 — Medical Information Scanning {#HU-013}

*Epic:* EP-004

> *As* an authenticated user  
> *I want* to scan textual information from my medical studies  
> *so that* I can facilitate the incorporation of information from medical documents.

### Acceptance Criteria

gherkin
Scenario 1: Successful text recognition
  Given that the user has a compatible medical study
  When the user uses the scanning functionality
  Then the system must process the image using text recognition
  And must display the recognized information for review.

Scenario 2: Text not recognized
  Given that the user attempts to scan a medical study
  When the system cannot recognize sufficient information
  Then the system must inform the user that the text could not be recognized
  And must allow the user to continue through the available alternative flow.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 8 |
| Priority | Could Have |
| Target Sprint | Sprint 4 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-011 |
| Affected Service(s) | Medical Studies / ML Kit |

---

## HU-014 — Integrated Chat {#HU-014}

*Epic:* EP-005

> *As* an authenticated user  
> *I want* to use the integrated chat  
> *so that* I can have a communication channel within the application.

### Acceptance Criteria

gherkin
Scenario 1: Accessing the chat
  Given that the user is authenticated
  When the user accesses the communication module
  Then the system must display the available chat
  And must allow the user to use the implemented communication functionalities.

Scenario 2: Operation without connection
  Given that the user does not have an Internet connection
  When the user attempts to perform a chat operation that requires remote communication
  Then the system must inform the user that the operation requires an Internet connection.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Should Have |
| Target Sprint | Sprint 4 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Chat Service |

---

## HU-015 — View Achievements {#HU-015}

*Epic:* EP-006

> *As* an authenticated user  
> *I want* to view my achievements  
> *so that* I can track my progress within the application.

### Acceptance Criteria

gherkin
Scenario 1: Viewing achievements
  Given that the user is authenticated
  When the user accesses the achievements module
  Then the system must display the available achievements
  And must indicate which achievements have been obtained.

Scenario 2: No achievements obtained
  Given that the user has not yet obtained any achievements
  When the user accesses the achievements module
  Then the system must display the available achievements
  And must indicate which achievements remain pending.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 5 |
| Priority | Could Have |
| Target Sprint | Sprint 4 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-002 |
| Affected Service(s) | Achievements Service |

---

## HU-016 — View Information Offline {#HU-016}

*Epic:* EP-007

> *As* an authenticated user  
> *I want* to view previously stored information when I do not have an Internet connection  
> *so that* I can continue accessing available information without depending on an Internet connection.

### Acceptance Criteria

gherkin
Scenario 1: Offline consultation with stored information
  Given that the device does not have an Internet connection
  And locally stored data exists
  When the user accesses a functionality supported offline
  Then the system must retrieve the information from local storage
  And must display it to the user.

Scenario 2: Information unavailable locally
  Given that the device does not have an Internet connection
  And there is no local data for the requested information
  When the user attempts to access that information
  Then the system must inform the user that the information is unavailable offline.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 8 |
| Priority | Must Have |
| Target Sprint | Sprint 4 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-005, HU-008, HU-009, HU-011 |
| Affected Service(s) | OfflineCacheManager / Room Database |

---

## HU-017 — Information Synchronization {#HU-017}

*Epic:* EP-007

> *As* an authenticated user  
> *I want* to synchronize local information when the connection is restored  
> *so that* data used while offline can remain up to date.

### Acceptance Criteria

gherkin
Scenario 1: Synchronization after connection recovery
  Given that the device was offline
  And there is information pending synchronization
  When the Internet connection is restored
  Then the system must execute the defined synchronization process
  And must update the corresponding information.

Scenario 2: Synchronization error
  Given that there is information pending synchronization
  When an error occurs during the synchronization process
  Then the system must preserve the local information
  And must allow another synchronization attempt later.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 8 |
| Priority | Must Have |
| Target Sprint | Sprint 4 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | HU-016 |
| Affected Service(s) | OfflineCacheManager / Room Database / Retrofit API |

---

## HU-018 — Loading and Empty States {#HU-018}

*Epic:* EP-007

> *As* a GestionTurnosApp user  
> *I want* to clearly view loading and empty states  
> *so that* I can understand the status of the information while it is being processed or when no records exist.

### Acceptance Criteria

gherkin
Scenario 1: Loading state
  Given that the application is retrieving information
  When the data is not yet available
  Then the system must display a loading state
  And must use the visual component defined for this state.

Scenario 2: Empty state
  Given that a section contains no information
  When the user accesses that section
  Then the system must display an empty state
  And must clearly indicate that no data is available.


### Definition of Done

- Code reviewed and approved
- Unit tests written
- Acceptance criteria verified (manual or automated)
- API contract updated if applicable
- Deployed to staging

| *Field* | *Value* |
|---|---|
| Story Points | 3 |
| Priority | Should Have |
| Target Sprint | Sprint 4 |
| Assigned To | To be assigned |
| Status | Ready |
| Dependencies | None |
| Affected Service(s) | Presentation / UI Layer |

---

# Backlog Summary

| *ID* | *User Story* | *Epic* | *Story Points* | *Priority* | *Target Sprint* | *Status* |
|---|---|---|---:|---|---|---|
| HU-001 | User Registration | EP-001 | 5 | Must Have | Sprint 1 | Ready |
| HU-002 | Login | EP-001 | 3 | Must Have | Sprint 1 | Ready |
| HU-003 | Account Verification | EP-001 | 3 | Must Have | Sprint 1 | Ready |
| HU-004 | Biometric Authentication | EP-001 | 5 | Should Have | Sprint 2 | Ready |
| HU-005 | View Medical Appointments | EP-002 | 3 | Must Have | Sprint 1 | Ready |
| HU-006 | View Appointment Details | EP-002 | 3 | Must Have | Sprint 1 | Ready |
| HU-007 | Request Medical Appointment | EP-002 | 5 | Must Have | Sprint 2 | Ready |
| HU-008 | Medication Management | EP-003 | 5 | Must Have | Sprint 2 | Ready |
| HU-009 | Symptom Registration | EP-003 | 3 | Must Have | Sprint 3 | Ready |
| HU-010 | View Health Statistics | EP-003 | 5 | Should Have | Sprint 3 | Ready |
| HU-011 | Store Medical Studies | EP-004 | 5 | Must Have | Sprint 3 | Ready |
| HU-012 | View Medical Studies | EP-004 | 3 | Must Have | Sprint 3 | Ready |
| HU-013 | Medical Information Scanning | EP-004 | 8 | Could Have | Sprint 4 | Ready |
| HU-014 | Integrated Chat | EP-005 | 5 | Should Have | Sprint 4 | Ready |
| HU-015 | View Achievements | EP-006 | 5 | Could Have | Sprint 4 | Ready |
| HU-016 | View Information Offline | EP-007 | 8 | Must Have | Sprint 4 | Ready |
| HU-017 | Information Synchronization | EP-007 | 8 | Must Have | Sprint 4 | Ready |
| HU-018 | Loading and Empty States | EP-007 | 3 | Should Have | Sprint 4 | Ready |
| *TOTAL* | | | *85* | | | |

---

## Backlog Notes

- All 18 User Stories are currently marked as *Ready*.
- All User Stories include acceptance criteria using the *Given / When / Then* format.
- Each User Story includes a *Definition of Done*.
- Story Points are assigned using a relative estimation scale.
- Priorities are defined using *Must Have, **Should Have, and **Could Have*.
- Dependencies between User Stories are explicitly identified.
- The backlog is distributed across *4 sprints* and *2 cuts*.
- *Cut 1* contains Sprints 1 and 2, with 8 User Stories.
- *Cut 2* contains Sprints 3 and 4, with 10 User Stories.
- The total estimated effort is *85 Story Points*.
- The current status of all User Stories is *Ready*.