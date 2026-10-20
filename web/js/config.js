// ============================================================================
// Configuración de entorno — Salud Activa Web Client
// ============================================================================
// Edita los valores según el entorno donde vayas a ejecutar la app.
// Si trabajas en localhost, la app detecta automáticamente la URL local.
const CONFIG = {
  // URL base de la API del backend
  // Desarrollo local: 'http://localhost:3000/api'
  // Producción (Render): 'https://saludactiva-backend.onrender.com/api'
  API_BASE_URL: window.location.hostname === 'localhost'
    ? 'http://localhost:3000/api'
    : 'https://saludactiva-backend.onrender.com/api',

  // Tiempo máximo de espera para peticiones HTTP (milésimas de segundo)
  REQUEST_TIMEOUT: 15000,

  // Claves usadas en almacenamiento del navegador
  STORAGE_KEYS: {
    TOKEN: 'saludactiva_token',
    USER: 'saludactiva_user',
    DARK_MODE: 'saludactiva_dark',
    SYMPTOMS: 'saludactiva_sintomas',
    REMEMBER_ME: 'saludactiva_remember_me'
  },

  // Modo de almacenamiento: 'session' (más seguro, se borra al cerrar pestaña) o 'local'
  // Por defecto usamos 'session' para reducir exposición al token
  STORAGE_MODE: 'session',

  // Tiempo de anticipación para renovar aviso de expiración (segundos)
  TOKEN_EXPIRY_WARNING: 300, // 5 minutos antes

  // Mensajes de error estándar
  MESSAGES: {
    NETWORK_ERROR: 'No se pudo conectar con el servidor. Verifica tu conexión a internet.',
    TIMEOUT_ERROR: 'El servidor tardó demasiado en responder. Intenta de nuevo.',
    UNAUTHORIZED: 'Sesión expirada. Inicia sesión de nuevo.',
    UNKNOWN: 'Ocurrió un error inesperado.'
  }
};

// Helper: obtener el objeto de almacenamiento activo según el modo
window.getActiveStorage = function () {
  return window.CONFIG.STORAGE_MODE === 'local' ? localStorage : sessionStorage;
};

// Hacer accesible a los demás scripts
window.CONFIG = CONFIG;
