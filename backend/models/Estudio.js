const mongoose = require('mongoose');
const { estudiosConnection } = require('../config/dbConnections');

const EstudioSchema = new mongoose.Schema({
    usuarioId: { type: String, required: true },
    titulo: { type: String, required: true },
    fecha: { type: String, required: true },
    tipo: { type: String, default: "General" },
    resultadoBreve: { type: String, default: "" },
    urlDocumento: { type: String, default: null },
    notas: { type: String, default: "" }
});

module.exports = estudiosConnection.model('Estudio', EstudioSchema);
