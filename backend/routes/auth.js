const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const authenticateToken = require('../middleware/auth');
const rateLimit = require('express-rate-limit');

const SECRET_KEY = process.env.JWT_SECRET || 'SaludActiva_Secret_Key_2024';

// LIMITADOR DE PETICIONES (Evita fuerza bruta)
const authLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15 minutos
    max: 10, // Máximo 10 intentos por IP
    message: { mensaje: "Demasiados intentos desde esta IP, por favor intenta en 15 minutos" },
    standardHeaders: true,
    legacyHeaders: false,
});

// REGISTRO
router.post('/register', authLimiter, async (req, res) => {
    try {
        const { nombre, email, telefono, contrasena } = req.body;
        if (!nombre || !email || !contrasena) {
            return res.status(400).json({ mensaje: "Faltan datos obligatorios" });
        }

        // Validación de formato de email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return res.status(400).json({ mensaje: "Formato de email inválido" });
        }

        // Validación de contraseña
        if (contrasena.length < 6) {
            return res.status(400).json({ mensaje: "La contraseña debe tener al menos 6 caracteres" });
        }

        const existe = await User.findOne({ email });
        if (existe) return res.status(400).json({ mensaje: "El email ya está registrado" });

        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(contrasena, salt);

        const nuevoUsuario = new User({
            nombre, email, telefono, contrasena: hashedPassword, verificationCode: null, isVerified: true
        });
        await nuevoUsuario.save();

        console.log(`👤 Usuario registrado: ${email}`);
        res.status(201).json({ mensaje: "OK", email: nuevoUsuario.email });
    } catch (error) {
        console.error("Error en register:", error);
        res.status(500).json({ mensaje: "Error en el servidor durante el registro" });
    }
});

// LOGIN
router.post('/login', authLimiter, async (req, res) => {
    try {
        const { email, contrasena } = req.body;
        const user = await User.findOne({ email });
        if (!user || !await bcrypt.compare(contrasena, user.contrasena)) {
            return res.status(401).json({ mensaje: "Credenciales inválidas" });
        }

        const token = jwt.sign({ userId: user._id }, SECRET_KEY, { expiresIn: '30d' });
        res.json({
            mensaje: "OK",
            usuario: {
                id: user._id,
                nombre: user.nombre,
                email: user.email,
                telefono: user.telefono,
                tipoSanguineo: user.tipoSanguineo,
                alergias: user.alergias,
                condiciones: user.condiciones,
                contactoEmergencia: user.contactoEmergencia
            },
            token
        });
    } catch (error) {
        console.error("Error en login:", error);
        res.status(500).json({ mensaje: "Error en el servidor" });
    }
});

// PERFIL
router.put('/profile', authenticateToken, async (req, res) => {
    try {
        const { nombre, telefono, tipoSanguineo, alergias, condiciones, contactoEmergencia } = req.body;
        const user = await User.findByIdAndUpdate(
            req.userId,
            { nombre, telefono, tipoSanguineo, alergias, condiciones, contactoEmergencia },
            { new: true }
        );
        if (!user) return res.status(404).json({ mensaje: "Usuario no encontrado" });
        res.json({
            id: user._id,
            nombre: user.nombre,
            email: user.email,
            telefono: user.telefono,
            tipoSanguineo: user.tipoSanguineo,
            alergias: user.alergias,
            condiciones: user.condiciones,
            contactoEmergencia: user.contactoEmergencia
        });
    } catch (error) {
        res.status(500).json({ mensaje: "Error al actualizar perfil" });
    }
});

// FCM TOKEN
router.post('/fcm-token', authenticateToken, async (req, res) => {
    try {
        const { token } = req.body;
        await User.findByIdAndUpdate(req.userId, { fcmToken: token });
        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error FCM" });
    }
});

// MOCKS PARA COMPATIBILIDAD
router.post('/verify', async (req, res) => res.status(200).json({ mensaje: "OK" }));
router.post('/resend-code', async (req, res) => res.status(200).json({ mensaje: "OK" }));

module.exports = router;
