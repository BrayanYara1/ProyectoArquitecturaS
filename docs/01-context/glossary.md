# Project Glossary

> **Instructions:** Define here all technical and business terms used in the project.
> This is the official dictionary — if there is ambiguity, this document wins.
> Add terms throughout the project, not only at the start.

---

## How to use this glossary

1. Before using a technical or business term in code, docs, or conversations: look it up here.
2. If it's not there: add it with its definition.
3. If there is disagreement about the definition: discuss it as a team and update this document.

---

## Domain terms

| *Term* | *Definition* | *Notes / Synonyms* |
|---|---|---|
| *Medical appointment* | A scheduled time or slot for a user to receive medical care. | Also referred to as a medical visit or appointment. |
| *User* | A person who uses GestionTurnosApp to manage medical appointments and track personal health information. | Do not confuse with system administrator. |
| *Medication* | A substance or treatment registered by the user to track its use. | Related to the Health and Medication module. |
| *Symptom* | A sign or manifestation related to the user's health that can be recorded in the application. | Used for personal health tracking. |
| *Medical study* | A medical document, result, or information stored and accessible through the application. | May include images obtained through scanning. |
| *Medical result* | Information obtained from a medical study that can be stored and viewed by the user. | Part of the Medical Studies module. |
| *Achievement* | A recognition obtained by the user as part of the application's gamification system. | Also referred to as a reward or achievement. |
| *Gamification* | The use of game-like elements, such as achievements, to encourage application usage. | Applied to user engagement and health tracking. |
| *Offline mode* | Application operation without an Internet connection by using locally stored information. | Supported through OfflineCacheManager and Room Database. |
| *Synchronization* | The process of keeping local and remote information coordinated. | Identified as a potential risk involving OfflineCacheManager. |
| *Notification* | A message sent to the user to communicate relevant information from the application. | Implemented through Firebase Cloud Messaging. |
| *Chat* | An integrated communication system within the application. | Part of the Communication module. |

---

## Technical terms of the project

| *Term* | *Definition* |
|---|---|
| *Clean Architecture* | An architectural approach used to separate responsibilities and organize the application into different layers. |
| *MVVM* | The Model-View-ViewModel architectural pattern used to separate the user interface, state, and presentation logic. |
| *View* | The layer responsible for displaying the user interface and presenting information to the user. |
| *ViewModel* | A component that manages the UI state (UiState) and preserves that state across configuration changes. |
| *Data Layer* | The layer responsible for managing and retrieving application data. |
| *Repository Pattern* | A pattern used to centralize data access and abstract local and remote data sources. |
| *Room Database* | An Android library that provides local data persistence. |
| *OfflineCacheManager* | A component that mediates locally stored information to allow the application to operate without an Internet connection. |
| *Retrofit* | A library used to consume REST APIs from the Android application. |
| *OkHttp* | A library used for network communication in the application. |
| *GSON* | A library used to convert data between objects and JSON representations. |
| *Hilt* | A framework used to implement dependency injection in the Android application. |
| *Dependency Injection* | A technique that provides the dependencies required by application components instead of having those components create them directly. |
| *Fragment* | An Android component used to represent a portion of the user interface within the application. |
| *ViewBinding* | An Android mechanism used to access interface views safely. |
| *Jetpack Navigation Component* | A component used to manage navigation between different screens or destinations in the application. |
| *UiState* | A representation of the current user interface state managed by the ViewModel. |
| *Material Components* | User interface components used to build a consistent visual experience in Android. |
| *Lottie* | A library used to display animations in the user interface. |
| *Facebook Shimmer* | A library used to create skeleton screens while content is loading. |
| *Skeleton Screen* | A visual placeholder representing the structure of a screen while its data is loading. |
| *Empty State* | A visual state displayed when a screen or section has no information to show. |
| *MPAndroidChart* | A library used to display charts and health statistics in the application. |
| *Security Crypto* | A tool used to protect sensitive information stored locally. |
| *SharedPreferences* | An Android local storage mechanism used together with Security Crypto for sensitive information. |
| *REST API* | A communication interface that allows the application to exchange information with remote services following REST principles. |
| *Firebase Cloud Messaging* | A Firebase service used to send notifications to the application. |
| *Crashlytics* | A Firebase tool used to detect and analyze application errors and crashes. |
| *Analytics* | A tool used to collect information about application usage. |
| *ML Kit* | A toolkit used for machine learning features, including text recognition. |
| *WorkManager* | An Android component used to execute background tasks. |
| *Biometric API* | An Android API used to provide authentication through biometric mechanisms available on the device. |
| *Kotlin Coroutines* | A Kotlin mechanism used to manage asynchronous operations and concurrent tasks. |
| *DAO* | A component responsible for defining operations to access data stored in the database. |
| *AAR* | A file format used to distribute Android libraries. |
| *ImageStorageManager* | A component responsible for managing the local storage of files related to medical studies. |
| *Responsiveness* | The ability of the user interface to adapt to different device sizes and configurations. |
| *SDK* | A software development kit that provides tools and resources for developing and running the Android application. |

---

## Acronyms

| *Acronym* | *Meaning* |
|---|---|
| *PDR* | Preliminary Design Review |
| *UI* | User Interface |
| *UX* | User Experience |
| *MVVM* | Model-View-ViewModel |
| *API* | Application Programming Interface |
| *REST* | Representational State Transfer |
| *DI* | Dependency Injection |
| *DAO* | Data Access Object |
| *SDK* | Software Development Kit |
| *AAR* | Android Archive |
| *JSON* | JavaScript Object Notation |
| *GSON* | Google Gson |
| *ML* | Machine Learning |
| *FCM* | Firebase Cloud Messaging |
| *UIState* | User Interface State |
| *CRUD* | Create, Read, Update, Delete |
| *FR* | Functional Requirement |
| *NFR* | Non-Functional Requirement |
| *SLO* | Service Level Objective |
| *SLA* | Service Level Agreement |
| *ADR* | Architecture Decision Record |
| *PR* | Pull Request |
| *DoD* | Definition of Done |

---

## Project-specific components

| *Term* | *Definition* |
|---|---|
| *GestionTurnosApp* | The name of the mobile application focused on medical appointment management and personal health information tracking. |
| *Authentication Module* | The module responsible for login, registration, account verification, and biometric security. |
| *Appointment Management Module* | The module responsible for listing, viewing details, and requesting new medical appointments. |
| *Health and Medication Module* | The module responsible for medication tracking, symptom recording, and health statistics. |
| *Medical Studies Module* | The module responsible for storing and displaying medical study results. |
| *Communication Module* | The module that provides the application's integrated chat system. |
| *Gamification Module* | The module responsible for the achievement system used to encourage application usage. |

---

## Version and platform terms

| *Term* | *Definition* |
|---|---|
| *Android 15* | The Android version for which the application is optimized. |
| *SDK 35* | The SDK level used as the development and optimization reference for Android 15. |
| *SDK 24* | The minimum SDK level supported by the application according to the PDR. |
| *PREMIUM* | The functional status or version indicated for the application in the PDR. |
| *Version 1.0.5* | The application version indicated in the current PDR status. 