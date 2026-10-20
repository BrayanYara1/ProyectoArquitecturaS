class AuthController {
    constructor(registerUser, loginUser, userRepository) {
        this.registerUser = registerUser;
        this.loginUser = loginUser;
        this.userRepository = userRepository;
    }

    async register(req, res, next) {
        try {
            const result = await this.registerUser.execute(req.body);
            res.status(201).json({ mensaje: "OK", email: result.email });
        } catch (error) {
            next(error);
        }
    }

    async login(req, res, next) {
        try {
            const result = await this.loginUser.execute(req.body);
            res.json({ mensaje: "OK", ...result });
        } catch (error) {
            next(error);
        }
    }

    async updateProfile(req, res, next) {
        try {
            const { nombre, telefono, tipoSanguineo, alergias, condiciones, contactoEmergencia } = req.body;
            const user = await this.userRepository.update(
                req.userId,
                { nombre, telefono, tipoSanguineo, alergias, condiciones, contactoEmergencia }
            );
            if (!user) return res.status(404).json({ mensaje: "Usuario no encontrado" });

            res.json({
                id: user.id,
                nombre: user.nombre,
                email: user.email,
                telefono: user.telefono,
                tipoSanguineo: user.tipoSanguineo,
                alergias: user.alergias,
                condiciones: user.condiciones,
                contactoEmergencia: user.contactoEmergencia
            });
        } catch (error) {
            next(error);
        }
    }

    async updateFcmToken(req, res, next) {
        try {
            const { token } = req.body;
            await this.userRepository.update(req.userId, { fcmToken: token });
            res.status(200).send();
        } catch (error) {
            next(error);
        }
    }
}

module.exports = AuthController;
