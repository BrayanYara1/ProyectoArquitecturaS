const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/auth');
const { sendPushNotification } = require('../middleware/notification');

const MongooseChatRepository = require('../src/infrastructure/adapters/persistence/MongooseChatRepository');
const EnviarMensajeChat = require('../src/application/use-cases/EnviarMensajeChat');

const chatRepository = new MongooseChatRepository();
const enviarMensajeChat = new EnviarMensajeChat(chatRepository, sendPushNotification);

// OBTENER HISTORIAL DE CHAT
router.get('/', authenticateToken, async (req, res) => {
    try {
        const mensajes = await enviarMensajeChat.obtenerHistorial(req.userId);
        res.json(mensajes);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al obtener mensajes" });
    }
});

// ENVIAR MENSAJE (PACIENTE)
router.post('/', authenticateToken, async (req, res) => {
    try {
        const nuevoMensaje = await enviarMensajeChat.enviarMensajePaciente({
            ...req.body,
            usuarioId: req.userId
        }, req.user || null);

        res.status(201).json(nuevoMensaje);
    } catch (error) {
        const statusCode = error.statusCode || 500;
        res.status(statusCode).json({ mensaje: error.message || "Error al enviar mensaje" });
    }
});

module.exports = router;
