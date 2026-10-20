# Salud Activa — Plataforma de Gestión Médica

**Salud Activa** es un ecosistema profesional para la gestión de turnos médicos, medicamentos, estudios y chat con profesionales de la salud. Se compone de una aplicación móvil Android nativa (Kotlin/MVVM), un cliente web estático (Nginx), un backend API resiliente (Node.js 22 + Express) y despliegue automatizado en infraestructura AWS EC2.

---

## 🏛️ Arquitectura del Sistema

El proyecto está diseñado bajo principios de **Clean Architecture**, **Arquitectura Hexagonal (Ports & Adapters)** y **DevSecOps (12-Factor App)**.

Para consultar los diagramas formales y detallados C4 Nivel 1 (Contexto), C4 Nivel 2 (Contenedores), Componentes del Backend, Despliegue en EC2 y Pipeline CI/CD, visita:
👉 **[Documentación Completa de Arquitectura (docs/arquitectura/README.md)](file:///C:/Users/andyb/AndroidStudioProjects/GestionTurnosApp/docs/arquitectura/README.md)**

*(Nota: Menciones anteriores a AWS Fargate o RDS PostgreSQL corresponden al plan de arquitectura previa, sustituido por la arquitectura de contenedores Docker Compose y 5 bases de datos de dominio aisladas en MongoDB 7).*

---

## 🚀 Correr en Local (Un solo comando)

Gracias a Docker Compose, puedes levantar el entorno local completo (Backend API, Cliente Web Nginx y MongoDB) con un solo comando:

```bash
# 1. Copiar el archivo de configuración de entorno
cp backend/.env.example .env

# 2. Construir y levantar los contenedores
docker compose up --build
```

### URLs del Entorno Local
- 🌐 **Cliente Web:** `http://localhost:8080`
- 📡 **Backend API (Directo):** `http://localhost:3000/api`
- 🩺 **Endpoint de Salud (Healthcheck):** `http://localhost:8080/api/health`

---

## 📋 Tabla de Variables de Entorno

| Variable | Descripción | Valor por Defecto / Ejemplo | Obligatorio |
| :--- | :--- | :--- | :--- |
| `PORT` | Puerto HTTP para el servidor Backend API | `3000` | Sí |
| `MONGODB_URI` | Cadena de conexión base a MongoDB | `mongodb://mongo:27017/SaludActiva` | Sí |
| `JWT_SECRET` | Clave secreta para la firma y verificación de tokens JWT | `CambiarPorUnSecretoSeguroEnProduccion2026` | Sí |
| `CORS_ORIGIN` | Dominios autorizados para solicitudes CORS | `*` | No |
| `ADMIN_KEY` | Clave de seguridad para endpoints administrativos | `saludactiva_admin_secret_key_2026` | No |
| `APP_VERSION` | Versión actual de la aplicación y la API | `1.2.1` | No |
| `FIREBASE_SERVICE_ACCOUNT` | JSON de credenciales de Firebase para notificaciones Push | (Simulación si no se provee) | No |

---

## 📱 Aplicación Móvil Android (`/app`)

- **Patrón:** MVVM + Clean Architecture + Repository Pattern.
- **Inyección de Dependencias:** Hilt / Dagger.
- **Red:** Retrofit 2 + OkHttp. URL base configurada dinámicamente vía `BuildConfig.API_BASE_URL` (`http://10.0.2.2:8080/` en modo debug).

### Compilar y Ejecutar Android
```bash
# Abrir en Android Studio o compilar vía Gradle:
./gradlew app:assembleDebug
```

---

## ⚙️ Despliegue y Pipeline CI/CD

El proyecto cuenta con Integración y Despliegue Continuo automatizado mediante **GitHub Actions** (`.github/workflows/ci-cd.yml`):

1. **Pruebas Automáticas:** Ejecución de suite de pruebas unitarias en Jest (`npm test`).
2. **Construcción & Escaneo:** Generación de imágenes Docker para API y Web con escaneo de vulnerabilidades mediante **Trivy**.
3. **Publicación GHCR:** Publicación de imágenes tagged a GitHub Container Registry (`ghcr.io`).
4. **Despliegue SSH a AWS EC2:** Despliegue automatizado por SSH a instancia EC2 (t3.micro) con script de aprovisionamiento `infra/user-data.sh` y verificación del estado en `/api/health`.
