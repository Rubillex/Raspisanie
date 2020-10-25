package dev.prokrostinatorbl.raspisanie;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import java.util.Map;


public class Saved extends AppCompatActivity {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    public static void init(Context context) {
        if (preferences == null){
            preferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            editor = preferences.edit();
        }
    }



    void load_app_info(){
        app_info.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
    }

    void save_fucktable(){
        editor.putString("link_from", FUCKTABLE.link_from);
    }

    void load_fucktable(){
        FUCKTABLE.link_from = preferences.getString("link_from", "");
        FUCKTABLE.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
        FUCKTABLE.APP_PREFERENCES_PREMIUM = preferences.getString("premium", "false");
    }


    void save_grouplist(){
        editor.putString("group", group_list.APP_PREFERENCES_START_GROUP);
        editor.putString("start_frame", group_list.APP_PREFERENCES_STARTFRAME);
        editor.putString("start_uni", group_list.APP_PREFERENCES_START_UNI);
        editor.putString("link", group_list.APP_PREFERENCES_LINK);
        editor.putBoolean("first", false);
        editor.apply();
    }
    void load_grouplist(){
        group_list.APP_PREFERENCES_FIRST = preferences.getBoolean("first", true);
        group_list.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
        group_list.APP_PREFERENCES_START_GROUP = preferences.getString("group", "standart");
        group_list.APP_PREFERENCES_STARTFRAME = preferences.getString("start_frame", "main");
        group_list.APP_PREFERENCES_START_UNI = preferences.getString("start_uni", "univer");
        group_list.APP_PREFERENCES_LINK = preferences.getString("link", "1");
        group_list.APP_PREFERENCES_PREMIUM = preferences.getString("premium", "false");
    }

    void save_favorite(){
        editor.putString("group", Favorite.APP_PREFERENCES_START_GROUP);
        editor.putString("start_frame", Favorite.APP_PREFERENCES_STARTFRAME);
        editor.putString("start_uni", Favorite.APP_PREFERENCES_START_UNI);
        editor.putString("link", Favorite.APP_PREFERENCES_LINK);
        editor.putBoolean("first", false);
        editor.apply();
    }

    void load_favorite(){
        Favorite.APP_PREFERENCES_FIRST = preferences.getBoolean("first", true);
        Favorite.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
        Favorite.APP_PREFERENCES_START_GROUP = preferences.getString("group", "standart");
        Favorite.APP_PREFERENCES_STARTFRAME = preferences.getString("start_frame", "main");
        Favorite.APP_PREFERENCES_START_UNI = preferences.getString("start_uni", "univer");
        Favorite.APP_PREFERENCES_LINK = preferences.getString("link", "1");
        Favorite.APP_PREFERENCES_PREMIUM = preferences.getString("premium", "false");
    }


    void load_main(){
        MainActivity.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
        MainActivity.firstrun = preferences.getBoolean("firstrun", true);
    }
    void save_main(){
        editor.putString("theme", "auto");
        editor.putBoolean("firstrun", MainActivity.firstrun);
        editor.apply();
    }


    void save_setting(){
        editor.putString("theme", Setting.APP_PREFERENCES_THEME);
        editor.putString("premium", Setting.APP_PREFERENCES_PREMIUM);
        editor.putString("group", "standart");
        editor.putString("start_frame", "main");
        editor.putString("start_uni", "univer");
        editor.putString("link", "1");
        editor.apply();
    }
    void load_setting(){
        Setting.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
        Setting.APP_PREFERENCES_PREMIUM = preferences.getString("premium", "false");
        Setting.APP_PREFERENCES_START_GROUP = preferences.getString("group", "standart");
        Setting.APP_PREFERENCES_STARTFRAME = preferences.getString("start_frame", "main");
        Setting.APP_PREFERENCES_START_UNI = preferences.getString("start_uni", "univer");
        Setting.APP_PREFERENCES_LINK = preferences.getString("link", "1");
    }

    void load_splash(){
        splash.APP_PREFERENCES_START_GROUP = preferences.getString("group", "standart");
        splash.APP_PREFERENCES_STARTFRAME = preferences.getString("start_frame", "main");
        splash.APP_PREFERENCES_START_UNI = preferences.getString("start_uni", "univer");
        splash.APP_PREFERENCES_LINK = preferences.getString("link", "1");
        splash.APP_PREFERENCES_THEME = preferences.getString("theme", "auto");
    }
}
