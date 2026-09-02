# Problem Framing — Problem Definition

# Problem Definition - Salud Activa

This document defines the core challenges **Salud Activa** (GestionTurnosApp) aims to solve, ensuring that all technical development in Android, Node.js, and AWS aligns with the business goals.

---

## 1. The Problem in One Sentence

**Patients and Healthcare Professionals** who **manage medical appointments** struggle with **inefficient scheduling, double-booking errors, and high no-show rates** because **current manual or legacy processes lack real-time synchronization and automated notifications**, resulting in **20% absenteeism and significant administrative overhead for clinics**.

---

## 2. Affected Users

| Segment | Description | Estimated Size | Priority |
| :--- | :--- | :--- | :--- |
| **Patients** | Individuals seeking medical care who value time and convenience. | ~10,000 users | High |
| **Doctors** | Healthcare providers needing a clean, organized, and conflict-free agenda. | ~50 professionals | High |
| **Clinic Admins** | Staff responsible for coordinating doctors and ensuring the center operates profitably. | ~5 users | Medium |

### Jobs-to-be-Done (JTBD)

> **When** I need medical attention or a routine checkup,
> **I want** to see real-time availability and book an appointment instantly from my phone,
> **so that** I don't waste time on phone calls or travel to a clinic only to find no slots are available.

---

## 3. Evidence of the Problem

| Evidence Type | Source | Key Finding |
| :--- | :--- | :--- |
| **Support Data** | Backend logs (Error 500 phase) | High frequency of users abandoning the booking flow due to server instability and lack of feedback. |
| **Direct Observation** | Clinic Shadowing | Receptionists spend ~4 hours daily manually calling patients to confirm attendance. |
| **Benchmarking** | Competitive Analysis | Local competitors lack a native Android experience with real-time push notifications (FCM). |

---

## 4. Current User Solution (and its problems)

| Current Solution | Limitations | Cost/Friction |
| :--- | :--- | :--- |
| **Phone Calls / WhatsApp** | Limited to business hours; no real-time calendar view. | ~15 mins per booking; high risk of human error. |
| **Paper Agendas** | Zero searchability; no backups; impossible to share between staff. | Causes ~10% double-booking rate. |

---

## 5. Solution Hypothesis

**We believe that** a native Android platform with real-time Node.js synchronization and automated FCM reminders **for** patients and medical professionals, **will achieve** a drastic reduction in no-shows and administrative manual work. **We will know we succeeded when** the no-show rate drops below **10%** and the server diagnostic tools confirm zero unhandled 500 errors.

---

## 6. Success Metrics (North Star)

| Metric | Current Baseline | 6-Month Target | Measurement Tool |
| :--- | :--- | :--- | :--- |
| **Absenteeism Rate** | 20% | < 10% | Backend Event Logs |
| **Avg. Booking Time** | 15 minutes (manual) | < 60 seconds (app) | Firebase Analytics |
| **Server Reliability** | 95% (due to 500s) | 99.9% | AWS CloudWatch |

**North Star Metric:** **Total Completed Appointments per Month.**

---

## 7. Hypothesis Risks

| Risk | Probability | Impact | Validation Experiment |
| :--- | :--- | :--- | :--- |
| **Technical Instability** | Medium | High | Rigorous Stress testing of the AWS ECS/Fargate infrastructure. |
| **Low Adoption** | Low | High | Pilot program with a single partner clinic (Beta testing). |
| **Notification Fatigue** | Medium | Medium | A/B testing reminder frequency (24h vs 2h before). |

---

## 8. Out of Scope (What we are NOT solving now)

*   **Integrated Payment Gateway:** Payments will remain outside the app for the MVP to reduce PCI compliance complexity.
*   **Full Electronic Health Record (EHR):** We will store appointment notes, but not a full longitudinal history (Lab results, X-rays).
*   **Video Consultations (Telemedicine):** The initial version focuses exclusively on in-person appointments.

---

## 🔗 Correlations
- **Product Vision:** [`docs/03-product/vision.md`](file:///docs/03-product/vision.md)
- **Security Standards:** [`docs/00-governance/security-standards.md`](file:///docs/00-governance/security-standards.md)
- **Infrastructure Docs:** [`docs/13-operations/aws-ecs-setup.md`](file:///docs/13-operations/aws-ecs-setup.md)