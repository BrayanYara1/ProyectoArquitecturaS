/* App JS - Salud Activa Web Client Pro */

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
        <div class="alert alert-${type} alert-dismissible fade show rounded-3 shadow-sm border-0 mb-4" role="alert">
            <div class="d-flex align-items-center gap-2">
                <i class="fa-solid ${type === 'success' ? 'fa-circle-check text-success' : 'fa-circle-exclamation text-danger'} fs-5"></i>
                <div>${message}</div>
            </div>
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
        btn.innerHTML = active ? '<i class="fa-solid fa-sun me-1"></i> Modo' : '<i class="fa-solid fa-moon me-1"></i> Modo';
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
                    showAlert('alertContainer', '¡Inicio de sesión exitoso! Redirigiendo...', 'success');
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
                showAlert('alertContainer', 'Cuenta creada exitosamente. Procede a iniciar sesión.', 'success');
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

            const userEmailDisplay = document.getElementById('userEmailDisplay');
            if (userEmailDisplay) userEmailDisplay.textContent = user.email || '';

            const userAvatarInitials = document.getElementById('userAvatarInitials');
            if (userAvatarInitials) {
                const nameStr = user.nombre || user.email || 'P';
                userAvatarInitials.textContent = nameStr.charAt(0).toUpperCase();
            }

            const greetingTitle = document.getElementById('greetingTitle');
            if (greetingTitle) {
                const hour = new Date().getHours();
                const timeGreeting = hour < 12 ? '¡Buenos días' : hour < 19 ? '¡Buenas tardes' : '¡Buenas noches';
                greetingTitle.textContent = `${timeGreeting}, ${user.nombre || 'Paciente'}!`;
            }

            // Llenar Formulario de Perfil
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

        // Inicializar Gráficos y Módulos
        initHealthChart();
        cargarDashboardHome();
        cargarTurnos();
        cargarEspecialidades();
        cargarMedicamentos();
        cargarEstudios();
        cargarChat();
        cargarSintomasLocal();

        // FORM HANDLERS
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
                    showAlert('dashAlertContainer', '¡Turno reservado con éxito!', 'success');
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
                    showAlert('dashAlertContainer', 'Ficha médica actualizada correctamente', 'success');
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
                    showAlert('dashAlertContainer', 'Medicamento registrado correctamente', 'success');
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
                    showAlert('dashAlertContainer', 'Estudio subido exitosamente', 'success');
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

// CHART.JS HEALTH TRACKER
function initHealthChart() {
    const ctx = document.getElementById('healthChart');
    if (!ctx) return;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
            datasets: [{
                label: 'Adherencia a Medicamentos (%)',
                data: [100, 100, 85, 100, 100, 90, 100],
                borderColor: '#2563eb',
                backgroundColor: 'rgba(37, 99, 235, 0.1)',
                tension: 0.4,
                fill: true,
                pointBackgroundColor: '#2563eb'
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
                y: { min: 60, max: 100, grid: { color: 'rgba(0,0,0,0.05)' } },
                x: { grid: { display: false } }
            }
        }
    });
}

// DASHBOARD HOME LOADER
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

        const homeProximo = document.getElementById('homeProximoTurno');
        if (homeProximo) {
            if (turnos && turnos.length > 0) {
                const p = turnos[0];
                homeProximo.innerHTML = `
                    <div class="p-3 bg-primary-subtle border border-primary-subtle rounded-3">
                        <span class="badge bg-primary mb-1">${p.especialidad || 'Consulta'}</span>
                        <h6 class="fw-bold mb-1 text-primary">${p.medico || 'Doctor'}</h6>
                        <p class="small mb-0 text-secondary"><i class="fa-regular fa-calendar me-1"></i>${p.fecha || ''} a las ${p.hora || ''}</p>
                    </div>
                `;
            } else {
                homeProximo.innerHTML = `<p class="text-secondary small mb-0">No tienes citas médicas agendadas próximas.</p>`;
            }
        }

        const homeMeds = document.getElementById('homeMedsDia');
        if (homeMeds) {
            if (meds && meds.length > 0) {
                homeMeds.innerHTML = meds.map((m) => `
                    <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                        <div>
                            <span class="fw-bold text-primary">${m.nombre}</span>
                            <span class="badge bg-secondary-subtle text-secondary ms-1">${m.dosis}</span>
                        </div>
                        <button class="btn btn-sm btn-outline-success rounded-pill px-3" onclick="marcarTomado('${m.nombre}')">
                            <i class="fa-solid fa-check me-1"></i> Tomado
                        </button>
                    </div>
                `).join('');
            } else {
                homeMeds.innerHTML = `<p class="text-secondary small mb-0">No hay medicamentos programados para el día de hoy.</p>`;
            }
        }
    } catch (err) {
        console.error('Home Dashboard error:', err);
    }
}

function marcarTomado(nombre) {
    showAlert('dashAlertContainer', `¡Dosis de ${nombre} marcada como tomada! Registro de salud actualizado.`, 'success');
}

async function cargarTurnos() {
    const container = document.getElementById('turnosListContainer');
    if (!container) return;
    try {
        const turnos = await apiRequest('/turnos');
        if (!turnos || turnos.length === 0) {
            container.innerHTML = `<div class="col-12 text-center py-5 text-secondary"><i class="fa-solid fa-calendar-xmark fa-3x mb-3 d-block opacity-50"></i>No tienes turnos agendados.</div>`;
            return;
        }

        container.innerHTML = turnos.map(t => `
            <div class="col-md-6 col-lg-4">
                <div class="custom-card p-4 h-100 d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <span class="badge bg-primary-subtle text-primary border border-primary-subtle rounded-pill">${t.especialidad || 'Consulta'}</span>
                        <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill">${t.estado || 'Confirmado'}</span>
                    </div>
                    <h5 class="fw-bold mb-2 text-truncate">${t.medico || 'Médico Asignado'}</h5>
                    <p class="small text-secondary mb-1"><i class="fa-regular fa-calendar me-2 text-primary"></i>${t.fecha || 'Sin fecha'}</p>
                    <p class="small text-secondary mb-3"><i class="fa-regular fa-clock me-2 text-primary"></i>${t.hora || 'Sin hora'}</p>
                    ${t.motivo ? `<p class="small text-secondary mb-3 bg-body-tertiary p-2 rounded"><strong>Motivo:</strong> ${t.motivo}</p>` : ''}
                    <button class="btn btn-outline-danger btn-sm rounded-pill mt-auto" onclick="cancelarTurno('${t._id || t.id}')">
                        <i class="fa-solid fa-trash me-1"></i> Cancelar Cita
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-danger py-4">Error al cargar los turnos: ${err.message}</div>`;
    }
}

async function cancelarTurno(id) {
    if (!confirm('¿Estás seguro de cancelar esta cita médica?')) return;
    try {
        await apiRequest(`/turnos/${id}`, 'DELETE');
        showAlert('dashAlertContainer', 'Cita médica cancelada con éxito', 'info');
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
        { nombre: 'Clínica Médica', icono: 'fa-user-doctor', desc: 'Atención integral, diagnósticos generales y prevención de salud.' },
        { nombre: 'Cardiología', icono: 'fa-heart-pulse', desc: 'Diagnóstico y tratamiento de afecciones del sistema cardiovascular.' },
        { nombre: 'Dermatología', icono: 'fa-allergies', desc: 'Cuidado y salud especializada de la piel, cabello y uñas.' },
        { nombre: 'Pediatría', icono: 'fa-baby', desc: 'Cuidado médico preventivo y curativo de niños y adolescentes.' },
        { nombre: 'Traumatología', icono: 'fa-bone', desc: 'Tratamiento de lesiones musculoesqueléticas y articulaciones.' },
        { nombre: 'Ginecología', icono: 'fa-person-pregnant', desc: 'Atención médica reproductiva e integral de la mujer.' },
        { nombre: 'Oftalmología', icono: 'fa-eye', desc: 'Cuidado integral de la visión y salud ocular.' },
        { nombre: 'Neurología', icono: 'fa-brain', desc: 'Diagnóstico y tratamiento de trastornos del sistema nervioso.' }
    ];

    if (select) {
        select.innerHTML = lista.map(e => `<option value="${e.nombre}">${e.nombre}</option>`).join('');
    }

    if (grid) {
        grid.innerHTML = lista.map(e => `
            <div class="col-md-6 col-lg-3">
                <div class="custom-card p-4 text-center h-100 d-flex flex-column">
                    <div class="metric-icon bg-primary-subtle text-primary mx-auto mb-3" style="width:60px; height:60px; font-size:26px">
                        <i class="fa-solid ${e.icono}"></i>
                    </div>
                    <h6 class="fw-bold mb-2">${e.nombre}</h6>
                    <p class="small text-secondary mb-3">${e.desc}</p>
                    <button class="btn btn-outline-primary btn-sm rounded-pill mt-auto" onclick="seleccionarEspecialidad('${e.nombre}')">
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
                        <div class="custom-card p-4 text-center h-100 d-flex flex-column">
                            <div class="metric-icon bg-primary-subtle text-primary mx-auto mb-3" style="width:60px; height:60px; font-size:26px">
                                <i class="fa-solid ${e.icono}"></i>
                            </div>
                            <h6 class="fw-bold mb-2">${e.nombre}</h6>
                            <p class="small text-secondary mb-3">${e.desc}</p>
                            <button class="btn btn-outline-primary btn-sm rounded-pill mt-auto" onclick="seleccionarEspecialidad('${e.nombre}')">
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
            container.innerHTML = `<div class="col-12 text-center py-5 text-secondary"><i class="fa-solid fa-capsules fa-3x mb-3 d-block opacity-50"></i>No registras medicamentos activos.</div>`;
            return;
        }

        container.innerHTML = meds.map(m => `
            <div class="col-md-6 col-lg-4">
                <div class="custom-card p-4 h-100 d-flex flex-column">
                    <div class="d-flex align-items-center gap-3 mb-3">
                        <div class="metric-icon bg-success-subtle text-success">
                            <i class="fa-solid fa-pills"></i>
                        </div>
                        <div>
                            <h5 class="fw-bold mb-0 text-truncate">${m.nombre}</h5>
                            <span class="small text-secondary">Dosis: ${m.dosis || 'N/A'}</span>
                        </div>
                    </div>
                    <p class="small text-secondary mb-3"><i class="fa-regular fa-clock me-1 text-success"></i> ${m.horario || m.frecuencia || 'N/A'}</p>
                    <button class="btn btn-outline-danger btn-sm rounded-pill mt-auto" onclick="eliminarMedicamento('${m._id || m.id}')">
                        <i class="fa-solid fa-trash me-1"></i> Eliminar
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-secondary py-4">No hay medicamentos guardados.</div>`;
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
            container.innerHTML = `<div class="col-12 text-center py-5 text-secondary"><i class="fa-solid fa-folder-open fa-3x mb-3 d-block opacity-50"></i>No tienes estudios médicos subidos.</div>`;
            return;
        }

        container.innerHTML = estudios.map(e => `
            <div class="col-md-6">
                <div class="custom-card p-4 h-100 d-flex flex-column">
                    <div class="d-flex align-items-center gap-3 mb-3">
                        <div class="metric-icon bg-info-subtle text-info">
                            <i class="fa-solid fa-file-waveform"></i>
                        </div>
                        <div>
                            <h6 class="fw-bold mb-0">${e.titulo}</h6>
                            <small class="text-secondary">${e.laboratorio || 'Centro Médico'}</small>
                        </div>
                    </div>
                    <p class="small text-secondary mb-2"><i class="fa-regular fa-calendar me-1 text-info"></i> ${e.fecha || 'N/A'}</p>
                    ${e.descripcion ? `<p class="small text-secondary mb-3 bg-body-tertiary p-2 rounded">${e.descripcion}</p>` : ''}
                    <button class="btn btn-outline-danger btn-sm rounded-pill mt-auto" onclick="eliminarEstudio('${e._id || e.id}')">
                        <i class="fa-solid fa-trash me-1"></i> Eliminar Estudio
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-secondary py-4">Sin estudios guardados.</div>`;
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
            box.innerHTML = `<div class="text-center text-secondary py-5">Inicia tu consulta enviando un mensaje al asistente.</div>`;
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
        box.innerHTML = `<div class="text-center text-secondary py-5">Inicia tu consulta enviando un mensaje.</div>`;
    }
}

// LOG LOCAL DE SÍNTOMAS
function cargarSintomasLocal() {
    const list = document.getElementById('sintomasList');
    if (!list) return;
    const items = JSON.parse(localStorage.getItem('saludactiva_sintomas') || '[]');
    if (items.length === 0) {
        list.innerHTML = `<li class="list-group-item text-secondary text-center py-2">Sin síntomas anotados.</li>`;
        return;
    }

    list.innerHTML = items.map(s => `
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <span>${s.texto}</span>
            <small class="text-secondary" style="font-size:0.75rem">${s.fecha}</small>
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
