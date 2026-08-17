package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestionturnosapp.data.model.Medicamento

@Dao
interface MedicamentoDao {
    @Query("SELECT * FROM medicamentos")
    suspend fun getAllMedicamentos(): List<Medicamento>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicamentos(meds: List<Medicamento>)

    @Query("DELETE FROM medicamentos")
    suspend fun deleteAllMedicamentos()

    @Query("DELETE FROM medicamentos WHERE id = :medId")
    suspend fun deleteById(medId: String)

    @androidx.room.Transaction
    suspend fun clearAndInsert(meds: List<Medicamento>) {
        deleteAllMedicamentos()
        insertMedicamentos(meds)
    }
}
