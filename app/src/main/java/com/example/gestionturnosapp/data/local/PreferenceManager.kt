package com.example.gestionturnosapp.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getLocale(): String {
        return prefs.getString(KEY_LOCALE, "es") ?: "es"
    }

    fun setLocale(localeTag: String) {
        prefs.edit { putString(KEY_LOCALE, localeTag) }
        val appLocale: androidx.core.os.LocaleListCompat = androidx.core.os.LocaleListCompat.forLanguageTags(localeTag)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun isDarkMode(): Boolean {
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkMode(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_MODE, enabled) }
        applyTheme(enabled)
    }

    fun applyTheme(enabled: Boolean) {
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS, true)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_NOTIFICATIONS, enabled) }
    }

    fun isBiometricEnabled(): Boolean {
        return prefs.getBoolean(KEY_BIOMETRIC, false)
    }

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_BIOMETRIC, enabled) }
    }

    fun getTrustedContact(): String? {
        return prefs.getString(KEY_TRUSTED_CONTACT, null)
    }

    fun setTrustedContact(phone: String) {
        prefs.edit { putString(KEY_TRUSTED_CONTACT, phone) }
    }

    fun isPrivacyModeEnabled(): Boolean {
        return prefs.getBoolean(KEY_PRIVACY_MODE, false)
    }

    fun setPrivacyMode(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_PRIVACY_MODE, enabled) }
    }

    fun getHeight(): Float {
        return prefs.getFloat(KEY_HEIGHT, 0f)
    }

    fun setHeight(height: Float) {
        prefs.edit { putFloat(KEY_HEIGHT, height) }
    }

    fun isHydrationReminderEnabled(): Boolean {
        return prefs.getBoolean(KEY_HYDRATION_REMINDERS, false)
    }

    fun setHydrationReminderEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_HYDRATION_REMINDERS, enabled) }
    }

    fun getHealthStreak(): Int {
        return prefs.getInt(KEY_HEALTH_STREAK, 0)
    }

    fun updateHealthStreak(newScore: Int) {
        val today = java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US).format(java.util.Date())
        val lastUpdate = prefs.getString(KEY_STREAK_LAST_UPDATE, "")
        
        if (lastUpdate != today && newScore > 80) {
            val currentStreak = getHealthStreak()
            prefs.edit {
                putInt(KEY_HEALTH_STREAK, currentStreak + 1)
                putString(KEY_STREAK_LAST_UPDATE, today)
            }
        }
    }

    companion object {
        private const val PREF_NAME = "app_settings_prefs"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_LOCALE = "app_locale"
        private const val KEY_BIOMETRIC = "biometric_enabled"
        private const val KEY_TRUSTED_CONTACT = "trusted_contact"
        private const val KEY_PRIVACY_MODE = "privacy_mode"
        private const val KEY_HEIGHT = "user_height"
        private const val KEY_HYDRATION_REMINDERS = "hydration_reminders_enabled"
        private const val KEY_HEALTH_STREAK = "health_streak"
        private const val KEY_STREAK_LAST_UPDATE = "streak_last_update"

        fun areNotificationsEnabled(context: Context): Boolean {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_NOTIFICATIONS, true)
        }

        fun isBiometricEnabled(context: Context): Boolean {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_BIOMETRIC, false)
        }
        
        fun isPrivacyModeEnabled(context: Context): Boolean {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_PRIVACY_MODE, false)
        }
    }
}
