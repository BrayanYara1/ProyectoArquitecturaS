class SolicitarTurno {
    constructor(turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    async estaDisponible(fecha, hora) {
        return await this.turnoRepository.checkDisponibilidad(fecha, hora);
    }

    async ejecutar({ usuarioId, pacienteNombre, fecha, hora, motivo, especialidad, doctor, medico }, userProfile = {}) {
        // DTO validation: DTO de entrada con lista blanca de campos
        if (!fecha || !fecha.trim()) {
            const err = new Error("El campo 'fecha' es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        if (!hora || !hora.trim()) {
            const err = new Error("El campo 'hora' es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        const disponible = await this.estaDisponible(fecha, hora);
        if (!disponible) {
            const err = new Error("Este horario ya fue tomado por otro paciente");
            err.statusCode = 400;
            throw err;
        }

        // Sanitización y fallback de campos
        const nombrePaciente = pacienteNombre || userProfile.nombre || 'Paciente';
        const doctorAsignado = doctor || medico || 'Dr. Asignado';
        const especialidadTurno = especialidad || 'General';
        const motivoTurno = motivo || 'Consulta General';

        return await this.turnoRepository.save({
            usuarioId,
            pacienteNombre: nombrePaciente,
            fecha,
            hora,
            motivo: motivoTurno,
            especialidad: especialidadTurno,
            doctor: doctorAsignado,
            estado: 'Pendiente'
        });
    }

    async obtenerTurnos(usuarioId) {
        return await this.turnoRepository.findByUsuarioId(usuarioId);
    }

    async cancelarTurno(id, usuarioId) {
        return await this.turnoRepository.deleteByIdAndUsuarioId(id, usuarioId);
    }
}

module.exports = SolicitarTurno;
