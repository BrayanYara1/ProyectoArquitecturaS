package com.example.gestionturnosapp

import android.app.Application
import com.example.gestionturnosapp.notifications.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GestionTurnosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            NotificationHelper.createNotificationChannels(this)
        } catch (t: Throwable) {
            android.util.Log.e("GestionTurnosApp", "Error in onCreate", t)
        }
    }
}
