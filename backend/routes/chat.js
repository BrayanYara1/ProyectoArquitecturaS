const express = require('express');
const router = express.Router();
const Message = require('../models/Message');
const User = require('../models/User');
const authenticateToken = require('../middleware/auth');
const { sendPushNotification } = require('../middleware/notification');

// OBTENER HISTORIAL DE CHAT
router.get('/', authenticateToken, async (req, res) => {
    try {
        const mensajes = await Message.find({ usuarioId: req.userId }).sort({ fecha: 1 });
        res.json(mensajes);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al obtener mensajes" });
    }
});

// ENVIAR MENSAJE (PACIENTE)
router.post('/', authenticateToken, async (req, res) => {
    try {
        const { texto } = req.body;
        if (!texto) return res.status(400).json({ mensaje: "El texto es obligatorio" });

        const nuevoMensaje = new Message({
            usuarioId: req.userId,
            remitente: 'PACIENTE',
            texto: texto
        });

        await nuevoMensaje.save();

        // SIMULACIÓN: Respuesta automática del doctor después de 2 segundos (Solo para pruebas)
        setTimeout(async () => {
            const respuestaDoctor = new Message({
                usuarioId: req.userId,
                remitente: 'DOCTOR',
                texto: "Hola, recibí tu mensaje. En breve un especialista revisará tu caso."
            });
            await respuestaDoctor.save();

            // Notificar al paciente por Push
            if (req.user && req.user.fcmToken) {
                sendPushNotification(
                    req.user.fcmToken,
                    "👨‍⚕️ Respuesta Médica",
                    "Tienes un nuevo mensaje de tu doctor."
                ).then(result => {
                    if (result && result.error === 'NotRegistered') {
                        User.findByIdAndUpdate(req.userId, { fcmToken: null }).exec();
                    }
                });
            }
        }, 2000);

        res.status(201).json(nuevoMensaje);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al enviar mensaje" });
    }
});

module.exports = router;
