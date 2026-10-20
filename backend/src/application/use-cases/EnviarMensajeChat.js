class EnviarMensajeChat {
    constructor(chatRepository, notificarUsuario = null) {
        this.chatRepository = chatRepository;
        this.notificarUsuario = notificarUsuario;
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

        // Simulación: Respuesta automática del doctor con try/catch para evitar unhandled rejections
        setTimeout(async () => {
            try {
                await this.chatRepository.save({
                    usuarioId,
                    remitente: 'DOCTOR',
                    texto: "Hola, recibí tu mensaje. En breve un especialista revisará tu caso."
                });

                if (userObj && this.notificarUsuario) {
                    await this.notificarUsuario(
                        userObj,
                        "👨‍⚕️ Respuesta Médica",
                        "Tienes un nuevo mensaje de tu doctor."
                    );
                }
            } catch (err) {
                console.error("❌ Error en respuesta simulada de chat:", err.message);
            }
        }, 2000);

        return mensajePaciente;
    }
}

module.exports = EnviarMensajeChat;
