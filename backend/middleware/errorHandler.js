/**
 * Middleware centralizado para manejo de errores.
 * Captura y registra errores detallados en stdout/stderr y devuelve respuestas sanitizadas al cliente.
 */
function manejadorErrores(err, req, res, next) {
    console.error(`❌ [${req.method} ${req.originalUrl}] Error:`, err.stack || err.message || err);

    // Si es un error personalizado con código de estado explícito
    if (err.statusCode) {
        return res.status(err.statusCode).json({ mensaje: err.message });
    }

    // Si es un error de validación de Mongoose
    if (err.name === 'ValidationError') {
        return res.status(400).json({ mensaje: err.message });
    }

    // Si es error de duplicidad de Mongoose (11000)
    if (err.code === 11000) {
        return res.status(400).json({ mensaje: 'El registro ya existe en la base de datos' });
    }

    // Para errores internos o fallas de BD, responder 500 genérico sin expone detalles internos
    res.status(500).json({ mensaje: 'Error interno en el servidor' });
}

module.exports = manejadorErrores;
