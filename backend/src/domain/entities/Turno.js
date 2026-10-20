class Turno {
    constructor({
        id,
        usuarioId,
        pacienteNombre,
        fecha,
        hora,
        motivo = "General",
        especialidad = "General",
        doctor = "Dr. Asignado",
        estado = "Pendiente"
    }) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pacienteNombre = pacienteNombre;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.especialidad = especialidad;
        this.doctor = doctor;
        this.estado = estado;
    }
}

module.exports = Turno;
