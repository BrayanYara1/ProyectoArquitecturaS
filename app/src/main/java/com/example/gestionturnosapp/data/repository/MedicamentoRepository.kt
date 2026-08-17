package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.remote.ApiService
import com.example.gestionturnosapp.data.remote.dto.NuevoMedicamentoRequest
import com.example.gestionturnosapp.util.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicamentoRepository @Inject constructor(
    private val apiService: ApiService,
    private val offlineCacheManager: OfflineCacheManager,
    private val medicationLogDao: com.example.gestionturnosapp.data.local.MedicationLogDao,
    private val networkUtils: NetworkUtils
) {

    suspend fun getMedicamentos(): List<Medicamento> {
        val response = try {
            apiService.getMedicamentos()
        } catch (e: Exception) {
            return offlineCacheManager.getCachedMedicamentos()
        }

        return if (response.isSuccessful) {
            val meds = response.body() ?: emptyList()
            offlineCacheManager.saveMedicamentos(meds)
            meds
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            offlineCacheManager.getCachedMedicamentos()
        }
    }

    suspend fun agregarMedicamento(request: NuevoMedicamentoRequest): Medicamento? {
        val response = try {
            apiService.agregarMedicamento(request)
        } catch (e: Exception) {
            if (offlineCacheManager.isNetworkError(e)) {
                val tempMed = Medicamento(
                    id = "local_${System.currentTimeMillis()}",
                    nombre = request.nombre,
                    dosis = request.dosis,
                    frecuencia = request.frecuencia,
                    proximaToma = request.proximaToma,
                    stockActual = request.stockActual,
                    stockMinimo = request.stockMinimo,
                    notas = request.notas
                )
                offlineCacheManager.addPendingMed(tempMed)
                return tempMed
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

    suspend fun updateMedicamento(id: String, med: Medicamento): Medicamento? {
        val response = try {
            apiService.updateMedicamento(id, med)
        } catch (e: Exception) {
            // Si falla la red, guardamos en cache local al menos
            val currentMeds = offlineCacheManager.getCachedMedicamentos().toMutableList()
            val index = currentMeds.indexOfFirst { it.id == id }
            if (index != -1) {
                currentMeds[index] = med
                offlineCacheManager.saveMedicamentos(currentMeds)
            }
            return med
        }

        if (response.isSuccessful) {
            val updated = response.body() ?: med
            // Actualizar cache local
            val currentMeds = offlineCacheManager.getCachedMedicamentos().toMutableList()
            val index = currentMeds.indexOfFirst { it.id == id }
            if (index != -1) {
                currentMeds[index] = updated
                offlineCacheManager.saveMedicamentos(currentMeds)
            }
            return updated
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }

    suspend fun eliminarMedicamento(id: String) {
        val response = apiService.eliminarMedicamento(id)
        if (!response.isSuccessful) {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }

    fun getPendingMeds() = offlineCacheManager.getPendingMeds()
    fun removePendingMeds(synced: List<Medicamento>) = offlineCacheManager.removePendingMeds(synced)

    suspend fun addMedicationLog(log: com.example.gestionturnosapp.data.model.MedicationLog) = 
        medicationLogDao.insertLog(log)

    fun getAllMedicationLogs() = medicationLogDao.getAllLogs()
}
