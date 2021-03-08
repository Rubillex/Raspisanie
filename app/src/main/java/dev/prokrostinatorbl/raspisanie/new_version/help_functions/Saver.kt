package dev.prokrostinatorbl.raspisanie.new_version.help_functions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class Saver: AppCompatActivity() {

    companion object {

        private lateinit var editor: SharedPreferences.Editor
        private lateinit var preferences: SharedPreferences

        @SuppressLint("CommitPrefEdits")
        fun init(context: Context){
            preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
            editor = preferences.edit()
        }

        private val premium_default = "false"
        private val theme = "theme"
        private val premium = "premium"

        private val group = "group"
        private val start_uni = "start_uni"
        private val link = "link"
        private val table = "table"
        private val start_frame = "start_frame"
        private val start_link = "start_link"

        private val timetable_1 = "timetable_1"
        private val timetable_2 = "timetable_2"

        private val service = "service"

        fun save_service(bool: String){
            editor.putString(service, bool)
            editor.apply()
        }

        fun load_service(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[service] = preferences.getString(service, "false")!!
            return result
        }

        fun save_timetable_1(table: String){
            editor.putString(timetable_1, table)
            editor.apply()
        }

        fun load_timetable_1(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[timetable_1] = preferences.getString(timetable_1, "-")!!
            return result
        }

        fun save_timetable_2(table: String){
            editor.putString(timetable_2, table)
            editor.apply()
        }

        fun load_timetable_2(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[timetable_2] = preferences.getString(timetable_2, "-")!!
            return result
        }

        fun save_group(_group: String,
                       _start_uni: String,
                       _link: String,
                       _table: String,
                       _start_frame: String,
                       _start_link: String
                       ){
            editor.putString(group, _group)
            editor.putString(start_uni, _start_uni)
            editor.putString(link, _link)
            editor.putString(table, _table)
            editor.putString(start_frame, _start_frame)
            editor.putString(start_link, _start_link)
            editor.apply()
        }

        fun load_group(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[group] = preferences.getString(group, "0")!!
            result[start_uni] = preferences.getString(start_uni, "0")!!
            result[link] = preferences.getString(link, "0")!!
            result[table] = preferences.getString(table, "0")!!
            result[start_frame] = preferences.getString(start_frame, "main")!!
            result[start_link] = preferences.getString(start_link, "0")!!
            return result
        }

        fun load_main(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[theme] = preferences.getString(theme, "auto")!!
            return result
        }

        fun save_main(theme: String){
            editor.putString("theme", theme)
        }

        fun save_setting(theme: String,
                         premium: String,
                         group: String,
                         start_frame: String,
                         start_uni: String,
                         link: String
        ){
            editor.putString("theme", theme)
            editor.putString("premium", premium)
            editor.putString("group", group)
            editor.putString("start_frame", start_frame)
            editor.putString("start_uni", start_uni)
            editor.putString("link", link)
            editor.apply()
        }

        fun load_splash(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[theme] = preferences.getString(theme, "auto")!!
            result[group] = preferences.getString(group, "standart")!!
            result[start_frame] = preferences.getString(start_frame, "main")!!
            result[start_uni] = preferences.getString(start_uni, "univer")!!
            result[link] = preferences.getString(link, "1")!!
            return result
        }

        fun load_setting(): MutableMap<String, String>{
            val result: MutableMap<String, String> = HashMap<String, String>()
            result[theme] = preferences.getString(theme, "auto")!!
            result[premium] = preferences.getString(premium, premium_default)!!
            result[group] = preferences.getString(group, "standart")!!
            result[start_frame] = preferences.getString(start_frame, "main")!!
            result[start_uni] = preferences.getString(start_uni, "univer")!!
            result[link] = preferences.getString(link, "1")!!
            return result
        }


    }
}