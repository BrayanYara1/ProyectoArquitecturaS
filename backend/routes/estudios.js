const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const Estudio = require('../models/Estudio');
const authenticateToken = require('../middleware/auth');

// Configuración de multer para subida de documentos de estudios
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, path.join(__dirname, '..', 'uploads'));
    },
    filename: (req, file, cb) => {
        const timestamp = Date.now();
        const safeName = file.originalname.replace(/\s+/g, '_');
        cb(null, `${timestamp}_${safeName}`);
    }
});

const upload = multer({
    storage,
    limits: { fileSize: 10 * 1024 * 1024 }, // Máximo 10 MB
    fileFilter: (req, file, cb) => {
        const allowedTypes = [
            'application/pdf',
            'image/jpeg',
            'image/jpg',
            'image/png',
            'image/webp',
            'application/msword',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
            'text/plain'
        ];
        if (allowedTypes.includes(file.mimetype)) {
            cb(null, true);
        } else {
            cb(new Error('Tipo de archivo no permitido. Use PDF, JPG, PNG, WEBP, DOC, DOCX o TXT.'), false);
        }
    }
});

router.get('/', authenticateToken, async (req, res) => {
    try {
        const estudios = await Estudio.find({ usuarioId: req.userId }).sort({ fecha: -1, createdAt: -1 });
        res.json(estudios);
    } catch (error) {
        res.status(500).json({ mensaje: "Error al obtener estudios" });
    }
});

router.post('/', authenticateToken, upload.single('documento'), async (req, res) => {
    try {
        const estudioData = {
            ...req.body,
            usuarioId: req.userId,
            urlDocumento: req.file ? `/uploads/${req.file.filename}` : null
        };
        const estudio = new Estudio(estudioData);
        await estudio.save();
        res.status(201).json(estudio);
    } catch (error) {
        console.error('Error creando estudio:', error.message);
        res.status(500).json({ mensaje: error.message || "Error al crear estudio" });
    }
});

router.delete('/:id', authenticateToken, async (req, res) => {
    try {
        const estudio = await Estudio.findOne({ _id: req.params.id, usuarioId: req.userId });
        if (!estudio) {
            return res.status(404).json({ mensaje: "Estudio no encontrado" });
        }

        // Eliminar archivo físico si existe
        if (estudio.urlDocumento) {
            const fs = require('fs');
            const filePath = path.join(__dirname, '..', estudio.urlDocumento);
            if (fs.existsSync(filePath)) {
                fs.unlinkSync(filePath);
            }
        }

        await Estudio.findOneAndDelete({ _id: req.params.id, usuarioId: req.userId });
        res.status(200).send();
    } catch (error) {
        res.status(500).json({ mensaje: "Error al eliminar estudio" });
    }
});

module.exports = router;
