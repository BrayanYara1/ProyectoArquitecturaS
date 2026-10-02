const mongoose = require('mongoose');

// Base URI por defecto si no se especifican instancias individuales por variable de entorno
const DEFAULT_BASE_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017';

// Configuración de conexiones de base de datos aisladas por Dominio (Cumplimiento Regla 7.1 Aislamiento de Datos)
const authDbUri = process.env.AUTH_MONGODB_URI || `${DEFAULT_BASE_URI.split('?')[0]}_auth?${DEFAULT_BASE_URI.split('?')[1] || ''}`;
const turnosDbUri = process.env.TURNOS_MONGODB_URI || `${DEFAULT_BASE_URI.split('?')[0]}_turnos?${DEFAULT_BASE_URI.split('?')[1] || ''}`;
const medicamentosDbUri = process.env.MEDICAMENTOS_MONGODB_URI || `${DEFAULT_BASE_URI.split('?')[0]}_medicamentos?${DEFAULT_BASE_URI.split('?')[1] || ''}`;
const estudiosDbUri = process.env.ESTUDIOS_MONGODB_URI || `${DEFAULT_BASE_URI.split('?')[0]}_estudios?${DEFAULT_BASE_URI.split('?')[1] || ''}`;
const chatDbUri = process.env.CHAT_MONGODB_URI || `${DEFAULT_BASE_URI.split('?')[0]}_chat?${DEFAULT_BASE_URI.split('?')[1] || ''}`;

const options = {
    connectTimeoutMS: 30000,
    socketTimeoutMS: 45000,
};

// Crear conexiones independientes (instancias y volúmenes aislados por dominio)
const authConnection = mongoose.createConnection(authDbUri, options);
const turnosConnection = mongoose.createConnection(turnosDbUri, options);
const medicamentosConnection = mongoose.createConnection(medicamentosDbUri, options);
const estudiosConnection = mongoose.createConnection(estudiosDbUri, options);
const chatConnection = mongoose.createConnection(chatDbUri, options);

authConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Auth] conectada correctamente'));
turnosConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Turnos] conectada correctamente'));
medicamentosConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Medicamentos] conectada correctamente'));
estudiosConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Estudios] conectada correctamente'));
chatConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Chat] conectada correctamente'));

module.exports = {
    authConnection,
    turnosConnection,
    medicamentosConnection,
    estudiosConnection,
    chatConnection
};
