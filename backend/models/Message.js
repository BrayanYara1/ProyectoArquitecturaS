const mongoose = require('mongoose');
const { chatConnection } = require('../config/dbConnections');

const MessageSchema = new mongoose.Schema({
    usuarioId: { type: String, required: true },
    remitente: { type: String, enum: ['PACIENTE', 'DOCTOR'], required: true },
    texto: { type: String, required: true },
    fecha: { type: Date, default: Date.now },
    leido: { type: Boolean, default: false }
});

module.exports = chatConnection.model('Message', MessageSchema);
