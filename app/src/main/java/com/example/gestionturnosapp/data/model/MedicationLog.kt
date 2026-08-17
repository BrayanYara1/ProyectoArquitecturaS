package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_logs")
data class MedicationLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val medId: String,
    val medName: String,
    val dose: String,
    val takenAt: Long = System.currentTimeMillis()
)
