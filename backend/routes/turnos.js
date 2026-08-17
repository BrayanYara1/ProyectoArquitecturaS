const express = require('express');
const router = express.Router();
const Turno = require('../models/Turno');
const User = require('../models/User');
const authenticateToken = require('../middleware/auth');
const { sendPushNotification } = require('../middleware/notification');

// OBTENER TURNOS DEL USUARIO
router.get('/', authenticateToken, async (req, res) => {
    try {
        const turnos = await Turno.find({ usuarioId: req.userId }).sort({ fecha: 1, hora: 1 });
        res.json(turnos);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al obtener turnos" });
    }
});

// VERIFICAR DISPONIBILIDAD GLOBAL
router.get('/check-availability', authenticateToken, async (req, res) => {
    try {
        const { fecha, hora } = req.query;
        const turnoExistente = await Turno.findOne({
            fecha,
            hora,
            estado: { $nin: ["Cancelado", "cancelled", "CANCELADO"] }
        });
        res.json({ disponible: !turnoExistente });
    } catch (error) {
        res.status(500).json({ mensaje: "Error al verificar disponibilidad" });
    }
});

// CREAR TURNO
router.post('/', authenticateToken, async (req, res) => {
    try {
        const { fecha, hora } = req.body;

        const ocupado = await Turno.findOne({
            fecha,
            hora,
            estado: { $nin: ["Cancelado", "cancelled", "CANCELADO"] }
        });

        if (ocupado) {
            return res.status(400).json({ mensaje: "Este horario ya fue tomado por otro paciente" });
        }

        const nuevoTurno = new Turno({ ...req.body, usuarioId: req.userId });
        await nuevoTurno.save();

        // Helper para formato AM/PM
        const formatAMPM = (time) => {
            if (!time || !time.includes(':')) return time;
            let [hours, minutes] = time.split(':');
            hours = parseInt(hours);
            const ampm = hours >= 12 ? 'PM' : 'AM';
            hours = hours % 12;
            hours = hours ? hours : 12;
            return `${hours}:${minutes} ${ampm}`;
        };

        // Enviar notificación Push de confirmación
        if (req.user && req.user.fcmToken) {
            sendPushNotification(
                req.user.fcmToken,
                "✅ Turno Confirmado",
                `Tu cita para el ${fecha} a las ${formatAMPM(hora)} ha sido agendada con éxito.`,
                { turnoId: nuevoTurno._id.toString(), type: "TURNO_CONFIRMADO" }
            ).then(result => {
                if (result && result.error === 'NotRegistered') {
                    User.findByIdAndUpdate(req.userId, { fcmToken: null }).exec();
                }
            });
        }

        res.status(201).json(nuevoTurno);
    } catch (error) {
        console.error("❌ Error al crear turno:", error);
        res.status(500).json({ mensaje: "Error al crear turno", error: error.message });
    }
});

// ELIMINAR/CANCELAR TURNO
router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        const turno = await Turno.findOne({ _id: req.params.id, usuarioId: req.userId });
        if (!turno) return res.status(404).json({ mensaje: "Turno no encontrado" });

        await Turno.findByIdAndDelete(req.params.id);

        // Notificar cancelación
        if (req.user && req.user.fcmToken) {
            sendPushNotification(
                req.user.fcmToken,
                "⚠️ Turno Cancelado",
                `Has cancelado tu turno del día ${turno.fecha}.`,
                { type: "TURNO_CANCELADO" }
            ).then(result => {
                if (result && result.error === 'NotRegistered') {
                    User.findByIdAndUpdate(req.userId, { fcmToken: null }).exec();
                }
            });
        }

        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error al eliminar" });
    }
});

module.exports = router;
