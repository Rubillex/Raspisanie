package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class donate extends AppCompatActivity {

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");

            if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
                mCounter = "auto";
            }

            switch(mCounter){
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
        }

        setContentView(R.layout.donate);





    }
}