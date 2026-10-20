// ============================================================================
// Configuración de entorno — Salud Activa Web Client
// ============================================================================
const CONFIG = {
  // URL base de la API del backend (Ruta relativa enrutada por Nginx)
  API_BASE_URL: '/api',

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
  STORAGE_MODE: 'session',

  // Tiempo de anticipación para renovar aviso de expiración (segundos)
  EXPIRATION_WARNING_ADVANCE_SEC: 300
};

// Exportar globalmente
window.CONFIG = CONFIG;
