package dev.prokrostinatorbl.raspisanie.new_version.help_functions

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

const val THEME_UNDEFINED = -1
const val THEME_LIGHT = 0
const val THEME_DARK = 1
const val THEME_SYSTEM = 2
const val THEME_BATTERY = 3
const val PREFS_NAME = "theme_prefs"
const val KEY_THEME = "prefs.theme"

const val CHANNEL_1_ID = "channel1"
const val CHANNEL_2_ID = "channel2"

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

        this.createNotificationChannels()
    }



    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "This is channel 1"
            val channel2 = NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            )
            channel1.description = "This is channel 2"
            val manager = this.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }
}