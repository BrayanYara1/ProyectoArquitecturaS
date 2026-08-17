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
                    (map["mensaje"] as? String) 
                        ?: (map["message"] as? String) 
                        ?: (map["error"] as? String) 
                        ?: "Error $code"
                } else {
                    errorBody.take(100).ifEmpty { response.message().ifEmpty { "Error $code" } }
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
