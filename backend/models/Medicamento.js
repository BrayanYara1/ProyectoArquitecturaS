const mongoose = require('mongoose');
const { medicamentosConnection } = require('../config/dbConnections');

const MedicamentoSchema = new mongoose.Schema({
    usuarioId: { type: String, required: true },
    nombre: { type: String, required: true },
    dosis: { type: String, required: true },
    frecuencia: { type: String, required: true },
    proximaToma: { type: String, required: true },
    notas: { type: String, default: "" }
});

module.exports = medicamentosConnection.model('Medicamento', MedicamentoSchema);
