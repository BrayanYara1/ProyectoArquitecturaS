package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestionturnosapp.data.model.HealthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE type = :type ORDER BY date ASC")
    fun getRecordsByType(type: String): Flow<List<HealthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: HealthRecord)

    @Query("DELETE FROM health_records WHERE id = :id")
    suspend fun deleteRecord(id: Long)
}
