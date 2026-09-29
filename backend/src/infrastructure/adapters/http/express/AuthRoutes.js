const express = require('express');
const router = express.Router();
const rateLimit = require('express-rate-limit');
const authenticateToken = require('../../../../middleware/auth');

const MongooseUserRepository = require('../../persistence/MongooseUserRepository');
const RegisterUser = require('../../../../application/use-cases/RegisterUser');
const LoginUser = require('../../../../application/use-cases/LoginUser');
const AuthController = require('./AuthController');

const SECRET_KEY = process.env.JWT_SECRET || 'SaludActiva_Secret_Key_2024';

const userRepository = new MongooseUserRepository();
const registerUser = new RegisterUser(userRepository);
const loginUser = new LoginUser(userRepository, SECRET_KEY);
const authController = new AuthController(registerUser, loginUser, userRepository);

const authLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100,
    message: { mensaje: "Demasiados intentos desde esta IP, por favor intenta en 15 minutos" },
    standardHeaders: true,
    legacyHeaders: false,
});

router.post('/register', authLimiter, (req, res) => authController.register(req, res));
router.post('/login', authLimiter, (req, res) => authController.login(req, res));
router.put('/profile', authenticateToken, (req, res) => authController.updateProfile(req, res));
router.post('/fcm-token', authenticateToken, (req, res) => authController.updateFcmToken(req, res));

// MOCKS
router.post('/verify', async (req, res) => res.status(200).json({ mensaje: "OK" }));
router.post('/resend-code', async (req, res) => res.status(200).json({ mensaje: "OK" }));

module.exports = router;
