class Message {
    constructor({
        id,
        usuarioId,
        remitente,
        texto,
        fecha = new Date(),
        leido = false
    }) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.remitente = remitente;
        this.texto = texto;
        this.fecha = fecha;
        this.leido = leido;
    }
}

module.exports = Message;
