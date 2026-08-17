package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symptom_records")
data class SymptomRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val intensity: Int, // 1 to 10
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
)
