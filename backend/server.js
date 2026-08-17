require('dotenv').config();
const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');

// --- APP CONFIG ---
const app = express();
const PORT = process.env.PORT || 10000;

// Limpiador general de peticiones (100 por cada 15 min por IP)
const generalLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100,
    standardHeaders: true,
    legacyHeaders: false,
});

app.use(helmet());
app.use(generalLimiter);
app.use(cors());
app.use(express.json());

// --- CONEXIÓN MONGODB ---
if (!process.env.MONGODB_URI) {
    console.error('❌ ERROR: MONGODB_URI no definida en .env');
    process.exit(1);
}

mongoose.connect(process.env.MONGODB_URI)
    .then(() => console.log('✅ MongoDB Atlas conectado a:', mongoose.connection.name))
    .catch(err => {
        console.error('❌ Error MongoDB:', err.message);
        process.exit(1);
    });

// --- IMPORTAR RUTAS ---
const authRoutes = require('./routes/auth');
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

// --- ROOT & LISTEN ---
app.get('/', (req, res) => res.send('🚀 Salud Activa Backend Online (v1.2 - Modular)'));

app.listen(PORT, '0.0.0.0', () => {
    console.log(`🚀 Servidor corriendo en el puerto ${PORT}`);
    console.log(`📡 Rutas cargadas: Auth, Turnos, Medicamentos, Estudios, Admin`);
});
