package com.example.gestionturnosapp.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.model.Usuario
import com.example.gestionturnosapp.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val userManager: UserManager,
    private val apiService: ApiService
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        _uiState.update { it.copy(user = userManager.getUser()) }
    }

    fun updateProfile(nuevoUsuario: Usuario) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isUpdateSuccessful = false) }
            try {
                val response = apiService.updateProfile(nuevoUsuario)
                if (response.isSuccessful) {
                    val actual = response.body() ?: nuevoUsuario
                    userManager.saveUser(actual)
                    _uiState.update { it.copy(user = actual, isLoading = false, isUpdateSuccessful = true) }
                } else {
                    val code = response.code()
                    if (code == 401) {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "SESSION_EXPIRED") }
                    } else {
                        userManager.saveUser(nuevoUsuario)
                        _uiState.update { it.copy(user = nuevoUsuario, isLoading = false, errorMessage = "OFFLINE_SAVED") }
                    }
                }
            } catch (e: Exception) {
                userManager.saveUser(nuevoUsuario)
                _uiState.update { it.copy(user = nuevoUsuario, isLoading = false, errorMessage = "OFFLINE_SAVED") }
            }
        }
    }

    fun resetUpdateStatus() {
        _uiState.update { it.copy(isUpdateSuccessful = false, errorMessage = null) }
    }
}
