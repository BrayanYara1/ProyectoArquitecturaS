# --- Etapa 1: Base (producción mínima) ---
FROM node:22-alpine AS base

WORKDIR /app

# Copiar archivos de definición de dependencias
COPY package*.json ./

# Instalar solo dependencias de producción
RUN npm ci --omit=dev

# Copiar código fuente del backend
COPY src ./src
COPY config ./config
COPY routes ./routes
COPY models ./models
COPY middleware ./middleware
COPY server.js ./server.js

# --- Etapa 2: Test (ejecución de pruebas automatizadas) ---
FROM node:22-alpine AS test

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npm test

# --- Etapa 3: Producción ---
FROM base AS prod

ARG APP_VERSION=1.2.1
ENV NODE_ENV=production
ENV APP_VERSION=${APP_VERSION}

WORKDIR /app

USER node

EXPOSE 3000

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://127.0.0.1:3000/api/health || exit 1

CMD [ "node", "server.js" ]
