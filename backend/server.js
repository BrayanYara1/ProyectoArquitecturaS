const env = require('./config/env');
const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const path = require('path');

// --- APP CONFIG ---
const app = express();
const PORT = env.PORT;

// Limpiador general de peticiones (100 por cada 15 min por IP)
const generalLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100,
    standardHeaders: true,
    legacyHeaders: false,
});

app.use(helmet({ contentSecurityPolicy: false }));
app.use(generalLimiter);
app.use(cors({ origin: env.CORS_ORIGIN }));
app.use(express.json());

// Archivos estáticos del cliente Web (si la carpeta web existe)
app.use(express.static(path.join(__dirname, '../web')));
app.use(express.static(path.join(__dirname, 'web')));

// Archivos subidos por usuarios (documentos de estudios médicos)
app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

let dbStatus = 'Desconectado';

mongoose.connect(env.MONGODB_URI)
    .then(() => {
        console.log('✅ MongoDB conectado a:', mongoose.connection.name);
        dbStatus = 'Conectado a MongoDB';
    })
    .catch(err => {
        console.error('❌ Error MongoDB:', err.message);
        dbStatus = `Error de Conexión: ${err.message}`;
    });

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

// --- ROOT & HEALTH ---
app.get('/', (req, res) => {
    res.send(`
        <h1>🚀 Salud Activa Backend Online</h1>
        <p><b>Estado DB:</b> ${dbStatus}</p>
        <p><b>Versión:</b> ${env.APP_VERSION}</p>
        <p><b>Fecha Servidor:</b> ${new Date().toLocaleString()}</p>
    `);
});
app.get('/api/status', (req, res) => res.json({ status: 'online', timestamp: new Date() }));
app.get('/api/health', (req, res) => {
    res.status(200).json({
        status: 'UP',
        timestamp: new Date().toISOString(),
        dbStatus: dbStatus,
        service: 'saludactiva-api',
        version: env.APP_VERSION
    });
});

if (require.main === module) {
    app.listen(PORT, '0.0.0.0', () => {
        console.log(`🚀 Servidor corriendo en el puerto ${PORT}`);
        console.log(`📡 Rutas cargadas: Auth, Turnos, Medicamentos, Estudios, Admin, Chat`);
    });
}

module.exports = app;
