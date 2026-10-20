/* App JS - Salud Activa Web Client Pro */

// URL base de la API (definida en js/config.js)
const API_BASE_URL = window.CONFIG && window.CONFIG.API_BASE_URL
    ? window.CONFIG.API_BASE_URL
    : 'https://saludactiva-backend.onrender.com/api';

// Helpers de Sesión (usa sessionStorage por defecto: más seguro, se borra al cerrar pestaña)
const storage = window.getActiveStorage();

function getToken() {
    return storage.getItem(window.CONFIG.STORAGE_KEYS.TOKEN);
}
function setToken(token, remember = false) {
    const target = remember ? localStorage : sessionStorage;
    target.setItem(window.CONFIG.STORAGE_KEYS.TOKEN, token);
    if (!remember) {
        sessionStorage.setItem(window.CONFIG.STORAGE_KEYS.REMEMBER_ME, 'false');
    }
}
function getUser() {
    const u = storage.getItem(window.CONFIG.STORAGE_KEYS.USER);
    return u ? JSON.parse(u) : null;
}
function setUser(user, remember = false) {
    const target = remember ? localStorage : sessionStorage;
    target.setItem(window.CONFIG.STORAGE_KEYS.USER, JSON.stringify(user));
}
function clearSession() {
    sessionStorage.removeItem(window.CONFIG.STORAGE_KEYS.TOKEN);
    localStorage.removeItem(window.CONFIG.STORAGE_KEYS.TOKEN);
    sessionStorage.removeItem(window.CONFIG.STORAGE_KEYS.USER);
    localStorage.removeItem(window.CONFIG.STORAGE_KEYS.USER);
    sessionStorage.removeItem(window.CONFIG.STORAGE_KEYS.REMEMBER_ME);
    localStorage.removeItem(window.CONFIG.STORAGE_KEYS.REMEMBER_ME);
    if (!window.location.pathname.endsWith('index.html') && window.location.pathname !== '/') {
        window.location.href = 'index.html';
    }
}

// Utilidades JWT (sin librerías externas)
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64).split('').map(function (c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join('')
        );
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

function isTokenExpired(token) {
    if (!token) return true;
    const payload = parseJwt(token);
    if (!payload || !payload.exp) return true;
    const now = Math.floor(Date.now() / 1000);
    // Consideramos expirado si falta menos de TOKEN_EXPIRY_WARNING segundos
    return payload.exp <= now + window.CONFIG.TOKEN_EXPIRY_WARNING;
}

// Verifica al cargar la página si el token ya expiró
function enforceTokenExpiry() {
    const token = getToken();
    if (token && isTokenExpired(token)) {
        clearSession();
        showAlert('alertContainer', window.CONFIG.MESSAGES.UNAUTHORIZED, 'warning');
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

// ============================================================================
// Validación de formularios (login y registro)
// ============================================================================

const VALIDATION_RULES = {
    email: {
        required: 'El correo electrónico es obligatorio.',
        pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
        message: 'Ingresa un correo electrónico válido (ejemplo@correo.com).'
    },
    password: {
        required: 'La contraseña es obligatoria.',
        minLength: 6,
        pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/,
        message: 'Mínimo 6 caracteres, al menos una mayúscula, un número y un símbolo (@$!%*?&).'
    },
    dni: {
        required: 'El DNI / ID es obligatorio.',
        pattern: /^\d{7,10}$/,
        message: 'Ingresa un DNI válido (7 a 10 dígitos numéricos).'
    },
    telefono: {
        optional: true,
        pattern: /^\+?[\d\s\-]{7,15}$/,
        message: 'Ingresa un teléfono válido (7 a 15 dígitos).'
    },
    nombre: {
        required: 'El nombre completo es obligatorio.',
        minLength: 3,
        message: 'Ingresa tu nombre completo (mínimo 3 caracteres).'
    }
};

function showFieldError(inputId, message) {
    const input = document.getElementById(inputId);
    if (!input) return;

    input.classList.add('is-invalid');
    input.classList.remove('is-valid');

    // Eliminar mensaje previo si existe
    const existing = input.parentNode.querySelector('.invalid-feedback');
    if (existing) existing.remove();

    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback d-block';
    feedback.textContent = message;
    input.parentNode.appendChild(feedback);
}

function clearFieldError(inputId) {
    const input = document.getElementById(inputId);
    if (!input) return;

    input.classList.remove('is-invalid');
    input.classList.add('is-valid');

    const existing = input.parentNode.querySelector('.invalid-feedback');
    if (existing) existing.remove();
}

function clearAllFieldErrors(formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.querySelectorAll('.is-invalid, .is-valid').forEach(el => {
        el.classList.remove('is-invalid', 'is-valid');
    });
    form.querySelectorAll('.invalid-feedback').forEach(el => el.remove());
}

function validateField(fieldName, value, rules) {
    if (rules.required && (!value || value.trim() === '')) {
        return rules.required;
    }

    // Si es opcional y está vacío, pasar
    if (rules.optional && (!value || value.trim() === '')) {
        return null;
    }

    if (rules.pattern && !rules.pattern.test(value.trim())) {
        return rules.message;
    }

    if (rules.minLength && value.trim().length < rules.minLength) {
        return `Mínimo ${rules.minLength} caracteres.`;
    }

    return null;
}

function validateLoginForm() {
    clearAllFieldErrors('loginForm');

    const email = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPassword').value;

    let errors = 0;

    const emailError = validateField('email', email, VALIDATION_RULES.email);
    if (emailError) {
        showFieldError('loginEmail', emailError);
        errors++;
    } else {
        clearFieldError('loginEmail');
    }

    const passError = validateField('password', password, VALIDATION_RULES.password);
    if (passError) {
        showFieldError('loginPassword', passError);
        errors++;
    } else {
        clearFieldError('loginPassword');
    }

    return errors === 0;
}

function validateRegisterForm() {
    clearAllFieldErrors('registerForm');

    const nombre = document.getElementById('regNombre').value.trim();
    const email = document.getElementById('regEmail').value.trim();
    const dni = document.getElementById('regDni').value.trim();
    const telefono = document.getElementById('regTelefono').value.trim();
    const password = document.getElementById('regPassword').value;

    let errors = 0;

    const nombreError = validateField('nombre', nombre, VALIDATION_RULES.nombre);
    if (nombreError) {
        showFieldError('regNombre', nombreError);
        errors++;
    } else {
        clearFieldError('regNombre');
    }

    const emailError = validateField('email', email, VALIDATION_RULES.email);
    if (emailError) {
        showFieldError('regEmail', emailError);
        errors++;
    } else {
        clearFieldError('regEmail');
    }

    const dniError = validateField('dni', dni, VALIDATION_RULES.dni);
    if (dniError) {
        showFieldError('regDni', dniError);
        errors++;
    } else {
        clearFieldError('regDni');
    }

    const telError = validateField('telefono', telefono, VALIDATION_RULES.telefono);
    if (telError) {
        showFieldError('regTelefono', telError);
        errors++;
    } else {
        clearFieldError('regTelefono');
    }

    const passError = validateField('password', password, VALIDATION_RULES.password);
    if (passError) {
        showFieldError('regPassword', passError);
        errors++;
    } else {
        clearFieldError('regPassword');
    }

    return errors === 0;
}

// Fetch Interceptor con timeout y manejo de errores de red
async function apiRequest(endpoint, method = 'GET', body = null) {
    // Si el token ya expiró y no es un endpoint de auth, bloquear la petición
    const isAuthEndpoint = endpoint.includes('/auth/login') || endpoint.includes('/auth/register');
    if (!isAuthEndpoint) {
        const token = getToken();
        if (token && isTokenExpired(token)) {
            clearSession();
            throw new Error(window.CONFIG.MESSAGES.UNAUTHORIZED);
        }
    }

    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const options = { method, headers };
    if (body) options.body = JSON.stringify(body);

    // Controlador de abort para timeout
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), window.CONFIG.REQUEST_TIMEOUT);
    options.signal = controller.signal;

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        clearTimeout(timeoutId);

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
        clearTimeout(timeoutId);

        // Clasificar tipo de error
        if (err.name === 'AbortError') {
            throw new Error(window.CONFIG.MESSAGES.TIMEOUT_ERROR);
        }
        if (err.type === 'failed' || err.message === 'Failed to fetch') {
            throw new Error(window.CONFIG.MESSAGES.NETWORK_ERROR);
        }

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

    // Si el token existe y no está expirado, ir directo al dashboard
    if (getToken() && !isTokenExpired(getToken())) {
        window.location.href = 'dashboard.html';
        return;
    }

    // Si el token expiró, limpiarlo para permitir nuevo login
    if (getToken() && isTokenExpired(getToken())) {
        clearSession();
    }

    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (!validateLoginForm()) {
                showAlert('alertContainer', 'Por favor corrige los errores en el formulario antes de continuar.', 'warning');
                return;
            }

            const email = document.getElementById('loginEmail').value.trim();
            const password = document.getElementById('loginPassword').value;

            try {
                const res = await apiRequest('/auth/login', 'POST', {
                    email,
                    contrasena: password
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

            if (!validateRegisterForm()) {
                showAlert('alertContainer', 'Por favor corrige los errores en el formulario antes de continuar.', 'warning');
                return;
            }

            const nombre = document.getElementById('regNombre').value.trim();
            const email = document.getElementById('regEmail').value.trim();
            const dni = document.getElementById('regDni').value.trim();
            const telefono = document.getElementById('regTelefono').value.trim();
            const password = document.getElementById('regPassword').value;

            try {
                await apiRequest('/auth/register', 'POST', {
                    nombre, email, dni, telefono, contrasena: password
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
        if (!getToken() || isTokenExpired(getToken())) { window.location.href = 'index.html'; return; }

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
                const archivo = document.getElementById('estudioDocumento').files[0];

                // Validar que título y fecha estén presentes
                if (!titulo || !fecha) {
                    showAlert('dashAlertContainer', 'Título y fecha son obligatorios', 'warning');
                    return;
                }

                const formData = new FormData();
                formData.append('titulo', titulo);
                formData.append('laboratorio', laboratorio);
                formData.append('fecha', fecha);
                formData.append('descripcion', descripcion);
                if (archivo) formData.append('documento', archivo);

                // Deshabilitar botón mientras se sube
                const btnSubmit = formNuevoEstudio.querySelector('button[type="submit"]');
                const btnOriginal = btnSubmit ? btnSubmit.innerHTML : '';
                if (btnSubmit) {
                    btnSubmit.disabled = true;
                    btnSubmit.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-1"></i> Subiendo...';
                }

                try {
                    const res = await fetch(`${API_BASE_URL}/estudios`, {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${getToken()}`
                        },
                        body: formData
                    });

                    const data = await res.json().catch(() => ({}));
                    if (!res.ok) {
                        throw new Error(data.mensaje || 'Error al guardar estudio');
                    }

                    showAlert('dashAlertContainer', 'Estudio subido exitosamente', 'success');
                    formNuevoEstudio.reset();
                    const modalEl = document.getElementById('modalNuevoEstudio');
                    const modal = bootstrap.Modal.getInstance(modalEl);
                    if (modal) modal.hide();
                    cargarEstudios();
                    cargarDashboardHome();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al guardar estudio');
                } finally {
                    if (btnSubmit) {
                        btnSubmit.disabled = false;
                        btnSubmit.innerHTML = btnOriginal;
                    }
                }
            });
        }

        const formChat = document.getElementById('formChat');
        if (formChat) {
            const inputMsg = document.getElementById('inputChatMsg');
            const btnSend = formChat.querySelector('button[type="submit"]');
            const box = document.getElementById('chatMessages');

            formChat.addEventListener('submit', async (e) => {
                e.preventDefault();
                const mensaje = inputMsg.value.trim();
                if (!mensaje) return;

                // Deshabilitar input y botón mientras se envía
                inputMsg.disabled = true;
                if (btnSend) btnSend.disabled = true;

                // Agregar mensaje del usuario inmediatamente (optimistic UI)
                const now = new Date();
                const timeStr = now.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
                const userBubble = `
                    <div class="chat-message sent mb-3 d-flex justify-content-end" id="temp-msg-${Date.now()}">
                        <div class="chat-bubble sent">
                            <div class="message-text">${mensaje}</div>
                            <div class="message-time">${timeStr}</div>
                        </div>
                    </div>
                `;
                box.insertAdjacentHTML('beforeend', userBubble);
                box.scrollTop = box.scrollHeight;
                inputMsg.value = '';

                try {
                    await apiRequest('/chat', 'POST', { mensaje });

                    // Pequeña pausa para simular que el asistente "está escribiendo"
                    await new Promise(resolve => setTimeout(resolve, 800));

                    // Recargar el chat para traer la respuesta del servidor
                    cargarChat();
                } catch (err) {
                    showAlert('dashAlertContainer', err.message || 'Error al enviar mensaje');
                } finally {
                    inputMsg.disabled = false;
                    if (btnSend) btnSend.disabled = false;
                    inputMsg.focus();
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

// ============================================================================
// Exportar turnos a PDF (jsPDF)
// ============================================================================
async function exportarTurnosPDF() {
    const container = document.getElementById('turnosListContainer');
    if (!container) return;

    // Verificar si hay turnos cargados en el DOM
    const turnoCards = container.querySelectorAll('.custom-card');
    if (!turnoCards || turnoCards.length === 0) {
        showAlert('dashAlertContainer', 'No hay turnos para exportar.', 'warning');
        return;
    }

    // Mostrar estado de carga
    const btn = document.querySelector('button[onclick="exportarTurnosPDF()"]');
    const originalBtnText = btn ? btn.innerHTML : '';
    if (btn) {
        btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-1"></i> Generando...';
        btn.disabled = true;
    }

    try {
        // Obtener los datos completos desde la API
        const turnos = await apiRequest('/turnos');

        if (!turnos || turnos.length === 0) {
            showAlert('dashAlertContainer', 'No hay turnos registrados para exportar.', 'info');
            return;
        }

        const { jsPDF } = window.jspdf;

        const doc = new jsPDF({
            orientation: 'portrait',
            unit: 'mm',
            format: 'a4'
        });

        const fechaActual = new Date().toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
        const usuario = getUser();
        const nombreUsuario = usuario ? (usuario.nombre || usuario.email) : 'Paciente';

        // Encabezado
        doc.setFillColor(37, 99, 235);
        doc.rect(0, 0, 210, 30, 'F');

        doc.setTextColor(255, 255, 255);
        doc.setFontSize(18);
        doc.setFont('helvetica', 'bold');
        doc.text('Salud Activa', 20, 15);
        doc.setFontSize(11);
        doc.setFont('helvetica', 'normal');
        doc.text('Reporte de Turnos Médicos', 20, 23);
        doc.text(`Fecha de generación: ${fechaActual}`, 20, 29);

        // Información del paciente
        doc.setTextColor(37, 99, 235);
        doc.setFontSize(12);
        doc.setFont('helvetica', 'bold');
        doc.text('Paciente:', 20, 45);
        doc.setTextColor(0, 0, 0);
        doc.setFont('helvetica', 'normal');
        doc.text(nombreUsuario, 45, 45);

        // Tabla de turnos
        doc.setFontSize(10);
        const headers = ['Especialidad', 'Médico', 'Fecha', 'Hora', 'Estado', 'Motivo'];
        const data = turnos.map(t => [
            t.especialidad || 'Consulta',
            t.medico || 'No asignado',
            t.fecha || '-',
            t.hora || '-',
            t.estado || 'Pendiente',
            t.motivo || '-'
        ]);

        // Calcular altura de tabla para evitar overflow
        const rowHeight = 8;
        const headerHeight = 8;
        const totalHeight = headerHeight + (turnos.length * rowHeight) + 10;

        if (totalHeight > 250) {
            // Si hay muchos turnos, agregar páginas automáticamente
            doc.autoTable({
                head: [headers],
                body: data,
                startY: 55,
                theme: 'grid',
                headStyles: { fillColor: [37, 99, 235], textColor: [255, 255, 255], fontSize: 9 },
                bodyStyles: { fontSize: 8, textColor: [0, 0, 0] },
                columnStyles: {
                    0: { cellWidth: 35 },
                    1: { cellWidth: 40 },
                    2: { cellWidth: 25 },
                    3: { cellWidth: 20 },
                    4: { cellWidth: 25 },
                    5: { cellWidth: 45 }
                },
                margin: { left: 20, right: 20 },
                didDrawPage: function (data) {
                    doc.setFontSize(8);
                    doc.setTextColor(150, 150, 150);
                    doc.text(`Página ${data.pageNumber}`, 20, 285);
                }
            });
        } else {
            doc.autoTable({
                head: [headers],
                body: data,
                startY: 55,
                theme: 'grid',
                headStyles: { fillColor: [37, 99, 235], textColor: [255, 255, 255], fontSize: 9 },
                bodyStyles: { fontSize: 8, textColor: [0, 0, 0] },
                columnStyles: {
                    0: { cellWidth: 35 },
                    1: { cellWidth: 40 },
                    2: { cellWidth: 25 },
                    3: { cellWidth: 20 },
                    4: { cellWidth: 25 },
                    5: { cellWidth: 45 }
                },
                margin: { left: 20, right: 20 }
            });
        }

        // Pie de página
        const finalY = doc.lastAutoTable.finalY + 10;
        doc.setFontSize(8);
        doc.setTextColor(150, 150, 150);
        doc.text('Generado por Salud Activa - Portal Web de Pacientes', 20, finalY);
        doc.text('Este documento es un resumen de citas médicas agendadas.', 20, finalY + 5);

        // Descargar archivo
        const nombreArchivo = `turnos-saludactiva-${fechaActual.replace(/ /g, '-')}.pdf`;
        doc.save(nombreArchivo);

        showAlert('dashAlertContainer', `PDF descargado: ${nombreArchivo}`, 'success');
    } catch (err) {
        showAlert('dashAlertContainer', err.message || 'Error al exportar PDF', 'danger');
    } finally {
        if (btn) {
            btn.innerHTML = originalBtnText;
            btn.disabled = false;
        }
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
                    ${e.urlDocumento ? `
                        <a href="${API_BASE_URL}${e.urlDocumento}" target="_blank" class="btn btn-outline-info btn-sm rounded-pill mb-2">
                            <i class="fa-solid fa-download me-1"></i> Ver Documento
                        </a>
                    ` : ''}
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

    // Estado de carga
    box.innerHTML = `
        <div class="text-center py-4">
            <div class="spinner-border text-primary" role="status"></div>
            <p class="small text-secondary mt-2">Cargando conversación...</p>
        </div>
    `;

    try {
        const msgs = await apiRequest('/chat');

        if (!msgs || msgs.length === 0) {
            box.innerHTML = `
                <div class="text-center text-secondary py-5">
                    <i class="fa-solid fa-comments fa-3x mb-3 opacity-50 d-block"></i>
                    <p class="mb-0">Inicia tu consulta enviando un mensaje al asistente.</p>
                </div>
            `;
            return;
        }

        // Formatear fecha para mostrar
        const formatTime = (isoString) => {
            if (!isoString) return '';
            try {
                const date = new Date(isoString);
                return date.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
            } catch (e) {
                return isoString;
            }
        };

        box.innerHTML = msgs.map(m => {
            const isMe = m.remitente === 'usuario' || m.isUser || m.sender === 'user';
            const time = formatTime(m.fecha || m.timestamp || m.createdAt);
            return `
                <div class="chat-message ${isMe ? 'sent' : 'received'} mb-3 d-flex ${isMe ? 'justify-content-end' : 'justify-content-start'}">
                    <div class="chat-bubble ${isMe ? 'sent' : 'received'}">
                        <div class="message-text">${m.mensaje || m.texto || ''}</div>
                        <div class="message-time">${time}</div>
                    </div>
                </div>
            `;
        }).join('');

        // Scroll automático al final
        box.scrollTop = box.scrollHeight;
    } catch (err) {
        box.innerHTML = `
            <div class="text-center text-danger py-4">
                <i class="fa-solid fa-triangle-exclamation fa-2x mb-2"></i>
                <p class="mb-0 small">No se pudo cargar la conversación. Intenta de nuevo.</p>
                <button class="btn btn-sm btn-outline-danger mt-2" onclick="cargarChat()">
                    <i class="fa-solid fa-rotate-right me-1"></i> Reintentar
                </button>
            </div>
        `;
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
