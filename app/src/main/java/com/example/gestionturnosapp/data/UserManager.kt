package com.example.gestionturnosapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.gestionturnosapp.data.model.Usuario
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val _userState = MutableStateFlow<Usuario?>(null)
    val userState: StateFlow<Usuario?> = _userState.asStateFlow()

    var usuarioActual: Usuario? = null
        private set

    var token: String? = null
        get() {
            if (field == null) {
                field = prefs.getString(KEY_TOKEN, null)
            }
            return field
        }
        private set

    init {
        loadUser()
    }

    fun saveUser(usuario: Usuario, authToken: String? = null) {
        this.usuarioActual = usuario
        _userState.value = usuario
        prefs.edit {
            putString(KEY_USER, gson.toJson(usuario))
            if (authToken != null) {
                token = authToken
                putString(KEY_TOKEN, authToken)
            }
        }
    }

    fun getUser(): Usuario? {
        if (usuarioActual == null) {
            loadUser()
        }
        return usuarioActual
    }

    private fun loadUser() {
        val json = prefs.getString(KEY_USER, null)
        if (json != null) {
            try {
                usuarioActual = gson.fromJson(json, Usuario::class.java)
                _userState.value = usuarioActual
            } catch (e: Exception) {
                usuarioActual = null
                _userState.value = null
            }
        }
        token = prefs.getString(KEY_TOKEN, null)
    }

    fun saveFcmToken(fcmToken: String) {
        prefs.edit { 
            putString(KEY_FCM_TOKEN, fcmToken)
            putBoolean(KEY_FCM_SYNCED, false)
        }
    }

    fun getFcmToken(): String? = prefs.getString(KEY_FCM_TOKEN, null)

    fun markFcmAsSynced() {
        prefs.edit { putBoolean(KEY_FCM_SYNCED, true) }
    }

    fun isFcmSynced(): Boolean = prefs.getBoolean(KEY_FCM_SYNCED, false)

    fun logout() {
        usuarioActual = null
        _userState.value = null
        token = null
        prefs.edit { clear() }
    }

    companion object {
        private const val PREF_NAME = "secure_user_prefs_v11"
        private const val KEY_USER = "current_user"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_FCM_TOKEN = "fcm_token"
        private const val KEY_FCM_SYNCED = "fcm_synced"
    }
}
