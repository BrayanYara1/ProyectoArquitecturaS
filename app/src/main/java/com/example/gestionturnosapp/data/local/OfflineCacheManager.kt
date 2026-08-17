package com.example.gestionturnosapp.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.gestionturnosapp.data.model.*
import com.example.gestionturnosapp.data.remote.dto.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineCacheManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
    private val gson: Gson
) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // --- TURNOS (ROOM) ---
    suspend fun saveTurnos(turnos: List<Turno>) = withContext(Dispatchers.IO) {
        database.turnoDao().clearAndInsert(turnos)
    }

    suspend fun getCachedTurnos(): List<Turno> = withContext(Dispatchers.IO) {
        database.turnoDao().getAllTurnos()
    }

    // --- MEDICAMENTOS (ROOM) ---
    suspend fun saveMedicamentos(meds: List<Medicamento>) = withContext(Dispatchers.IO) {
        database.medicamentoDao().clearAndInsert(meds)
    }

    suspend fun getCachedMedicamentos(): List<Medicamento> = withContext(Dispatchers.IO) {
        database.medicamentoDao().getAllMedicamentos()
    }

    // --- ESTUDIOS (ROOM) ---
    suspend fun saveEstudios(estudios: List<EstudioMedico>) = withContext(Dispatchers.IO) {
        database.estudioDao().clearAndInsert(estudios)
    }

    suspend fun getCachedEstudios(): List<EstudioMedico> = withContext(Dispatchers.IO) {
        database.estudioDao().getAllEstudios()
    }

    // --- MENSAJES (ROOM) ---
    suspend fun saveMensajes(mensajes: List<Mensaje>) = withContext(Dispatchers.IO) {
        val dao = database.mensajeDao()
        dao.deleteAllMensajes()
        dao.insertMensajes(mensajes)
    }

    suspend fun getCachedMensajes(): List<Mensaje> = withContext(Dispatchers.IO) {
        database.mensajeDao().getAllMensajes()
    }

    // --- PENDIENTES (SHARED PREFERENCES) ---
    fun addPendingTurno(request: NuevoTurnoRequest) {
        val pending = getPendingTurnos().toMutableList()
        pending.add(request)
        prefs.edit { putString(KEY_PENDING_TURNOS, gson.toJson(pending)) }
    }

    fun getPendingTurnos(): List<NuevoTurnoRequest> {
        val json = prefs.getString(KEY_PENDING_TURNOS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<NuevoTurnoRequest>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun removePendingTurnos(synced: List<NuevoTurnoRequest>) {
        val current = getPendingTurnos().toMutableList()
        current.removeAll(synced)
        if (current.isEmpty()) {
            prefs.edit { remove(KEY_PENDING_TURNOS) }
        } else {
            prefs.edit { putString(KEY_PENDING_TURNOS, gson.toJson(current)) }
        }
    }

    fun addPendingMed(med: Medicamento) {
        val pending = getPendingMeds().toMutableList()
        pending.add(med)
        prefs.edit { putString(KEY_PENDING_MEDS, gson.toJson(pending)) }
    }

    fun getPendingMeds(): List<Medicamento> {
        val json = prefs.getString(KEY_PENDING_MEDS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Medicamento>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun removePendingMeds(synced: List<Medicamento>) {
        val current = getPendingMeds().toMutableList()
        current.removeAll(synced)
        if (current.isEmpty()) {
            prefs.edit { remove(KEY_PENDING_MEDS) }
        } else {
            prefs.edit { putString(KEY_PENDING_MEDS, gson.toJson(current)) }
        }
    }

    fun addPendingEstudio(estudio: EstudioMedico) {
        val pending = getPendingEstudios().toMutableList()
        pending.add(estudio)
        prefs.edit { putString(KEY_PENDING_ESTUDIOS, gson.toJson(pending)) }
    }

    fun getPendingEstudios(): List<EstudioMedico> {
        val json = prefs.getString(KEY_PENDING_ESTUDIOS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<EstudioMedico>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun removePendingEstudios(synced: List<EstudioMedico>) {
        val current = getPendingEstudios().toMutableList()
        current.removeAll(synced)
        if (current.isEmpty()) {
            prefs.edit { remove(KEY_PENDING_ESTUDIOS) }
        } else {
            prefs.edit { putString(KEY_PENDING_ESTUDIOS, gson.toJson(current)) }
        }
    }

    suspend fun clearCache() = withContext(Dispatchers.IO) {
        database.turnoDao().deleteAllTurnos()
        database.medicamentoDao().deleteAllMedicamentos()
        database.estudioDao().deleteAllEstudios()
        database.mensajeDao().deleteAllMensajes()
        prefs.edit { clear() }
    }

    fun isNetworkError(e: Exception): Boolean {
        if (e is java.net.UnknownHostException || 
            e is java.net.SocketTimeoutException || 
            e is java.net.ConnectException ||
            e is java.io.IOException) {
            return true
        }
        val msg = e.localizedMessage ?: ""
        return msg.contains(other = "Unable to resolve host", ignoreCase = true) || 
               msg.contains(other = "timeout", ignoreCase = true) || 
               msg.contains(other = "Failed to connect", ignoreCase = true)
    }

    companion object {
        private const val PREF_NAME = "offline_cache"
        private const val KEY_PENDING_TURNOS = "pending_turnos"
        private const val KEY_PENDING_MEDS = "pending_meds"
        private const val KEY_PENDING_ESTUDIOS = "pending_estudios"
    }
}
