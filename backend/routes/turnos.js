const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/auth');
const { notificarUsuario } = require('../services/NotificationService');
const { formatAMPM } = require('../utils/fechas');

const MongooseTurnoRepository = require('../src/infrastructure/adapters/persistence/MongooseTurnoRepository');
const SolicitarTurno = require('../src/application/use-cases/SolicitarTurno');

const turnoRepository = new MongooseTurnoRepository();
const solicitarTurno = new SolicitarTurno(turnoRepository);

// OBTENER TURNOS DEL USUARIO
router.get('/', authenticateToken, async (req, res, next) => {
    try {
        const turnos = await solicitarTurno.obtenerTurnos(req.userId);
        res.json(turnos);
    } catch (error) {
        next(error);
    }
});

// VERIFICAR DISPONIBILIDAD GLOBAL
router.get('/check-availability', authenticateToken, async (req, res, next) => {
    try {
        const { fecha, hora } = req.query;
        const disponible = await solicitarTurno.estaDisponible(fecha, hora);
        res.json({ disponible });
    } catch (error) {
        next(error);
    }
});

// CREAR TURNO
router.post('/', authenticateToken, async (req, res, next) => {
    try {
        const nuevoTurno = await solicitarTurno.ejecutar({
            ...req.body,
            usuarioId: req.userId
        }, req.user || {});

        const horaFormateada = formatAMPM(nuevoTurno.hora);
        await notificarUsuario(
            req.user,
            "✅ Turno Confirmado",
            `Tu cita para el ${nuevoTurno.fecha} a las ${horaFormateada} ha sido agendada con éxito.`,
            { turnoId: String(nuevoTurno.id), type: "TURNO_CONFIRMADO" }
        );

        res.status(201).json(nuevoTurno);
    } catch (error) {
        next(error);
    }
});

// ELIMINAR/CANCELAR TURNO
router.delete('/:id', authenticateToken, async (req, res, next) => {
    try {
        const eliminado = await solicitarTurno.cancelarTurno(req.params.id, req.userId);
        if (!eliminado) return res.status(404).json({ mensaje: "Turno no encontrado" });

        await notificarUsuario(
            req.user,
            "⚠️ Turno Cancelado",
            `Has cancelado tu turno del día ${eliminado.fecha}.`,
            { type: "TURNO_CANCELADO" }
        );

        res.status(200).send();
    } catch (error) {
        next(error);
    }
});

module.exports = router;
