/* App JS - Salud Activa Web Client */

// Configuración de API Base URL (Render Producción)
const API_BASE_URL = 'https://saludactiva-backend.onrender.com/api';

// Helper Token JWT
function getToken() {
    return localStorage.getItem('saludactiva_token');
}

function setToken(token) {
    localStorage.setItem('saludactiva_token', token);
}

function clearSession() {
    localStorage.removeItem('saludactiva_token');
    localStorage.removeItem('saludactiva_user');
    window.location.href = 'index.html';
}

function getUser() {
    const u = localStorage.getItem('saludactiva_user');
    return u ? JSON.parse(u) : null;
}

function setUser(user) {
    localStorage.setItem('saludactiva_user', JSON.stringify(user));
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

// Interceptor Fetch
async function apiRequest(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const options = { method, headers };
    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        if (response.status === 401) {
            clearSession();
            return null;
        }
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(data.error || data.message || `Error ${response.status}`);
        }
        return data;
    } catch (err) {
        console.error('API Error:', err);
        throw err;
    }
}

// --- LÓGICA DE LA PÁGINA LOGIN/REGISTRO (index.html) ---
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    if (loginForm) {
        // Redirigir a dashboard si ya hay token
        if (getToken()) {
            window.location.href = 'dashboard.html';
            return;
        }

        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('loginEmail').value.trim();
            const password = document.getElementById('loginPassword').value;

            try {
                const res = await apiRequest('/auth/login', 'POST', { email, password });
                if (res && res.token) {
                    setToken(res.token);
                    setUser(res.usuario || res.user || { email });
                    showAlert('alertContainer', '¡Inicio de sesión exitoso! Redirigiendo...', 'success');
                    setTimeout(() => { window.location.href = 'dashboard.html'; }, 800);
                } else {
                    showAlert('alertContainer', 'Respuesta de servidor no válida');
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
                const res = await apiRequest('/auth/register', 'POST', { nombre, email, dni, telefono, password });
                showAlert('alertContainer', 'Registro exitoso. Ahora puedes iniciar sesión.', 'success');
                registerForm.reset();
                const loginTab = document.getElementById('login-tab');
                if (loginTab) loginTab.click();
            } catch (err) {
                showAlert('alertContainer', err.message || 'Error en el registro');
            }
        });
    }

    // --- LÓGICA DEL DASHBOARD (dashboard.html) ---
    const btnLogout = document.getElementById('btnLogout');
    if (btnLogout) {
        // Verificar autenticación
        if (!getToken()) {
            window.location.href = 'index.html';
            return;
        }

        const user = getUser();
        if (user) {
            const userNameDisplay = document.getElementById('userNameDisplay');
            if (userNameDisplay) userNameDisplay.textContent = user.nombre || user.email || 'Paciente';
        }

        btnLogout.addEventListener('click', () => {
            clearSession();
        });

        // Cargar módulos iniciales
        cargarTurnos();
        cargarEspecialidades();
        cargarMedicamentos();
        cargarEstudios();
        cargarChat();

        // Handlers de Formularios Dashboard
        const formSolicitarTurno = document.getElementById('formSolicitarTurno');
        if (formSolicitarTurno) {
            formSolicitarTurno.addEventListener('submit', async (e) => {
                e.preventDefault();
                const especialidad = document.getElementById('selectEspecialidad').value;
                const medico = document.getElementById('inputMedico').value;
                const fecha = document.getElementById('inputFecha').value;
                const hora = document.getElementById('inputHora').value;
                const motivo = document.getElementById('inputMotivo').value;

                try {
                    await apiRequest('/turnos', 'POST', { especialidad, medico, fecha, hora, motivo });
                    showAlert('dashAlertContainer', 'Turno reservado con éxito', 'success');
                    formSolicitarTurno.reset();
                    document.getElementById('tab-turnos').click();
                    cargarTurnos();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al reservar turno');
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
                    // Cerrar modal Bootstrap
                    const modalEl = document.getElementById('modalNuevoMedicamento');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    if (modal) modal.hide();
                    cargarMedicamentos();
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
                    showAlert('dashAlertContainer', 'Estudio guardado exitosamente', 'success');
                    formNuevoEstudio.reset();
                    const modalEl = document.getElementById('modalNuevoEstudio');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    if (modal) modal.hide();
                    cargarEstudios();
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
    }
});

// --- FUNCIONES DE CARGA Y RENDER ---
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
                        <i class="fa-solid fa-trash me-1"></i> Cancelar
                    </button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="col-12 text-center text-danger py-3">Error al cargar turnos: ${err.message}</div>`;
    }
}

async function cancelarTurno(id) {
    if (!confirm('¿Estás seguro de que deseas cancelar este turno?')) return;
    try {
        await apiRequest(`/turnos/${id}`, 'DELETE');
        showAlert('dashAlertContainer', 'Turno cancelado correctamente', 'info');
        cargarTurnos();
    } catch (err) {
        showAlert('dashAlertContainer', err.message || 'Error al cancelar turno');
    }
}

async function cargarEspecialidades() {
    const select = document.getElementById('selectEspecialidad');
    if (!select) return;
    // Lista por defecto de especialidades médicas
    const especialidades = ['Clínica Médica', 'Cardiología', 'Dermatología', 'Pediatría', 'Traumatología', 'Ginecología', 'Oftalmología', 'Neurología'];
    select.innerHTML = especialidades.map(e => `<option value="${e}">${e}</option>`).join('');
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
    if (!confirm('¿Deseas eliminar este medicamento?')) return;
    try {
        await apiRequest(`/medicamentos/${id}`, 'DELETE');
        cargarMedicamentos();
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
            box.innerHTML = `<div class="text-center text-muted py-4">Inicia una conversación con el médico o asistente.</div>`;
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
