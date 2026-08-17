package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestionturnosapp.data.model.EstudioMedico

@Dao
interface EstudioDao {
    @Query("SELECT * FROM estudios ORDER BY fecha DESC")
    suspend fun getAllEstudios(): List<EstudioMedico>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstudios(estudios: List<EstudioMedico>)

    @Query("DELETE FROM estudios")
    suspend fun deleteAllEstudios()

    @Query("DELETE FROM estudios WHERE id = :estudioId")
    suspend fun deleteById(estudioId: String)

    @androidx.room.Transaction
    suspend fun clearAndInsert(estudios: List<EstudioMedico>) {
        deleteAllEstudios()
        insertEstudios(estudios)
    }
}
