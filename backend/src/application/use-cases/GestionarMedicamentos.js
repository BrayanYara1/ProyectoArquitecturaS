class GestionarMedicamentos {
    constructor(medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    async listar(usuarioId) {
        return await this.medicamentoRepository.findByUsuarioId(usuarioId);
    }

    async crear({ usuarioId, nombre, dosis, frecuencia, proximaToma, horario, notas }) {
        // Validación DTO
        if (!nombre || !nombre.trim()) {
            const err = new Error("El campo 'nombre' es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        if (!dosis || !dosis.trim()) {
            const err = new Error("El campo 'dosis' es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        // Mapeo flexible del contrato (web puede enviar 'horario')
        const frecuenciaValor = frecuencia || horario || 'Diario';
        const proximaTomaValor = proximaToma || horario || '08:00 AM';

        return await this.medicamentoRepository.save({
            usuarioId,
            nombre,
            dosis,
            frecuencia: frecuenciaValor,
            proximaToma: proximaTomaValor,
            notas: notas || ''
        });
    }

    async actualizar(id, usuarioId, datos) {
        // Lista blanca de actualización
        const updateData = {};
        if (datos.nombre) updateData.nombre = datos.nombre;
        if (datos.dosis) updateData.dosis = datos.dosis;
        if (datos.frecuencia || datos.horario) updateData.frecuencia = datos.frecuencia || datos.horario;
        if (datos.proximaToma || datos.horario) updateData.proximaToma = datos.proximaToma || datos.horario;
        if (datos.notas !== undefined) updateData.notas = datos.notas;

        return await this.medicamentoRepository.update(id, usuarioId, updateData);
    }

    async eliminar(id, usuarioId) {
        return await this.medicamentoRepository.delete(id, usuarioId);
    }
}

module.exports = GestionarMedicamentos;
