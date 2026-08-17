# ADR-001: Selección de Estilo Arquitectónico - Hexagonal (Puertos y Adaptadores)

## Estado
Aprobado

## Contexto
El sistema debe ser altamente distribuido, permitiendo que diferentes servicios (como Auth, Turnos o Historial) escalen de forma independiente. Además, necesitamos asegurar que la lógica de negocio (dominio) sea inmune a los cambios en las tecnologías externas (Bases de datos, APIs de terceros, UI).

## Decisión
Hemos elegido la **Arquitectura Hexagonal**. Este patrón nos permite desacoplar el núcleo de negocio de la infraestructura mediante el uso de **Puertos** (Interfaces) y **Adaptadores** (Implementaciones).

## Ruta de Decisión
1.  **¿Necesitamos alta testabilidad?** Sí -> Arquitectura en capas o hexagonal.
2.  **¿Habrá cambios frecuentes en infraestructura?** (Ej. cambiar de Room a SQL remoto o Firebase) Sí -> Hexagonal es superior.
3.  **¿Complejidad del dominio?** Moderada-Alta -> Hexagonal facilita el DDD (Domain Driven Design).

## Consecuencias
*   **Positivas:** 
    *   Independencia total del framework.
    *   Facilidad para realizar pruebas unitarias del dominio sin mocks complejos.
    *   Preparado para microservicios.
*   **Negativas:**
    *   Mayor cantidad de código inicial (Boilerplate).
    *   Curva de aprendizaje inicial para el equipo.
