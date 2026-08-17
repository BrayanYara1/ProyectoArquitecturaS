package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionturnosapp.data.model.SymptomRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomDao {
    @Insert
    suspend fun insertSymptom(record: SymptomRecord)

    @Query("SELECT * FROM symptom_records ORDER BY date DESC")
    fun getAllSymptoms(): Flow<List<SymptomRecord>>

    @Query("DELETE FROM symptom_records WHERE id = :id")
    suspend fun deleteSymptom(id: Long)
}
