package com.example.gestionturnosapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gestionturnosapp.data.model.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET progress = progress + 1 WHERE id = :id AND isUnlocked = 0")
    suspend fun incrementProgress(id: String)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :date WHERE id = :id AND progress >= target")
    suspend fun unlockIfReached(id: String, date: Long)
}
