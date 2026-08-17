package com.example.gestionturnosapp.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.HealthRecord
import com.example.gestionturnosapp.data.model.Medicamento
import com.example.gestionturnosapp.data.model.Turno
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {

    fun generateHealthReport(
        context: Context,
        userName: String,
        records: List<HealthRecord>,
        meds: List<Medicamento>,
        appointments: List<Turno>
    ): File? {
        val pdfDocument = PdfDocument()
        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
            color = Color.BLACK
        }
        val headerPaint = Paint().apply {
            textSize = 18f
            isFakeBoldText = true
            color = Color.DKGRAY
        }
        val textPaint = Paint().apply {
            textSize = 14f
            color = Color.BLACK
        }

        // Page 1
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        var y = 50f
        canvas.drawText(context.getString(R.string.pdf_title), 50f, y, titlePaint)
        y += 40f
        
        val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText(context.getString(R.string.pdf_generated_at, dateStr), 50f, y, textPaint)
        y += 30f
        
        canvas.drawText("${context.getString(R.string.pdf_patient)} $userName", 50f, y, textPaint)
        y += 50f

        // Medicamentos
        canvas.drawText(context.getString(R.string.pdf_meds_title), 50f, y, headerPaint)
        y += 25f
        if (meds.isEmpty()) {
            canvas.drawText("- ${context.getString(R.string.pdf_no_meds)}", 60f, y, textPaint)
            y += 20f
        } else {
            meds.forEach { med ->
                canvas.drawText("• ${med.nombre} (${med.dosis}) - ${med.frecuencia}", 60f, y, textPaint)
                y += 20f
            }
        }
        y += 30f

        // Turnos
        canvas.drawText(context.getString(R.string.pdf_appointments_title), 50f, y, headerPaint)
        y += 25f
        if (appointments.isEmpty()) {
            canvas.drawText("- ${context.getString(R.string.pdf_no_appointments)}", 60f, y, textPaint)
            y += 20f
        } else {
            appointments.take(5).forEach { turno ->
                canvas.drawText("• ${turno.fecha} ${turno.hora}: ${turno.motivo} (${turno.doctor})", 60f, y, textPaint)
                y += 20f
            }
        }
        y += 40f

        // Stats Summary
        canvas.drawText("Resumen de Signos Vitales", 50f, y, headerPaint)
        y += 25f
        val latestWeight = records.filter { it.type == "WEIGHT" }.lastOrNull()
        val latestGlucose = records.filter { it.type == "GLUCOSE" }.lastOrNull()
        val latestBP = records.filter { it.type == "BLOOD_PRESSURE" }.lastOrNull()

        latestWeight?.let {
            canvas.drawText("Último Peso: ${it.value} kg", 60f, y, textPaint)
            y += 20f
        }
        latestGlucose?.let {
            canvas.drawText("Última Glucosa: ${it.value} mg/dL", 60f, y, textPaint)
            y += 20f
        }
        latestBP?.let {
            val sys = it.value.toInt()
            val dia = it.valueSecondary?.toInt()
            val bpText = if (dia != null) "$sys/$dia mmHg" else "$sys mmHg"
            canvas.drawText("Última Presión: $bpText", 60f, y, textPaint)
            y += 20f
        }

        // Footer
        canvas.drawText(context.getString(R.string.pdf_footer), 50f, 800f, textPaint)

        pdfDocument.finishPage(page)

        // Save file
        val fileName = "SaludActiva_Reporte_${System.currentTimeMillis()}.pdf"
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val filePath = File(directory, fileName)

        try {
            val outputStream = FileOutputStream(filePath)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()
            return filePath
        } catch (e: Exception) {
            e.printStackTrace()
            try { pdfDocument.close() } catch (_: Exception) {}
            return null
        }
    }
}
