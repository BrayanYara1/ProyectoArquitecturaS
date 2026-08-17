package com.example.gestionturnosapp.ui.estudios

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.data.model.EstudioMedico
import com.example.gestionturnosapp.data.repository.EstudioRepository
import com.example.gestionturnosapp.data.local.OfflineCacheManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstudiosViewModel @Inject constructor(
    application: Application,
    private val repository: EstudioRepository,
    private val offlineCacheManager: OfflineCacheManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(EstudiosUiState())
    val uiState: StateFlow<EstudiosUiState> = _uiState.asStateFlow()

    init {
        loadEstudios()
    }

    fun loadEstudios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val list = repository.getEstudios()
                val pending = offlineCacheManager.getPendingEstudios()
                val combined = (list + pending).distinctBy { it.id }
                _uiState.update { it.copy(allEstudios = combined, isLoading = false) }
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

    fun setDateFilter(start: String?, end: String?) {
        _uiState.update { it.copy(filterStart = start, filterEnd = end) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val query = state.searchQuery.lowercase()
        val start = state.filterStart
        val end = state.filterEnd

        val filtered = state.allEstudios.filter { estudio ->
            val matchesDate = ((start == null || estudio.fecha >= start) && (end == null || estudio.fecha <= end))
            val matchesQuery = query.isEmpty() || 
                              estudio.titulo.lowercase().contains(query) || 
                              estudio.tipo.lowercase().contains(query)
            matchesDate && matchesQuery
        }
        _uiState.update { it.copy(filteredEstudios = filtered) }
    }

    fun agregarEstudio(titulo: String, fecha: String, tipo: String, resultado: String, photoUrl: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val nuevo = EstudioMedico(
                id = "pending_${System.currentTimeMillis()}",
                titulo = titulo, fecha = fecha, tipo = tipo,
                resultadoBreve = resultado, urlDocumento = photoUrl
            )
            try {
                repository.agregarEstudio(nuevo)
                _uiState.update { it.copy(isOperationSuccessful = true) }
                loadEstudios()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }

    fun resetOperationState() {
        _uiState.update { it.copy(isOperationSuccessful = false, errorMessage = null) }
    }

    fun syncPendingEstudios() {
        val pending = offlineCacheManager.getPendingEstudios()
        if (pending.isEmpty()) return
        viewModelScope.launch {
            val synced = mutableListOf<EstudioMedico>()
            pending.forEach { 
                try { 
                    if (repository.agregarEstudio(it) != null) synced.add(it)
                } catch (_: Exception) { } 
            }
            if (synced.isNotEmpty()) offlineCacheManager.removePendingEstudios(synced)
            loadEstudios()
        }
    }

    fun eliminarEstudio(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.eliminarEstudio(id)
                loadEstudios()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }
        }
    }
}
