System Overview - Salud Activa
What is Salud Activa?
Salud Activa is a comprehensive digital platform designed to modernize and simplify medical appointment management and patient wellbeing. It bridges the gap between healthcare professionals and patients by providing a seamless, real-time interface for scheduling, tracking medical history, and managing clinical agendas.
The system solves the common friction of traditional medical scheduling—such as long wait times, manual booking errors, and missed appointments—by centralizing the entire experience into a modern mobile application supported by a robust cloud infrastructure. For the patient, it is a personal health assistant; for the medical professional, it is a high-performance administrative tool.
 Key Objectives
•
Simplify Access to Health: Allow patients to find specialists and book appointments in seconds from their mobile devices.
•
Reduce Absenteeism: Use automated reminders and notifications to ensure patients attend their scheduled consultations.
•
Empower Professionals: Provide doctors with a clear, real-time view of their agenda and patient records, reducing administrative overhead.
•
Ensure Data Security: Protect sensitive medical information using industry-standard encryption and secure cloud storage.
👤 User Profiles
1.
Patient: The primary user who searches for specialties, manages their appointments, and tracks their health stats.
2.
Medical Professional: The provider who manages their availability, views their daily agenda, and interacts with patient records.
3.
Administrator: The business user responsible for managing medical centers, staff rosters, and system-wide configurations.
🔗 Related Documentation
•
Project Glossary:   docs/02-domain/glossary.md
•
Architecture Overview:   docs/05-architecture/overview.md
•
User Manual:   docs/04-requirements/user-manual.md

## Problem it solves

**Before the system:**
Medical appointment management was a manual and inefficient process. Patients were forced to make phone calls during restricted business hours or visit clinics in person just to check for availability. Clinical agendas were often kept on paper or in fragmented spreadsheets, leading to frequent double-booking errors. Additionally, the lack of an automated reminder system resulted in high rates of patient no-shows, and medical records were scattered across different physical files.

**With the system:**
Salud Activa digitizes and centralizes the entire medical experience. Patients have 24/7 access to search for specialists and book appointments instantly from their Android devices. Automated push notifications significantly reduce absenteeism by keeping patients informed of upcoming consultations. Doctors gain a real-time, conflict-free view of their daily agenda and immediate access to digital medical histories, while the entire organization benefits from a secure, scalable AWS cloud infrastructure.

## Main users

| Role | Description | What they do in the system |
| :--- | :--- | :--- |
| **Patient** | The individual seeking medical services. | Searches for medical specialties, schedules/cancels appointments, views their own medical history, and receives health reminders. |
| **Doctor** | Healthcare provider / Professional. | Manages their daily clinical agenda, consults patient records before appointments, and updates patient health status. |
| **Administrator** | Business and infrastructure manager. | Manages the staff roster, configures medical centers and specialties, oversees global appointment data, and monitors system logs. |

---

## Related Documentation
- **Architecture Overview:** [overview.md](file:///docs/05-architecture/overview.md)
- **Domain Glossary:** [glossary.md](file:///docs/02-domain/glossary.md)
- **Security Standards:** [security-standards.md](file:///docs/00-governance/security-standards.md)

## Technology Stack - Salud Activa

| Layer | Technology | Justification |
| :--- | :--- | :--- |
| **Frontend (Mobile)** | **Android Native (Kotlin)** | Native performance, deep integration with Android Keystore for security, and modern UI using **Jetpack Compose**. |
| **Backend** | **Node.js (Express)** | Optimized for asynchronous I/O, providing high concurrency for appointment scheduling and fast JSON processing. |
| **Database** | **MongoDB Atlas + AWS RDS** | **MongoDB** for flexible patient records and clinical data; **PostgreSQL (RDS)** for relational transactional consistency. |
| **Communications** | **FCM (Firebase)** | Industry standard for reliable, real-time push notifications and appointment reminders on Android. |
| **Infrastructure** | **AWS (ECS Fargate)** | Serverless container orchestration for high availability and automatic scaling without managing underlying servers. |
| **IaC** | **Terraform** | Enables **Infrastructure as Code** to ensure reproducible and documented cloud environments across Dev and Prod. |

---

## Current Status

- **Phase:** **Active Development** (Currently in Sprint 12).
- **Current Version:** `v1.2.0`
- **Last Release:** August 14, 2026 (Core appointment module deployed to AWS Staging).
- **Next Milestone:** **Full Real-time Chat & Production Migration** — Scheduled for August 28, 2026.

---

## Project Contacts

| Role | Name | Contact |
| :--- | :--- | :--- |
| **Tech Lead** | [Name] | [email/Slack] |
| **Product Owner** | [Name] | [email/Slack] |
| **Mobile Lead** | [Name] | [email/Slack] |
| **DevOps / Cloud** | [Name] | [email/Slack] |

---

## 🔗 Related Documentation
- **Engineering Handbook:** [engineering-handbook.md](file:///docs/00-governance/engineering-handbook.md)
- **Deployment Guide:** [aws-ecs-setup.md](file:///docs/13-operations/aws-ecs-setup.md)
- **API Contracts:** [openapi/](file:///docs/07-api/contracts/openapi/)