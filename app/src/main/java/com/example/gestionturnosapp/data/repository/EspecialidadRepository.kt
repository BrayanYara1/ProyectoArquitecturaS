package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.model.Especialidad
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EspecialidadRepository @Inject constructor() {
    fun getEspecialidades(): List<Especialidad> {
        return listOf(
            Especialidad(1, R.string.name_cardiology, R.string.desc_cardiology, R.drawable.ic_specialty_cardiology),
            Especialidad(2, R.string.name_pediatrics, R.string.desc_pediatrics, R.drawable.ic_specialty_pediatrics),
            Especialidad(3, R.string.name_traumatology, R.string.desc_traumatology, R.drawable.ic_specialty_traumatology),
            Especialidad(4, R.string.name_dermatology, R.string.desc_dermatology, R.drawable.ic_specialty_dermatology),
            Especialidad(5, R.string.name_neurology, R.string.desc_neurology, R.drawable.ic_specialty_neurology)
        )
    }
}
