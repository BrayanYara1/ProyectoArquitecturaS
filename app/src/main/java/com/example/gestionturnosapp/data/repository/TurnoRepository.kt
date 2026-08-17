package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.data.model.Turno
import com.example.gestionturnosapp.data.remote.ApiService
import com.example.gestionturnosapp.data.remote.dto.NuevoTurnoRequest
import com.example.gestionturnosapp.util.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TurnoRepository @Inject constructor(
    private val apiService: ApiService,
    private val offlineCacheManager: OfflineCacheManager,
    private val networkUtils: NetworkUtils
) {

    /**
     * Obtiene los turnos intentando primero el servidor y guardando en caché.
     * Si falla la red, devuelve los datos de la caché local.
     */
    suspend fun getTurnos(): List<Turno> {
        val response = try {
            apiService.getTurnos()
        } catch (e: Exception) {
            // Error de conexión física: Devolver cache silenciosamente
            return offlineCacheManager.getCachedTurnos()
        }

        return if (response.isSuccessful) {
            val turnos = response.body() ?: emptyList()
            offlineCacheManager.saveTurnos(turnos)
            turnos
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            // Otros errores (500, etc): Usar cache
            offlineCacheManager.getCachedTurnos()
        }
    }

    suspend fun getCachedTurnos(): List<Turno> {
        return offlineCacheManager.getCachedTurnos()
    }

    suspend fun checkAvailability(fecha: String, hora: String): Boolean {
        val response = try {
            apiService.checkAvailability(fecha, hora)
        } catch (e: Exception) {
            return true
        }

        return if (response.isSuccessful) {
            val body = response.body()
            body?.get("disponible") ?: body?.get("available") ?: true
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            true
        }
    }

    suspend fun crearTurno(request: NuevoTurnoRequest): Turno? {
        val response = try {
            apiService.crearTurno(request)
        } catch (e: Exception) {
            if (offlineCacheManager.isNetworkError(e)) {
                offlineCacheManager.addPendingTurno(request)
                return null
            }
            throw e
        }

        if (response.isSuccessful) {
            val nuevo = response.body()
            nuevo?.let {
                val current = offlineCacheManager.getCachedTurnos().toMutableList()
                current.add(it)
                offlineCacheManager.saveTurnos(current)
            }
            return nuevo
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }

    suspend fun eliminarTurno(id: String) {
        val response = try {
            apiService.eliminarTurno(id)
        } catch (e: Exception) {
            // Eliminar de cache local si falla red para UI inmediata
            val current = offlineCacheManager.getCachedTurnos().toMutableList()
            current.removeAll { it.id == id }
            offlineCacheManager.saveTurnos(current)
            return
        }

        if (response.isSuccessful) {
            val current = offlineCacheManager.getCachedTurnos().toMutableList()
            current.removeAll { it.id == id }
            offlineCacheManager.saveTurnos(current)
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }
}
