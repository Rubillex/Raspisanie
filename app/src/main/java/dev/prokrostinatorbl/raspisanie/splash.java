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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

        switch (APP_PREFERENCES_THEME) {
            case "white":
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    setTheme(R.style.Light_statusbar);
                } else {
                    setTheme(R.style.Light);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;
            case "black":
                setTheme(R.style.Dark);
                break;
            case "pink":
                break;
            case "auto":
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            setTheme(R.style.Light_statusbar);
                        } else {
                            setTheme(R.style.Light);
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        setTheme(R.style.Dark);
                        break;
                    default:
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            setTheme(R.style.Light_statusbar);
                        } else {
                            setTheme(R.style.Light);
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }
                        break;
                    // We don't know what mode we're in, assume notnight
                }
                break;
        }


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

        if(signInAccount != null){
            if(APP_PREFERENCES_STARTFRAME.equals("main")){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("back", "false");
                startActivity(intent);
            } else {

                String start;


                DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
                c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

                String weekStart = df.format(c.getTime());
// we do not need the same day a week after, that why use 6, not 7
                c.add(Calendar.DAY_OF_MONTH, 6);
                String weekEnd = df.format(c.getTime());

                start = "?date=" + weekStart + "-" + weekEnd;


                Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
                intent.putExtra("link", APP_PREFERENCES_LINK); //ссылка на расписание
                intent.putExtra("instit", APP_PREFERENCES_START_UNI); //факультет
                intent.putExtra("key", APP_PREFERENCES_START_GROUP); //номер группы
                intent.putExtra("back", "false");
                intent.putExtra("from", "main");
                intent.putExtra("startUp", start);
                intent.putExtra("online", "true");
                startActivity(intent);
            }

        } else {
            Intent intent = new Intent(this, auth.class);
            startActivity(intent);
        }

    }
}


