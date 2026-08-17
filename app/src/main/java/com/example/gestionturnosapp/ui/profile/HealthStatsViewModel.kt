package com.example.gestionturnosapp.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.model.HealthRecord
import com.example.gestionturnosapp.data.repository.HealthRepository
import com.example.gestionturnosapp.data.repository.MedicamentoRepository
import com.example.gestionturnosapp.data.repository.TurnoRepository
import com.example.gestionturnosapp.util.PdfGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HealthStatsViewModel @Inject constructor(
    private val repository: HealthRepository,
    private val medRepository: MedicamentoRepository,
    private val turnoRepository: TurnoRepository,
    private val achievementManager: com.example.gestionturnosapp.util.AchievementManager,
    private val userManager: UserManager
) : ViewModel() {

    val weightRecords = repository.getRecordsByType("WEIGHT").asLiveData()
    val glucoseRecords = repository.getRecordsByType("GLUCOSE").asLiveData()
    val bloodPressureRecords = repository.getRecordsByType("BLOOD_PRESSURE").asLiveData()
    val waterRecords = repository.getRecordsByType("WATER").asLiveData()
    val medicationLogs = medRepository.getAllMedicationLogs().asLiveData()
    val symptomRecords = repository.getAllSymptoms().asLiveData()

    private val _adherencePercentage = MutableLiveData<Int>(0)
    val adherencePercentage: LiveData<Int> = _adherencePercentage

    init {
        calculateAdherence()
    }

    private fun calculateAdherence() {
        viewModelScope.launch {
            medRepository.getAllMedicationLogs().collect { logs ->
                if (logs.isEmpty()) {
                    _adherencePercentage.postValue(0)
                    return@collect
                }
                // Cálculo simplificado: tomas en los últimos 7 días
                val weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
                val recentLogs = logs.filter { it.takenAt > weekAgo }.size
                val expectedLogs = 21 // Suponiendo 3 tomas al día de media
                
                val percentage = ((recentLogs.toFloat() / expectedLogs) * 100).toInt().coerceIn(0, 100)
                _adherencePercentage.postValue(percentage)
            }
        }
    }

    private val _pdfFile = MutableLiveData<File?>()
    val pdfFile: LiveData<File?> = _pdfFile

    fun addRecord(type: String, value: Float, valueSecondary: Float? = null) {
        viewModelScope.launch {
            val record = HealthRecord(
                type = type,
                value = value,
                valueSecondary = valueSecondary
            )
            repository.insertRecord(record)
            achievementManager.onHealthRecordAdded()
        }
    }

    fun deleteRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteRecord(id)
        }
    }

    fun deleteSymptom(id: Long) {
        viewModelScope.launch {
            repository.deleteSymptom(id)
        }
    }

    fun addSymptom(description: String, intensity: Int) {
        viewModelScope.launch {
            val record = com.example.gestionturnosapp.data.model.SymptomRecord(
                description = description,
                intensity = intensity
            )
            repository.insertSymptom(record)
        }
    }

    fun generatePdf(context: Context) {
        viewModelScope.launch {
            val userName = userManager.usuarioActual?.nombre ?: "Usuario"
            val meds = medRepository.getMedicamentos()
            val appointments = turnoRepository.getTurnos()
            
            // Recolectar records de forma segura directamente de los flows
            val weight = repository.getRecordsByType("WEIGHT").first()
            val glucose = repository.getRecordsByType("GLUCOSE").first()
            val pressure = repository.getRecordsByType("BLOOD_PRESSURE").first()
            val combined = weight + glucose + pressure

            val file = PdfGenerator.generateHealthReport(
                context, userName, combined, meds, appointments
            )
            _pdfFile.postValue(file)
        }
    }
    
    fun clearPdfState() {
        _pdfFile.value = null
    }
}
