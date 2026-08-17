package com.example.gestionturnosapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.model.Turno
import com.example.gestionturnosapp.util.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleMedicationReminder(med: Medicamento) {
        val parsedTime = DateUtils.parseTime(med.proximaToma) ?: return
        val calendar = Calendar.getInstance().apply {
            val now = Calendar.getInstance()
            val timeCal = Calendar.getInstance().apply { time = parsedTime }
            
            set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Si la hora ya pasó hoy, programar para mañana
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val displayTime = DateUtils.formatDisplayTime(med.proximaToma)
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TITLE", context.getString(R.string.title_reminder_medication))
            putExtra("MESSAGE", context.getString(R.string.msg_reminder_medication, med.nombre, med.dosis, displayTime))
            putExtra("TYPE", "MEDICAMENTO")
            putExtra("MED_ID", med.id)
            putExtra("MED_NAME", med.nombre)
            putExtra("MED_DOSIS", med.dosis)
            putExtra("MED_FREQ", med.frecuencia)
            putExtra("MED_TIME", displayTime)
            putExtra("NOTIFICATION_ID", med.id.hashCode())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            med.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        try {
            if (canScheduleExact()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } catch (e: Exception) {
            android.util.Log.e("ReminderManager", "Error scheduling alarm", e)
        }
    }

    private fun canScheduleExact(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun cancelMedicationReminder(medId: String) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun cancelAppointmentReminder(turnoId: String) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            turnoId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun scheduleAppointmentReminder(turno: Turno) {
        val timeStr = "${turno.fecha} ${DateUtils.formatTo24h(turno.hora)}"
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.US)
        
        try {
            val date = sdf.parse(timeStr) ?: return
            val calendar = Calendar.getInstance().apply {
                time = date
                add(Calendar.HOUR_OF_DAY, -1)
            }

            if (calendar.before(Calendar.getInstance())) return

            val displayTime = DateUtils.formatDisplayTime(turno.hora)
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("TITLE", context.getString(R.string.title_reminder_appointment))
                putExtra("MESSAGE", context.getString(R.string.msg_reminder_appointment, turno.doctor ?: context.getString(R.string.label_default_doctor), displayTime))
                putExtra("TYPE", "TURNO")
                putExtra("TURNO_ID", turno.id)
                putExtra("TURNO_TIME", displayTime)
                putExtra("NOTIFICATION_ID", turno.id.hashCode())
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                turno.id.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (canScheduleExact()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (e: Exception) {
            android.util.Log.e("ReminderManager", "Error scheduling appointment alarm", e)
        }
    }

    fun scheduleHydrationReminder() {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TITLE", context.getString(R.string.title_hydration_reminder))
            putExtra("MESSAGE", context.getString(R.string.msg_hydration_reminder))
            putExtra("TYPE", "HIDRATACION")
            putExtra("NOTIFICATION_ID", 9999)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9999,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Recordatorio cada 2 horas durante el día (aprox)
        val interval = 2 * 60 * 60 * 1000L
        val triggerTime = System.currentTimeMillis() + interval

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            interval,
            pendingIntent
        )
        android.util.Log.d("ReminderManager", "Recordatorio de hidratación programado")
    }

    fun cancelHydrationReminder() {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9999,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            android.util.Log.d("ReminderManager", "Recordatorio de hidratación cancelado")
        }
    }
}
