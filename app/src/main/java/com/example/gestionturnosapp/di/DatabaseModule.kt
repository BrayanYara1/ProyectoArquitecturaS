package com.example.gestionturnosapp.di

import android.content.Context
import androidx.room.Room
import com.example.gestionturnosapp.data.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "salud_activa_production_v12"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideTurnoDao(database: AppDatabase): TurnoDao = database.turnoDao()

    @Provides
    fun provideMedicamentoDao(database: AppDatabase): MedicamentoDao = database.medicamentoDao()

    @Provides
    fun provideEstudioDao(database: AppDatabase): EstudioDao = database.estudioDao()
    
    @Provides
    fun provideMensajeDao(database: AppDatabase): MensajeDao = database.mensajeDao()

    @Provides
    fun provideHealthRecordDao(database: AppDatabase): HealthRecordDao = database.healthRecordDao()

    @Provides
    fun provideAchievementDao(database: AppDatabase): AchievementDao = database.achievementDao()

    @Provides
    fun provideMedicationLogDao(database: AppDatabase): MedicationLogDao = database.medicationLogDao()

    @Provides
    fun provideSymptomDao(database: AppDatabase): SymptomDao = database.symptomDao()
}
