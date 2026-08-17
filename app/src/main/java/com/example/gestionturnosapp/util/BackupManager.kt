package com.example.gestionturnosapp.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.gestionturnosapp.data.local.AppDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun exportDatabase(destinationUri: Uri): Boolean {
        val dbFile = context.getDatabasePath(AppDatabase.DB_NAME)
        if (!dbFile.exists()) return false

        return try {
            context.contentResolver.openOutputStream(destinationUri)?.use { output ->
                FileInputStream(dbFile).use { input ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            Log.e("BackupManager", "Export error", e)
            false
        }
    }

    fun importDatabase(sourceUri: Uri): Boolean {
        val dbFile = context.getDatabasePath(AppDatabase.DB_NAME)
        
        return try {
            // Cerrar la base de datos antes de sobrescribir (idealmente mediante un callback o restart)
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            Log.e("BackupManager", "Import error", e)
            false
        }
    }
}
