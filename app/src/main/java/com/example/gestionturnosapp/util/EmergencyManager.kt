package com.example.gestionturnosapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferenceManager: com.example.gestionturnosapp.data.local.PreferenceManager
) {

    @SuppressLint("MissingPermission")
    fun sendEmergencyAlert(callback: (Boolean, String) -> Unit) {
        val trustedPhone = preferenceManager.getTrustedContact()
        if (trustedPhone.isNullOrBlank()) {
            callback(false, "No has configurado un contacto de confianza")
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val cts = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val mapsUrl = "https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}"
                    val message = "¡EMERGENCIA! Necesito ayuda. Mi ubicación actual es: $mapsUrl"
                    
                    try {
                        val smsManager = context.getSystemService(SmsManager::class.java)
                        smsManager.sendTextMessage(trustedPhone, null, message, null, null)
                        callback(true, "Alerta enviada con éxito a $trustedPhone")
                    } catch (e: Exception) {
                        Log.e("EmergencyManager", "Error enviando SMS", e)
                        callback(false, "Error al enviar SMS: ${e.localizedMessage}")
                    }
                } else {
                    callback(false, "No se pudo obtener tu ubicación precisa")
                }
            }
            .addOnFailureListener { e ->
                Log.e("EmergencyManager", "Error obteniendo ubicación", e)
                callback(false, "Fallo al obtener ubicación")
            }
    }
}
