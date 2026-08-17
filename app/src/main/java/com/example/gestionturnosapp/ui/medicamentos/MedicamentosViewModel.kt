package com.example.gestionturnosapp.ui.medicamentos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.remote.dto.NuevoMedicamentoRequest
import com.example.gestionturnosapp.data.repository.MedicamentoRepository
import com.example.gestionturnosapp.data.local.OfflineCacheManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicamentosViewModel @Inject constructor(
    application: Application,
    private val repository: MedicamentoRepository,
    private val offlineCacheManager: OfflineCacheManager,
    private val reminderManager: com.example.gestionturnosapp.notifications.ReminderManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MedicamentosUiState())
    val uiState: StateFlow<MedicamentosUiState> = _uiState.asStateFlow()

    init {
        loadMedicamentos()
    }

    fun loadMedicamentos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // 1. Mostrar lo que tenemos localmente ya
                val cached = repository.getMedicamentos()
                _uiState.update { it.copy(medicamentos = cached) }

                // 2. El Repo intenta Server -> Cache
                val serverList = repository.getMedicamentos()
                
                // 3. Volver a mostrar incluyendo pendientes
                val pending = repository.getPendingMeds()
                val combined = (serverList + pending).distinctBy { it.nombre.lowercase().trim() }
                
                _uiState.update { it.copy(medicamentos = combined, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun agregarMedicamento(nombre: String, dosis: String, frecuencia: String, proximaToma: String, stock: Int = 30, minStock: Int = 5) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                val request = NuevoMedicamentoRequest(nombre, dosis, frecuencia, proximaToma, stockActual = stock, stockMinimo = minStock)
                val result = repository.agregarMedicamento(request)
                if (result != null) {
                    reminderManager.scheduleMedicationReminder(result)
                    _uiState.update { it.copy(
                        isLoading = false, 
                        isOperationSuccessful = true,
                        newlyAddedMedication = result 
                    ) }
                    loadMedicamentos()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun syncPendingMeds() {
        viewModelScope.launch {
            val pending = repository.getPendingMeds()
            if (pending.isEmpty()) return@launch

            val synced = mutableListOf<Medicamento>()
            pending.forEach { med ->
                try {
                    val request = NuevoMedicamentoRequest(med.nombre, med.dosis, med.frecuencia, med.proximaToma, med.stockActual, med.stockMinimo, med.notas)
                    repository.agregarMedicamento(request)
                    synced.add(med)
                } catch (_: Exception) {}
            }
            if (synced.isNotEmpty()) {
                repository.removePendingMeds(synced)
                loadMedicamentos()
            }
        }
    }

    fun resetOperationState() {
        _uiState.update { it.copy(isOperationSuccessful = false, newlyAddedMedication = null) }
    }

    fun eliminarMedicamento(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                reminderManager.cancelMedicationReminder(id)
                repository.eliminarMedicamento(id)
                
                val newList = _uiState.value.medicamentos.filter { it.id != id }
                _uiState.update { it.copy(medicamentos = newList, isLoading = false) }
                
                offlineCacheManager.saveMedicamentos(newList)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun marcarComoTomado(med: Medicamento) {
        viewModelScope.launch {
            try {
                val hours = med.frecuencia.filter { it.isDigit() }.toIntOrNull() ?: 8
                val calendar = java.util.Calendar.getInstance()
                com.example.gestionturnosapp.util.DateUtils.parseTime(med.proximaToma)?.let {
                    val pCal = java.util.Calendar.getInstance().apply { time = it }
                    calendar.set(java.util.Calendar.HOUR_OF_DAY, pCal.get(java.util.Calendar.HOUR_OF_DAY))
                    calendar.set(java.util.Calendar.MINUTE, pCal.get(java.util.Calendar.MINUTE))
                }
                calendar.add(java.util.Calendar.HOUR_OF_DAY, hours)
                val nuevaProxima = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(calendar.time)
                val updated = med.copy(proximaToma = nuevaProxima, stockActual = (med.stockActual - 1).coerceAtLeast(0))
                
                repository.updateMedicamento(med.id, updated)
                repository.addMedicationLog(com.example.gestionturnosapp.data.model.MedicationLog(medId = med.id, medName = med.nombre, dose = med.dosis))
                reminderManager.scheduleMedicationReminder(updated)
                
                loadMedicamentos()
            } catch (_: Exception) { }
        }
    }
}
