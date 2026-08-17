package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestionturnosapp.data.model.Turno

@Dao
interface TurnoDao {
    @Query("SELECT * FROM turnos ORDER BY fecha ASC, hora ASC")
    suspend fun getAllTurnos(): List<Turno>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTurnos(turnos: List<Turno>)

    @Query("DELETE FROM turnos")
    suspend fun deleteAllTurnos()

    @androidx.room.Transaction
    suspend fun clearAndInsert(turnos: List<Turno>) {
        deleteAllTurnos()
        insertTurnos(turnos)
    }
    
    @Query("DELETE FROM turnos WHERE id = :turnoId")
    suspend fun deleteById(turnoId: String)
}
