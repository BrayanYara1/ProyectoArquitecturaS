package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.data.model.Mensaje
import com.example.gestionturnosapp.data.remote.ApiService
import com.example.gestionturnosapp.util.NetworkUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val apiService: ApiService,
    private val networkUtils: NetworkUtils
) {

    suspend fun getMensajes(): List<Mensaje> {
        val response = apiService.getMensajes()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }

    suspend fun enviarMensaje(texto: String): Mensaje? {
        val response = apiService.enviarMensaje(mapOf("texto" to texto))
        if (response.isSuccessful) {
            return response.body()
        } else {
            if (networkUtils.isSessionExpired(response)) {
                throw Exception("SESSION_EXPIRED")
            }
            throw Exception(networkUtils.parseError(response))
        }
    }
}

