# Modelado del Contexto Acotado: Gestión de Turnos

Este documento describe el modelado táctico del dominio de Turnos siguiendo los principios de Diseño Orientado al Dominio (DDD).

## 1. Raíz Agregada (Aggregate Root)
*   **Turno:** Es la entidad principal que controla el ciclo de vida de una cita médica. Asegura que todas las reglas de negocio se cumplan antes de persistir cualquier cambio.

## 2. Entidades y Objetos de Valor (Value Objects)

### Entidades:
*   **Paciente:** Identificado por su ID único. En este contexto, solo nos interesa su relación con el turno.
*   **Profesional (Médico):** Identificado por su ID y su especialidad.

### Objetos de Valor (Value Objects):
*   **HorarioConsulta:** Contiene la fecha y hora de inicio y fin. Es inmutable.
*   **EstadoTurno:** Enum que define los estados (Programado, Cancelado, Completado, Ausente).
*   **MotivoConsulta:** Texto descriptivo inmutable.

## 3. Invariantes (Reglas de Negocio a Proteger)
*   **Disponibilidad Doble:** Un Profesional no puede tener dos turnos en el mismo intervalo de tiempo.
*   **Cruce de Paciente:** Un Paciente no puede agendar dos citas que se solapen en horario.
*   **Fecha Futura:** Un turno nuevo solo puede ser creado para una fecha y hora futura.
*   **Integridad de Estado:** Un turno en estado "Completado" no puede ser marcado como "Cancelado".
*   **Duración Mínima:** La diferencia entre la hora de inicio y fin debe ser de al menos 15 minutos.

## 4. Eventos de Dominio Emitidos
*   **TurnoAgendado:** Emitido cuando un nuevo turno se crea con éxito.
*   **TurnoCancelado:** Emitido cuando el paciente o el médico anulan la cita.
*   **TurnoReagendado:** Emitido cuando se modifica el horario de una cita existente.
*   **TurnoCompletado:** Emitido cuando el médico finaliza la consulta (este evento suele disparar la creación de una entrada en el *Historial Clínico*).

---
> [!TIP]
> Los límites y contratos de definición (Interfaces/Ports) de este contexto se formalizarán en la **Sesión 2 (Planificación)**.
