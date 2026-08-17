# Salud Activa - Gestión de Bienestar Médico

## Descripción
**Salud Activa** es una plataforma profesional para la gestión de turnos y bienestar médico. El proyecto incluye una aplicación móvil Android nativa moderna y un backend escalable de alto rendimiento desplegado en la nube de AWS.

## Arquitectura del Proyecto
El proyecto sigue principios de **Clean Architecture** y **Clean Code**:

1.  **App Android (`/app`):** 
    - **MVVM:** Separación clara entre la lógica de negocio (ViewModels) y la interfaz (Fragments).
    - **Repository Pattern:** Centralización del acceso a datos (API y Caché Offline).
    - **Data Binding / View Binding:** Interfaz reactiva y limpia.
    - Retrofit + OkHttp para la comunicación con el backend (v2.11.0).
    - Kotlin Coroutines para operaciones asíncronas fluidas.
    - Material Design 3 para una experiencia de usuario profesional y accesible.

2.  **Backend (`/backend`):**
    - Node.js + Express.
    - MongoDB Atlas como base de datos de documentos.
    - Autenticación segura mediante JSON Web Tokens (JWT).
    - Despliegue optimizado mediante Docker.

3.  **Infraestructura (`/terraform_aws`):**
    - Infraestructura como Código (IaC) usando Terraform.
    - Despliegue en AWS ECS Fargate (Serverless Containers).
    - Base de Datos Relacional RDS (PostgreSQL).
    - Load Balancer (ALB) para distribución de tráfico y alta disponibilidad global.

## Containerización
El backend está completamente containerizado para asegurar la paridad entre entornos.
- **Dockerfile:** Utiliza una construcción multi-etapa basada en Node-Alpine para minimizar el tamaño de la imagen.

## Ejecución Local

### Backend
1. Entra a la carpeta `backend/`.
2. Instala las dependencias: `npm install`.
3. Crea un archivo `.env` con las variables necesarias.
4. Ejecuta: `npm start`.

### Android
1. Abre el proyecto en Android Studio.
2. Sincroniza Gradle.
3. El `BASE_URL` apunta por defecto a la nube pública de Salud Activa.
4. Ejecuta la app en un emulador o dispositivo físico.

## Infraestructura en AWS
Para desplegar la infraestructura:
1. Ve a `terraform_aws/`.
2. Ejecuta `terraform init`.
3. Ejecuta `terraform apply`.
