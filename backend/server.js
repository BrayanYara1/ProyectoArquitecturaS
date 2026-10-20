const env = require('./config/env');
const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const path = require('path');

const {
    authConnection,
    turnosConnection,
    medicamentosConnection,
    estudiosConnection,
    chatConnection
} = require('./config/dbConnections');

const app = express();
const PORT = env.PORT;

app.use(helmet({ contentSecurityPolicy: false }));

// EXCLUIR /api/health del rate limiting para el HEALTHCHECK de Docker y CI/CD
const generalLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100,
    standardHeaders: true,
    legacyHeaders: false,
    skip: (req) => req.path === '/api/health' || req.path === '/health'
});

app.use(generalLimiter);
app.use(cors({ origin: env.CORS_ORIGIN }));
app.use(express.json());

// Archivos estáticos del cliente Web
app.use(express.static(path.join(__dirname, '../web')));
app.use(express.static(path.join(__dirname, 'web')));

// Archivos subidos por usuarios (documentos de estudios médicos)
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// --- HEALTHCHECK (Verifica la salud de las 5 bases de datos de dominio) ---
app.get('/api/health', (req, res) => {
    const bases = {
        auth: authConnection.readyState === 1 ? 'ok' : 'disconnected',
        turnos: turnosConnection.readyState === 1 ? 'ok' : 'disconnected',
        medicamentos: medicamentosConnection.readyState === 1 ? 'ok' : 'disconnected',
        estudios: estudiosConnection.readyState === 1 ? 'ok' : 'disconnected',
        chat: chatConnection.readyState === 1 ? 'ok' : 'disconnected',
    };

    const allConnected = Object.values(bases).every(status => status === 'ok');

    const responsePayload = {
        estado: allConnected ? 'ok' : 'error',
        version: env.APP_VERSION,
        bases
    };

    if (allConnected) {
        return res.status(200).json(responsePayload);
    } else {
        return res.status(503).json(responsePayload);
    }
});

app.get('/api/status', (req, res) => res.json({ status: 'online', timestamp: new Date() }));

// --- IMPORTAR RUTAS ---
const authRoutes = require('./src/infrastructure/adapters/http/express/AuthRoutes');
const turnoRoutes = require('./routes/turnos');
const medicamentoRoutes = require('./routes/medicamentos');
const estudioRoutes = require('./routes/estudios');
const adminRoutes = require('./routes/admin');
const chatRoutes = require('./routes/chat');

// --- USAR RUTAS ---
app.use('/api/auth', authRoutes);
app.use('/api/turnos', turnoRoutes);
app.use('/api/medicamentos', medicamentoRoutes);
app.use('/api/estudios', estudioRoutes);
app.use('/api/admin', adminRoutes);
app.use('/api/chat', chatRoutes);

// --- ROOT ---
app.get('/', (req, res) => {
    const allConnected = [authConnection, turnosConnection, medicamentosConnection, estudiosConnection, chatConnection]
        .every(conn => conn.readyState === 1);
    res.send(`
        <h1>🚀 Salud Activa Backend Online</h1>
        <p><b>Estado DB:</b> ${allConnected ? 'Conectado (5 dominios)' : 'Degradado / Desconectado'}</p>
        <p><b>Versión:</b> ${env.APP_VERSION}</p>
        <p><b>Fecha Servidor:</b> ${new Date().toLocaleString()}</p>
    `);
});

if (require.main === module) {
    app.listen(PORT, '0.0.0.0', () => {
        console.log(`🚀 Servidor corriendo en el puerto ${PORT}`);
        console.log(`📡 Rutas cargadas: Auth, Turnos, Medicamentos, Estudios, Admin, Chat`);
    });
}

module.exports = app;
