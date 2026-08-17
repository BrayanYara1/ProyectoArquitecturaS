package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_records")
data class HealthRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "WEIGHT", "GLUCOSE", "BLOOD_PRESSURE"
    val value: Float,
    val valueSecondary: Float? = null, // Para presión sistólica/diastólica
    val date: Long = System.currentTimeMillis(),
    val note: String? = null
)
