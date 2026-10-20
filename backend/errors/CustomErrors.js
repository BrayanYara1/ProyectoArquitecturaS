class ErrorValidacion extends Error {
    constructor(mensaje) {
        super(mensaje);
        this.name = 'ErrorValidacion';
        this.statusCode = 400;
    }
}

class NoEncontrado extends Error {
    constructor(mensaje = 'Recurso no encontrado') {
        super(mensaje);
        this.name = 'NoEncontrado';
        this.statusCode = 404;
    }
}

module.exports = {
    ErrorValidacion,
    NoEncontrado
};
