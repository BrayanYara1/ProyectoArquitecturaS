package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "medicamentos")
data class Medicamento(
    @PrimaryKey
    @SerializedName("_id", alternate = ["id"])
    val id: String,
    val nombre: String,
    val dosis: String,
    val frecuencia: String, // ej: "Cada 8 horas"
    val proximaToma: String,
    val stockActual: Int = 0,
    val stockMinimo: Int = 5,
    val notas: String? = null
)
