# Esbozo de Arquitectura Candidata - Proyecto Distribuidos

Este documento describe la propuesta inicial de arquitectura para el sistema de gestión de turnos médicos, identificando los contextos delimitados y la estrategia de servicios.

## Contextos Delimitados (Bounded Contexts)

1. **Gestión de Identidad y Acceso (IAM):** Maneja la autenticación, autorización y perfiles de usuario. Es el núcleo de seguridad.
2. **Gestión de Turnos y Citas:** Responsable de la agenda médica, disponibilidad de profesionales y el proceso de reserva de citas.
3. **Historial Clínico y Seguimiento:** Almacena registros médicos, resultados de estudios y evoluciones de salud del paciente.
4. **Gestión de Medicamentos:** Controla prescripciones, recordatorios de tomas y registros históricos de medicación.
5. **Comunicación (Chat):** Facilita la interacción asíncrona entre el paciente y el personal médico.
6. **Gamificación y Engagement:** Gestiona logros, estadísticas de salud y mecanismos para motivar al paciente.

## Propuesta de Servicios

### Contextos que podrían ser servicios independientes:
* **Servicio de Usuarios (IAM):** Debe ser un microservicio independiente debido a su criticidad y necesidad de alta disponibilidad.
* **Servicio de Mensajería:** Ideal para ser independiente, permitiendo el uso de tecnologías de tiempo real (WebSockets/gRPC) sin afectar al resto del sistema.

### Contextos que se mantienen juntos (Inicialmente):
* **Turnos + Historial Clínico:** En el MVP, estos contextos se mantendrán en un mismo servicio. La razón es la alta cohesión: una cita médica casi siempre genera o modifica una entrada en el historial clínico. Mantenerlos juntos reduce la complejidad de transacciones distribuidas y latencia.
* **Medicamentos + Gamificación:** Se mantendrán juntos para facilitar la lógica de recompensas basada en el cumplimiento del tratamiento.

## Justificación
La arquitectura busca un equilibrio entre **desacoplamiento** y **simplicidad operativa**. Separar IAM permite centralizar la seguridad, mientras que agrupar Turnos e Historial facilita la consistencia de datos necesaria para la experiencia del paciente en esta etapa temprana.

> [!IMPORTANT]
> Esta propuesta será discutida y formalizada como **ADR-002** en la **Sesión 2 (Planificación)**.
