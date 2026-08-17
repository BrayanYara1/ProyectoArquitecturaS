# ADR-001: Architectural Style - Hexagonal Architecture (Ports and Adapters)

## Status
Proposed

## Context
The "ProyectDistribuidos" (GestionTurnosApp) requires a robust architecture that facilitates testing, scalability, and independence from external technologies. As a distributed system, the business logic must be isolated from infrastructure concerns like databases, external APIs, and user interfaces.

## Decision
We have chosen to implement **Hexagonal Architecture** (also known as Ports and Adapters). 

This style organizes the application into:
1.  **Core (Domain/Application):** Contains the business logic, entities, and use cases. It defines "Ports" (interfaces).
2.  **Adapters:** Implement the Ports to connect the Core with external systems.

## Rationale
-   **Testability:** Business logic can be tested in isolation.
-   **Maintainability:** Changes in external tools do not affect the domain logic.
-   **Distributed Ready:** Facilitates communication between different microservices or modules.

## Consequences
-   **Pros:** High decoupling, easier long-term maintenance.
-   **Cons:** Increased initial complexity due to more boilerplate.
