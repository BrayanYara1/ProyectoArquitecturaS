package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gestionturnosapp.data.model.MedicationLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationLogDao {
    @Insert
    suspend fun insertLog(log: MedicationLog)

    @Query("SELECT * FROM medication_logs ORDER BY takenAt DESC")
    fun getAllLogs(): Flow<List<MedicationLog>>

    @Query("SELECT * FROM medication_logs WHERE medId = :medId ORDER BY takenAt DESC")
    fun getLogsByMed(medId: String): Flow<List<MedicationLog>>
    
    @Query("DELETE FROM medication_logs WHERE medId = :medId")
    suspend fun deleteLogsByMed(medId: String)
}
