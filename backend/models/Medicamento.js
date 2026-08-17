const mongoose = require('mongoose');

const MedicamentoSchema = new mongoose.Schema({
    usuarioId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    nombre: { type: String, required: true },
    dosis: { type: String, required: true },
    frecuencia: { type: String, required: true },
    proximaToma: { type: String, required: true },
    notas: { type: String, default: "" }
});

module.exports = mongoose.model('Medicamento', MedicamentoSchema);
