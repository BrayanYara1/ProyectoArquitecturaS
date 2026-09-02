# Domain Events

> **What to fill in here:** A domain event is a fact that occurred in the business.
> They are the backbone of asynchronous communication between bounded contexts.
> The name is ALWAYS in past tense and in the ubiquitous language of the domain.

---

# Domain Events - Salud Activa

This document defines the asynchronous communication backbone of the **Salud Activa** (GestionTurnosApp) ecosystem. Domain events represent immutable facts that have occurred in our business processes.

---

## 📢 What is a Domain Event?

A **Domain Event** communicates that something important occurred in the business. It is an immutable message that describes a fact in the **past tense**.

*   **✓ `AppointmentScheduled`**
*   **✓ `AppointmentCancelled`**
*   **✓ `MedicalRecordUpdated`**
*   **✓ `PatientRegistered`**

### Difference between Command and Event

| Concept | Intent | Tense | Can fail? |
| :--- | :--- | :--- | :--- |
| **Command** | Instruction to do something (intent) | Present | **Yes** (Validation can fail) |
| **Event** | Notification of a fact (occurred) | Past | **No** (It already happened) |

**Flow Example:**
`User (Android App) → [ScheduleAppointment] (Command) → Backend → [AppointmentScheduled] (Event) → Consumers (FCM/Analytics)`

---

## 🗂️ Event Catalog

### Event: `AppointmentScheduled`

| Field | Value |
| :--- | :--- |
| **Name** | `AppointmentScheduled` |
| **Bounded Context** | `Appointments` |
| **Aggregate** | `Appointment` |
| **Trigger** | A patient successfully books a medical slot in the app. |
| **Consumers** | `NotificationService` (FCM), `AnalyticsService`, `AuditLog`. |
| **Channel (Topic)** | `salud-activa.appointments.scheduled` |
| **Schema Version** | `v1` |
| **Delivery Guarantee** | **At-least-once** (Requires idempotency in consumers). |

**Payload Example:**
```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "eventType": "AppointmentScheduled",
  "aggregateId": "appt-12345",
  "aggregateType": "Appointment",
  "occurredAt": "2026-08-23T19:30:00Z",
  "version": 1,
  "payload": {
    "patientId": "user-789",
    "doctorId": "doc-456",
    "specialty": "Cardiology",
    "appointmentDate": "2026-08-25T10:00:00Z",
    "clinicId": "central-clinic"
  },
  "metadata": {
    "correlationId": "corr-abc-123",
    "userId": "user-789"
  }
}

[Patient (Android App)]
  │
  │  ScheduleAppointment (command)
  ▼
[Aggregate: Appointment]
  │
  │  AppointmentScheduled (event)
  ├──────────────────────────────────┐
  │                                   ▼
  │                          [Service: Notifications (FCM)]
  │                          Sends Push Notification to Patient
  │                          "Your appointment is confirmed!"
  │
  │  AppointmentScheduled (event)
  └──────────────────────────────────┐
                                      ▼
                            [Service: Analytics]
                            Updates daily booking metrics

## Schema evolution strategy

Events are contracts. We follow strict rules to avoid breaking the Android App or Backend Services.
•
Compatible Changes (Safe):
◦
Add a new optional field to the payload.
◦
Add a new event type.
•
Incompatible Changes (Breaking):
◦
Remove a field from the payload.
◦
Change a data type (e.g., String to Number).
◦
Rename the event.
Evolution Process:
1.
Publish EventV2 alongside EventV1.
2.
Migrate all consumers (App/Services) to EventV2.
3.
Stop publishing EventV1 after verification.
🛡️ Resilience & Idempotency
Since networks are unstable (especially on mobile), we assume events might be delivered more than once. Consumers MUST be idempotent.
// Idempotent consumer example (Node.js)
async function handleEvent(event) {
  // 1. Check if eventId was already processed
  if (await db.processedEvents.exists({ eventId: event.eventId })) {
    return; // Ignore duplicate
  }

  // 2. Perform business logic (e.g., updating stats)
  await processAppointment(event.payload);

  // 3. Mark as processed
  await db.processedEvents.create({ eventId: event.eventId });
}

    logger.info(`Event ${event.eventId} already processed, ignoring`);
    return;
  }

  // 2. Process the event
  await updateModel(event.payload);

  // 3. Mark as processed (in the same transaction)
  await markEventProcessed(event.eventId);
}
Event Resilience & Reliability - Salud Activa
This document defines the strategies used to ensure that business events are processed reliably across the Salud Activa (GestionTurnosApp) ecosystem, even in the event of network instability or temporary service outages.
🛡️ 1. At-Least-Once Delivery + Idempotency
Dead Letter Queue (DLQ)
Our message broker (SNS/SQS or RabbitMQ) guarantees that an event will be delivered at least once. However, due to network retries or transient errors, a consumer might receive the same event multiple times.
When an event fails after N retries, it goes to the DLQ.
Configuration
Recommended value
To prevent side effects (such as sending duplicate push notifications or double-booking a slot), all consumers MUST be idempotent.
Retries before DLQ
3-5
Idempotent Consumer Implementation (Node.js Example)
Backoff
Exponential (1s → 2s → 4s → 8s)
DLQ retention
7 days
Every consumer must store the unique eventId of processed messages to act as a safeguard.

/**
 * Idempotent consumer for Appointment events.
 * Safeguards against duplicate processing of the same business fact.
 */
async function processAppointmentScheduledEvent(event: AppointmentScheduled): Promise<void> {
  // 1. Check if the event was already successfully processed
  if (await eventStore.isProcessed(event.eventId)) {
    logger.info(`Event ${event.eventId} already processed. Skipping to avoid duplicates.`);
    return;
  }

  try {
    // 2. Execute business logic (e.g., updating appointment stats)
    await medicalService.updateAgendaMetrics(event.payload);

    // 3. Mark as processed (ideally in the same DB transaction as step 2)
    await eventStore.markAsProcessed(event.eventId);
    
    logger.info(`Successfully processed event: ${event.eventType} [ID: ${event.eventId}]`);
  } catch (error) {
    logger.error(`Failed to process event ${event.eventId}: ${error.message}`);
    throw error; // Re-throw to trigger broker retry mechanism
  }
}

2. Dead Letter Queue (DLQ)
If an event fails repeatedly despite retries, it is moved to a Dead Letter Queue (DLQ). This prevents a single "poison pill" message from blocking the entire processing pipeline.
Standard Configuration
Parameter
Recommended Value
Description
Retries before DLQ
5
Total attempts before giving up on the message.
Backoff Strategy
Exponential
Delay between retries: 1s → 2s → 4s → 8s → 16s.
DLQ Retention
7 Days
Duration the failed message stays in the DLQ for inspection.
Monitoring Alert
DLQ_Depth > 0
Immediate alert to DevOps/Tech Lead when a message enters the DLQ.
🚦 3. Operational Recovery
When a message lands in the DLQ:
1.
Analyze: The runbook.md for the specific service provides steps to inspect the event payload and failure reason.
2.
Fix: Resolve the underlying issue (e.g., a database timeout or a code bug).
3.
Replay: Once fixed, use the management CLI to move the message back to the primary queue for reprocessing.
🔗 Related Documentation
•
Microservice Runbook:   docs/09-microservices/services/NN-service/runbook.md
•
Domain Event Catalog:   docs/02-domain/domain-events.md
•
Error Handling Strategy:   docs/00-governance/error-handling.md