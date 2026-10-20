class GestionarEstudios {
    constructor(estudioRepository) {
        this.estudioRepository = estudioRepository;
    }

    async listar(usuarioId) {
        return await this.estudioRepository.findByUsuarioId(usuarioId);
    }

    async crear({ usuarioId, titulo, fecha, tipo, laboratorio, resultadoBreve, descripcion, notas, urlDocumento }) {
        // DTO Validation
        if (!titulo || !titulo.trim()) {
            const err = new Error("El campo 'titulo' es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        if (!fecha || !fecha.trim()) {
            const err = new Error("El campo 'fecha' es obligatorio");
            err.statusCode = 400;
            throw err;
        }

        // Mapeo flexible para web y móvil
        const tipoFinal = tipo || laboratorio || 'General';
        const resultadoFinal = resultadoBreve || descripcion || '';
        const notasFinales = notas || descripcion || '';

        return await this.estudioRepository.save({
            usuarioId,
            titulo,
            fecha,
            tipo: tipoFinal,
            resultadoBreve: resultadoFinal,
            urlDocumento: urlDocumento || null,
            notas: notasFinales
        });
    }

    async obtenerPorId(id, usuarioId) {
        return await this.estudioRepository.findByIdAndUsuarioId(id, usuarioId);
    }

    async eliminar(id, usuarioId) {
        return await this.estudioRepository.delete(id, usuarioId);
    }
}

module.exports = GestionarEstudios;
