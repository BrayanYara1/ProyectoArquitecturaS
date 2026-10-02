const mongoose = require('mongoose');

// Helper para construir la URI aislada por dominio de forma segura (Regla 7.1)
function getDomainDbUri(domainName) {
    const envVarName = `${domainName.toUpperCase()}_MONGODB_URI`;
    if (process.env[envVarName]) {
        return process.env[envVarName];
    }

    const baseUri = process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/SaludActiva';
    try {
        if (baseUri.includes('?')) {
            const parts = baseUri.split('?');
            const mainPart = parts[0];
            const queryPart = parts[1];
            const lastSlash = mainPart.lastIndexOf('/');

            if (lastSlash !== -1 && lastSlash > mainPart.indexOf('://') + 2) {
                const dbName = mainPart.substring(lastSlash + 1);
                const baseUrl = mainPart.substring(0, lastSlash);
                return `${baseUrl}/${dbName || 'SaludActiva'}_${domainName}?${queryPart}`;
            } else {
                return `${mainPart}/SaludActiva_${domainName}?${queryPart}`;
            }
        } else {
            const lastSlash = baseUri.lastIndexOf('/');
            if (lastSlash !== -1 && lastSlash > baseUri.indexOf('://') + 2) {
                return `${baseUri}_${domainName}`;
            } else {
                return `${baseUri}/SaludActiva_${domainName}`;
            }
        }
    } catch (e) {
        return baseUri;
    }
}

const options = {
    connectTimeoutMS: 30000,
    socketTimeoutMS: 45000,
};

// Crear conexiones independientes por Dominio (Instancias y volúmenes aislados)
const authDbUri = getDomainDbUri('auth');
const turnosDbUri = getDomainDbUri('turnos');
const medicamentosDbUri = getDomainDbUri('medicamentos');
const estudiosDbUri = getDomainDbUri('estudios');
const chatDbUri = getDomainDbUri('chat');

const authConnection = mongoose.createConnection(authDbUri, options);
const turnosConnection = mongoose.createConnection(turnosDbUri, options);
const medicamentosConnection = mongoose.createConnection(medicamentosDbUri, options);
const estudiosConnection = mongoose.createConnection(estudiosDbUri, options);
const chatConnection = mongoose.createConnection(chatDbUri, options);

// Manejadores de eventos de error OBLIGATORIOS para evitar que Node.js colapse
authConnection.on('error', err => console.error('⚠️ DB Auth Note:', err.message));
turnosConnection.on('error', err => console.error('⚠️ DB Turnos Note:', err.message));
medicamentosConnection.on('error', err => console.error('⚠️ DB Medicamentos Note:', err.message));
estudiosConnection.on('error', err => console.error('⚠️ DB Estudios Note:', err.message));
chatConnection.on('error', err => console.error('⚠️ DB Chat Note:', err.message));

authConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Auth] conectada'));
turnosConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Turnos] conectada'));
medicamentosConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Medicamentos] conectada'));
estudiosConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Estudios] conectada'));
chatConnection.on('connected', () => console.log('✅ Base de Datos [Dominio Chat] conectada'));

module.exports = {
    authConnection,
    turnosConnection,
    medicamentosConnection,
    estudiosConnection,
    chatConnection
};
