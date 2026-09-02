# Design Patterns and Microservices Guide

This document serves as the project's pattern catalog. For each pattern, we define when to use it, when NOT to use it, and provide an implementation example. Patterns are tools to be used when a specific problem requires them, not rigid recipes.

---

## 1. Creational Patterns

### Factory Method
**Problem:** You want to create objects without exposing the creation logic or coupling code to a concrete type.
*   **When to use it:** When the exact type of object to create is not known until runtime or when creation involves complex validation logic.
*   **Domain Example (Kotlin):**
    ```kotlin
    class Appointment {
        companion object {
            fun create(patientId: String, date: LocalDateTime): Appointment {
                if (date.isBefore(LocalDateTime.now())) {
                    throw IllegalArgumentException("Date cannot be in the past")
                }
                return Appointment(UUID.randomUUID().toString(), patientId, date, Status.PENDING)
            }
        }
    }
    ```

### Builder
**Problem:** An object has many optional parameters, making construction unreadable or error-prone.
*   **When to use it:** Complex configuration objects or test data builders.
*   **Example:**
    ```kotlin
    val appointment = AppointmentBuilder()
        .withPatient("patient-001")
        .atDate(tomorrow)
        .withSpecialty("Cardiology")
        .build()
    ```

### Singleton
**Problem:** A class must have exactly one instance (e.g., a database connection).
*   **Warning:** Singletons make testing difficult. In this project, we prefer instances managed by the Dependency Injection (DI) container (Hilt) over hardcoded static instances.

---

## 2. Structural Patterns

### Adapter
**Problem:** You want to use an existing class, but its interface does not match the one your domain requires.
*   **When to use it:** Integration with external APIs (e.g., Payment Gateways) or third-party libraries.
*   **Example:**
    ```kotlin
    class StripeAdapter(private val stripeClient: StripeClient) : PaymentGateway {
        override fun process(amount: Double) {
            stripeClient.makeCharge(amount.toCents())
        }
    }
    ```

### Decorator
**Problem:** You want to add behavior to an object without modifying it or using inheritance.
*   **When to use it:** Adding logging, caching, or validation around existing repositories.

---

## 3. Microservices Patterns

### API Gateway
*   **Problem:** Clients need to call multiple services to get a complete response.
*   **Solution:** A single entry point that handles routing, authentication, and rate limiting.

### Backend for Frontend (BFF)
*   **Problem:** Mobile and Web clients need data in different formats but share the same internal API.
*   **Solution:** Create specific APIs for each client type to optimize payload size.

---

## 4. Resilience Patterns

### Circuit Breaker
*   **Problem:** A failing downstream service causes your service to hang or fail (cascading failure).
*   **States:**
    *   **CLOSED:** Normal operation.
    *   **OPEN:** Failure threshold reached; calls are blocked immediately to allow the target service to recover.
    *   **HALF-OPEN:** Test calls are allowed to check if the service has recovered.

### Retry with Exponential Backoff
*   **Problem:** Transient network failures or service restarts.
*   **Solution:** Retry the operation with increasing delays (e.g., 1s, 2s, 4s) plus random "jitter" to avoid overwhelming the system.

---

## 5. Data and Consistency Patterns

### Database per Service
*   **Rule:** Each microservice has its own private database. No service directly accesses another service's database.

### Saga (Distributed Transactions)
*   **Problem:** A business transaction spans multiple services.
*   **Solution:** A sequence of local transactions. If one step fails, "compensating transactions" are executed to undo the previous successful steps.

### Outbox Pattern
*   **Problem:** Guaranteeing that a database update and a message publication happen atomically.
*   **Solution:** Save the event in an "Outbox" table within the same database transaction as the business data. A separate process then reads the table and publishes the events.

### CQRS (Command Query Responsibility Segregation)
*   **Problem:** The logic for writing data is vastly different from the logic for reading it.
*   **Solution:** Separate the write model (Commands) from the read model (Queries) to optimize performance.

---

## 6. When NOT to use these patterns

*   **CQRS:** Do not use if the read and write models are similar.
*   **Event Sourcing:** Do not use if you don't need a full audit trail.
*   **Saga:** Do not use if the transaction can fit within a single service using standard ACID transactions.
*   **BFF:** Do not use if all clients have the same data requirements.

---

## 7. Adopted Patterns in Salud Activa

*   **Repository Pattern:** Adopted for all data access in the Android App.
*   **MVVM:** Adopted for all UI features.
*   **Dependency Injection:** Adopted via Hilt.
*   **Offline Sync:** Adopted using `SyncWorker.kt`.
*   **API Gateway:** Adopted via AWS Application Load Balancer (ALB).
