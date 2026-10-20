const TurnoModel = require('../../../../models/Turno');
const TurnoEntity = require('../../../domain/entities/Turno');
const TurnoRepository = require('../../../domain/ports/TurnoRepository');
const { ESTADOS_CANCELADOS } = require('../../../domain/constants/EstadosTurno');

class MongooseTurnoRepository extends TurnoRepository {
    async findByUsuarioId(usuarioId) {
        const docs = await TurnoModel.find({ usuarioId }).sort({ fecha: 1, hora: 1 });
        return docs.map(doc => this._mapToEntity(doc));
    }

    async findById(id) {
        const doc = await TurnoModel.findById(id);
        return doc ? this._mapToEntity(doc) : null;
    }

    async checkDisponibilidad(fecha, hora) {
        const turnoExistente = await TurnoModel.findOne({
            fecha,
            hora,
            estado: { $nin: ESTADOS_CANCELADOS }
        });
        return !turnoExistente;
    }

    async save(turnoData) {
        const doc = new TurnoModel({
            usuarioId: turnoData.usuarioId,
            pacienteNombre: turnoData.pacienteNombre,
            fecha: turnoData.fecha,
            hora: turnoData.hora,
            motivo: turnoData.motivo || 'General',
            especialidad: turnoData.especialidad || 'General',
            doctor: turnoData.doctor || 'Dr. Asignado',
            estado: turnoData.estado || 'Pendiente'
        });
        const saved = await doc.save();
        return this._mapToEntity(saved);
    }

    async deleteByIdAndUsuarioId(id, usuarioId) {
        const doc = await TurnoModel.findOne({ _id: id, usuarioId });
        if (!doc) return null;
        await TurnoModel.findByIdAndDelete(id);
        return this._mapToEntity(doc);
    }

    _mapToEntity(doc) {
        return new TurnoEntity({
            id: doc._id,
            usuarioId: doc.usuarioId,
            pacienteNombre: doc.pacienteNombre,
            fecha: doc.fecha,
            hora: doc.hora,
            motivo: doc.motivo,
            especialidad: doc.especialidad,
            doctor: doc.doctor,
            estado: doc.estado
        });
    }
}

module.exports = MongooseTurnoRepository;
