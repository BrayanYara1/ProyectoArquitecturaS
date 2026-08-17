package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "mensajes")
data class Mensaje(
    @PrimaryKey
    @SerializedName("_id") val id: String = "",
    val remitente: String, // "PACIENTE" o "DOCTOR"
    val texto: String,
    val fecha: Date = Date(),
    val leido: Boolean = false
)
