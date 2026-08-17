package com.example.gestionturnosapp.util

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.example.gestionturnosapp.data.model.Turno
import java.util.Calendar

object CalendarHelper {

    fun addToCalendar(context: Context, turno: Turno) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "Cita Médica: ${turno.especialidad ?: "Consulta"}")
            putExtra(CalendarContract.Events.DESCRIPTION, "Doctor: ${turno.doctor ?: "Pendiente"}\nMotivo: ${turno.motivo}")
            
            val calendar = Calendar.getInstance()
            try {
                val timeParts = DateUtils.formatTo24h(turno.hora).split(":")
                val dateParts = turno.fecha.split("-")
                
                if (dateParts.size == 3 && timeParts.size >= 2) {
                    calendar.set(dateParts[0].toInt(), dateParts[1].toInt() - 1, dateParts[2].toInt(), 
                                 timeParts[0].toInt(), timeParts[1].toInt())
                }
            } catch (_: Exception) {}

            val startTime = calendar.timeInMillis
            val endTime = startTime + 60 * 60 * 1000 // 1 hour duration

            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            putExtra(CalendarContract.Events.EVENT_LOCATION, "Centro Médico")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
