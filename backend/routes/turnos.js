const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/auth');
const { sendPushNotification } = require('../middleware/notification');

const MongooseTurnoRepository = require('../src/infrastructure/adapters/persistence/MongooseTurnoRepository');
const SolicitarTurno = require('../src/application/use-cases/SolicitarTurno');

const turnoRepository = new MongooseTurnoRepository();
const solicitarTurno = new SolicitarTurno(turnoRepository);

// OBTENER TURNOS DEL USUARIO
router.get('/', authenticateToken, async (req, res) => {
    try {
        const turnos = await solicitarTurno.obtenerTurnos(req.userId);
        res.json(turnos);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al obtener turnos" });
    }
});

// VERIFICAR DISPONIBILIDAD GLOBAL
router.get('/check-availability', authenticateToken, async (req, res) => {
    try {
        const { fecha, hora } = req.query;
        const disponible = await solicitarTurno.estaDisponible(fecha, hora);
        res.json({ disponible });
    } catch (error) {
        res.status(500).json({ mensaje: "Error al verificar disponibilidad" });
    }
});

// CREAR TURNO
router.post('/', authenticateToken, async (req, res) => {
    try {
        const nuevoTurno = await solicitarTurno.ejecutar({
            ...req.body,
            usuarioId: req.userId
        }, req.user || {});

        // Enviar notificación Push de confirmación
        if (req.user && req.user.fcmToken) {
            sendPushNotification(
                req.user.fcmToken,
                "✅ Turno Confirmado",
                `Tu cita para el ${nuevoTurno.fecha} a las ${nuevoTurno.hora} ha sido agendada con éxito.`,
                { turnoId: String(nuevoTurno.id), type: "TURNO_CONFIRMADO" }
            );
        }

        res.status(201).json(nuevoTurno);
    } catch (error) {
        const statusCode = error.statusCode || 500;
        res.status(statusCode).json({ mensaje: error.message || "Error al crear turno" });
    }
});

// ELIMINAR/CANCELAR TURNO
router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        const eliminado = await solicitarTurno.cancelarTurno(req.params.id, req.userId);
        if (!eliminado) return res.status(404).json({ mensaje: "Turno no encontrado" });

        // Notificar cancelación
        if (req.user && req.user.fcmToken) {
            sendPushNotification(
                req.user.fcmToken,
                "⚠️ Turno Cancelado",
                `Has cancelado tu turno del día ${eliminado.fecha}.`,
                { type: "TURNO_CANCELADO" }
            );
        }

        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error al eliminar" });
    }
});

module.exports = router;
