# Mapa de Contexto - Proyecto Distribuidos

Este mapa describe los límites y las relaciones entre los diferentes dominios del sistema de Gestión de Turnos.

```mermaid
graph TD
    IAM[Gestión de Identidad e Inicio de Sesión]
    Turnos[Gestión de Turnos y Citas]
    Historial[Historial Clínico y Resultados]
    Medicina[Gestión de Medicamentos]
    Chat[Comunicación Paciente-Médico]
    Gami[Gamificación y Estadísticas]

    IAM -- "Provee Usuario (Upstream)" --> Turnos
    IAM -- "Provee Usuario (Upstream)" --> Historial
    Turnos -- "Genera Evento (Customer/Supplier)" --> Historial
    Historial -- "Relaciona Datos" --> Medicina
    Medicina -- "Monitorea Cumplimiento" --> Gami
    Chat -- "Soporte" --> Turnos
```

## Relaciones Clave:
*   **IAM -> Todos:** Es un contexto núcleo (Core) que provee la identidad necesaria para el resto de los servicios.
*   **Turnos <-> Historial:** Existe una relación estrecha; cada cita finalizada alimenta el historial clínico del paciente.
*   **Medicina -> Gamificación:** El cumplimiento de las tomas de medicamentos dispara los logros y estadísticas de salud.
