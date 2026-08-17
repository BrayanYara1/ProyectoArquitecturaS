package com.example.gestionturnosapp.ui.turnos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Turno
import com.example.gestionturnosapp.data.repository.TurnoRepository
import com.example.gestionturnosapp.data.remote.dto.NuevoTurnoRequest
import com.example.gestionturnosapp.util.Resource
import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.util.DateUtils
import com.example.gestionturnosapp.notifications.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TurnosListViewModel @Inject constructor(
    application: Application,
    private val repository: TurnoRepository,
    private val achievementManager: com.example.gestionturnosapp.util.AchievementManager,
    private val reminderManager: ReminderManager,
    private val offlineCacheManager: OfflineCacheManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TurnosUiState())
    val uiState: StateFlow<TurnosUiState> = _uiState.asStateFlow()

    private var availabilityJob: Job? = null

    // CAMPOS PARA FORMULARIO (SolicitarTurnoFragment)
    val formPacienteNombre = MutableLiveData("")
    val formFecha = MutableLiveData("")
    val formHora = MutableLiveData("")
    val formMotivo = MutableLiveData("")
    
    private val _isSlotAvailable = MutableLiveData<Boolean?>(null)
    val isSlotAvailable: LiveData<Boolean?> = _isSlotAvailable

    private val _createTurnoResource = MutableLiveData<Resource<Turno>>(Resource.Idle)
    val createTurnoResource: LiveData<Resource<Turno>> = _createTurnoResource

    val isFormValid = MediatorLiveData<Boolean>().apply {
        val observer = { _: Any? ->
            val nombre = formPacienteNombre.value ?: ""
            val fecha = formFecha.value ?: ""
            val hora = formHora.value ?: ""
            val motivo = formMotivo.value ?: ""
            val disponible = _isSlotAvailable.value
            
            value = (nombre.isNotBlank() && 
                    fecha.isNotBlank() && 
                    hora.isNotBlank() && 
                    motivo.isNotBlank() &&
                    disponible != false)
        }
        addSource(formPacienteNombre, observer)
        addSource(formFecha, observer)
        addSource(formHora, observer)
        addSource(formMotivo, observer)
        addSource(_isSlotAvailable, observer)
    }

    init {
        fetchTurnos()
    }

    fun fetchTurnos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val serverList = repository.getTurnos()
                val pending = offlineCacheManager.getPendingTurnos().map { req ->
                    val uniqueId = "pending_${req.fecha}_${req.hora}_${System.currentTimeMillis()}"
                    Turno(uniqueId, req.nombre, req.fecha, req.hora, req.motivo, getApplication<Application>().getString(R.string.status_pending_offline), req.especialidad, req.doctor)
                }
                val combined = (serverList + pending).distinctBy { it.id }
                _uiState.update { it.copy(allTurnos = combined, isLoading = false) }
                applyFilters()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun setFilterStatus(status: String) {
        _uiState.update { it.copy(filterStatus = status) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val query = state.searchQuery.lowercase().trim()
        val status = state.filterStatus

        val filtered = state.allTurnos.filter { turno ->
            val matchesQuery = query.isEmpty() || 
                              turno.pacienteNombre.lowercase().contains(query) || 
                              turno.motivo.lowercase().contains(query) ||
                              (turno.especialidad?.lowercase()?.contains(query) ?: false)
            
            val matchesStatus = when (status) {
                "PENDIENTE" -> isStatusPending(turno.estado)
                "COMPLETADO" -> isStatusCompleted(turno.estado)
                "CANCELADO" -> isStatusCancelled(turno.estado)
                else -> true
            }
            matchesQuery && matchesStatus
        }
        _uiState.update { it.copy(filteredTurnos = filtered) }
    }

    private fun isStatusPending(estado: String): Boolean {
        val e = estado.lowercase()
        return e.contains("pend") || e.contains("espera") || e.contains("waiting")
    }

    private fun isStatusCompleted(estado: String): Boolean {
        val e = estado.lowercase()
        return e.contains("compl") || e.contains("final") || e.contains("done")
    }

    private fun isStatusCancelled(estado: String): Boolean {
        val e = estado.lowercase()
        return e.contains("canc") || e.contains("anul") || e.contains("abort")
    }

    fun eliminarTurno(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // CANCELAR RECORDATORIO
                reminderManager.cancelAppointmentReminder(id)
                repository.eliminarTurno(id)
                _uiState.update { it.copy(isDeleteSuccessful = true) }
                fetchTurnos()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun crearNuevoTurno(especialidad: String? = null, doctor: String? = null) {
        val nombre = formPacienteNombre.value ?: ""
        val fecha = formFecha.value ?: ""
        val hora = formHora.value ?: ""
        val motivo = formMotivo.value ?: ""

        viewModelScope.launch {
            _createTurnoResource.value = Resource.Loading
            _uiState.update { it.copy(isLoading = true) }

            val fechaNormalizada = fecha.replace("/", "-").trim()
            val horaNormalizada = DateUtils.formatTo24h(hora.trim())
            
            val finalSpec = especialidad ?: getApplication<Application>().getString(R.string.label_default_specialty)
            val finalDoctor = doctor ?: getApplication<Application>().getString(R.string.label_default_doctor)

            val request = NuevoTurnoRequest(
                nombre, 
                fechaNormalizada, 
                horaNormalizada, 
                motivo, 
                finalSpec, 
                finalDoctor,
            )
            try {
                val nuevoTurno = repository.crearTurno(request)
                if (nuevoTurno != null) {
                    _createTurnoResource.value = Resource.Success(nuevoTurno)
                    // PROGRAMAR RECORDATORIO
                    reminderManager.scheduleAppointmentReminder(nuevoTurno)
                } else {
                    val pendingStatus = getApplication<Application>().getString(R.string.status_pending_offline)
                    val pendingTurno = Turno("pending_${System.currentTimeMillis()}", nombre, fechaNormalizada, horaNormalizada, motivo, pendingStatus, finalSpec, finalDoctor)
                    _createTurnoResource.value = Resource.Success(pendingTurno)
                }
                achievementManager.onAppointmentCreated()
                _uiState.update { it.copy(isCreateSuccessful = true) }
                fetchTurnos()
            } catch (e: Exception) {
                _createTurnoResource.value = Resource.Error(e.localizedMessage ?: "Error")
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun resetNavegacion() {
        _createTurnoResource.value = Resource.Idle
        _isSlotAvailable.value = null
        formPacienteNombre.value = ""
        formFecha.value = ""
        formHora.value = ""
        formMotivo.value = ""
        _uiState.update { it.copy(isDeleteSuccessful = false, isCreateSuccessful = false, errorMessage = null, isSlotAvailable = null) }
    }
    
    fun resetState() { resetNavegacion() }

    fun verificarDisponibilidad(fecha: String, hora: String) {
        val fLimpia = fecha.replace("/", "-").trim()
        val hLimpia = DateUtils.formatTo24h(hora.trim())

        if (fLimpia.isEmpty() || hLimpia.isEmpty()) {
            _isSlotAvailable.value = null
            return
        }
        
        availabilityJob?.cancel()
        availabilityJob = viewModelScope.launch {
            try {
                val disponible = repository.checkAvailability(fLimpia, hLimpia)
                _isSlotAvailable.postValue(disponible)
                _uiState.update { it.copy(isSlotAvailable = disponible) }
            } catch (_: Exception) {
                // Si hay error de red, permitimos por defecto para no bloquear la experiencia premium
                _isSlotAvailable.postValue(true)
                _uiState.update { it.copy(isSlotAvailable = true) }
            }
        }
    }

}
