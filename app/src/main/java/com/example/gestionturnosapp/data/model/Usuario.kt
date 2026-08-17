package com.example.gestionturnosapp.data.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("_id", alternate = ["id"])
    val id: String = "",
    val nombre: String = "Usuario",
    val email: String = "",
    val telefono: String? = null,
    val tipoSanguineo: String? = null,
    val alergias: String? = null,
    val condiciones: String? = null,
    val contactoEmergencia: String? = null
)
