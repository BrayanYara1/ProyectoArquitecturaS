const mongoose = require('mongoose');

const MessageSchema = new mongoose.Schema({
    usuarioId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    remitente: { type: String, enum: ['PACIENTE', 'DOCTOR'], required: true },
    texto: { type: String, required: true },
    fecha: { type: Date, default: Date.now },
    leido: { type: Boolean, default: false }
});

module.exports = mongoose.model('Message', MessageSchema);
