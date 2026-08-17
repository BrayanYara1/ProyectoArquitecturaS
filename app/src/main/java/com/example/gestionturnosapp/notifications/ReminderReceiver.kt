package com.example.gestionturnosapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.repository.MedicamentoRepository
import com.example.gestionturnosapp.data.repository.TurnoRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderManager: ReminderManager

    @Inject
    lateinit var medRepository: MedicamentoRepository

    @Inject
    lateinit var turnoRepository: TurnoRepository

    @Inject
    lateinit var achievementManager: com.example.gestionturnosapp.util.AchievementManager

    @Inject
    lateinit var preferenceManager: com.example.gestionturnosapp.data.local.PreferenceManager

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("ReminderReceiver", "Broadcast recibido: $action")

        if (action == Intent.ACTION_BOOT_COMPLETED) {
            restoreAlarms()
            return
        }

        if (action == "ACTION_TAKE_MED") {
            val medId = intent.getStringExtra("MED_ID")
            val notifId = intent.getIntExtra("NOTIFICATION_ID", -1)
            if (medId != null) {
                markAsTakenFromNotification(context, medId)
            }
            if (notifId != -1) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.cancel(notifId)
            }
            return
        }

        if (action == "ACTION_SNOOZE") {
            val medId = intent.getStringExtra("MED_ID")
            val notifId = intent.getIntExtra("NOTIFICATION_ID", -1)
            if (notifId != -1) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.cancel(notifId)
            }
            
            // Reprogramar en 15 minutos
            val calendar = java.util.Calendar.getInstance()
            calendar.add(java.util.Calendar.MINUTE, 15)
            
            // IMPORTANTE: Limpiar la acción para que cuando se dispare de nuevo no vuelva a entrar en ACTION_SNOOZE
            val snoozeIntent = Intent(context, ReminderReceiver::class.java).apply {
                putExtras(intent)
                setAction(null) 
            }
            
            val pendingIntent = android.app.PendingIntent.getBroadcast(
                context,
                (medId?.hashCode() ?: 0) + 99,
                snoozeIntent,
                android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT
            )
            
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                } else {
                    alarmManager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                }
            } catch (e: Exception) {
                alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            return
        }

        // Mostrar la notificación
        val title = intent.getStringExtra("TITLE") ?: context.getString(R.string.welcome)
        val message = intent.getStringExtra("MESSAGE") ?: context.getString(R.string.no_upcoming_appointments)
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 1001)
        val type = intent.getStringExtra("TYPE") ?: "TURNO"
        
        val channelId = when (type) {
            "MEDICAMENTO" -> NotificationHelper.CHANNEL_MEDICATION
            "HIDRATACION" -> NotificationHelper.CHANNEL_GENERAL
            else -> NotificationHelper.CHANNEL_REMINDERS
        }

        val data = mutableMapOf<String, String>()
        intent.extras?.let { extras ->
            for (key in extras.keySet()) {
                @Suppress("DEPRECATION")
                val value = extras.get(key)
                value?.toString()?.let { data[key] = it }
            }
        }

        NotificationHelper.showNotification(
            context = context,
            title = title,
            body = message,
            channelId = channelId,
            notificationId = notificationId,
            data = data
        )
    }

    private fun restoreAlarms() {
        scope.launch {
            try {
                if (preferenceManager.isHydrationReminderEnabled()) {
                    reminderManager.scheduleHydrationReminder()
                }

                val medicamentos = medRepository.getMedicamentos()
                medicamentos.forEach { med ->
                    reminderManager.scheduleMedicationReminder(med)
                }
                
                val turnos = turnoRepository.getTurnos()
                turnos.forEach { turno ->
                    if (!com.example.gestionturnosapp.util.DateUtils.isPastDateTime(turno.fecha, turno.hora)) {
                        reminderManager.scheduleAppointmentReminder(turno)
                    }
                }
                Log.d("ReminderReceiver", "Alarmas restauradas tras el boot")
            } catch (e: Exception) {
                Log.e("ReminderReceiver", "Error restaurando alarmas", e)
            }
        }
    }

    private fun markAsTakenFromNotification(context: Context, medId: String) {
        scope.launch {
            try {
                // Buscamos el medicamento real para no perder datos como stock o notas
                val meds = medRepository.getMedicamentos()
                val med = meds.find { it.id == medId } ?: return@launch
                
                val hoursToAdd = med.frecuencia.filter { it.isDigit() }.toIntOrNull() ?: 8
                val calendar = java.util.Calendar.getInstance()
                calendar.add(java.util.Calendar.HOUR_OF_DAY, hoursToAdd)
                
                val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US)
                val nuevaProximaToma = sdf.format(calendar.time)
                
                // Reducir stock
                val nuevoStock = if (med.stockActual > 0) med.stockActual - 1 else 0
                
                val updatedMed = med.copy(proximaToma = nuevaProximaToma, stockActual = nuevoStock)
                
                // Persistir en el repositorio (esto actualiza tanto remoto como local cache)
                medRepository.updateMedicamento(medId, updatedMed)
                
                // Guardar log de toma
                medRepository.addMedicationLog(
                    com.example.gestionturnosapp.data.model.MedicationLog(
                        medId = med.id,
                        medName = med.nombre,
                        dose = med.dosis
                    )
                )

                // Notificar si el stock es bajo
                if (nuevoStock <= med.stockMinimo && nuevoStock > 0) {
                    NotificationHelper.showNotification(
                        context,
                        context.getString(R.string.title_low_stock, med.nombre),
                        context.getString(R.string.msg_low_stock, nuevoStock),
                        NotificationHelper.CHANNEL_GENERAL,
                        notificationId = (medId + "_stock").hashCode()
                    )
                } else if (nuevoStock == 0) {
                    NotificationHelper.showNotification(
                        context,
                        context.getString(R.string.title_no_stock, med.nombre),
                        context.getString(R.string.msg_no_stock),
                        NotificationHelper.CHANNEL_GENERAL,
                        notificationId = (medId + "_stock").hashCode()
                    )
                }
                
                // Programar la siguiente alarma con el objeto actualizado
                reminderManager.scheduleMedicationReminder(updatedMed)
                
                // Actualizar logros
                achievementManager.onMedicationTaken()
                
                Log.d("ReminderReceiver", "Medicamento $medId marcado como tomado exitosamente")
            } catch (e: Exception) {
                Log.e("ReminderReceiver", "Error marking as taken from notification", e)
            }
        }
    }
}
