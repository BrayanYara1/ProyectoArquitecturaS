const jwt = require('jsonwebtoken');
const User = require('../models/User');
const SECRET_KEY = process.env.JWT_SECRET || 'SaludActiva_Secret_Key_2024';

const authenticateToken = async (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) return res.status(401).json({ mensaje: "Token no proporcionado" });

    jwt.verify(token, SECRET_KEY, async (err, decoded) => {
        if (err) return res.status(403).json({ mensaje: "Token inválido o expirado" });

        try {
            const user = await User.findById(decoded.userId);
            if (!user) return res.status(401).json({ mensaje: "Usuario ya no existe" });

            req.userId = decoded.userId;
            req.user = user;
            next();
        } catch (error) {
            res.status(500).json({ mensaje: "Error de servidor en autenticación" });
        }
    });
};

module.exports = authenticateToken;
