const RegisterUser = require('../../src/application/use-cases/RegisterUser');

describe('Caso de Uso: RegisterUser', () => {
    let mockUserRepository;
    let registerUser;

    beforeEach(() => {
        mockUserRepository = {
            findByEmail: jest.fn(),
            save: jest.fn()
        };
        registerUser = new RegisterUser(mockUserRepository);
    });

    it('debería rechazar un correo con formato inválido', async () => {
        const inputData = {
            nombre: 'Juan Pérez',
            email: 'correo-invalido',
            contrasena: '123456'
        };

        await expect(registerUser.execute(inputData))
            .rejects
            .toThrow("Formato de email inválido");
    });

    it('debería rechazar una contraseña con menos de 6 caracteres', async () => {
        const inputData = {
            nombre: 'Juan Pérez',
            email: 'juan@test.com',
            contrasena: '123'
        };

        await expect(registerUser.execute(inputData))
            .rejects
            .toThrow("La contraseña debe tener al menos 6 caracteres");
    });

    it('debería registrar un usuario con datos válidos', async () => {
        const inputData = {
            nombre: 'Juan Pérez',
            email: 'juan@test.com',
            contrasena: '123456',
            telefono: '3001234567'
        };

        mockUserRepository.findByEmail.mockResolvedValue(null);
        mockUserRepository.save.mockImplementation(async user => ({ ...user, id: 'user-id-123' }));

        const resultado = await registerUser.execute(inputData);

        expect(resultado).toHaveProperty('id');
        expect(resultado.email).toBe('juan@test.com');
        expect(mockUserRepository.save).toHaveBeenCalled();
    });
});
