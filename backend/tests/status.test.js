const request = require('supertest');
const app = require('../app');
const { cerrarConexiones } = require('../config/dbConnections');

describe('API Health and Status Endpoints', () => {
  afterAll(async () => {
    await cerrarConexiones();
  });

  it('debería responder con estado 200 y el estado del servidor en /api/status', async () => {
    const res = await request(app).get('/api/status');
    expect(res.statusCode).toEqual(200);
    expect(res.body).toHaveProperty('status');
    expect(res.body.status).toBe('online');
  });

  it('debería responder en /api/health con el objeto de salud de las 5 bases', async () => {
    const res = await request(app).get('/api/health');
    expect([200, 503]).toContain(res.statusCode);
    expect(res.body).toHaveProperty('estado');
    expect(res.body).toHaveProperty('version');
    expect(res.body).toHaveProperty('bases');
    expect(res.body.bases).toHaveProperty('auth');
    expect(res.body.bases).toHaveProperty('turnos');
    expect(res.body.bases).toHaveProperty('medicamentos');
    expect(res.body.bases).toHaveProperty('estudios');
    expect(res.body.bases).toHaveProperty('chat');
  });

  it('debería responder 404 para rutas inexistentes', async () => {
    const res = await request(app).get('/api/ruta-que-no-existe');
    expect(res.statusCode).toEqual(404);
  });
});
