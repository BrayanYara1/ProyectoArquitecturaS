package com.example.gestionturnosapp.util

import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(private val gson: Gson) {

    fun parseError(response: Response<*>): String {
        val code = response.code()
        return try {
            val errorBody = response.errorBody()?.string()
            if (!errorBody.isNullOrEmpty()) {
                if (errorBody.trim().startsWith("{")) {
                    val map = gson.fromJson(errorBody, Map::class.java)
                    val serverMessage = (map["mensaje"] as? String) 
                        ?: (map["message"] as? String) 
                        ?: (map["error"] as? String)
                        ?: (map["detalle"] as? String)
                    
                    if (serverMessage != null) {
                        // Si es un error 500, mostramos el mensaje del servidor + código para diagnóstico
                        if (code >= 500) "Servidor ($code): $serverMessage" else serverMessage
                    } else {
                        "Error $code"
                    }
                } else {
                    val plainError = errorBody.take(100).ifEmpty { response.message() }
                    if (code >= 500) "Servidor ($code): $plainError" else plainError
                }
            } else {
                "Error $code: ${response.message()}"
            }
        } catch (_: Exception) {
            "Error $code"
        }
    }

    fun isSessionExpired(response: Response<*>): Boolean {
        return response.code() == 401 || response.code() == 403
    }
}
