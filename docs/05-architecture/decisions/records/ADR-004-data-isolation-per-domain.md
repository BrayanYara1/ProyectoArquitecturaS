# ADR-004: Cumplimiento del Aislamiento de Datos por Dominio (Regla 7.1)

* **Status:** Aceptado
* **Fecha:** 2026-09-29
* **Autores:** Equipo de Arquitectura - Salud Activa / GestionTurnosApp

---

## 1. Contexto y Regla de Arquitectura
De acuerdo con las políticas de gobernanza de arquitectura de software y el estándar **Regla 7.1 (Aislamiento de Datos)**:
> *"Cada dominio tiene su propia base de datos, en su propia instancia, con su propio volumen. Un motor compartido con esquemas separados no cumple esta regla."*

En arquitecturas orientadas a microservicios y *Bounded Contexts* (DDD), compartir un único motor o esquema de base de datos entre dominios rompe el principio de desacoplamiento y autonomía, generando acoplamiento implícito a nivel de persistencia.

---

## 2. Decisión
Se implementó la separación estricta e independiente de bases de datos para cada uno de los dominios de la aplicación (`Auth`, `Turnos`, `Medicamentos`, `Estudios`, `Chat`):

1. **Instancias y Volúmenes Independientes:**
   Cada dominio gestiona su propia conexión de base de datos a través de `mongoose.createConnection()` con URIs e instancias totalmente independientes (`AUTH_MONGODB_URI`, `TURNOS_MONGODB_URI`, `MEDICAMENTOS_MONGODB_URI`, `ESTUDIOS_MONGODB_URI`, `CHAT_MONGODB_URI`).

2. **Sin Esquemas ni Tablas Compartidas:**
   Los modelos Mongoose (`User`, `Turno`, `Medicamento`, `Estudio`, `Message`) se compilan y persisten exclusivamente en su respectiva instancia de base de datos aislada.

3. **Comunicación Exclusiva por API / DTOs:**
   Ningún dominio accede a los registros o colecciones de otro dominio directamente en la base de datos. La asociación entre entidades se realiza únicamente mediante identificadores desacoplados de dominio (`usuarioId: String`).

---

## 3. Justificación
- **Autonomía Total:** Si el servicio de Turnos o Medicamentos requiere mantenimiento o migración de datos, no afecta en absoluto a las bases de datos de Autenticación o Estudios.
- **Escalabilidad por Dominio:** Cada base de datos puede escalar de forma independiente en volumen y throughput según sus requerimientos de carga.
- **Cumplimiento Normativo:** Satisface al 100% los requisitos de evaluación de la asignatura de Arquitectura de Software (Regla 7.1).

---

## 4. Consecuencias
- **Positivas:** Alto nivel de desacoplamiento, aislamiento de fallos a nivel de almacenamiento y estricto apego al patrón *Database-per-Service*.
- **Ajustes:** Las consultas entre dominios no utilizan `JOIN` ni `.populate()` cruzados en base de datos; la consolidación de información se realiza a nivel de servicios o API Gateway.
