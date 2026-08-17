package com.example.gestionturnosapp.data.local

import androidx.room.*
import com.example.gestionturnosapp.data.model.Mensaje

@Dao
interface MensajeDao {
    @Query("SELECT * FROM mensajes ORDER BY fecha ASC")
    suspend fun getAllMensajes(): List<Mensaje>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMensajes(mensajes: List<Mensaje>)

    @Query("DELETE FROM mensajes")
    suspend fun deleteAllMensajes()
}
