package dev.prokrostinatorbl.raspisanie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;


public class maps extends Activity implements View.OnClickListener {

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    SharedPreferences mSettings;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");

            if (!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")) {
                mCounter = "auto";
            }

            switch (mCounter) {
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


        setContentView(R.layout.maps);

        Button setButton = (Button) findViewById(R.id.setting_button);
        setButton.setOnClickListener(this);
        Button mapsButton = (Button) findViewById(R.id.maps);
        mapsButton.setOnClickListener(this);
        Button timeButton = (Button) findViewById(R.id.timetable);
        timeButton.setOnClickListener(this);


        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Карта");

        Button asu_k = (Button) findViewById(R.id.asu_k);
        asu_k.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.setting_button:
                Intent setting = new Intent(maps.this, Setting.class);
                startActivity(setting);
                break;
            case R.id.maps:

                break;
            case R.id.timetable:
                Intent timetable = new Intent(maps.this, MainActivity.class);
                startActivity(timetable);
                break;
            case R.id.asu_k:
                Uri gmmIntentUri = Uri.parse("geo:53.342760, 83.771518?q=53.342760, 83.771518");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
