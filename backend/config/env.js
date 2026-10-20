require('dotenv').config();

const requiredEnvVars = ['MONGODB_URI', 'JWT_SECRET'];

// Validar variables obligatorias y fallar rápido
const missingVars = requiredEnvVars.filter(varName => !process.env[varName] || process.env[varName].trim() === '');

if (missingVars.length > 0) {
    console.error(`❌ FATAL: Faltan las siguientes variables de entorno obligatorias: ${missingVars.join(', ')}`);
    console.error(`Asegúrate de configurarlas en tu archivo .env o en las variables del entorno.`);
    process.exit(1);
}

const env = {
    PORT: parseInt(process.env.PORT || '3000', 10),
    NODE_ENV: process.env.NODE_ENV || 'development',
    MONGODB_URI: process.env.MONGODB_URI,
    JWT_SECRET: process.env.JWT_SECRET,
    CORS_ORIGIN: process.env.CORS_ORIGIN || '*',
    ADMIN_KEY: process.env.ADMIN_KEY || 'saludactiva_admin_secret_key_2026',
    APP_VERSION: process.env.APP_VERSION || '1.2.1',
    FIREBASE_SERVICE_ACCOUNT: process.env.FIREBASE_SERVICE_ACCOUNT || null,

    // URIs aisladas de bases de datos por dominio
    AUTH_MONGODB_URI: process.env.AUTH_MONGODB_URI || null,
    TURNOS_MONGODB_URI: process.env.TURNOS_MONGODB_URI || null,
    MEDICAMENTOS_MONGODB_URI: process.env.MEDICAMENTOS_MONGODB_URI || null,
    ESTUDIOS_MONGODB_URI: process.env.ESTUDIOS_MONGODB_URI || null,
    CHAT_MONGODB_URI: process.env.CHAT_MONGODB_URI || null,
};

module.exports = env;
