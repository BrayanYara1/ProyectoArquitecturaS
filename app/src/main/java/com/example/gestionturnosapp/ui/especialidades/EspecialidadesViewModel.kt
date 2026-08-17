package com.example.gestionturnosapp.ui.especialidades

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.data.model.Especialidad
import com.example.gestionturnosapp.data.repository.EspecialidadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EspecialidadesViewModel @Inject constructor(
    application: Application,
    private val repository: EspecialidadRepository,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(EspecialidadesUiState())
    val uiState: StateFlow<EspecialidadesUiState> = _uiState.asStateFlow()

    init {
        loadEspecialidades()
    }

    fun loadEspecialidades() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val list = repository.getEspecialidades()
            _uiState.update { it.copy(allEspecialidades = list, isLoading = false) }
            applyFilters()
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val list = state.allEspecialidades
        val query = state.searchQuery.lowercase(Locale.getDefault())

        val filtered = if (query.isEmpty()) {
            list
        } else {
            val context = getApplication<Application>().applicationContext
            list.filter { especialidad ->
                val name = context.getString(especialidad.nombreRes).lowercase(Locale.getDefault())
                val desc = context.getString(especialidad.descripcionRes).lowercase(Locale.getDefault())
                name.contains(query) || desc.contains(query)
            }
        }
        _uiState.update { it.copy(filteredEspecialidades = filtered) }
    }

    fun refreshForLocale() {
        loadEspecialidades()
    }
}
