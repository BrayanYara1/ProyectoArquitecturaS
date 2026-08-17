package com.example.gestionturnosapp.util

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object MedicineScanner {

    fun scanLabel(context: Context, imageUri: Uri, onResult: (String?, String?) -> Unit) {
        try {
            val image = InputImage.fromFilePath(context, imageUri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Lógica básica para extraer nombre y dosis
                    val lines = visionText.textBlocks.flatMap { it.lines }.map { it.text }
                    
                    var name: String? = null
                    var dose: String? = null

                    // Intentar encontrar la dosis (ej: 500mg, 10ml, 1g)
                    val doseRegex = Regex("(\\d+[,.]?\\d*)\\s*(mg|ml|g|mcg|gr|comprimidos|capsulas)", RegexOption.IGNORE_CASE)
                    
                    val possibleNames = mutableListOf<String>()

                    for (line in lines) {
                        val trimmedLine = line.trim()
                        if (trimmedLine.isEmpty()) continue

                        val match = doseRegex.find(trimmedLine)
                        if (match != null && dose == null) {
                            dose = match.value
                        } else if (trimmedLine.length > 3 && !trimmedLine.any { it.isDigit() }) {
                            // Si no tiene números y es suficientemente larga, podría ser el nombre
                            possibleNames.add(trimmedLine)
                        }
                    }

                    // El nombre suele ser la línea más destacada o una de las primeras
                    name = possibleNames.firstOrNull { it.length > 4 } ?: possibleNames.firstOrNull()

                    val finalName = name?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() } ?: "Desconocido"

                    onResult(finalName, dose ?: "")
                }
                .addOnFailureListener {
                    onResult(null, null)
                }
        } catch (e: Exception) {
            onResult(null, null)
        }
    }
}
