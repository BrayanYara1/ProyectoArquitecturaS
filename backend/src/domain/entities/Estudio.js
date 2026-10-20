class Estudio {
    constructor({
        id,
        usuarioId,
        titulo,
        fecha,
        tipo = "General",
        resultadoBreve = "",
        urlDocumento = null,
        notas = ""
    }) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.fecha = fecha;
        this.tipo = tipo;
        this.resultadoBreve = resultadoBreve;
        this.urlDocumento = urlDocumento;
        this.notas = notas;
    }
}

module.exports = Estudio;
