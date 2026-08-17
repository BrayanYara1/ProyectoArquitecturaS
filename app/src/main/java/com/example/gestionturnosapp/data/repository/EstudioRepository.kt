package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.data.model.EstudioMedico
import com.example.gestionturnosapp.data.remote.ApiService
import com.example.gestionturnosapp.util.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EstudioRepository @Inject constructor(
    private val apiService: ApiService,
    private val offlineCacheManager: OfflineCacheManager,
    private val networkUtils: NetworkUtils
) {

    suspend fun getEstudios(): List<EstudioMedico> {
        val response = try {
            apiService.getEstudios()
        } catch (e: Exception) {
            return offlineCacheManager.getCachedEstudios()
        }

        return if (response.isSuccessful) {
            val estudios = response.body() ?: emptyList()
            offlineCacheManager.saveEstudios(estudios)
            estudios
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            offlineCacheManager.getCachedEstudios()
        }
    }

    suspend fun agregarEstudio(estudio: EstudioMedico): EstudioMedico? {
        val response = try {
            apiService.agregarEstudio(estudio)
        } catch (e: Exception) {
            if (offlineCacheManager.isNetworkError(e)) {
                if (!estudio.id.startsWith("pending_")) {
                    offlineCacheManager.addPendingEstudio(estudio)
                }
                return estudio
            }
            throw e
        }

        if (response.isSuccessful) {
            return response.body()
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }

    suspend fun eliminarEstudio(id: String) {
        val response = apiService.eliminarEstudio(id)
        if (!response.isSuccessful) {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }
}
