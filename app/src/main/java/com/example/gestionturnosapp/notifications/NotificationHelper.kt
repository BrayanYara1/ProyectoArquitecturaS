package com.example.gestionturnosapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.gestionturnosapp.MainActivity
import com.example.gestionturnosapp.R

object NotificationHelper {

    private const val TAG = "NotificationHelper"

    const val CHANNEL_GENERAL = "general_channel"
    const val CHANNEL_REMINDERS = "reminders_channel"
    const val CHANNEL_MEDICATION = "medication_channel"
    const val CHANNEL_CHAT = "chat_channel"

    const val GROUP_MEDICATION = "com.example.gestionturnosapp.MEDICATION"
    const val GROUP_REMINDERS = "com.example.gestionturnosapp.REMINDERS"
    const val GROUP_CHAT = "com.example.gestionturnosapp.CHAT"
    const val GROUP_GENERAL = "com.example.gestionturnosapp.GENERAL"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channels = listOf(
                NotificationChannel(
                    CHANNEL_GENERAL,
                    context.getString(R.string.label_general_channel),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notificaciones generales de la aplicación"
                    setShowBadge(true)
                    enableLights(true)
                    lightColor = android.graphics.Color.BLUE
                },
                NotificationChannel(
                    CHANNEL_REMINDERS,
                    context.getString(R.string.label_reminders_channel),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 400, 200, 400)
                    description = "Recordatorios de citas médicas"
                    lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                    enableLights(true)
                    lightColor = android.graphics.Color.GREEN
                },
                NotificationChannel(
                    CHANNEL_MEDICATION,
                    context.getString(R.string.label_medication_channel),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 100, 500, 100, 500)
                    description = "Avisos para toma de medicamentos"
                    lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                    enableLights(true)
                    lightColor = android.graphics.Color.RED
                },
                NotificationChannel(
                    CHANNEL_CHAT,
                    context.getString(R.string.label_chat_channel),
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(true)
                    description = "Mensajes del asistente médico"
                    enableLights(true)
                    lightColor = android.graphics.Color.MAGENTA
                }
            )
            notificationManager.createNotificationChannels(channels)
            Log.d(TAG, "Canales de notificación creados y refinados")
        }
    }

    private fun formatAllTimesInString(context: Context, input: String): String {
        // Regex para detectar horas (HH:mm) y marcadores AM/PM
        val timePattern = Regex("([01]?[0-9]|2[0-3]):([0-5][0-9])(\\s*(?i:AM|PM|A\\.\\s*M\\.|P\\.\\s*M\\.))?")
        // Regex para detectar fechas ISO (YYYY-MM-DD)
        val datePattern = Regex("(\\d{4})-(\\d{2})-(\\d{2})")
        
        var result = input
        try {
            // Reemplazar horas
            result = timePattern.replace(result) { matchResult ->
                com.example.gestionturnosapp.util.DateUtils.formatDisplayTime(matchResult.value)
            }
            // Reemplazar fechas por formato largo y amigable
            result = datePattern.replace(result) { matchResult ->
                com.example.gestionturnosapp.util.DateUtils.formatDisplayDate(context, matchResult.value, forceLong = true)
            }
        } catch (_: Exception) {}
        return result
    }

    fun showNotification(
        context: Context,
        title: String,
        body: String,
        channelId: String = CHANNEL_GENERAL,
        notificationId: Int = (System.currentTimeMillis() % 10000).toInt(),
        data: Map<String, String>? = null
    ) {
        Log.d(TAG, "Intentando mostrar notificación: $title - Channel: $channelId")

        if (!com.example.gestionturnosapp.data.local.PreferenceManager.areNotificationsEnabled(context)) {
            Log.w(TAG, "Notificaciones desactivadas en ajustes de la app")
            return
        }

        val formattedBody = formatAllTimesInString(context, body)
        
        // Detectar si es una notificación de cancelación de forma más robusta
        val isCancelled = (title.contains("cancelado", ignoreCase = true)) || 
                         (title.contains("⚠️", ignoreCase = true)) ||
                         (body.contains("cancelado", ignoreCase = true)) ||
                         (data?.get("STATUS")?.uppercase() == "CANCELADO") ||
                         (data?.get("TYPE") == "CANCELACION")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            data?.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val color = when {
            isCancelled -> androidx.core.content.ContextCompat.getColor(context, R.color.error)
            channelId == CHANNEL_MEDICATION -> androidx.core.content.ContextCompat.getColor(context, R.color.accent)
            channelId == CHANNEL_REMINDERS -> androidx.core.content.ContextCompat.getColor(context, R.color.primary)
            channelId == CHANNEL_CHAT -> androidx.core.content.ContextCompat.getColor(context, R.color.secondary)
            else -> androidx.core.content.ContextCompat.getColor(context, R.color.secondary)
        }

        val icon = when {
            isCancelled -> R.drawable.ic_warning
            channelId == CHANNEL_MEDICATION -> R.drawable.ic_medical_logo
            channelId == CHANNEL_REMINDERS -> R.drawable.ic_nav_calendar
            channelId == CHANNEL_CHAT -> R.drawable.ic_chat
            else -> R.drawable.ic_medical_logo
        }

        val groupKey = when (channelId) {
            CHANNEL_MEDICATION -> GROUP_MEDICATION
            CHANNEL_REMINDERS -> GROUP_REMINDERS
            CHANNEL_CHAT -> GROUP_CHAT
            else -> GROUP_GENERAL
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setColor(color)
            .setContentTitle(title)
            .setContentText(formattedBody)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .setContentIntent(pendingIntent)
            .setPriority(
                if (channelId == CHANNEL_GENERAL) NotificationCompat.PRIORITY_DEFAULT 
                else NotificationCompat.PRIORITY_HIGH
            )
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setStyle(NotificationCompat.BigTextStyle().bigText(formattedBody))
            .setGroup(groupKey)

        // Acciones para medicamentos
        if (channelId == CHANNEL_MEDICATION && data?.get("TYPE") == "MEDICAMENTO") {
            val medId = data["MED_ID"]
            
            val takeIntent = Intent(context, ReminderReceiver::class.java).apply {
                action = "ACTION_TAKE_MED"
                putExtra("MED_ID", medId)
                putExtra("NOTIFICATION_ID", notificationId)
            }
            val takePendingIntent = PendingIntent.getBroadcast(
                context, notificationId + 1, takeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.addAction(R.drawable.ic_check, context.getString(R.string.label_take), takePendingIntent)
            
            val snoozeIntent = Intent(context, ReminderReceiver::class.java).apply {
                action = "ACTION_SNOOZE"
                putExtra("MED_ID", medId)
                putExtra("NOTIFICATION_ID", notificationId)
            }
            val snoozePendingIntent = PendingIntent.getBroadcast(
                context, notificationId + 2, snoozeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.addAction(R.drawable.ic_clock, context.getString(R.string.label_snooze), snoozePendingIntent)
        }

        // Acción para Turnos
        if (channelId == CHANNEL_REMINDERS && data?.get("TYPE") == "TURNO") {
            if (isCancelled) {
                // Si está cancelado, ofrecer re-agendar
                val scheduleIntent = Intent(context, MainActivity::class.java).apply {
                    putExtra("NAVIGATE_TO", "SOLICITAR_TURNO")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val schedulePendingIntent = PendingIntent.getActivity(
                    context, notificationId + 4, scheduleIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                builder.addAction(R.drawable.ic_add_circle, context.getString(R.string.btn_schedule_appointment), schedulePendingIntent)
            } else {
                val turnoId = data["TURNO_ID"]
                val viewIntent = Intent(context, MainActivity::class.java).apply {
                    putExtra("TURNO_ID", turnoId)
                    putExtra("NAVIGATE_TO", "TURNO_DETAIL")
                    putExtra("TYPE", "TURNO")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val viewPendingIntent = PendingIntent.getActivity(
                    context, notificationId + 3, viewIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
                builder.addAction(R.drawable.ic_nav_search, context.getString(R.string.title_appointment_detail), viewPendingIntent)
            }
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "No hay permiso POST_NOTIFICATIONS concedido")
            }
        }

        notificationManager.notify(notificationId, builder.build())
        
        // Mostrar notificación de resumen para el grupo
        showSummaryNotification(context, channelId, groupKey, color, isCancelled)

        Log.d(TAG, "Notificación enviada al sistema")
    }

    private fun showSummaryNotification(context: Context, channelId: String, groupKey: String, color: Int, isCancelled: Boolean) {
        val summaryIcon = when (groupKey) {
            GROUP_MEDICATION -> R.drawable.ic_medical_logo
            GROUP_REMINDERS -> if (isCancelled) R.drawable.ic_warning else R.drawable.ic_nav_calendar
            GROUP_CHAT -> R.drawable.ic_chat
            else -> R.drawable.ic_medical_logo
        }

        val summaryTitle = when (groupKey) {
            GROUP_MEDICATION -> "Medicamentos Pendientes"
            GROUP_REMINDERS -> if (isCancelled) "Alertas de Citas" else "Próximas Citas"
            GROUP_CHAT -> "Nuevos Mensajes"
            else -> "Notificaciones de Salud"
        }

        val summaryNotification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(summaryTitle)
            .setContentText("Tienes múltiples notificaciones")
            .setSmallIcon(summaryIcon)
            .setColor(color)
            .setStyle(NotificationCompat.InboxStyle()
                .setBigContentTitle(summaryTitle)
                .setSummaryText(summaryTitle))
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(groupKey.hashCode(), summaryNotification)
    }
}
