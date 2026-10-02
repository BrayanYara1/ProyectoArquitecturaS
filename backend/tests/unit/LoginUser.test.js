const LoginUser = require('../../src/application/use-cases/LoginUser');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

jest.mock('bcryptjs');
jest.mock('jsonwebtoken');

describe('LoginUser Use Case', () => {
    let mockUserRepository;
    let loginUser;
    const SECRET_KEY = 'test_secret';

    beforeEach(() => {
        mockUserRepository = {
            findByEmail: jest.fn(),
        };
        loginUser = new LoginUser(mockUserRepository, SECRET_KEY);
    });

    it('should throw an error if user is not found', async () => {
        mockUserRepository.findByEmail.mockResolvedValue(null);

        await expect(loginUser.execute({ email: 'test@test.com', contrasena: 'password' }))
            .rejects.toThrow('Credenciales inválidas');
    });

    it('should throw an error if password is invalid', async () => {
        const mockUser = { id: '1', email: 'test@test.com', contrasena: 'hashed_password' };
        mockUserRepository.findByEmail.mockResolvedValue(mockUser);
        bcrypt.compare.mockResolvedValue(false);

        await expect(loginUser.execute({ email: 'test@test.com', contrasena: 'wrong_password' }))
            .rejects.toThrow('Credenciales inválidas');
    });

    it('should return user info and token if login is successful', async () => {
        const mockUser = {
            id: '1',
            nombre: 'Test User',
            email: 'test@test.com',
            contrasena: 'hashed_password',
            telefono: '123456',
            tipoSanguineo: 'O+',
            alergias: 'None',
            condiciones: 'None',
            contactoEmergencia: '911'
        };
        mockUserRepository.findByEmail.mockResolvedValue(mockUser);
        bcrypt.compare.mockResolvedValue(true);
        jwt.sign.mockReturnValue('mocked_token');

        const result = await loginUser.execute({ email: 'test@test.com', contrasena: 'password' });

        expect(result.token).toBe('mocked_token');
        expect(result.usuario.nombre).toBe('Test User');
        expect(mockUserRepository.findByEmail).toHaveBeenCalledWith('test@test.com');
    });
});
