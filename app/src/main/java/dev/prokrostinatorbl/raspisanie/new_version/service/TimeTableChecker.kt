package dev.prokrostinatorbl.raspisanie.new_version.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.LoadStudents
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.LoadTeacher
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.Saver
import dev.prokrostinatorbl.raspisanie.new_version.models.TimeTableModel
import java.lang.Math.abs
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


private lateinit var mHandler: Handler
private lateinit var mRunnable: Runnable

val time: Long = 1000 * 60 * 60 //час

class TimeTableChecker : Service() {

    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
//        Toast.makeText(this, "Service started.", Toast.LENGTH_SHORT).show()

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable { check() }
        mHandler.postDelayed(mRunnable, time)

        return START_STICKY
    }

    override fun onDestroy() {
        //mHandler.removeCallbacks(mRunnable)
        super.onDestroy()
//        Toast.makeText(this, "Service destroyed.", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, TimeTableChecker::class.java)
        startService(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        val intent = Intent(this, TimeTableChecker::class.java)
        startService(intent)
    }

    private fun check(){
        val context = this
        Saver.init(context)
        mSettingMap.clear()
        mSettingMap.putAll(Saver.load_group())
        mSettingMap.putAll(Saver.load_timetable_1())
        mSettingMap.putAll(Saver.load_timetable_2())

        val timetable_loaded: ArrayList<TimeTableModel> = ArrayList()

        Async(context, mSettingMap["group"].toString(), mSettingMap["link"].toString(), mSettingMap["start_uni"].toString(), mSettingMap)
    }
}

class Async(val context: Context, val href: String, val name: String, val group: String, val mSettingMap: MutableMap<String, String>): AsyncTask<Void, Void, Void>() {
    init{
        execute()
    }

    fun hasConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNW = cm.activeNetworkInfo
        return if (activeNW != null && activeNW.isConnected) {
            true
        } else false
    }

    override fun doInBackground(vararg params: Void?): Void? {

        if (hasConnection(context)){
            load(context = context, href = href, name = name, group = group)
        } else {
            reload()
        }

        return null
    }

    override fun onPostExecute(res: Void?) {
        super.onPostExecute(res)
        check()
    }

    val mTimeTable: ArrayList<TimeTableModel> = ArrayList()
    val timetable_loaded: ArrayList<TimeTableModel> = ArrayList()

    fun reload(){
        mHandler.postDelayed(mRunnable, time)
    }

    fun load(context: Context, href: String, name: String, group: String){
        if (mSettingMap["table"] == "stud"){
            mTimeTable.clear()
            mTimeTable.addAll(LoadStudents(context = context, href = href, name = name, page = 0, group = group))
            mTimeTable.addAll(LoadStudents(context = context, href = href, name = name, page = 6, group = group))
        } else{
            mTimeTable.clear()
            mTimeTable.addAll(LoadTeacher(context = context, href = href, name = name, page = 0, group = group))
            mTimeTable.addAll(LoadTeacher(context = context, href = href, name = name, page = 6, group = group))
        }
    }
    val NOTIFICATION_ID = 101
    val CHANNEL_ID = "channelID"
    fun check(){

        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<TimeTableModel?>?>() {}.type
        timetable_loaded.clear()
        timetable_loaded.addAll(gson.fromJson(mSettingMap["timetable_1"], type))
        timetable_loaded.addAll(gson.fromJson(mSettingMap["timetable_2"], type))

        mTimeTable.forEach {
            val pos =  mTimeTable.indexOf(it)
//            Log.e("SERVICE_TASKER", "${it.date} ${timetable_loaded[pos].date}")
        }

        var boolean = false

        for (i in 0 until mTimeTable.size) {
            if (mTimeTable[i].date == timetable_loaded[i].date!!
                    &&mTimeTable[i].audit == timetable_loaded[i].audit!!
                    &&mTimeTable[i].name == timetable_loaded[i].name!!
                    &&mTimeTable[i].prepod == timetable_loaded[i].prepod!!
                    &&mTimeTable[i].time == timetable_loaded[i].time!!){
                boolean = false
            } else{

                val date_1 = mTimeTable[i].date.split(" ")
                val date_2 = timetable_loaded[i].date.split(" ")

                if (date_1.size > 0 &&
                        date_2.size > 0){

                    val day_1 = date_1[0].split(".")
                    val day_2 = date_2[0].split(".")

                    if (day_1[0]!=""
                            &&day_2[0]!=""){
                        val first = day_1[0].toInt()
                        val second = day_2[0].toInt()

                        if (kotlin.math.abs(first - second) > 5){
                            boolean = true
                            break
                        }
                    }
                }
            }

        }

//        Toast.makeText(context, "working", Toast.LENGTH_SHORT).show()

        if (boolean){

            val mBuilder = NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_icon_app_new_round)
                    .setContentTitle("Помощник АГУ")
                    .setContentText("Появились изменения в расписании")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            // Obtain NotificationManager system service in order to show the notification

            val targetIntent = Intent(context, TimeTableChecker::class.java)
            val contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            mBuilder.setContentIntent(contentIntent)
            // Obtain NotificationManager system service in order to show the notification
            val notificationId = 1
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "Your_channel_id"
                val channel = NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
                mBuilder.setChannelId(channelId)
            }


            notificationManager.notify(notificationId, mBuilder.build())

        }

        mHandler.postDelayed(mRunnable, time)
    }
}