# Planificación MVP 1.0: Contratos, Datos y Historias Verticales

Este documento define la integración técnica y el desglose de la primera característica vertical para el MVP 1.0.

## 1. Contratos entre Servicios

### Comunicación Sincrónica (REST/gRPC)
*   **IAM -> Turnos:** El servicio de Turnos consulta al IAM para validar la identidad y el rol del usuario antes de permitir un agendamiento.
*   **Turnos -> Perfil:** Consulta de datos básicos del paciente (nombre, edad) para pre-llenar la solicitud de cita.

### Comunicación Asíncrona (Eventos de Dominio)
*   **Evento `TurnoCompletado`:** Emitido por el servicio de **Turnos** cuando el médico finaliza la consulta. Consumido por el servicio de **Historial Clínico** para generar automáticamente un borrador de la evolución médica.
*   **Evento `TurnoAgendado`:** Consumido por el servicio de **Notificaciones** para enviar el recordatorio al paciente vía FCM.

## 2. Propiedad de los Datos (Data Ownership)

| Entidad | Servicio Propietario | Razón |
| :--- | :--- | :--- |
| **Usuario / Credenciales** | `Servicio IAM` | Centralización de la seguridad y tokens de acceso. |
| **Cita / Disponibilidad** | `Servicio de Turnos` | Gestión exclusiva de la agenda médica y estados del turno. |
| **Registros Clínicos** | `Servicio de Historial` | Almacenamiento de largo plazo de datos sensibles de salud. |
| **Prescripciones** | `Servicio de Medicamentos`| Control de inventario y alarmas de toma. |

## 3. Anti-Corruption Layers (ACLs)

Para proteger nuestro dominio de cambios en sistemas externos:
*   **ACL-FCM:** Adaptador que traduce nuestros eventos de dominio (`TurnoAgendado`) al formato específico de Firebase Cloud Messaging.
*   **ACL-GoogleMaps:** Interfaz que abstrae la búsqueda de geolocalización de clínicas, evitando que el SDK de Google Maps se filtre en la lógica de negocio de Turnos.

## 4. Característica Vertical: Agendamiento Completo de Citas

### HU-004: Selección de Especialidad y Profesional
**Criterio de Aceptación:**
*   [ ] El sistema debe listar las especialidades activas.
*   [ ] Al elegir una especialidad, solo deben mostrarse los médicos calificados para esa área.
*   [ ] **Verificación:** Filtrar por "Cardiología" y comprobar que solo aparezcan cardiólogos.

### HU-005: Selección de Horario Disponible
**Criterio de Aceptación:**
*   [ ] El calendario solo debe permitir elegir fechas futuras.
*   [ ] El sistema debe ocultar o deshabilitar los horarios que ya estén ocupados por otros pacientes.
*   [ ] **Verificación:** Agendar un turno a las 10:00 AM y comprobar que el horario desaparece para otros usuarios.

### HU-006: Confirmación y Resumen del Turno
**Criterio de Aceptación:**
*   [ ] Antes de finalizar, se debe mostrar un resumen: Médico, Fecha, Hora y Lugar.
*   [ ] El sistema debe emitir el evento `TurnoAgendado` tras la confirmación exitosa.
*   [ ] **Verificación:** Confirmar un turno y verificar que llegue la notificación push al dispositivo.
