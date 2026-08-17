package com.example.gestionturnosapp.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.data.model.*
import com.example.gestionturnosapp.data.repository.*
import com.example.gestionturnosapp.data.local.*
import com.example.gestionturnosapp.util.SmartAssistant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    private val repository: ChatRepository,
    private val turnoRepository: TurnoRepository,
    private val medRepository: MedicamentoRepository,
    private val healthRepository: HealthRepository,
    private val smartAssistant: SmartAssistant,
    private val offlineCacheManager: OfflineCacheManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ChatUiState(isAiAssistantMode = true))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        fetchMensajes()
    }

    fun setAiMode(enabled: Boolean) {
        _uiState.update { it.copy(isAiAssistantMode = enabled) }
        fetchMensajes()
    }

    fun fetchMensajes() {
        if (_uiState.value.isAiAssistantMode) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val cached = offlineCacheManager.getCachedMensajes()
            if (cached.isNotEmpty()) _uiState.update { it.copy(mensajes = cached) }

            try {
                val list = repository.getMensajes()
                offlineCacheManager.saveMensajes(list)
                _uiState.update { it.copy(mensajes = list, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return
        
        val userMsg = Mensaje(id = "local_${System.currentTimeMillis()}", texto = texto, remitente = "PACIENTE", fecha = Date())
        val updatedList = _uiState.value.mensajes + userMsg
        _uiState.update { it.copy(mensajes = updatedList) }

        if (_uiState.value.isAiAssistantMode) {
            enviarMensajeIA(texto, updatedList)
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(isDoctorTyping = true) }
                try {
                    val mensaje = repository.enviarMensaje(texto)
                    if (mensaje != null) {
                        delay(1000)
                        fetchMensajes()
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.localizedMessage) }
                } finally {
                    _uiState.update { it.copy(isDoctorTyping = false) }
                }
            }
        }
    }

    private fun enviarMensajeIA(texto: String, currentList: List<Mensaje>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDoctorTyping = true) }
            delay(1500)
            
            val turnos = try { turnoRepository.getTurnos() } catch(_: Exception) { emptyList() }
            val meds = try { medRepository.getMedicamentos() } catch(_: Exception) { emptyList() }
            val weight = try { healthRepository.getRecordsByType("WEIGHT").first() } catch(_: Exception) { emptyList() }
            val glucose = try { healthRepository.getRecordsByType("GLUCOSE").first() } catch(_: Exception) { emptyList() }
            val pressure = try { healthRepository.getRecordsByType("BLOOD_PRESSURE").first() } catch(_: Exception) { emptyList() }
            val symptoms = try { healthRepository.getAllSymptoms().first() } catch(_: Exception) { emptyList() }
            
            val response = smartAssistant.generateResponse(texto, turnos, meds, weight + glucose + pressure, symptoms)
            val aiMsg = Mensaje(id = "ai_${System.currentTimeMillis()}", texto = response, remitente = "DOCTOR", fecha = Date())
            
            _uiState.update { it.copy(mensajes = currentList + aiMsg, isDoctorTyping = false) }
        }
    }
}
