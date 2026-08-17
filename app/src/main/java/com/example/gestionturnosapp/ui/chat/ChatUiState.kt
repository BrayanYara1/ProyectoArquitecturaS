package com.example.gestionturnosapp.ui.chat

import com.example.gestionturnosapp.data.model.Mensaje

data class ChatUiState(
    val mensajes: List<Mensaje> = emptyList(),
    val isLoading: Boolean = false,
    val isDoctorTyping: Boolean = false,
    val isAiAssistantMode: Boolean = false,
    val errorMessage: String? = null
)
