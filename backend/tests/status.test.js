const request = require('supertest');
const app = require('../server');
const mongoose = require('mongoose');

describe('GET /api/status', () => {
  afterAll(async () => {
    await mongoose.connection.close();
  });

  it('debería responder con estado 200 y el estado del servidor', async () => {
    const res = await request(app).get('/api/status');
    expect(res.statusCode).toEqual(200);
    expect(res.body).toHaveProperty('status');
    expect(res.body.status).toBe('online');
  });

  it('debería responder 404 para rutas inexistentes', async () => {
    const res = await request(app).get('/api/ruta-que-no-existe');
    expect(res.statusCode).toEqual(404);
  });
});
