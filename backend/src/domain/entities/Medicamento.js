class Medicamento {
    constructor({
        id,
        usuarioId,
        nombre,
        dosis,
        frecuencia,
        proximaToma,
        notas = ""
    }) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.proximaToma = proximaToma;
        this.notas = notas;
    }
}

module.exports = Medicamento;
