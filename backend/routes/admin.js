const express = require('express');
const router = express.Router();
const User = require('../models/User');
const Turno = require('../models/Turno');
const Medicamento = require('../models/Medicamento');
const Estudio = require('../models/Estudio');

router.delete('/reset-database', async (req, res) => {
    const adminKey = req.headers['x-admin-key'];
    if (!adminKey || adminKey !== process.env.ADMIN_KEY) {
        console.warn(`⚠️ Intento de reset no autorizado desde IP: ${req.ip}`);
        return res.status(403).json({ mensaje: "No autorizado" });
    }

    try {
        await User.deleteMany({});
        await Turno.deleteMany({});
        await Medicamento.deleteMany({});
        await Estudio.deleteMany({});
        res.json({ mensaje: "Base de datos reiniciada con éxito" });
    } catch (error) {
        res.status(500).json({ mensaje: "Error al reiniciar base de datos" });
    }
});

module.exports = router;
