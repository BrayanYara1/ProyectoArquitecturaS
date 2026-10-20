const EstudioModel = require('../../../../models/Estudio');
const EstudioEntity = require('../../../domain/entities/Estudio');
const EstudioRepository = require('../../../domain/ports/EstudioRepository');

class MongooseEstudioRepository extends EstudioRepository {
    async findByUsuarioId(usuarioId) {
        const docs = await EstudioModel.find({ usuarioId }).sort({ fecha: -1 });
        return docs.map(doc => this._mapToEntity(doc));
    }

    async findByIdAndUsuarioId(id, usuarioId) {
        const doc = await EstudioModel.findOne({ _id: id, usuarioId });
        return doc ? this._mapToEntity(doc) : null;
    }

    async save(estudioData) {
        const doc = new EstudioModel({
            usuarioId: estudioData.usuarioId,
            titulo: estudioData.titulo,
            fecha: estudioData.fecha,
            tipo: estudioData.tipo || 'General',
            resultadoBreve: estudioData.resultadoBreve || '',
            urlDocumento: estudioData.urlDocumento || null,
            notas: estudioData.notas || ''
        });
        const saved = await doc.save();
        return this._mapToEntity(saved);
    }

    async delete(id, usuarioId) {
        const deleted = await EstudioModel.findOneAndDelete({ _id: id, usuarioId });
        return deleted ? this._mapToEntity(deleted) : null;
    }

    _mapToEntity(doc) {
        return new EstudioEntity({
            id: doc._id,
            usuarioId: doc.usuarioId,
            titulo: doc.titulo,
            fecha: doc.fecha,
            tipo: doc.tipo,
            resultadoBreve: doc.resultadoBreve,
            urlDocumento: doc.urlDocumento,
            notas: doc.notas
        });
    }
}

module.exports = MongooseEstudioRepository;
