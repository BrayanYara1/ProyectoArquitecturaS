package com.example.gestionturnosapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val isoDateFormat get() = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private fun getDisplayTimeFormat() = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val inputFormats = listOf("hh:mm a", "h:mm a", "HH:mm", "H:mm")

    fun parseTime(time: String?): Date? {
        if (time.isNullOrBlank()) return null
        
        // Limpieza de formatos comunes en español y otros
        val cleanTime = time.uppercase()
            .replace("A. M.", "AM")
            .replace("P. M.", "PM")
            .replace("A.M.", "AM")
            .replace("P.M.", "PM")
            .replace("A M", "AM")
            .replace("P M", "PM")
            .trim()

        // Primero intentamos con el Locale actual
        for (fmt in inputFormats) {
            try {
                val sdf = SimpleDateFormat(fmt, Locale.getDefault())
                sdf.isLenient = false
                val date = sdf.parse(cleanTime)
                if (date != null) return date
            } catch (_: Exception) {}
        }

        // Luego con US como fallback universal para AM/PM
        for (fmt in inputFormats) {
            try {
                val sdf = SimpleDateFormat(fmt, Locale.US)
                sdf.isLenient = false
                val date = sdf.parse(cleanTime)
                if (date != null) return date
            } catch (_: Exception) {}
        }
        
        // Fallback manual para casos extremos
        return try {
            val parts = cleanTime.filter { it.isDigit() || it == ':' || it == ' ' || it == 'A' || it == 'P' || it == 'M' }
                .split(":")
            if (parts.size >= 2) {
                val cal = Calendar.getInstance()
                var h = parts[0].trim().toIntOrNull() ?: 0
                val m = parts[1].filter { it.isDigit() }.toIntOrNull() ?: 0
                if (cleanTime.contains("PM") && h < 12) h += 12
                if (cleanTime.contains("AM") && h == 12) h = 0
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, m)
                cal.set(Calendar.SECOND, 0)
                cal.time
            } else null
        } catch (_: Exception) { null }
    }

    fun formatDisplayTime(time: String?): String {
        val dateObj = parseTime(time)
        return if (dateObj != null) getDisplayTimeFormat().format(dateObj) else time ?: ""
    }

    fun formatDisplayDate(context: android.content.Context?, dateStr: String?, forceLong: Boolean = false): String {
        if (dateStr.isNullOrBlank()) return ""
        
        return try {
            val date = isoDateFormat.parse(dateStr)
            if (date != null) {
                val calendar = Calendar.getInstance()
                val today = calendar.time
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val tomorrow = calendar.time
                
                val sdfDay = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val isToday = sdfDay.format(date) == sdfDay.format(today)
                val isTomorrow = sdfDay.format(date) == sdfDay.format(tomorrow)

                if (!forceLong && context != null) {
                    when {
                        isToday -> return context.getString(com.example.gestionturnosapp.R.string.today)
                        isTomorrow -> return context.getString(com.example.gestionturnosapp.R.string.tomorrow)
                    }
                }

                val pattern = if (Locale.getDefault().language == "es") "EEEE d 'de' MMMM" else "EEEE, MMMM d"
                SimpleDateFormat(pattern, Locale.getDefault()).format(date).replaceFirstChar { it.uppercase() }
            } else dateStr
        } catch (e: Exception) {
            dateStr
        }
    }

    fun getTodayLongDisplay(): String {
        val pattern = if (Locale.getDefault().language == "es") "EEEE d 'de' MMMM 'de' yyyy" else "EEEE, MMMM d, yyyy"
        return SimpleDateFormat(pattern, Locale.getDefault()).format(Date()).replaceFirstChar { it.uppercase() }
    }

    fun isPastDateTime(fecha: String, hora: String): Boolean {
        return try {
            val dateObj = isoDateFormat.parse(fecha) ?: return true
            val timeStr = formatTo24h(hora)
            val timeParts = timeStr.split(":")
            
            if (timeParts.size < 2) return true

            val hourInt = timeParts[0].toIntOrNull() ?: return true
            val minuteInt = timeParts[1].toIntOrNull() ?: return true

            val calendar = Calendar.getInstance()
            calendar.time = dateObj
            calendar[Calendar.HOUR_OF_DAY] = hourInt
            calendar[Calendar.MINUTE] = minuteInt
            calendar[Calendar.SECOND] = 0
            
            calendar.time.before(Date())
        } catch (e: Exception) {
            true
        }
    }

    fun isToday(dateStr: String?): Boolean {
        if (dateStr == null) return false
        val today = isoDateFormat.format(Date())
        return dateStr == today
    }

    fun isTomorrow(dateStr: String?): Boolean {
        if (dateStr == null) return false
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = isoDateFormat.format(calendar.time)
        return dateStr == tomorrow
    }

    fun formatTo24h(time: String): String {
        val date = parseTime(time)
        
        return if (date != null) {
            SimpleDateFormat("HH:mm", Locale.US).format(date)
        } else {
            // Manual fallback for cases like "3:00 PM"
            val cleanTime = time.uppercase().replace("A. M.", "AM").replace("P. M.", "PM")
            val isPM = cleanTime.contains("PM")
            val isAM = cleanTime.contains("AM")
            val digits = cleanTime.filter { (it.isDigit() || it == ':') }.split(":")
            
            if (digits.size >= 2) {
                var h = digits[0].toIntOrNull() ?: 0
                val m = digits[1].take(2).padStart(2, '0')
                if (isPM && h < 12) h += 12
                if (isAM && h == 12) h = 0
                String.format(Locale.US, "%02d:%02d", h, m)
            } else time
        }
    }
}
