const MedicamentoModel = require('../../../../models/Medicamento');
const MedicamentoEntity = require('../../../domain/entities/Medicamento');
const MedicamentoRepository = require('../../../domain/ports/MedicamentoRepository');

class MongooseMedicamentoRepository extends MedicamentoRepository {
    async findByUsuarioId(usuarioId) {
        const docs = await MedicamentoModel.find({ usuarioId });
        return docs.map(doc => this._mapToEntity(doc));
    }

    async save(medicamentoData) {
        const doc = new MedicamentoModel({
            usuarioId: medicamentoData.usuarioId,
            nombre: medicamentoData.nombre,
            dosis: medicamentoData.dosis,
            frecuencia: medicamentoData.frecuencia,
            proximaToma: medicamentoData.proximaToma,
            notas: medicamentoData.notas || ''
        });
        const saved = await doc.save();
        return this._mapToEntity(saved);
    }

    async update(id, usuarioId, medicamentoData) {
        const updated = await MedicamentoModel.findOneAndUpdate(
            { _id: id, usuarioId },
            medicamentoData,
            { new: true }
        );
        return updated ? this._mapToEntity(updated) : null;
    }

    async delete(id, usuarioId) {
        const deleted = await MedicamentoModel.findOneAndDelete({ _id: id, usuarioId });
        return deleted ? this._mapToEntity(deleted) : null;
    }

    _mapToEntity(doc) {
        return new MedicamentoEntity({
            id: doc._id,
            usuarioId: doc.usuarioId,
            nombre: doc.nombre,
            dosis: doc.dosis,
            frecuencia: doc.frecuencia,
            proximaToma: doc.proximaToma,
            notas: doc.notas
        });
    }
}

module.exports = MongooseMedicamentoRepository;
