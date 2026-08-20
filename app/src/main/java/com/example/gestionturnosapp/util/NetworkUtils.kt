package com.example.gestionturnosapp.util

import android.content.Context
import com.example.gestionturnosapp.R
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(
    private val gson: Gson,
    @ApplicationContext private val context: Context
) {

    fun parseError(response: Response<*>): String {
        val code = response.code()
        return try {
            val errorBody = response.errorBody()?.string()
            
            if (!errorBody.isNullOrEmpty()) {
                val trimmedBody = errorBody.trim()
                
                // 1. Detectar si es JSON
                if (trimmedBody.startsWith("{")) {
                    val map = gson.fromJson(trimmedBody, Map::class.java)
                    val serverMessage = (map["mensaje"] as? String) 
                        ?: (map["message"] as? String) 
                        ?: (map["error"] as? String)
                        ?: (map["detalle"] as? String)
                    
                    if (serverMessage != null) {
                        return if (code >= 500) context.getString(R.string.msg_server_error, serverMessage) else serverMessage
                    }
                }
                
                // 2. Detectar si es HTML (Render/Cloudflare)
                if (trimmedBody.contains("<!DOCTYPE html>", ignoreCase = true) || trimmedBody.contains("<html", ignoreCase = true)) {
                    android.util.Log.w("NetworkUtils", "Se recibió HTML en lugar de JSON (Código $code). Cuerpo: ${trimmedBody.take(200)}...")
                    return when (code) {
                        502, 503, 504 -> context.getString(R.string.msg_server_error, "El servidor está iniciando o está sobrecargado")
                        403 -> "Acceso denegado por seguridad (Cloudflare)"
                        else -> context.getString(R.string.msg_server_error, "Respuesta inesperada del servidor")
                    }
                }

                // 3. Fallback para texto plano corto
                val plainError = trimmedBody.take(100).ifEmpty { response.message() }
                if (code >= 500) context.getString(R.string.msg_server_error, plainError) else plainError
            } else {
                "Error $code: ${response.message()}"
            }
        } catch (e: Exception) {
            android.util.Log.e("NetworkUtils", "Error al parsear respuesta: ${e.message}")
            "Error $code"
        }
    }

    fun isSessionExpired(response: Response<*>): Boolean {
        return response.code() == 401 || response.code() == 403
    }
}
