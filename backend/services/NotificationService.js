const { sendPushNotification } = require('../middleware/notification');
const MongooseUserRepository = require('../src/infrastructure/adapters/persistence/MongooseUserRepository');

const userRepository = new MongooseUserRepository();

/**
 * Servicio centralizado para enviar notificaciones Push a un usuario y limpiar tokens inválidos.
 */
async function notificarUsuario(user, titulo, cuerpo, data = {}) {
    if (!user || !user.fcmToken) return null;

    try {
        const result = await sendPushNotification(user.fcmToken, titulo, cuerpo, data);
        if (result && result.error === 'NotRegistered' && user.id) {
            console.warn(`⚠️ Limpiando FCM token inválido para usuario: ${user.id}`);
            await userRepository.update(user.id, { fcmToken: null });
        }
        return result;
    } catch (error) {
        console.error('❌ Error en NotificationService:', error.message);
        return null;
    }
}

module.exports = {
    notificarUsuario
};
