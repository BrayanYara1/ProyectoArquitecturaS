const express = require('express');
const router = express.Router();
const Medicamento = require('../models/Medicamento');
const authenticateToken = require('../middleware/auth');

router.get('/', authenticateToken, async (req, res) => {
    try {
        const medicamentos = await Medicamento.find({ usuarioId: req.userId });
        res.json(medicamentos);
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

router.post('/', authenticateToken, async (req, res) => {
    try {
        const med = new Medicamento({ ...req.body, usuarioId: req.userId });
        await med.save();
        res.status(201).json(med);
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

router.put('/:id', authenticateToken, async (req, res) => {
    try {
        const med = await Medicamento.findOneAndUpdate(
            { _id: req.params.id, usuarioId: req.userId },
            req.body,
            { new: true }
        );
        res.json(med);
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        await Medicamento.findOneAndDelete({ _id: req.params.id, usuarioId: req.userId });
        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error" });
    }
});

module.exports = router;
