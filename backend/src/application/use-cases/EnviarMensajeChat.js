class EnviarMensajeChat {
    constructor(chatRepository, notificationService = null) {
        this.chatRepository = chatRepository;
        this.notificationService = notificationService;
    }

    async obtenerHistorial(usuarioId) {
        return await this.chatRepository.findByUsuarioId(usuarioId);
    }

    async enviarMensajePaciente({ usuarioId, texto, mensaje }, userObj = null) {
        // Soporta tanto 'texto' como 'mensaje' desde DTO
        const contenidoTexto = texto || mensaje;

        if (!contenidoTexto || !contenidoTexto.trim()) {
            const err = new Error("El texto del mensaje es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        const mensajePaciente = await this.chatRepository.save({
            usuarioId,
            remitente: 'PACIENTE',
            texto: contenidoTexto.trim()
        });

        // Simulación: Respuesta automática del doctor después de 2 segundos
        setTimeout(async () => {
            await this.chatRepository.save({
                usuarioId,
                remitente: 'DOCTOR',
                texto: "Hola, recibí tu mensaje. En breve un especialista revisará tu caso."
            });

            if (userObj && userObj.fcmToken && this.notificationService) {
                this.notificationService(
                    userObj.fcmToken,
                    "👨‍⚕️ Respuesta Médica",
                    "Tienes un nuevo mensaje de tu doctor."
                );
            }
        }, 2000);

        return mensajePaciente;
    }
}

module.exports = EnviarMensajeChat;
