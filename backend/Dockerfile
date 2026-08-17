# --- Etapa 1: Construcción ---
FROM node:18-alpine AS builder

WORKDIR /app

# Copiar archivos de definición de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar el código fuente
COPY . .

# --- Etapa 2: Producción ---
FROM node:18-alpine

# Definir variables de entorno de producción
ENV NODE_ENV=production
WORKDIR /app

# Copiar solo lo necesario desde la etapa de construcción
COPY --from=builder /app/package*.json ./
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app ./

# Eliminar dependencias de desarrollo para ahorrar espacio (opcional si usaste npm ci --only=production)
RUN npm prune --production

# Usar un usuario no raíz por seguridad (la imagen de node ya trae el usuario 'node')
USER node

# Exponer el puerto
EXPOSE 3000

# Comando para arrancar la app
CMD [ "node", "server.js" ]
