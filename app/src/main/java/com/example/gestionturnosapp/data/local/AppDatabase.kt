package com.example.gestionturnosapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gestionturnosapp.data.model.*

@Database(entities = [Turno::class, Medicamento::class, EstudioMedico::class, Mensaje::class, HealthRecord::class, Achievement::class, MedicationLog::class, SymptomRecord::class], version = 8, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun turnoDao(): TurnoDao
    abstract fun medicamentoDao(): MedicamentoDao
    abstract fun estudioDao(): EstudioDao
    abstract fun mensajeDao(): MensajeDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun achievementDao(): AchievementDao
    abstract fun medicationLogDao(): MedicationLogDao
    abstract fun symptomDao(): SymptomDao

    companion object {
        const val DB_NAME = "gestion_turnos_db_secure"
    }
}
