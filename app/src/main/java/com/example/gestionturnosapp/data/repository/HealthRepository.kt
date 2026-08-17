package com.example.gestionturnosapp.data.repository

import com.example.gestionturnosapp.data.local.HealthRecordDao
import com.example.gestionturnosapp.data.model.HealthRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepository @Inject constructor(
    private val healthRecordDao: HealthRecordDao,
    private val symptomDao: com.example.gestionturnosapp.data.local.SymptomDao
) {
    fun getRecordsByType(type: String): Flow<List<HealthRecord>> = 
        healthRecordDao.getRecordsByType(type)

    suspend fun insertRecord(record: HealthRecord) = 
        healthRecordDao.insertRecord(record)

    suspend fun deleteRecord(id: Long) = 
        healthRecordDao.deleteRecord(id)

    fun getAllSymptoms() = symptomDao.getAllSymptoms()
    suspend fun insertSymptom(record: com.example.gestionturnosapp.data.model.SymptomRecord) = symptomDao.insertSymptom(record)
    suspend fun deleteSymptom(id: Long) = symptomDao.deleteSymptom(id)
}
