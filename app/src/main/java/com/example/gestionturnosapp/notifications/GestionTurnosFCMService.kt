package com.example.gestionturnosapp.notifications

import com.example.gestionturnosapp.data.UserManager
import com.example.gestionturnosapp.data.remote.ApiService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GestionTurnosFCMService : FirebaseMessagingService() {

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var apiService: ApiService

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        userManager.saveFcmToken(token)
        syncTokenToServer(token)
    }

    private fun syncTokenToServer(fcmToken: String) {
        scope.launch {
            try {
                val response = apiService.updateFcmToken(mapOf("token" to fcmToken))
                if (response.isSuccessful) {
                    userManager.markFcmAsSynced()
                }
            } catch (e: Exception) {
                android.util.Log.e("FCM", "Error syncing token", e)
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        android.util.Log.d("FCM", "Mensaje recibido de: ${remoteMessage.from}")
        
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Salud Activa"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Tienes una nueva actualización"
        
        // Normalizar el tipo a mayúsculas para compatibilidad con NotificationHelper
        val type = (remoteMessage.data["type"] ?: remoteMessage.data["TYPE"] ?: "general").uppercase()
        
        val channelId = when (type) {
            "CHAT" -> NotificationHelper.CHANNEL_CHAT
            "TURNO" -> NotificationHelper.CHANNEL_REMINDERS
            "MEDICAMENTO" -> NotificationHelper.CHANNEL_MEDICATION
            else -> NotificationHelper.CHANNEL_GENERAL
        }

        // Asegurarnos que el map de data tenga el campo TYPE en mayúsculas para los checks de NotificationHelper
        val updatedData = remoteMessage.data.toMutableMap()
        updatedData["TYPE"] = type

        NotificationHelper.showNotification(
            context = applicationContext,
            title = title,
            body = body,
            channelId = channelId,
            data = updatedData,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
