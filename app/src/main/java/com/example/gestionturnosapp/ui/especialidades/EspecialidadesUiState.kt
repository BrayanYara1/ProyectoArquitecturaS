package com.example.gestionturnosapp.ui.especialidades

import com.example.gestionturnosapp.data.model.Especialidad

data class EspecialidadesUiState(
    val allEspecialidades: List<Especialidad> = emptyList(),
    val filteredEspecialidades: List<Especialidad> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
