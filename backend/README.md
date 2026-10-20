# Backend API — Salud Activa

Servidor API RESTful para la plataforma **Salud Activa**, construido con **Node.js 22**, **Express** y **MongoDB 7**. Aplica arquitectura hexagonal (Ports & Adapters), desacoplamiento por dominios y manejo de configuración 12-factor.

---

## 📂 Estructura del Código Fuente

```text
backend/
├── app.js                         # Configuración de Express, middlewares y rutas (probable de forma aislada)
├── server.js                      # Punto de entrada HTTP (escucha en PORT)
├── config/
│   ├── env.js                     # Validación y lectura centralizada de variables de entorno (Fail-Fast)
│   └── dbConnections.js           # Gestión de las 5 conexiones de MongoDB aisladas por dominio
├── errors/
│   └── CustomErrors.js            # Clases semánticas ErrorValidacion (400) y NoEncontrado (404)
├── middleware/
│   ├── auth.js                    # Middleware de autenticación y verificación de JWT
│   ├── errorHandler.js            # Manejador centralizado de errores con respuestas sanitizadas
│   └── notification.js            # Cliente Firebase Admin para notificaciones Push
├── routes/                        # Enrutadores HTTP Express
│   ├── turnos.js                  # Endpoints de citas médicas
│   ├── medicamentos.js            # Endpoints de tratamientos y medicamentos
│   ├── estudios.js                # Endpoints y subida de documentos médicos (Multer)
│   ├── admin.js                   # Endpoints administrativos
│   └── chat.js                    # Endpoints de mensajería
├── services/
│   └── NotificationService.js     # Servicio de envío de notificaciones y limpieza de tokens FCM
├── src/                           # Arquitectura Hexagonal
│   ├── domain/
│   │   ├── entities/              # Entidades de dominio (Turno, Medicamento, Estudio, Message, User)
│   │   ├── ports/                 # Puertos/Interfaces de Repositorio (TurnoRepository, etc.)
│   │   └── constants/             # Constantes de dominio (EstadosTurno.js)
│   ├── application/use-cases/     # Casos de Uso (SolicitarTurno, RegisterUser, LoginUser, etc.)
│   └── infrastructure/adapters/   # Adaptadores Mongoose y Controladores Express
│       ├── persistence/           # MongooseTurnoRepository, MongooseUserRepository, etc.
│       └── http/express/          # AuthRoutes.js, AuthController.js
├── utils/
│   └── fechas.js                  # Utilidades y formateadores (formatAMPM)
└── tests/                         # Pruebas unitarias e integración en Jest
    ├── status.test.js
    └── unit/                      # SolicitarTurno.test.js, RegisterUser.test.js, LoginUser.test.js
```

---

## 🧪 Pruebas Automáticas (Jest)

Las pruebas están diseñadas para ser **100% aisladas y repetibles** sin depender de un servidor de MongoDB activo:

```bash
# 1. Instalar dependencias
npm ci

# 2. Ejecutar suite de pruebas unitarias
npm test
```

El comando `npm test` ejecuta `jest --detectOpenHandles --forceExit` verificando los casos de uso, controladores y respuestas de la API.

---

## 🩺 Endpoints Principales

- `GET  /api/health` — Verificación de salud (valida las 5 bases de datos de dominio).
- `POST /api/auth/register` — Registro de usuarios con contraseñas encriptadas por bcrypt.
- `POST /api/auth/login` — Autenticación y generación de JWT.
- `GET  /api/turnos` — Listar turnos del paciente.
- `POST /api/turnos` — Solicitar nuevo turno.
- `GET  /api/medicamentos` — Listar medicamentos.
- `POST /api/medicamentos` — Registrar medicamento.
- `GET  /api/estudios` — Listar estudios.
- `POST /api/estudios` — Subir estudio médico con documento adjunto.
- `GET  /api/chat` / `POST /api/chat` — Mensajería con respuestas médicas simuladas.
