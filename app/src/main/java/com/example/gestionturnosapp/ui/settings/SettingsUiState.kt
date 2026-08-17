package com.example.gestionturnosapp.ui.settings

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val isPrivacyModeEnabled: Boolean = false,
    val isHydrationEnabled: Boolean = false
)
