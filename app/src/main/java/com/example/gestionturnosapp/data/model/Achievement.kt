package com.example.gestionturnosapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val iconResId: Int,
    val isUnlocked: Boolean = false,
    val progress: Int = 0,
    val target: Int = 1,
    val unlockedDate: Long? = null
)
