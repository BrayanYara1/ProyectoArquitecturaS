package com.example.gestionturnosapp.data.local

import android.content.Context
import android.net.Uri
import com.example.gestionturnosapp.data.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userManager: UserManager
) {
    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Guarda la imagen físicamente. Usa un prefijo v2 para forzar una nueva carga limpia.
     */
    fun saveProfileImage(userId: String, uri: Uri): String? {
        if (userId.isBlank()) return null
        val cleanId = userId.trim()
        
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "profile_$cleanId.jpg")
            val outputStream = FileOutputStream(file)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            
            val savedPath = file.absolutePath
            prefs.edit()
                .putString(KEY_PROFILE_IMAGE_PREFIX + cleanId, savedPath)
                .apply()
            
            savedPath
        } catch (e: Exception) {
            android.util.Log.e("ImageStorage", "Error saving photo for $cleanId", e)
            null
        }
    }

    fun getProfileImageUri(userId: String?): String? {
        if (userId.isNullOrBlank()) return null
        val cleanId = userId.trim()
        
        val savedPath = prefs.getString(KEY_PROFILE_IMAGE_PREFIX + cleanId, null)
        
        val file = if (savedPath != null) File(savedPath) else File(context.filesDir, "profile_$cleanId.jpg")
        
        return if (file.exists()) {
            Uri.fromFile(file).toString()
        } else {
            null
        }
    }

    fun getProfileImageUri(): String? {
        val user = userManager.getUser()
        return getProfileImageUri(user?.id)
    }

    /**
     * Copia una imagen de estudio médico al almacenamiento interno para persistencia.
     */
    fun saveStudyImage(uri: Uri): String? {
        return try {
            val fileName = "study_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            file.absolutePath
        } catch (e: Exception) {
            android.util.Log.e("ImageStorage", "Error saving study photo", e)
            null
        }
    }

    companion object {
        private const val PREF_NAME = "image_prefs"
        private const val KEY_PROFILE_IMAGE_PREFIX = "profile_path_v2_"
    }
}
