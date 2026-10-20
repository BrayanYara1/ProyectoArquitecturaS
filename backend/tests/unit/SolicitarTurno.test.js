const SolicitarTurno = require('../../src/application/use-cases/SolicitarTurno');
const TurnoRepository = require('../../src/domain/ports/TurnoRepository');

class FakeTurnoRepository extends TurnoRepository {
    constructor() {
        super();
        this.turnos = [];
    }

    async checkDisponibilidad(fecha, hora) {
        return !this.turnos.some(t => t.fecha === fecha && t.hora === hora && t.estado !== 'Cancelado');
    }

    async save(turnoData) {
        const nuevo = { id: `fake-${Date.now()}`, ...turnoData };
        this.turnos.push(nuevo);
        return nuevo;
    }

    async findByUsuarioId(usuarioId) {
        return this.turnos.filter(t => t.usuarioId === usuarioId);
    }
}

describe('Caso de Uso: SolicitarTurno', () => {
    let fakeRepo;
    let solicitarTurno;

    beforeEach(() => {
        fakeRepo = new FakeTurnoRepository();
        solicitarTurno = new SolicitarTurno(fakeRepo);
    });

    it('debería solicitar un turno con éxito en un horario libre', async () => {
        const turnoData = {
            usuarioId: 'user-123',
            pacienteNombre: 'Juan Pérez',
            fecha: '2026-11-15',
            hora: '10:00',
            especialidad: 'Medicina General',
            doctor: 'Dr. López'
        };

        const resultado = await solicitarTurno.ejecutar(turnoData);

        expect(resultado).toHaveProperty('id');
        expect(resultado.fecha).toBe('2026-11-15');
        expect(resultado.hora).toBe('10:00');
        expect(resultado.estado).toBe('Pendiente');
    });

    it('debería rechazar la solicitud si el horario ya está ocupado', async () => {
        const turnoData = {
            usuarioId: 'user-123',
            pacienteNombre: 'Juan Pérez',
            fecha: '2026-11-15',
            hora: '10:00'
        };

        // Guardar primer turno
        await solicitarTurno.ejecutar(turnoData);

        // Intentar reservar el mismo horario
        await expect(solicitarTurno.ejecutar(turnoData))
            .rejects
            .toThrow("Este horario ya fue tomado por otro paciente");
    });

    it('debería fallar si falta el campo obligatorio fecha u hora', async () => {
        await expect(solicitarTurno.ejecutar({ usuarioId: 'user-1', hora: '10:00' }))
            .rejects
            .toThrow("El campo 'fecha' es obligatorio");

        await expect(solicitarTurno.ejecutar({ usuarioId: 'user-1', fecha: '2026-11-15' }))
            .rejects
            .toThrow("El campo 'hora' es obligatorio");
    });
});
