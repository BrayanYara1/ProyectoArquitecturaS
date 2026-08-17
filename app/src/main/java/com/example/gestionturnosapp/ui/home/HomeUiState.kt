package com.example.gestionturnosapp.ui.home

import com.example.gestionturnosapp.data.model.HealthRecord
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.model.Turno
import com.example.gestionturnosapp.data.model.Usuario

data class HomeUiState(
    val userName: String = "",
    val user: Usuario? = null,
    val nextTurno: Turno? = null,
    val allTurnos: List<Turno> = emptyList(),
    val turnosCount: Int = 0,
    val medicamentos: List<Medicamento> = emptyList(),
    val healthScore: Int = 0,
    val healthStreak: Int = 0,
    val healthTipResId: Int? = null,
    val healthStatus: HomeViewModel.HealthStatus? = null,
    val healthInsights: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val weightRecords: List<HealthRecord> = emptyList(),
    val glucoseRecords: List<HealthRecord> = emptyList(),
    val pressureRecords: List<HealthRecord> = emptyList(),
    val waterIntakeToday: Int = 0
)
