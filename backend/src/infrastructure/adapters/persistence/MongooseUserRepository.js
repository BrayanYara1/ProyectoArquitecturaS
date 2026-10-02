const User = require('../../../../models/User');
const UserEntity = require('../../../../src/domain/entities/User');
const UserRepository = require('../../../../src/domain/ports/UserRepository');

class MongooseUserRepository extends UserRepository {
    async findByEmail(email) {
        const userDoc = await User.findOne({ email });
        if (!userDoc) return null;
        return this._mapToEntity(userDoc);
    }

    async findById(id) {
        const userDoc = await User.findById(id);
        if (!userDoc) return null;
        return this._mapToEntity(userDoc);
    }

    async save(userEntity) {
        const userDoc = new User({
            nombre: userEntity.nombre,
            email: userEntity.email,
            telefono: userEntity.telefono,
            contrasena: userEntity.contrasena,
            isVerified: userEntity.isVerified,
            verificationCode: userEntity.verificationCode,
            fcmToken: userEntity.fcmToken,
            tipoSanguineo: userEntity.tipoSanguineo,
            alergias: userEntity.alergias,
            condiciones: userEntity.condiciones,
            contactoEmergencia: userEntity.contactoEmergencia
        });
        const savedDoc = await userDoc.save();
        return this._mapToEntity(savedDoc);
    }

    async update(id, userData) {
        const updatedDoc = await User.findByIdAndUpdate(id, userData, { new: true });
        if (!updatedDoc) return null;
        return this._mapToEntity(updatedDoc);
    }

    _mapToEntity(userDoc) {
        return new UserEntity({
            id: userDoc._id,
            nombre: userDoc.nombre,
            email: userDoc.email,
            telefono: userDoc.telefono,
            contrasena: userDoc.contrasena,
            isVerified: userDoc.isVerified,
            verificationCode: userDoc.verificationCode,
            fcmToken: userDoc.fcmToken,
            tipoSanguineo: userDoc.tipoSanguineo,
            alergias: userDoc.alergias,
            condiciones: userDoc.condiciones,
            contactoEmergencia: userDoc.contactoEmergencia,
            fechaCreacion: userDoc.fechaCreacion
        });
    }
}

module.exports = MongooseUserRepository;
