package dev.prokrostinatorbl.raspisanie.new_version.help_functions

import android.R

import android.app.Activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.view.View


object Themer {
    fun onActivityCreateSetTheme(APP_PREFERENCES_THEME: String): String{

        when (APP_PREFERENCES_THEME) {
            "white" -> return "Light"
            "black" -> return "Dark"
            "auto" -> return "auto"
        }
        return "Light"
    }
}