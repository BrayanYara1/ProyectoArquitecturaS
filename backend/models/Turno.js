const mongoose = require('mongoose');
const { turnosConnection } = require('../config/dbConnections');

const TurnoSchema = new mongoose.Schema({
    usuarioId: { type: String, required: true },
    pacienteNombre: { type: String, required: true },
    fecha: { type: String, required: true },
    hora: { type: String, required: true },
    motivo: { type: String, default: "General" },
    especialidad: { type: String, default: "General" },
    doctor: { type: String, default: "Dr. Asignado" },
    estado: { type: String, default: "Pendiente" }
});

module.exports = turnosConnection.model('Turno', TurnoSchema);
