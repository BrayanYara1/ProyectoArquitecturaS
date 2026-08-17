package com.example.gestionturnosapp.util

import android.content.Context
import com.example.gestionturnosapp.R
import com.example.gestionturnosapp.data.local.AchievementDao
import com.example.gestionturnosapp.data.model.Achievement
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val achievementDao: AchievementDao
) {

    suspend fun initAchievements() {
        val existing = achievementDao.getAllAchievements().first()
        if (existing.isEmpty()) {
            val defaults = listOf(
                Achievement("first_pill", "Primer Paso", "Toma tu primer medicamento registrado", android.R.drawable.ic_menu_agenda, target = 1),
                Achievement("med_streak_3", "Constancia Inicial", "Toma 3 dosis de medicamentos", android.R.drawable.star_on, target = 3),
                Achievement("med_streak_10", "Paciente Ejemplar", "Toma 10 dosis de medicamentos", android.R.drawable.btn_star_big_on, target = 10),
                Achievement("record_health", "Auto-Control", "Registra tus signos vitales por primera vez", android.R.drawable.ic_menu_edit, target = 1),
                Achievement("first_appointment", "Responsable", "Agenda tu primera cita médica", android.R.drawable.ic_menu_today, target = 1)
            )
            defaults.forEach { achievementDao.insertAchievement(it) }
        }
    }

    suspend fun onMedicationTaken() {
        achievementDao.incrementProgress("first_pill")
        achievementDao.incrementProgress("med_streak_3")
        achievementDao.incrementProgress("med_streak_10")
        
        checkUnlocks()
    }

    suspend fun onHealthRecordAdded() {
        achievementDao.incrementProgress("record_health")
        checkUnlocks()
    }

    suspend fun onAppointmentCreated() {
        achievementDao.incrementProgress("first_appointment")
        checkUnlocks()
    }

    private suspend fun checkUnlocks() {
        val date = System.currentTimeMillis()
        val allAchievements = achievementDao.getAllAchievements().first()
        val toUnlock = listOf("first_pill", "med_streak_3", "med_streak_10", "record_health", "first_appointment")
        
        toUnlock.forEach { id ->
            val achievement = allAchievements.find { it.id == id }
            val wasUnlocked = achievement?.isUnlocked ?: false
            
            achievementDao.unlockIfReached(id, date)
            
            // Refrescar para ver si se desbloqueó
            val updatedAchievement = achievementDao.getAllAchievements().first().find { it.id == id }
            val isNowUnlocked = updatedAchievement?.isUnlocked ?: false
            
            if (!wasUnlocked && isNowUnlocked && updatedAchievement != null) {
                notifyUnlock(updatedAchievement)
            }
        }
    }

    private fun notifyUnlock(achievement: Achievement) {
        val mainHandler = android.os.Handler(android.os.Looper.getMainLooper())
        mainHandler.post {
            com.example.gestionturnosapp.notifications.NotificationHelper.showNotification(
                context,
                context.getString(R.string.msg_achievement_unlocked, achievement.title),
                achievement.description,
                com.example.gestionturnosapp.notifications.NotificationHelper.CHANNEL_GENERAL,
                notificationId = achievement.id.hashCode()
            )
        }
    }

    fun getAchievements() = achievementDao.getAllAchievements()
}
