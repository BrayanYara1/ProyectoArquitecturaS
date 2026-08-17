const express = require('express');
const router = express.Router();
const Estudio = require('../models/Estudio');
const authenticateToken = require('../middleware/auth');

router.get('/', authenticateToken, async (req, res) => {
    try {
        const estudios = await Estudio.find({ usuarioId: req.userId });
        res.json(estudios);
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

router.post('/', authenticateToken, async (req, res) => {
    try {
        const estudio = new Estudio({ ...req.body, usuarioId: req.userId });
        await estudio.save();
        res.status(201).json(estudio);
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        await Estudio.findOneAndDelete({ _id: req.params.id, usuarioId: req.userId });
        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

module.exports = router;
