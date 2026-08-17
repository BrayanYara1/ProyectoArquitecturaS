package com.example.gestionturnosapp.ui.medicamentos

import com.example.gestionturnosapp.data.model.Medicamento

data class MedicamentosUiState(
    val medicamentos: List<Medicamento> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOperationSuccessful: Boolean = false,
    val newlyAddedMedication: Medicamento? = null
)
