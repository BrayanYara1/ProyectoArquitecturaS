# System Scope

> **Why this document exists:** Scope prevents scope creep and aligns expectations.
> It is equally important to define what the system does NOT do as what it does.
> Review this document at the start of each planning cycle.

---


## In Scope

What the system *DOES build and maintain*:

### MVP Features

| # | Feature | Description | Responsible service |
|---|---|---|---|
| 1 | Authentication | Login, registration, account verification and biometric authentication. | Authentication Service |
| 2 | Appointment Management | List, view details and request new medical appointments. | Appointment Service |
| 3 | Medication Management | Register and track the user's medications. | Medication Service |
| 4 | Symptom Tracking | Register and monitor the user's health symptoms. | Health Service |
| 5 | Health Statistics | Display health information and statistics through charts. | Health Statistics Service |
| 6 | Medical Studies | Store and view medical studies and results, with possible text recognition support. | Medical Studies Service |
| 7 | Offline Support | Allow local data persistence and application functionality without an internet connection. | Offline Data Service |
| 8 | Notifications | Send notifications to users. | Notification Service |
| 9 | Integrated Chat | Provide an integrated communication chat. | Chat Service |
| 10 | Achievements | Provide an achievement system to encourage continued use of the application. | Gamification Service |

### Included integrations

| External system | Integration type | Purpose |
|---|---|---|
| Remote APIs | REST API | Synchronize and exchange application data with remote services. |
| Firebase Cloud Messaging | SDK | Send push notifications. |
| Firebase Crashlytics | SDK | Monitor and report application crashes. |
| Firebase Analytics | SDK | Collect application usage analytics. |
| ML Kit | SDK | Support possible text recognition in medical studies. |
| Android Biometric API | Android API | Provide biometric authentication. |

### Environments being built

| Environment | Purpose |
|---|---|
| Local | Development and testing on the developer's machine. |
| Development (dev) | Integration and testing of new application features. |
| Staging | Pre-production testing and validation before release. |
| Production | Final environment available to end users. |

---

## Out of Scope

What the system *does NOT build* in this version and why:

| # | What is out of scope | Reason | Future version? |
|---|---|---|---|
| 1 | Web application | The current project is focused on an Android mobile application. | Yes |
| 2 | iOS application | The current technology stack and design are focused on Android. | Yes |
| 3 | Medical diagnosis | The application manages health information but does not provide professional medical diagnoses. | No |
| 4 | Financial and billing management | This functionality is not part of the current application modules. | Possible |
| 5 | Hospital infrastructure management | The application focuses on personal health and appointment management. | No |
| 6 | Complete electronic medical record | The project stores personal medical studies but does not replace a complete clinical history system. | Possible |

### What another system / team handles (and why not us)

| Feature | Who builds it | Why not us |
|---|---|---|
| Push notification infrastructure | Firebase Cloud Messaging | Firebase provides the notification infrastructure. |
| Crash monitoring | Firebase Crashlytics | A specialized external service handles crash reporting. |
| Application analytics | Firebase Analytics | Firebase provides analytics services. |
| Biometric authentication | Android Platform | The application uses the native Android Biometric API. |
| Text recognition | ML Kit | The project uses an external SDK for this functionality. |
| Remote services | External API Provider | The application consumes remote services through REST APIs. |

---

## Scope assumptions

> These assumptions are taken to be true. If they change, the scope must be renegotiated.

| # | Assumption | Consequence if false |
|---|---|---|
| 1 | Remote services required by the application are available through REST APIs. | The integration strategy would need to be redesigned. |
| 2 | Users have Android devices with SDK 24 or higher. | Device compatibility would need to change. |
| 3 | Room Database can store the information required for offline operation. | The local persistence strategy would need to change. |
| 4 | Firebase services are available for notifications, analytics and crash monitoring. | Alternative services would need to be integrated. |
| 5 | Users have internet access when synchronization is required. | Data synchronization could be delayed. |
| 6 | User devices have enough storage for medical studies. | A different storage strategy would be required. |
| 7 | Remote changes remain compatible with OfflineCacheManager. | The synchronization logic would need to be modified. |

---

## Constraints

| Type | Description |
|---|---|
| *Time* | The MVP must be completed within the academic project development period. |
| *Budget* | No additional infrastructure budget is planned; the project will primarily use free or available development tools and services. |
| *Technology* | The application must use Kotlin, Coroutines, Clean Architecture, MVVM, Room, Retrofit and Hilt. |
| *Regulatory* | Sensitive user information must be handled securely using Security Crypto and biometric authentication mechanisms. |
| *Platform* | The application must support Android SDK 24 and above and is optimized for Android 15 (SDK 35). |
| *Team* | The project will be developed by the assigned academic development team. |

---

## External dependencies

| Dependency | Team / Provider | Required date | Status |
|---|---|---|---|
| Remote REST APIs | External API Provider | During development | 🟡 In progress |
| Firebase Cloud Messaging | Firebase | Before notification implementation | 🟢 Available |
| Firebase Crashlytics | Firebase | Before production testing | 🟢 Available |
| Firebase Analytics | Firebase | Before production testing | 🟢 Available |
| ML Kit | Google | Before medical studies implementation | 🟢 Available |
| Android Biometric API | Android Platform | During authentication development | 🟢 Available |
| MPAndroidChart AAR | Project dependency | Before statistics implementation | 🟡 In progress |
| Device local storage | User device | During application use | 🟢 Available |

---

## How to update the scope

The scope can change, but the change has a process:

1. Document the proposed change in this file.
2. Evaluate the impact on schedule and effort.
3. Obtain approval from the Product Owner and Tech Lead.
4. Update the roadmap in 03-product/vision.md.
5. Create or update HUs in 04-requirements/user-stories.md.

---

## Correlations

- Vision and roadmap → 03-product/vision.md
- Term glossary → 01-context/glossary.md
- System overview → 01-context/overview.md
- Scope-related risks → 15-project-control/risks.md