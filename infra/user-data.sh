#!/bin/bash
# ==============================================================================
# User Data Script para Aprovisionamiento de Instancia AWS EC2 (Ubuntu 22.04/24.04)
# Salud Activa Platform — Proyecto Arquitectura
# ==============================================================================

set -e

echo "🚀 Iniciando aprovisionamiento del servidor Salud Activa..."

# 1. Actualización de paquetes del sistema
apt-get update -y
apt-get upgrade -y

# 2. Instalación de dependencias necesarias y repositorio oficial de Docker
apt-get install -y ca-certificates curl gnupg lsb-release
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# 3. Instalación de Docker Engine y Docker Compose V2
apt-get update -y
apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# 4. Habilitación y arranque del servicio Docker
systemctl enable docker
systemctl start docker
usermod -aG docker ubuntu

# 5. Creación del directorio de trabajo para despliegue por SSH
mkdir -p /home/ubuntu/saludactiva
chown -R ubuntu:ubuntu /home/ubuntu/saludactiva

echo "✅ Servidor EC2 configurado exitosamente y listo para recibir despliegues por CI/CD."
