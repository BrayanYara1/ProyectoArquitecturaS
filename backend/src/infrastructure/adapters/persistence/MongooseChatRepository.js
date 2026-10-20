const MessageModel = require('../../../../models/Message');
const MessageEntity = require('../../../domain/entities/Message');
const ChatRepository = require('../../../domain/ports/ChatRepository');

class MongooseChatRepository extends ChatRepository {
    async findByUsuarioId(usuarioId) {
        const docs = await MessageModel.find({ usuarioId }).sort({ fecha: 1 });
        return docs.map(doc => this._mapToEntity(doc));
    }

    async save(messageData) {
        const doc = new MessageModel({
            usuarioId: messageData.usuarioId,
            remitente: messageData.remitente,
            texto: messageData.texto,
            fecha: messageData.fecha || new Date(),
            leido: messageData.leido || false
        });
        const saved = await doc.save();
        return this._mapToEntity(saved);
    }

    _mapToEntity(doc) {
        return new MessageEntity({
            id: doc._id,
            usuarioId: doc.usuarioId,
            remitente: doc.remitente,
            texto: doc.texto,
            fecha: doc.fecha,
            leido: doc.leido
        });
    }
}

module.exports = MongooseChatRepository;
