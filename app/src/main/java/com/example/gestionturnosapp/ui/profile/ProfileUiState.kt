package com.example.gestionturnosapp.ui.profile

import com.example.gestionturnosapp.data.model.Usuario

data class ProfileUiState(
    val user: Usuario? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUpdateSuccessful: Boolean = false
)
