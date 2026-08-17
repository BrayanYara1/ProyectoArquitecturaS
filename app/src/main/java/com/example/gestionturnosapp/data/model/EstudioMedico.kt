package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "estudios")
data class EstudioMedico(
    @PrimaryKey
    @SerializedName("_id", alternate = ["id"])
    val id: String,
    val titulo: String,
    val fecha: String,
    val tipo: String, // ej: "Análisis de Sangre", "Radiografía"
    val resultadoBreve: String,
    val urlDocumento: String? = null,
    val notas: String? = null
)
