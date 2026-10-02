# ADR-004: Cumplimiento de Anexo J — Base de Datos Única por Motor y Esquema por Dominio

* **Status:** Aceptado (Conforme a Anexo J 2026B)
* **Fecha:** 2026-09-29
* **Autores:** Equipo de Arquitectura - Salud Activa / GestionTurnosApp

---

## 1. Contexto y Anexo J (Corrección a la Norma)
Conforme a la expedición del **Anexo J — Corrección: Arquitectura del proyecto y base de datos única (2026B)**:
- Se deroga la exigencia previa de tener instancias de base de datos y contenedores separados por dominio (numerales 7.1 y 4.5.4 derogados).
- **Nueva Exigencia (Numerales J.2, J.3 y J.4):**
  - **Instancia Única por Motor:** Existe una única instancia y contenedor de MongoDB (y PostgreSQL) con su respectivo volumen persistente.
  - **Base de Datos / Esquema por Dominio dentro de la Instancia Única:** Para MongoDB, *"el equivalente del esquema es una base de datos por dominio dentro de esa instancia"* (`SaludActiva_auth`, `SaludActiva_turnos`, `SaludActiva_medicamentos`, `SaludActiva_estudios`, `SaludActiva_chat`).
  - **Aislamiento de Escritura:** Cada servicio escribe exclusivamente en la base/esquema de su dominio (`<dominio>_app`).

---

## 2. Decisión de Arquitectura
Se actualizó la persistencia del backend Node.js en [`backend/config/dbConnections.js`](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/backend/config/dbConnections.js) para cumplir al 100% con las disposiciones de Anexo J:

1. **Instancia Única con Conexiones Lógicas por Dominio:**
   El backend se conecta a la instancia única de MongoDB Atlas o local (`MONGODB_URI`), derivando la base de datos específica de cada dominio dentro de esa misma instancia:
   - **Auth Domain:** `SaludActiva_auth`
   - **Turnos Domain:** `SaludActiva_turnos`
   - **Medicamentos Domain:** `SaludActiva_medicamentos`
   - **Estudios Domain:** `SaludActiva_estudios`
   - **Chat Domain:** `SaludActiva_chat`

2. **Acceso Exclusivo y Seguridad:**
   Cada repositorio de persistencia opera de forma aislada sobre la base de datos de su dominio correspondiente sin realizar escrituras cruzadas ni compartir tablas entre dominios.

---

## 3. Justificación y Beneficios
- **Alineación con Anexo J:** Prevalece y reemplaza el numeral 7 previo de la norma.
- **Gestión Eficiente de Recursos:** Un solo contenedor/instancia con un único volumen persistente para MongoDB, mientras se mantiene la separación lógica de esquemas por dominio.
- **Resiliencia:** Manejo independiente de reconexión por dominio sin riesgo de caídas globales.
