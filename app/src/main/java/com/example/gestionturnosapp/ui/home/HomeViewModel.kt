package com.example.gestionturnosapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.local.OfflineCacheManager
import com.example.gestionturnosapp.data.model.*
import com.example.gestionturnosapp.data.remote.dto.*
import com.example.gestionturnosapp.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val turnoRepository: TurnoRepository,
    private val medRepository: MedicamentoRepository,
    private val healthRepository: HealthRepository,
    private val achievementManager: com.example.gestionturnosapp.util.AchievementManager,
    private val userManager: UserManager,
    private val preferenceManager: com.example.gestionturnosapp.data.local.PreferenceManager,
    private val offlineCacheManager: OfflineCacheManager,
    private val reminderManager: com.example.gestionturnosapp.notifications.ReminderManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            achievementManager.initAchievements()
            loadInitialData()
            observeHealthData()
            syncAll()
        }
    }

    private fun loadInitialData() {
        val user = userManager.getUser()
        val rawName = user?.nombre ?: getApplication<Application>().getString(R.string.label_anonymous)
        val cleanName = rawName.replace(Regex("\\s*\\(v?\\d+(\\.\\d+)*\\)\\s*"), "")
            .split("\n")[0]
            .trim()

        val tips = listOf(R.string.tip_health_1, R.string.tip_health_2, R.string.tip_health_3, R.string.tip_health_4, R.string.tip_health_5, R.string.tip_health_6, R.string.tip_health_7, R.string.tip_health_8, R.string.tip_health_9, R.string.tip_health_10)
        val tipResId = tips[Calendar.getInstance()[Calendar.DAY_OF_YEAR] % tips.size]

        _uiState.update { it.copy(
            userName = cleanName,
            user = user,
            healthTipResId = tipResId,
            healthStreak = preferenceManager.getHealthStreak()
        ) }
        
        refreshLocalData()
    }

    private fun observeHealthData() {
        viewModelScope.launch {
            try {
                combine(
                    healthRepository.getRecordsByType("WEIGHT"),
                    healthRepository.getRecordsByType("GLUCOSE"),
                    healthRepository.getRecordsByType("BLOOD_PRESSURE"),
                    healthRepository.getRecordsByType("WATER")
                ) { weight, glucose, pressure, water ->
                    try {
                        val today = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US).format(java.util.Date())
                        val waterToday = water.asSequence().filter {
                            try {
                                java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US).format(java.util.Date(it.date)) == today
                            } catch (_: Exception) { false }
                        }.sumOf { it.value.toInt() }

                        val latestGlucose = glucose.lastOrNull()?.value
                        val status = when {
                            latestGlucose == null -> null
                            latestGlucose < 70 -> HealthStatus(getApplication<Application>().getString(R.string.label_glucose_low), R.color.error, R.drawable.ic_medical_logo, getApplication<Application>().getString(R.string.msg_glucose_low))
                            latestGlucose > 140 -> HealthStatus(getApplication<Application>().getString(R.string.label_glucose_high), R.color.error, R.drawable.ic_medical_logo, getApplication<Application>().getString(R.string.msg_glucose_high))
                            else -> HealthStatus(getApplication<Application>().getString(R.string.label_status_normal), R.color.success, R.drawable.ic_nav_calendar, getApplication<Application>().getString(R.string.msg_glucose_normal))
                        }

                        _uiState.update { it.copy(
                            weightRecords = weight,
                            glucoseRecords = glucose,
                            pressureRecords = pressure,
                            waterIntakeToday = waterToday,
                            healthStatus = status
                        ) }
                        
                        calculateHealthScore()
                        updateInsights(glucose, "GLUCOSE")
                        updateInsights(weight, "WEIGHT")
                    } catch (e: Exception) {
                        android.util.Log.e("HomeViewModel", "Error procesando registros de salud", e)
                    }
                }.collect()
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Error en el flujo de salud", e)
            }
        }
    }

    private fun calculateHealthScore() {
        val state = _uiState.value
        var score = 0
        
        // 1. Agua (30 pts)
        score += ((state.waterIntakeToday / 2000.0) * 30).toInt().coerceAtMost(30)
        
        // 2. Medicamentos (40 pts)
        score += if (state.medicamentos.isNotEmpty()) {
            val lowStock = state.medicamentos.count { it.stockActual <= it.stockMinimo }
            (40 - (lowStock * 10)).coerceAtLeast(0)
        } else 40
        
        // 3. Registros (30 pts)
        if (state.weightRecords.isNotEmpty()) score += 15
        if (state.glucoseRecords.isNotEmpty()) score += 15
        
        val finalScore = score.coerceIn(0, 100)
        preferenceManager.updateHealthStreak(finalScore)
        
        _uiState.update { it.copy(
            healthScore = finalScore, 
            healthStreak = preferenceManager.getHealthStreak()
        ) }
    }

    private fun updateInsights(records: List<HealthRecord>, type: String) {
        if (records.size < 2) return
        try {
            val latest = records.last()
            val previous = records[records.size - 2]
            
            val insight = when(type) {
                "GLUCOSE" -> {
                    val trend = if (latest.value > previous.value) getApplication<Application>().getString(R.string.label_trend_increasing) else getApplication<Application>().getString(R.string.label_trend_decreasing)
                    getApplication<Application>().getString(R.string.msg_insight_glucose, trend, String.format(Locale.US, "%.1f", kotlin.math.abs(latest.value - previous.value)))
                }
                "WEIGHT" -> {
                    val trend = if (latest.value > previous.value) getApplication<Application>().getString(R.string.label_trend_increased) else getApplication<Application>().getString(R.string.label_trend_decreased)
                    getApplication<Application>().getString(R.string.msg_weight_trend, trend, String.format(Locale.US, "%.1f", kotlin.math.abs(latest.value - previous.value)))
                }
                else -> ""
            }
            _uiState.update { it.copy(healthInsights = insight) }
        } catch (e: Exception) {
            android.util.Log.e("HomeViewModel", "Error en updateInsights", e)
        }
    }

    fun refreshData() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val turnos = turnoRepository.getTurnos()
                val meds = medRepository.getMedicamentos()
                val pendingMeds = medRepository.getPendingMeds()
                val finalMeds = (meds + pendingMeds).distinctBy { it.nombre.lowercase().trim() }
                
                updateTurnosUI(turnos)
                _uiState.update { it.copy(medicamentos = finalMeds, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Error de red") }
            }
        }
    }

    private fun updateTurnosUI(turnos: List<Turno>) {
        val next = turnos.asSequence()
            .filter { it.estado.lowercase() in listOf("pendiente", "pending") }
            .minByOrNull { turno ->
                try {
                    val h = com.example.gestionturnosapp.util.DateUtils.formatTo24h(turno.hora)
                    "${turno.fecha} $h"
                } catch (e: Exception) { "${turno.fecha} ${turno.hora}" }
            }
        _uiState.update { it.copy(allTurnos = turnos, turnosCount = turnos.size, nextTurno = next) }
    }

    private fun refreshLocalData() {
        viewModelScope.launch {
            val meds = (offlineCacheManager.getCachedMedicamentos() + offlineCacheManager.getPendingMeds())
                .distinctBy { it.nombre.lowercase().trim() }
            val turnos = offlineCacheManager.getCachedTurnos()
            _uiState.update { it.copy(medicamentos = meds) }
            if (turnos.isNotEmpty()) updateTurnosUI(turnos)
        }
    }

    fun syncAll() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val pending = offlineCacheManager.getPendingMeds()
                if (pending.isNotEmpty()) {
                    val synced = mutableListOf<Medicamento>()
                    for (m in pending) {
                        try {
                            val req = NuevoMedicamentoRequest(m.nombre, m.dosis, m.frecuencia, m.proximaToma, m.stockActual, m.stockMinimo, m.notas)
                            if (medRepository.agregarMedicamento(req) != null) synced.add(m)
                        } catch (e: Exception) { continue }
                    }
                    if (synced.isNotEmpty()) offlineCacheManager.removePendingMeds(synced)
                }
                withContext(kotlinx.coroutines.Dispatchers.Main) {
                    refreshData()
                }
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Error in syncAll", e)
            }
        }
    }

    fun marcarComoTomado(med: Medicamento) {
        viewModelScope.launch {
            try {
                val hours = med.frecuencia.filter { it.isDigit() }.toIntOrNull() ?: 8
                val calendar = Calendar.getInstance()
                com.example.gestionturnosapp.util.DateUtils.parseTime(med.proximaToma)?.let {
                    val pCal = Calendar.getInstance().apply { time = it }
                    calendar.set(Calendar.HOUR_OF_DAY, pCal.get(Calendar.HOUR_OF_DAY))
                    calendar.set(Calendar.MINUTE, pCal.get(Calendar.MINUTE))
                }
                calendar.add(Calendar.HOUR_OF_DAY, hours)
                val nuevaProxima = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
                val updated = med.copy(proximaToma = nuevaProxima, stockActual = (med.stockActual - 1).coerceAtLeast(0))
                
                medRepository.updateMedicamento(med.id, updated)
                medRepository.addMedicationLog(MedicationLog(medId = med.id, medName = med.nombre, dose = med.dosis))
                reminderManager.scheduleMedicationReminder(updated)
                achievementManager.onMedicationTaken()
                refreshData()
            } catch (e: Exception) { }
        }
    }

    fun addWater(ml: Int) {
        viewModelScope.launch {
            healthRepository.insertRecord(HealthRecord(type = "WATER", value = ml.toFloat(), date = System.currentTimeMillis()))
            achievementManager.onHealthRecordAdded()
        }
    }

    fun updateUserData() { loadInitialData() }

    fun generateHealthPdf(context: android.content.Context, onResult: (java.io.File?) -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            val combined = state.weightRecords + state.glucoseRecords + state.pressureRecords
            val file = com.example.gestionturnosapp.util.PdfGenerator.generateHealthReport(
                context, state.userName, combined, state.medicamentos, state.allTurnos
            )
            onResult(file)
        }
    }

    data class HealthStatus(val status: String, val colorRes: Int, val iconRes: Int, val message: String)
}
