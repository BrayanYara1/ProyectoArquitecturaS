/* App JS - Salud Activa Web Client Completo */

const API_BASE_URL = 'https://saludactiva-backend.onrender.com/api';

// Helpers de Sesión
function getToken() { return localStorage.getItem('saludactiva_token'); }
function setToken(token) { localStorage.setItem('saludactiva_token', token); }
function getUser() { const u = localStorage.getItem('saludactiva_user'); return u ? JSON.parse(u) : null; }
function setUser(user) { localStorage.setItem('saludactiva_user', JSON.stringify(user)); }

function clearSession() {
    localStorage.removeItem('saludactiva_token');
    localStorage.removeItem('saludactiva_user');
    if (!window.location.pathname.endsWith('index.html') && window.location.pathname !== '/') {
        window.location.href = 'index.html';
    }
}

// Alertas dinámicas
function showAlert(containerId, message, type = 'danger') {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
}

// Fetch Interceptor
async function apiRequest(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        if (response.status === 401 && !endpoint.includes('/auth/login')) {
            clearSession();
            return null;
        }

        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            const errorMsg = data.mensaje || data.detalle || data.error || data.message || `Error ${response.status}`;
            throw new Error(errorMsg);
        }
        return data;
    } catch (err) {
        console.error('API Error:', err);
        throw err;
    }
}

// MODO OSCURO
function initDarkMode() {
    const btn = document.getElementById('btnDarkModeToggle');
    if (!btn) return;
    const isDark = localStorage.getItem('saludactiva_dark') === 'true';
    if (isDark) document.body.classList.add('dark-mode');

    btn.addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        const active = document.body.classList.contains('dark-mode');
        localStorage.setItem('saludactiva_dark', active);
        btn.innerHTML = active ? '<i class="fa-solid fa-sun"></i>' : '<i class="fa-solid fa-moon"></i>';
    });
}

// DOM CONTENT LOADED
document.addEventListener('DOMContentLoaded', () => {
    initDarkMode();

    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    if (loginForm) {
        if (getToken()) { window.location.href = 'dashboard.html'; return; }

        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('loginEmail').value.trim();
            const password = document.getElementById('loginPassword').value;

            try {
                const res = await apiRequest('/auth/login', 'POST', {
                    email,
                    contrasena: password,
                    password: password
                });

                if (res && res.token) {
                    setToken(res.token);
                    setUser(res.usuario || res.user || { email });
                    showAlert('alertContainer', '¡Inicio de sesión exitoso!', 'success');
                    setTimeout(() => { window.location.href = 'dashboard.html'; }, 600);
                } else if (res) {
                    showAlert('alertContainer', res.mensaje || 'Respuesta no válida del servidor');
                }
            } catch (err) {
                showAlert('alertContainer', err.message || 'Error al iniciar sesión');
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const nombre = document.getElementById('regNombre').value.trim();
            const email = document.getElementById('regEmail').value.trim();
            const dni = document.getElementById('regDni').value.trim();
            const telefono = document.getElementById('regTelefono').value.trim();
            const password = document.getElementById('regPassword').value;

            try {
                await apiRequest('/auth/register', 'POST', {
                    nombre, email, dni, telefono, contrasena: password, password
                });
                showAlert('alertContainer', 'Registro exitoso. Inicia sesión a continuación.', 'success');
                registerForm.reset();
                const loginTab = document.getElementById('login-tab');
                if (loginTab) loginTab.click();
            } catch (err) {
                showAlert('alertContainer', err.message || 'Error en el registro');
            }
        });
    }

    // --- DASHBOARD LOGIC ---
    const btnLogout = document.getElementById('btnLogout');
    if (btnLogout) {
        if (!getToken()) { window.location.href = 'index.html'; return; }

        const user = getUser();
        if (user) {
            const userNameDisplay = document.getElementById('userNameDisplay');
            if (userNameDisplay) userNameDisplay.textContent = user.nombre || user.email || 'Paciente';

            // Llenar formulario de Perfil
            const profNombre = document.getElementById('profNombre');
            if (profNombre) profNombre.value = user.nombre || '';
            const profTelefono = document.getElementById('profTelefono');
            if (profTelefono) profTelefono.value = user.telefono || '';
            const profTipo = document.getElementById('profTipoSanguineo');
            if (profTipo && user.tipoSanguineo) profTipo.value = user.tipoSanguineo;
            const profAlergias = document.getElementById('profAlergias');
            if (profAlergias) profAlergias.value = user.alergias || '';
            const profCondiciones = document.getElementById('profCondiciones');
            if (profCondiciones) profCondiciones.value = user.condiciones || '';
            const profEmergencia = document.getElementById('profContactoEmergencia');
            if (profEmergencia) profEmergencia.value = user.contactoEmergencia || '';
        }

        btnLogout.addEventListener('click', () => {
            clearSession();
            window.location.href = 'index.html';
        });

        // Cargar todos los módulos
        cargarDashboardHome();
        cargarTurnos();
        cargarEspecialidades();
        cargarMedicamentos();
        cargarEstudios();
        cargarChat();
        cargarSintomasLocal();

        // HANDLERS DE FORMULARIOS
        const formSolicitar = document.getElementById('formSolicitarTurno');
        if (formSolicitar) {
            formSolicitar.addEventListener('submit', async (e) => {
                e.preventDefault();
                const especialidad = document.getElementById('selectEspecialidad').value;
                const medico = document.getElementById('inputMedico').value;
                const fecha = document.getElementById('inputFecha').value;
                const hora = document.getElementById('inputHora').value;
                const motivo = document.getElementById('inputMotivo').value;

                try {
                    await apiRequest('/turnos', 'POST', { especialidad, medico, fecha, hora, motivo });
                    showAlert('dashAlertContainer', 'Turno reservado exitosamente', 'success');
                    formSolicitar.reset();
                    cargarDashboardHome();
                    cargarTurnos();
                    document.getElementById('tab-turnos').click();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al reservar turno');
                }
            });
        }

        const formPerfil = document.getElementById('formPerfilUsuario');
        if (formPerfil) {
            formPerfil.addEventListener('submit', async (e) => {
                e.preventDefault();
                const nombre = document.getElementById('profNombre').value;
                const telefono = document.getElementById('profTelefono').value;
                const tipoSanguineo = document.getElementById('profTipoSanguineo').value;
                const alergias = document.getElementById('profAlergias').value;
                const condiciones = document.getElementById('profCondiciones').value;
                const contactoEmergencia = document.getElementById('profContactoEmergencia').value;

                try {
                    const updated = await apiRequest('/auth/profile', 'PUT', {
                        nombre, telefono, tipoSanguineo, alergias, condiciones, contactoEmergencia
                    });
                    if (updated) setUser(updated);
                    showAlert('dashAlertContainer', 'Perfil de salud actualizado correctamente', 'success');
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al actualizar perfil');
                }
            });
        }

        const formNuevoMedicamento = document.getElementById('formNuevoMedicamento');
        if (formNuevoMedicamento) {
            formNuevoMedicamento.addEventListener('submit', async (e) => {
                e.preventDefault();
                const nombre = document.getElementById('medNombre').value;
                const dosis = document.getElementById('medDosis').value;
                const horario = document.getElementById('medHorario').value;

                try {
                    await apiRequest('/medicamentos', 'POST', { nombre, dosis, horario });
                    showAlert('dashAlertContainer', 'Medicamento agregado correctamente', 'success');
                    formNuevoMedicamento.reset();
                    const modalEl = document.getElementById('modalNuevoMedicamento');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    if (modal) modal.hide();
                    cargarMedicamentos();
                    cargarDashboardHome();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al agregar medicamento');
                }
            });
        }

        const formNuevoEstudio = document.getElementById('formNuevoEstudio');
        if (formNuevoEstudio) {
            formNuevoEstudio.addEventListener('submit', async (e) => {
                e.preventDefault();
                const titulo = document.getElementById('estudioTitulo').value;
                const laboratorio = document.getElementById('estudioLab').value;
                const fecha = document.getElementById('estudioFecha').value;
                const descripcion = document.getElementById('estudioDesc').value;

                try {
                    await apiRequest('/estudios', 'POST', { titulo, laboratorio, fecha, descripcion });
                    showAlert('dashAlertContainer', 'Estudio registrado exitosamente', 'success');
                    formNuevoEstudio.reset();
                    const modalEl = document.getElementById('modalNuevoEstudio');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    if (modal) modal.hide();
                    cargarEstudios();
                    cargarDashboardHome();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al guardar estudio');
                }
            });
        }

        const formChat = document.getElementById('formChat');
        if (formChat) {
            formChat.addEventListener('submit', async (e) => {
                e.preventDefault();
                const inputMsg = document.getElementById('inputChatMsg');
                const mensaje = inputMsg.value.trim();
                if (!mensaje) return;

                try {
                    await apiRequest('/chat', 'POST', { mensaje });
                    inputMsg.value = '';
                    cargarChat();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al enviar mensaje');
                }
            });
        }

        const formSintoma = document.getElementById('formSintoma');
        if (formSintoma) {
            formSintoma.addEventListener('submit', (e) => {
                e.preventDefault();
                const val = document.getElementById('inputSintoma').value.trim();
                if (!val) return;
                agregarSintomaLocal(val);
                document.getElementById('inputSintoma').value = '';
            });
        }
    }
});

// --- FUNCIONES MÓDULOS DE SALUD ---
async function cargarDashboardHome() {
    try {
        const [turnos, meds, estudios] = await Promise.all([
            apiRequest('/turnos').catch(() => []),
            apiRequest('/medicamentos').catch(() => []),
            apiRequest('/estudios').catch(() => [])
        ]);

        const statTurnos = document.getElementById('statTurnosCount');
        if (statTurnos) statTurnos.textContent = turnos ? turnos.length : 0;
        const statMeds = document.getElementById('statMedsCount');
        if (statMeds) statMeds.textContent = meds ? meds.length : 0;
        const statEstudios = document.getElementById('statEstudiosCount');
        if (statEstudios) statEstudios.textContent = estudios ? estudios.length : 0;

        // Próximo Turno Destacado
        const homeProximo = document.getElementById('homeProximoTurno');
        if (homeProximo) {
            if (turnos && turnos.length > 0) {
                const p = turnos[0];
                homeProximo.innerHTML = `
                    <div class="p-2 border-start border-3 border-primary bg-light rounded">
                        <h6 class="fw-bold mb-1">${p.especialidad || 'Consulta Médica'}</h6>
                        <p class="small mb-1 text-muted"><i class="fa-solid fa-user-doctor me-1"></i>${p.medico || 'Médico Asignado'}</p>
                        <span class="badge bg-primary"><i class="fa-regular fa-calendar me-1"></i>${p.fecha || ''} ${p.hora || ''}</span>
                    </div>
                `;
            } else {
                homeProximo.innerHTML = `<p class="text-muted mb-0">No tienes turnos programados.</p>`;
            }
        }

        // Dosis del Día
        const homeMeds = document.getElementById('homeMedsDia');
        if (homeMeds) {
            if (meds && meds.length > 0) {
                homeMeds.innerHTML = meds.map((m, idx) => `
                    <div class="d-flex justify-content-between align-items-center border-bottom py-1">
                        <div>
                            <span class="fw-bold">${m.nombre}</span> <small class="text-muted">(${m.dosis})</small>
                        </div>
                        <button class="btn btn-sm btn-outline-success py-0 px-2" onclick="marcarTomado('${m.nombre}')">
                            <i class="fa-solid fa-check"></i> Tomado
                        </button>
                    </div>
                `).join('');
            } else {
                homeMeds.innerHTML = `<p class="text-muted mb-0">Sin medicamentos programados para hoy.</p>`;
            }
        }
    } catch (err) {
        console.error('Home Dashboard error:', err);
    }
}

function marcarTomado(nombre) {
    showAlert('dashAlertContainer', `¡Marcarte la toma de ${nombre}! Registro de salud actualizado.`, 'success');
}

async function cargarTurnos() {
    const container = document.getElementById('turnosListContainer');
    if (!container) return;
    try {
        const turnos = await apiRequest('/turnos');
        if (!turnos || turnos.length === 0) {
            container.innerHTML = `<div class="col-12 text-center py-4 text-muted"><i class="fa-solid fa-calendar-xmark fa-2x mb-2 d-block"></i>No tienes turnos programados.</div>`;
            return;
        }

        container.innerHTML = turnos.map(t => `
            <div class="col-md-6 col-lg-4">
                <div class="card custom-card p-3 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="badge bg-primary rounded-pill">${t.especialidad || 'Consulta Médica'}</span>
                        <span class="badge bg-success status-badge">${t.estado || 'Confirmado'}</span>
                    </div>
                    <h5 class="card-title text-truncate">${t.medico || 'Médico Asignado'}</h5>
                    <p class="card-text mb-1 text-muted"><i class="fa-regular fa-calendar me-2"></i>${t.fecha || 'Sin fecha'}</p>
                    <p class="card-text mb-2 text-muted"><i class="fa-regular fa-clock me-2"></i>${t.hora || 'Sin hora'}</p>
                    ${t.motivo ? `<p class="small text-secondary mb-3"><strong>Motivo:</strong> ${t.motivo}</p>` : ''}
                    <button class="btn btn-outline-danger btn-sm mt-auto" onclick="cancelarTurno('${t._id || t.id}')">
                        <i class="fa-solid fa-trash me-1"></i> Cancelar Turno
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-danger py-3">Error al cargar turnos: ${err.message}</div>`;
    }
}

async function cancelarTurno(id) {
    if (!confirm('¿Deseas cancelar este turno?')) return;
    try {
        await apiRequest(`/turnos/${id}`, 'DELETE');
        showAlert('dashAlertContainer', 'Turno cancelado correctamente', 'info');
        cargarTurnos();
        cargarDashboardHome();
    } catch (err) {
        showAlert('dashAlertContainer', err.message || 'Error al cancelar turno');
    }
}

async function cargarEspecialidades() {
    const select = document.getElementById('selectEspecialidad');
    const grid = document.getElementById('especialidadesGrid');

    const lista = [
        { nombre: 'Clínica Médica', icono: 'fa-user-doctor', desc: 'Consultas generales y diagnóstico preventivo.' },
        { nombre: 'Cardiología', icono: 'fa-heart-pulse', desc: 'Cuidado y tratamiento del corazón y sistema circulatorio.' },
        { nombre: 'Dermatología', icono: 'fa-allergies', desc: 'Atención especializada para el cuidado de la piel.' },
        { nombre: 'Pediatría', icono: 'fa-baby', desc: 'Atención de salud integral para niños y adolescentes.' },
        { nombre: 'Traumatología', icono: 'fa-bone', desc: 'Tratamiento de lesiones óseas y articulares.' },
        { nombre: 'Ginecología', icono: 'fa-person-pregnant', desc: 'Salud reproductiva e integral de la mujer.' },
        { nombre: 'Oftalmología', icono: 'fa-eye', desc: 'Cuidado y examen de la visión.' },
        { nombre: 'Neurología', icono: 'fa-brain', desc: 'Especialidad del sistema nervioso central y cerebro.' }
    ];

    if (select) {
        select.innerHTML = lista.map(e => `<option value="${e.nombre}">${e.nombre}</option>`).join('');
    }

    if (grid) {
        grid.innerHTML = lista.map(e => `
            <div class="col-md-6 col-lg-3">
                <div class="card custom-card p-3 text-center h-100">
                    <i class="fa-solid ${e.icono} fa-3x text-primary mb-3"></i>
                    <h5 class="card-title fw-bold">${e.nombre}</h5>
                    <p class="small text-muted mb-3">${e.desc}</p>
                    <button class="btn btn-outline-primary btn-sm mt-auto" onclick="seleccionarEspecialidad('${e.nombre}')">
                        <i class="fa-solid fa-calendar-plus me-1"></i> Reservar
                    </button>
                </div>
            </div>
        `).join('');

        const inputSearch = document.getElementById('searchEspecialidad');
        if (inputSearch) {
            inputSearch.addEventListener('input', (e) => {
                const query = e.target.value.toLowerCase();
                const filtrados = lista.filter(item => item.nombre.toLowerCase().includes(query));
                grid.innerHTML = filtrados.map(e => `
                    <div class="col-md-6 col-lg-3">
                        <div class="card custom-card p-3 text-center h-100">
                            <i class="fa-solid ${e.icono} fa-3x text-primary mb-3"></i>
                            <h5 class="card-title fw-bold">${e.nombre}</h5>
                            <p class="small text-muted mb-3">${e.desc}</p>
                            <button class="btn btn-outline-primary btn-sm mt-auto" onclick="seleccionarEspecialidad('${e.nombre}')">
                                <i class="fa-solid fa-calendar-plus me-1"></i> Reservar
                            </button>
                        </div>
                    </div>
                `).join('');
            });
        }
    }
}

function seleccionarEspecialidad(nombre) {
    const select = document.getElementById('selectEspecialidad');
    if (select) select.value = nombre;
    const tabSolicitar = document.getElementById('tab-solicitar');
    if (tabSolicitar) tabSolicitar.click();
}

async function cargarMedicamentos() {
    const container = document.getElementById('medicamentosListContainer');
    if (!container) return;
    try {
        const meds = await apiRequest('/medicamentos');
        if (!meds || meds.length === 0) {
            container.innerHTML = `<div class="col-12 text-center py-4 text-muted"><i class="fa-solid fa-capsules fa-2x mb-2 d-block"></i>No registras medicamentos activos.</div>`;
            return;
        }

        container.innerHTML = meds.map(m => `
            <div class="col-md-6 col-lg-4">
                <div class="card custom-card p-3 h-100">
                    <h5 class="card-title text-primary"><i class="fa-solid fa-pills me-2"></i>${m.nombre}</h5>
                    <p class="mb-1"><strong>Dosis:</strong> ${m.dosis || 'N/A'}</p>
                    <p class="mb-2 text-muted"><strong>Horario:</strong> ${m.horario || m.frecuencia || 'N/A'}</p>
                    <button class="btn btn-outline-danger btn-sm mt-auto" onclick="eliminarMedicamento('${m._id || m.id}')">
                        <i class="fa-solid fa-trash me-1"></i> Eliminar
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-muted py-3">No hay medicamentos registrados o error de carga.</div>`;
    }
}

async function eliminarMedicamento(id) {
    if (!confirm('¿Eliminar este medicamento?')) return;
    try {
        await apiRequest(`/medicamentos/${id}`, 'DELETE');
        cargarMedicamentos();
        cargarDashboardHome();
    } catch (err) {
        showAlert('dashAlertContainer', err.message || 'Error al eliminar medicamento');
    }
}

async function cargarEstudios() {
    const container = document.getElementById('estudiosListContainer');
    if (!container) return;
    try {
        const estudios = await apiRequest('/estudios');
        if (!estudios || estudios.length === 0) {
            container.innerHTML = `<div class="col-12 text-center py-4 text-muted"><i class="fa-solid fa-folder-open fa-2x mb-2 d-block"></i>No tienes estudios registrados.</div>`;
            return;
        }

        container.innerHTML = estudios.map(e => `
            <div class="col-md-6">
                <div class="card custom-card p-3 h-100">
                    <h5 class="card-title text-success"><i class="fa-solid fa-file-medical me-2"></i>${e.titulo}</h5>
                    <p class="mb-1 text-muted"><strong>Centro:</strong> ${e.laboratorio || 'N/A'}</p>
                    <p class="mb-2 text-muted"><strong>Fecha:</strong> ${e.fecha || 'N/A'}</p>
                    ${e.descripcion ? `<p class="small text-secondary mb-2">${e.descripcion}</p>` : ''}
                    <button class="btn btn-outline-danger btn-sm mt-auto" onclick="eliminarEstudio('${e._id || e.id}')">
                        <i class="fa-solid fa-trash me-1"></i> Eliminar
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-muted py-3">Sin estudios guardados.</div>`;
    }
}

async function eliminarEstudio(id) {
    if (!confirm('¿Eliminar este estudio médico?')) return;
    try {
        await apiRequest(`/estudios/${id}`, 'DELETE');
        cargarEstudios();
        cargarDashboardHome();
    } catch (err) {
        showAlert('dashAlertContainer', err.message || 'Error al eliminar estudio');
    }
}

async function cargarChat() {
    const box = document.getElementById('chatMessages');
    if (!box) return;
    try {
        const msgs = await apiRequest('/chat');
        if (!msgs || msgs.length === 0) {
            box.innerHTML = `<div class="text-center text-muted py-4">Inicia una conversación enviando un mensaje.</div>`;
            return;
        }

        box.innerHTML = msgs.map(m => {
            const isMe = m.remitente === 'usuario' || m.isUser || m.sender === 'user';
            return `
                <div class="chat-bubble ${isMe ? 'sent' : 'received'}">
                    <div>${m.mensaje || m.texto || ''}</div>
                    <div class="small opacity-75 text-end mt-1" style="font-size:0.75rem">${m.fecha || ''}</div>
                </div>
            `;
        }).join('');
        box.scrollTop = box.scrollHeight;
    } catch (err) {
        box.innerHTML = `<div class="text-center text-muted py-4">Inicia una conversación enviando un mensaje.</div>`;
    }
}

// LOG LOCAL DE SÍNTOMAS
function cargarSintomasLocal() {
    const list = document.getElementById('sintomasList');
    if (!list) return;
    const items = JSON.parse(localStorage.getItem('saludactiva_sintomas') || '[]');
    if (items.length === 0) {
        list.innerHTML = `<li class="list-group-item text-muted text-center py-2">Sin síntomas anotados.</li>`;
        return;
    }

    list.innerHTML = items.map(s => `
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <span>${s.texto}</span>
            <small class="text-muted" style="font-size:0.75rem">${s.fecha}</small>
        </li>
    `).join('');
}

function agregarSintomaLocal(texto) {
    const items = JSON.parse(localStorage.getItem('saludactiva_sintomas') || '[]');
    const fecha = new Date().toLocaleDateString();
    items.unshift({ texto, fecha });
    localStorage.setItem('saludactiva_sintomas', JSON.stringify(items));
    cargarSintomasLocal();
}
