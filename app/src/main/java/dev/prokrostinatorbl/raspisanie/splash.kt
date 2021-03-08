package dev.prokrostinatorbl.raspisanie

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.PREFS_NAME
import dev.prokrostinatorbl.raspisanie.new_version.help_functions.Saver
import dev.prokrostinatorbl.raspisanie.new_version.parrent

class splash : AppCompatActivity() {

    private val sharedPrefs by lazy {  getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    private var mSettingMap: MutableMap<String, String> = HashMap<String, String>()

    private val SPLASH_DISPLAY_LENGTH = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())
        setContentView(R.layout.activity_splash)

        Saver.init(context = applicationContext)

        mSettingMap.putAll(Saver.load_group())

        Handler().postDelayed({ starter() }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    fun starter() {
        val signInAccount = GoogleSignIn.getLastSignedInAccount(this)

        /**
         *
         *
         * ДЛЯ GOOGLE PLAY !=
         *
         * ДЛЯ СЕБЯ ==
         *
         *
         */

        if (signInAccount == null){
            if (mSettingMap["start_frame"] == "main"){
                val intent = Intent(this, parrent::class.java)
                intent.putExtra("frame", "main")
                startActivity(intent)
            } else {
                val intent = Intent(this, parrent::class.java)
                intent.putExtra("frame", "timetable")
                startActivity(intent)
            }
        }





//        if(signInAccount != null){
//            if(APP_PREFERENCES_STARTFRAME.equals("main")){
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("back", "false");
//                startActivity(intent);
//            } else {
//
//                String start;
//
//
//                DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
//
//                Date date = new Date();
//                Calendar c = Calendar.getInstance();
//                c.setTime(date);
//                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
//                c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
//
//                String weekStart = df.format(c.getTime());
//// we do not need the same day a week after, that why use 6, not 7
//                c.add(Calendar.DAY_OF_MONTH, 6);
//                String weekEnd = df.format(c.getTime());
//
//                start = "?date=" + weekStart + "-" + weekEnd;
//
//
//                Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
//                intent.putExtra("link", APP_PREFERENCES_LINK); //ссылка на расписание
//                intent.putExtra("instit", APP_PREFERENCES_START_UNI); //факультет
//                intent.putExtra("key", APP_PREFERENCES_START_GROUP); //номер группы
//                intent.putExtra("back", "false");
//                intent.putExtra("from", "main");
//                intent.putExtra("startUp", start);
//                intent.putExtra("online", "true");
//                startActivity(intent);
//            }
//
//        } else {
//            Intent intent = new Intent(this, auth.class);
//            startActivity(intent);
//        }
    }

    companion object {
        var APP_PREFERENCES: String? = null
        @JvmField
        var APP_PREFERENCES_THEME // выбранная тема
                : String? = null
        @JvmField
        var APP_PREFERENCES_STARTFRAME: String? = null
        @JvmField
        var APP_PREFERENCES_START_UNI: String? = null
        @JvmField
        var APP_PREFERENCES_START_GROUP: String? = null
        @JvmField
        var APP_PREFERENCES_LINK: String? = null
    }
}

/*
package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import dev.prokrostinatorbl.raspisanie.new_version.parrent;

import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SUNDAY;

public class splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    public static String APP_PREFERENCES_STARTFRAME;
    public static String APP_PREFERENCES_START_UNI;
    public static String APP_PREFERENCES_START_GROUP;
    public static String APP_PREFERENCES_LINK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Saved.init(getApplicationContext());
        new Saved().load_splash();



        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

//        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);





        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                starter();


            }
        }, SPLASH_DISPLAY_LENGTH);


    }

    public void starter() {

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        /**
         *
         *
         * ДЛЯ GOOGLE PLAY !=
         *
         * ДЛЯ СЕБЯ ==
         *
         *
         */

        Intent intent = new Intent(this, parrent.class);
        startActivity(intent);

//        if(signInAccount != null){
//            if(APP_PREFERENCES_STARTFRAME.equals("main")){
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("back", "false");
//                startActivity(intent);
//            } else {
//
//                String start;
//
//
//                DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
//
//                Date date = new Date();
//                Calendar c = Calendar.getInstance();
//                c.setTime(date);
//                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
//                c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);
//
//                String weekStart = df.format(c.getTime());
//// we do not need the same day a week after, that why use 6, not 7
//                c.add(Calendar.DAY_OF_MONTH, 6);
//                String weekEnd = df.format(c.getTime());
//
//                start = "?date=" + weekStart + "-" + weekEnd;
//
//
//                Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
//                intent.putExtra("link", APP_PREFERENCES_LINK); //ссылка на расписание
//                intent.putExtra("instit", APP_PREFERENCES_START_UNI); //факультет
//                intent.putExtra("key", APP_PREFERENCES_START_GROUP); //номер группы
//                intent.putExtra("back", "false");
//                intent.putExtra("from", "main");
//                intent.putExtra("startUp", start);
//                intent.putExtra("online", "true");
//                startActivity(intent);
//            }
//
//        } else {
//            Intent intent = new Intent(this, auth.class);
//            startActivity(intent);
//        }

    }
}
 */