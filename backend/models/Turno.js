const mongoose = require('mongoose');

const TurnoSchema = new mongoose.Schema({
    usuarioId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    pacienteNombre: { type: String, required: true },
    fecha: { type: String, required: true },
    hora: { type: String, required: true },
    motivo: { type: String, default: "General" },
    especialidad: { type: String, default: "General" },
    doctor: { type: String, default: "Dr. Asignado" },
    estado: { type: String, default: "Pendiente" }
});

module.exports = mongoose.model('Turno', TurnoSchema);
