const express = require('express');
const router = express.Router();
const authenticateToken = require('../middleware/auth');
const { notificarUsuario } = require('../services/NotificationService');

const MongooseChatRepository = require('../src/infrastructure/adapters/persistence/MongooseChatRepository');
const EnviarMensajeChat = require('../src/application/use-cases/EnviarMensajeChat');

const chatRepository = new MongooseChatRepository();
const enviarMensajeChat = new EnviarMensajeChat(chatRepository, notificarUsuario);

// OBTENER HISTORIAL DE CHAT
router.get('/', authenticateToken, async (req, res, next) => {
    try {
        const mensajes = await enviarMensajeChat.obtenerHistorial(req.userId);
        res.json(mensajes);
    } catch (error) {
        next(error);
    }
});

// ENVIAR MENSAJE (PACIENTE)
router.post('/', authenticateToken, async (req, res, next) => {
    try {
        const nuevoMensaje = await enviarMensajeChat.enviarMensajePaciente({
            ...req.body,
            usuarioId: req.userId
        }, req.user || null);

        res.status(201).json(nuevoMensaje);
    } catch (error) {
        next(error);
    }
});

module.exports = router;
