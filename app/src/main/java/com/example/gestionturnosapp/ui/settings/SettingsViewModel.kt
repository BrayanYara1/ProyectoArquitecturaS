package com.example.gestionturnosapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gestionturnosapp.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val preferenceManager: PreferenceManager
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _uiState.update { it.copy(
            isDarkMode = preferenceManager.isDarkMode(),
            isBiometricEnabled = preferenceManager.isBiometricEnabled(),
            isNotificationsEnabled = preferenceManager.areNotificationsEnabled(),
            isPrivacyModeEnabled = preferenceManager.isPrivacyModeEnabled(),
            isHydrationEnabled = preferenceManager.isHydrationReminderEnabled()
        ) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        preferenceManager.setDarkMode(enabled)
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun toggleBiometric(enabled: Boolean) {
        preferenceManager.setBiometricEnabled(enabled)
        _uiState.update { it.copy(isBiometricEnabled = enabled) }
    }

    fun toggleNotifications(enabled: Boolean) {
        preferenceManager.setNotificationsEnabled(enabled)
        _uiState.update { it.copy(isNotificationsEnabled = enabled) }
    }

    fun toggleHydration(enabled: Boolean) {
        preferenceManager.setHydrationReminderEnabled(enabled)
        _uiState.update { it.copy(isHydrationEnabled = enabled) }
    }
}
