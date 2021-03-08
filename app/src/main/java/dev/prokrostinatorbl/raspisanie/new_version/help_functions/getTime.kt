package dev.prokrostinatorbl.raspisanie.new_version.help_functions

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getTime(time: Int): String{
    var start: String
    val df: DateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())


    val date = Date()
    val c: Calendar = Calendar.getInstance()
    c.time = date

    Log.e("TIME", time.toString())
    val dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.firstDayOfWeek
    c.add(Calendar.DAY_OF_MONTH, -dayOfWeek)

    c.add(Calendar.DATE, time)

    val weekStart = df.format(c.time)

    c.add(Calendar.DAY_OF_MONTH, 6)
    val weekEnd = df.format(c.time)

    start = "?date=$weekStart-$weekEnd"

    return start
}