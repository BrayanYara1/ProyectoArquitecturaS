package com.example.gestionturnosapp.util

import android.content.Context
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.HealthRecord
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.model.Turno
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SmartAssistant @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun generateResponse(
        query: String, 
        turnos: List<Turno>, 
        meds: List<Medicamento>,
        records: List<HealthRecord> = emptyList(),
        symptoms: List<com.example.gestionturnosapp.data.model.SymptomRecord> = emptyList()
    ): String {
        val q = query.lowercase().trim()

        return when {
            q.isEmpty() -> context.getString(R.string.ai_welcome)
            
            q.contains("hola") || q.contains("buenos días") || q.contains("buenas tardes") || q.contains("buenas noches") -> 
                context.getString(R.string.ai_welcome)

            q.contains("cita") || q.contains("turno") || q.contains("cuando") || q.contains("cuándo") || q.contains("agenda") -> {
                val next = turnos.asSequence()
                    .filter { it.estado.lowercase() in listOf("pendiente", "pending") }
                    .minByOrNull { "${it.fecha} ${it.hora}" }

                if (next != null) {
                    val displayDate = DateUtils.formatDisplayDate(context, next.fecha)
                    val displayTime = DateUtils.formatDisplayTime(next.hora)
                    "Tu próxima cita es de ${next.especialidad ?: "Consulta"} con el ${next.doctor ?: "Doctor"} para el día $displayDate a las $displayTime."
                } else {
                    context.getString(R.string.ai_no_appointments)
                }
            }

            q.contains("medicamento") || q.contains("pastilla") || q.contains("toma") || q.contains("remedio") -> {
                if (meds.isNotEmpty()) {
                    val medList = meds.joinToString("\n") { "- ${it.nombre} (${it.dosis}): ${it.frecuencia}" }
                    context.getString(R.string.ai_meds_list, medList)
                } else {
                    context.getString(R.string.ai_no_meds)
                }
            }

            // --- SALUD Y SIGNOS VITALES ---
            q.contains("como estoy") || q.contains("cómo estoy") || q.contains("mi salud") || q.contains("progreso") || q.contains("estado") -> {
                generateHealthSummaryAdvice(records, symptoms)
            }

            q.contains("presión") || q.contains("tensión") -> {
                val lastBP = records.filter { it.type == "BLOOD_PRESSURE" }.lastOrNull()
                if (lastBP != null) {
                    val sys = lastBP.value.toInt()
                    val dia = lastBP.valueSecondary?.toInt() ?: 0
                    val status = when {
                        sys > 140 || dia > 90 -> "está un poco alta. Evita la sal y descansa."
                        sys < 90 || dia < 60 -> "está baja. Asegúrate de estar bien hidratado."
                        else -> "está en rangos excelentes. ¡Sigue así!"
                    }
                    "Tu última presión registrada fue $sys/$dia mmHg. $status Recuerda que el control diario es clave para tu bienestar."
                } else {
                    "No tengo registros de tu presión arterial. ¿Te gustaría que te recuerde registrarla mañana por la mañana?"
                }
            }

            q.contains("dieta") || q.contains("comer") || q.contains("alimento") -> {
                "Una dieta balanceada es fundamental. Te recomiendo priorizar vegetales verdes, proteínas magras y granos integrales. ¿Tienes alguna condición como diabetes o hipertensión que deba considerar?"
            }

            q.contains("ejercicio") || q.contains("deporte") || q.contains("entrenar") -> {
                "¡Excelente iniciativa! Caminar 30 minutos al día reduce el riesgo cardiovascular en un 30%. Basado en tu Health Score actual, estás en buen camino para metas más exigentes."
            }

            q.contains("glucosa") || q.contains("azúcar") -> {
                val lastG = records.filter { it.type == "GLUCOSE" }.lastOrNull()
                if (lastG != null) {
                    val valG = lastG.value.toInt()
                    val advice = when {
                        valG > 180 -> "está elevada. Si no has comido recientemente, consulta con tu médico."
                        valG < 70 -> "está baja (hipoglucemia). Consume algo de azúcar rápido y descansa."
                        else -> "está en un rango saludable."
                    }
                    "Tu nivel de glucosa más reciente es de $valG mg/dL, lo cual $advice"
                } else {
                    "Aún no has registrado niveles de glucosa. Es importante hacerlo si llevas un control de diabetes."
                }
            }

            q.contains("peso") || q.contains("gordo") || q.contains("flaco") || q.contains("adelgazar") -> {
                val weights = records.filter { it.type == "WEIGHT" }
                if (weights.size >= 2) {
                    val diff = weights.last().value - weights[weights.size - 2].value
                    val trend = if (diff > 0) "has subido ${String.format(java.util.Locale.US, "%.1f", diff)} kg" else "has bajado ${String.format(java.util.Locale.US, "%.1f", Math.abs(diff))} kg"
                    "Desde tu registro anterior $trend. Tu peso actual es ${weights.last().value} kg. ¡Sigue con tus hábitos saludables!"
                } else if (weights.isNotEmpty()) {
                    "Tu peso actual es de ${weights.last().value} kg. Registra tu peso semanalmente para ver tu progreso."
                } else {
                    "No tengo registros de tu peso. Agregarlos te ayudará a ver tu evolución física."
                }
            }

            // --- TRIAGE Y SÍNTOMAS ---
            q.contains("pecho") || q.contains("corazón") || q.contains("infarto") ->
                context.getString(R.string.ai_symptom_chest_pain)
            
            q.contains("piel") || q.contains("mancha") || q.contains("rasquiña") || q.contains("picazón") || q.contains("acné") ->
                context.getString(R.string.ai_symptom_skin)
            
            q.contains("niño") || q.contains("hijo") || q.contains("bebé") || q.contains("vacuna") ->
                context.getString(R.string.ai_symptom_child)
            
            q.contains("hueso") || q.contains("espalda") || q.contains("rodilla") || q.contains("fractura") || q.contains("golpe") ->
                context.getString(R.string.ai_symptom_bones)
            
            q.contains("cabeza") || q.contains("migraña") || q.contains("mareo") || q.contains("convulsión") ->
                context.getString(R.string.ai_symptom_headache)

            q.contains("qué es") || q.contains("que es") || q.contains("significa") || q.contains("que significa") -> {
                explainMedicalTerm(q)
            }

            q.contains("gracias") || q.contains("gracia") || q.contains("ok") || q.contains("listo") -> 
                context.getString(R.string.ai_thanks_reply)
            
            q.contains("ayuda") || q.contains("asistencia") ->
                context.getString(R.string.ai_welcome)

            else -> context.getString(R.string.ai_generic_recommendation)
        }
    }

    private fun generateHealthSummaryAdvice(
        records: List<HealthRecord>,
        symptoms: List<com.example.gestionturnosapp.data.model.SymptomRecord> = emptyList()
    ): String {
        if (records.isEmpty() && symptoms.isEmpty()) return "Aún no tengo datos de salud para analizar. Comienza registrando tu peso, presión o síntomas."
        
        val lastBP = records.filter { it.type == "BLOOD_PRESSURE" }.lastOrNull()
        val lastG = records.filter { it.type == "GLUCOSE" }.lastOrNull()
        val lastWeight = records.filter { it.type == "WEIGHT" }.lastOrNull()
        val lastSymptom = symptoms.lastOrNull()
        
        val summary = StringBuilder("Resumen de tu estado actual:\n")
        
        lastBP?.let {
            val status = if (it.value > 135 || (it.valueSecondary ?: 0f) > 85) "un poco elevada" else "estable"
            summary.append("- Presión: $status.\n")
        }
        
        lastG?.let {
            val status = if (it.value > 140) "elevada" else "controlada"
            summary.append("- Glucosa: $status.\n")
        }

        lastWeight?.let {
            val h = com.example.gestionturnosapp.data.local.PreferenceManager(context).getHeight()
            if (h > 0) {
                val bmi = it.value / (h * h)
                val bmiStatus = when {
                    bmi < 18.5 -> "bajo peso"
                    bmi < 25 -> "un peso saludable"
                    bmi < 30 -> "sobrepeso"
                    else -> "obesidad"
                }
                summary.append("- IMC: Indica que estás en $bmiStatus (${String.format(java.util.Locale.US, "%.1f", bmi)}).\n")
            }
        }

        lastSymptom?.let {
            summary.append("- Último malestar: ${it.description} (Intensidad ${it.intensity}/10).\n")
            if (it.intensity > 7) summary.append("⚠️ Nota: Tu último síntoma reportado es fuerte. Considera consultar a un especialista.\n")
        }
        
        summary.append("\nSugerencia: Mantén tu registro diario para un mejor seguimiento. ¿Quieres que busque un especialista para ti?")
        return summary.toString()
    }

    private fun explainMedicalTerm(query: String): String {
        return when {
            query.contains("ecografía") || query.contains("eco") -> context.getString(R.string.ai_term_ecografia)
            query.contains("ayunas") || query.contains("ayuna") -> context.getString(R.string.ai_term_ayunas)
            query.contains("presión") || query.contains("tensión") || query.contains("hipertensión") -> context.getString(R.string.ai_term_presion)
            query.contains("diabetes") || query.contains("glucosa") || query.contains("azúcar") -> context.getString(R.string.ai_term_diabetes)
            query.contains("ayuda") -> context.getString(R.string.ai_welcome)
            else -> context.getString(R.string.ai_term_generic)
        }
    }
}
