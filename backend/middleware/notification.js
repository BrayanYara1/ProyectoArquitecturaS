const admin = require('firebase-admin');

// Intenta inicializar con variables de entorno para evitar archivos JSON sensibles en el repo
if (!admin.apps.length) {
    try {
        if (process.env.FIREBASE_SERVICE_ACCOUNT) {
            let jsonString = process.env.FIREBASE_SERVICE_ACCOUNT.trim();

            // Eliminar comillas simples o dobles accidentales al principio y al final
            if ((jsonString.startsWith("'") && jsonString.endsWith("'")) ||
                (jsonString.startsWith('"') && jsonString.endsWith('"'))) {
                jsonString = jsonString.slice(1, -1);
            }

            const serviceAccount = JSON.parse(jsonString);

            // CORRECCIÓN: Reemplazar saltos de línea literales para que Node.js los entienda
            if (serviceAccount.private_key) {
                serviceAccount.private_key = serviceAccount.private_key.replace(/\\n/g, '\n');
            }

            if (!serviceAccount.project_id || !serviceAccount.private_key) {
                throw new Error("El JSON de Firebase no tiene el formato correcto.");
            }
            admin.initializeApp({
                credential: admin.credential.cert(serviceAccount)
            });
            console.log("✅ Firebase Admin inicializado correctamente");
        } else {
            console.warn("⚠️ FIREBASE_SERVICE_ACCOUNT no encontrada. Las notificaciones Push estarán en modo simulación.");
        }
    } catch (error) {
        console.error("❌ Error al inicializar Firebase Admin:", error.message);
    }
}

const sendPushNotification = async (fcmToken, title, body, data = {}) => {
    if (!fcmToken) return;

    const message = {
        notification: { title, body },
        data: { ...data, click_action: "FLUTTER_NOTIFICATION_CLICK" },
        token: fcmToken
    };

    if (admin.apps.length > 0) {
        try {
            const response = await admin.messaging().send(message);
            console.log(`🚀 Notificación enviada: ${response}`);
            return { success: true, response };
        } catch (error) {
            console.error("❌ Error enviando Push:", error);
            if (error.code === 'messaging/registration-token-not-registered' ||
                error.code === 'messaging/invalid-registration-token') {
                return { success: false, error: 'NotRegistered' };
            }
            return { success: false, error: error.message };
        }
    } else {
        console.log(`[SIMULACIÓN PUSH] Para: ${fcmToken} | Título: ${title} | Body: ${body}`);
    }
};

module.exports = { sendPushNotification };
