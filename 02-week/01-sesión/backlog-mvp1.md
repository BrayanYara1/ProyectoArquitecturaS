# Backlog de Producto - MVP 1.0

Este documento contiene las primeras historias de usuario (HU) críticas para el arranque del desarrollo.

## Historias de Usuario (Pendientes)

### HU-001: Autenticación Segura (IAM)
**Como** paciente, **quiero** registrarme e iniciar sesión de forma segura **para** que mis datos de salud estén protegidos.

**Criterios de Aceptación:**
*   [ ] El registro debe validar que el email tenga un formato correcto y la contraseña tenga al menos 8 caracteres.
*   [ ] El inicio de sesión debe retornar un token JWT válido (o mock exitoso) si las credenciales coinciden con la base de datos.
*   [ ] El sistema debe mostrar un mensaje de error claro ("Usuario o contraseña incorrectos") si la validación falla.
*   [ ] **Verificación:** Probar con una cuenta inexistente y verificar que no permita el acceso.

### HU-002: Visualización de Próximos Turnos
**Como** usuario, **quiero** ver una lista de mis citas agendadas **para** organizar mi tiempo médico.

**Criterios de Aceptación:**
*   [ ] La lista debe mostrar: Especialidad, Nombre del Médico, Fecha y Hora.
*   [ ] Si no hay turnos, debe mostrarse una vista de "Estado Vacío" con un botón para solicitar uno.
*   [ ] Los turnos deben cargarse desde la fuente de datos (Infrastructure Layer) sin bloquear la interfaz.
*   [ ] **Verificación:** Insertar un turno manualmente en el JSON/BD y comprobar que aparece en la lista al refrescar.

### HU-003: Registro de Toma de Medicamento
**Como** paciente, **quiero** marcar cuando he tomado una medicina **para** llevar un control de mi tratamiento.

**Criterios de Aceptación:**
*   [ ] Al hacer clic en el recordatorio, el estado del medicamento debe cambiar a "Tomado" para el día actual.
*   [ ] El sistema debe registrar la hora exacta de la acción.
*   [ ] El usuario debe poder deshacer la acción en caso de error en los primeros 5 minutos.
*   [ ] **Verificación:** Verificar en los logs o en la base de datos local que el registro de cumplimiento se ha creado correctamente.
