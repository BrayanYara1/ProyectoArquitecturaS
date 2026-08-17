package com.example.gestionturnosapp.ui.estudios

import com.example.gestionturnosapp.data.model.EstudioMedico

data class EstudiosUiState(
    val allEstudios: List<EstudioMedico> = emptyList(),
    val filteredEstudios: List<EstudioMedico> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val filterStart: String? = null,
    val filterEnd: String? = null,
    val isOperationSuccessful: Boolean = false
)
