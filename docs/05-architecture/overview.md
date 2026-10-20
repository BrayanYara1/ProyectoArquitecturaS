# System Architecture Overview

# Technical Documentation: Salud Activa Project

This document serves as the master technical reference for the Salud Activa ecosystem, encompassing the Android application, Node.js backend, and AWS infrastructure.

> ℹ️ **Nota de Arquitectura Actualizada:**
> La arquitectura oficial del proyecto ha migrado de Fargate + RDS PostgreSQL a **contenedores Docker Compose en AWS EC2 (t3.micro)** con **5 bases de datos de dominio aisladas en MongoDB 7.0**.
> Para los diagramas formales y actualizados, consulta 👉 **[docs/arquitectura/README.md](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/arquitectura/README.md)**.

---

## 1. Architectural Decision Record: ADR-001

**Title:** Documentation Language
**ID:** ADR-001
**Date:** 2024-01-10
**Status:** Accepted
**Authors:** Maria Garcia — Tech Lead

### Context
The software industry standard operates in English. Mixing languages across artifacts creates a cognitive translation boundary.

### Decision
We decided to use English for all documentation and code.

---

## 2. System Architecture Overview

### Adopted Architectural Style
**Style:** Modular Monolith / Clean Architecture / Hexagonal Architecture (Ports & Adapters).

### Architecture Topology
*   **Android Client:** Kotlin/MVVM App utilizing Retrofit 2 and OkHttp.
*   **AWS Infrastructure:** AWS EC2 Instance (t3.micro) running Docker Compose (`web`, `api`, `mongo`).
*   **Database:** MongoDB 7.0 with isolated domain databases (`SaludActiva_auth`, `SaludActiva_turnos`, `SaludActiva_medicamentos`, `SaludActiva_estudios`, `SaludActiva_chat`).
*   **CI/CD:** Automated pipeline with GitHub Actions, Trivy vulnerability scanning, GHCR publishing, and SSH deployment.

---

## 3. Non-Functional Requirements (NFR)

### Performance & Availability
*   **Health Check Endpoint:** `GET /api/health` monitoring all 5 domain database connections.
*   **Production Deployment:** Containerized Docker Compose on AWS EC2.
*   **CI/CD Automated Execution:** `npm test` runs in isolated environment prior to build.

---

## 4. Hexagonal Architecture Guide

Dependencies point inward: **Infrastructure -> Application -> Domain**. Domain entities and ports remain free from framework dependencies.
