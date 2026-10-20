const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/auth');

const MongooseMedicamentoRepository = require('../src/infrastructure/adapters/persistence/MongooseMedicamentoRepository');
const GestionarMedicamentos = require('../src/application/use-cases/GestionarMedicamentos');

const medicamentoRepository = new MongooseMedicamentoRepository();
const gestionarMedicamentos = new GestionarMedicamentos(medicamentoRepository);

router.get('/', authenticateToken, async (req, res) => {
    try {
        const medicamentos = await gestionarMedicamentos.listar(req.userId);
        res.json(medicamentos);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al obtener medicamentos" });
    }
});

router.post('/', authenticateToken, async (req, res) => {
    try {
        const med = await gestionarMedicamentos.crear({ ...req.body, usuarioId: req.userId });
        res.status(201).json(med);
    } catch (error) {
        const statusCode = error.statusCode || 500;
        res.status(statusCode).json({ mensaje: error.message || "Error al crear medicamento" });
    }
});

router.put('/:id', authenticateToken, async (req, res) => {
    try {
        const med = await gestionarMedicamentos.actualizar(req.params.id, req.userId, req.body);
        if (!med) return res.status(404).json({ mensaje: "Medicamento no encontrado" });
        res.json(med);
    } catch (error) {
        const statusCode = error.statusCode || 500;
        res.status(statusCode).json({ mensaje: error.message || "Error al actualizar medicamento" });
    }
});

router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        const eliminado = await gestionarMedicamentos.eliminar(req.params.id, req.userId);
        if (!eliminado) return res.status(404).json({ mensaje: "Medicamento no encontrado" });
        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error al eliminar medicamento" });
    }
});

module.exports = router;
