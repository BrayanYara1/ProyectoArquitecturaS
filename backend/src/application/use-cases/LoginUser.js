const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { ErrorValidacion } = require('../../../errors/CustomErrors');

class LoginUser {
    constructor(userRepository, secretKey) {
        this.userRepository = userRepository;
        this.secretKey = secretKey;
    }

    async execute({ email, contrasena }) {
        if (!email || !contrasena) {
            throw new ErrorValidacion("Email y contraseña son obligatorios");
        }

        const user = await this.userRepository.findByEmail(email);
        if (!user) {
            const err = new ErrorValidacion("Credenciales inválidas");
            err.statusCode = 401;
            throw err;
        }

        const isPasswordValid = await bcrypt.compare(contrasena, user.contrasena);
        if (!isPasswordValid) {
            const err = new ErrorValidacion("Credenciales inválidas");
            err.statusCode = 401;
            throw err;
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
