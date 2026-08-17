package com.example.gestionturnosapp.ui.turnos

import com.example.gestionturnosapp.data.model.Turno

data class TurnosUiState(
    val allTurnos: List<Turno> = emptyList(),
    val filteredTurnos: List<Turno> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val filterStatus: String = "TODOS",
    val isDeleteSuccessful: Boolean = false,
    val isCreateSuccessful: Boolean = false,
    val isSlotAvailable: Boolean? = null
)
