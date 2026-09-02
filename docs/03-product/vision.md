# Product Vision

> The vision is the team's north star. All sprints, design decisions,
> and trade-offs are evaluated against this vision.
> It must be ambitious yet achievable, inspiring but specific.

---

## Vision statement

*For* people who need to manage their medical appointments and personal health information *who* face difficulties organizing appointments, medications, symptoms, and medical studies in one place, *GestionTurnosApp* *is a* mobile health management application *that* allows users to manage medical appointments, track medications and symptoms, store medical studies, and visualize health information in an organized and secure way, *unlike* traditional appointment management methods and disconnected health records, *our product* combines appointment management, personal health tracking, secure storage, notifications, and offline functionality in a single mobile application.

---

## Team mission

Our mission is to provide users with a reliable and secure mobile solution that simplifies the management of their medical appointments and personal health information. We seek to improve the user experience by integrating health management, offline access, security, and clear visualization in one application.

---

## Strategic pillars

| *Pillar* | *Description* | *Success metrics* |
|---|---|---|
| *Appointment Management* | Simplify the process of viewing, managing, and requesting medical appointments. | Successful appointment requests and access to appointment information. |
| *Personal Health Management* | Centralize medication tracking, symptom registration, health statistics, and medical studies. | Health records created and use of medication, symptom, statistics, and study features. |
| *Reliability and Offline Access* | Keep essential information accessible when the user has no internet connection. | Successful offline access and data synchronization. |
| *Security and Privacy* | Protect sensitive health information through authentication, biometric security, and secure data storage. | Successful authentication and protection of sensitive local data. |
| *User Experience* | Provide a clear, responsive, and pleasant interface with loading states, animations, empty states, and health-data visualization. | Successful completion of core user flows and use of visualization features. |

---

## High-level roadmap

text
H1 (Now) ───────────── H2 (Next) ───────────── H3 (Later)
 Core experience        Health expansion        Scale & evolution
       │                       │                       │
       ▼                       ▼                       ▼
 Appointment             Medical studies         Advanced health
 management              and communication       capabilities
 Authentication          Gamification            Scalability
 Offline capability      Health statistics       Optimization


| *Horizon* | *Period* | *Objective* | *Epics / Features* | *Uncertainty* |
|---|---|---|---|---|
| *H1 (Now)* | Q1 | Consolidate the core mobile health experience. | Authentication, appointment management, offline access, medication and symptom tracking. | Low |
| *H2 (Next)* | Q2 | Expand personal health-management capabilities. | Medical studies, health statistics, integrated chat, notifications, and achievement system. | Medium |
| *H3 (Later)* | Q3-Q4 | Improve scalability, reliability, and advanced health-management capabilities. | Synchronization optimization, medical-file management, analytics, and future integrations. | High |

---

## Product principles

1. *User-centered health management:* Every feature should make it easier for users to organize and access their medical information.
2. *Offline when it matters:* Essential information should remain available even without an internet connection.
3. *Security by design:* Sensitive health information must be protected through secure authentication and appropriate data storage.
4. *Simple and clear experience:* Interfaces should communicate loading, empty, and data states clearly while maintaining a responsive experience.
5. *Reliable architecture:* New functionality should preserve the separation of responsibilities and maintainability provided by Clean Architecture and MVVM.

---

## Product Definition of Done

> The product is "done" when it provides a reliable, secure, and usable mobile experience for managing medical appointments and personal health information.

*Objective:* Provide users with an integrated mobile solution for managing medical appointments and personal health information securely, reliably, and conveniently.

| *Key Result* | *Baseline* | *Target* | *Date* |
|---|---|---|---|
| *KR1: Core appointment management available* | Appointment module implemented | Users can view, manage, and request medical appointments successfully. | H1 |
| *KR2: Personal health information management* | Medication, symptoms, statistics, and studies modules available | Users can manage their main personal health information from one application. | H2 |
| *KR3: Offline access and synchronization* | OfflineCacheManager and Room implemented | Essential application data remains accessible offline and synchronizes when connectivity returns. | H1-H2 |
| *KR4: Secure access to sensitive information* | Authentication and biometric API implemented | Sensitive information is protected through secure authentication and storage. | H1 |
| *KR5: Reliable user experience* | Shimmer, Lottie, empty states, and charts implemented | Core user flows provide clear loading, empty, and data-visualization states. | H2 |

---

## Correlations

- Problem framing (the why) → 03-product/problem-framing.md
- Backlog that implements the vision → 04-requirements/user-stories.md
- KPIs in operations → 13-operations/README.md
- Technical foundation → Borrador del PDR Gestion_Turnos.md