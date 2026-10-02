const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

class LoginUser {
    constructor(userRepository, secretKey) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
    }

    async execute({ email, contrasena }) {
        const user = await this.userRepository.findByEmail(email);
        if (!user) {
            throw new Error("Credenciales inválidas");
        }

        const isPasswordValid = await bcrypt.compare(contrasena, user.contrasena);
        if (!isPasswordValid) {
            throw new Error("Credenciales inválidas");
        }

        const token = jwt.sign({ userId: user.id }, this.secretKey, { expiresIn: '30d' });

        return {
            usuario: {
                id: user.id,
                nombre: user.nombre,
                email: user.email,
                telefono: user.telefono,
                tipoSanguineo: user.tipoSanguineo,
                alergias: user.alergias,
                condiciones: user.condiciones,
                contactoEmergencia: user.contactoEmergencia
            },
            token
        };
    }
}

module.exports = LoginUser;
