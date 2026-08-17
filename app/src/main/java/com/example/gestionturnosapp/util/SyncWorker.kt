package com.example.gestionturnosapp.util

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.data.repository.MedicamentoRepository
import com.example.gestionturnosapp.data.repository.TurnoRepository
import com.example.gestionturnosapp.data.remote.dto.NuevoMedicamentoRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val medRepository: MedicamentoRepository,
    private val turnoRepository: TurnoRepository,
    private val healthRepository: com.example.gestionturnosapp.data.repository.HealthRepository,
    private val offlineCacheManager: OfflineCacheManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Sincronizar Medicamentos Pendientes
            val pendingMeds = offlineCacheManager.getPendingMeds()
            if (pendingMeds.isNotEmpty()) {
                val syncedMeds = mutableListOf<com.example.gestionturnosapp.data.model.Medicamento>()
                pendingMeds.forEach { med ->
                    val request = NuevoMedicamentoRequest(
                        med.nombre, med.dosis, med.frecuencia, med.proximaToma, 
                        med.stockActual, med.stockMinimo, med.notas
                    )
                    val res = medRepository.agregarMedicamento(request)
                    if (res != null) syncedMeds.add(med)
                }
                offlineCacheManager.removePendingMeds(syncedMeds)
            }

            // Sincronizar Turnos Pendientes
            val pendingTurnos = offlineCacheManager.getPendingTurnos()
            if (pendingTurnos.isNotEmpty()) {
                val syncedTurnos = mutableListOf<com.example.gestionturnosapp.data.remote.dto.NuevoTurnoRequest>()
                pendingTurnos.forEach { req ->
                    val res = turnoRepository.crearTurno(req)
                    if (res != null) syncedTurnos.add(req)
                }
                offlineCacheManager.removePendingTurnos(syncedTurnos)
            }

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
