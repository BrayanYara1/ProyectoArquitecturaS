const env = require('./config/env');
const app = require('./app');

const PORT = env.PORT;

if (require.main === module) {
    app.listen(PORT, '0.0.0.0', () => {
        console.log(`🚀 Servidor corriendo en el puerto ${PORT}`);
        console.log(`📡 Rutas cargadas: Auth, Turnos, Medicamentos, Estudios, Admin, Chat`);
    });
}

module.exports = app;
