package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.data.remote.ApiService
import com.example.gestionturnosapp.data.remote.dto.*
import com.example.gestionturnosapp.util.NetworkUtils
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val networkUtils: NetworkUtils
) {

    suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return apiService.login(request)
    }

    suspend fun register(request: RegisterRequest): Response<AuthResponse> {
        return apiService.register(request)
    }

    fun getErrorMessage(response: Response<*>): String {
        return networkUtils.parseError(response)
    }
}
