package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "turnos")
data class Turno(
    @PrimaryKey
    @SerializedName("_id", alternate = ["id"])
    val id: String,
    val pacienteNombre: String,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: String = "Pendiente",
    val especialidad: String? = "General",
    val doctor: String? = "Dr. Asignado"
)
