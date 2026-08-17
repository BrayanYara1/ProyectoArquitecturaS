const mongoose = require('mongoose');

const EstudioSchema = new mongoose.Schema({
    usuarioId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    titulo: { type: String, required: true },
    fecha: { type: String, required: true },
    tipo: { type: String, default: "General" },
    resultadoBreve: { type: String, default: "" },
    urlDocumento: { type: String, default: null },
    notas: { type: String, default: "" }
});

module.exports = mongoose.model('Estudio', EstudioSchema);
