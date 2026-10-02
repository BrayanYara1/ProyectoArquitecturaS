class User {
    constructor({
        id,
        nombre,
        email,
        telefono,
        contrasena,
        isVerified = false,
        verificationCode = null,
        fcmToken = null,
        tipoSanguineo = "",
        alergias = "",
        condiciones = "",
        contactoEmergencia = "",
        fechaCreacion = new Date()
    }) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.contrasena = contrasena;
        this.isVerified = isVerified;
        this.verificationCode = verificationCode;
        this.fcmToken = fcmToken;
        this.tipoSanguineo = tipoSanguineo;
        this.alergias = alergias;
        this.condiciones = condiciones;
        this.contactoEmergencia = contactoEmergencia;
        this.fechaCreacion = fechaCreacion;
    }
}

module.exports = User;
