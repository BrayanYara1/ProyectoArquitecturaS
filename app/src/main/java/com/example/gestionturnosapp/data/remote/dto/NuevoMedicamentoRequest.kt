package com.example.gestionturnosapp.data.remote.dto

data class NuevoMedicamentoRequest(
    val nombre: String,
    val dosis: String,
    val frecuencia: String,
    val proximaToma: String,
    val stockActual: Int = 0,
    val stockMinimo: Int = 5,
    val notas: String? = ""
)
