const bcrypt = require('bcryptjs');
const User = require('../../domain/entities/User');

class RegisterUser {
    constructor(userRepository) {
        this.userRepository = userRepository;
    }

    async execute({ nombre, email, telefono, contrasena }) {
        if (!nombre || !email || !contrasena) {
            throw new Error("Faltan datos obligatorios");
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            throw new Error("Formato de email inválido");
        }

        if (contrasena.length < 6) {
            throw new Error("La contraseña debe tener al menos 6 caracteres");
        }

        const existingUser = await this.userRepository.findByEmail(email);
        if (existingUser) {
            throw new Error("El email ya está registrado");
        }

        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(contrasena, salt);

        const user = new User({
            nombre,
            email,
            telefono,
            contrasena: hashedPassword,
            isVerified: true // Set to true as in original code
        });

        return await this.userRepository.save(user);
    }
}

module.exports = RegisterUser;
