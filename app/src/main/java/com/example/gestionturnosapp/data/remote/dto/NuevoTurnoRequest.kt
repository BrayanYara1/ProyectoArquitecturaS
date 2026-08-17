package com.example.gestionturnosapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NuevoTurnoRequest(
    @SerializedName("pacienteNombre")
    val nombre: String,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val especialidad: String? = "General",
    val doctor: String? = "Dr. Asignado"
)
