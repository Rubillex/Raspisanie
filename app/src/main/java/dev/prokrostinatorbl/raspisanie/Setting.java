package dev.prokrostinatorbl.raspisanie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends Activity {

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    SharedPreferences mSettings;

    RadioGroup radioTheme;

    private Toolbar toolbar;


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


        setContentView(R.layout.setting);


        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Выберите тему приложения");

        RadioButton whiteRadioButton = (RadioButton)findViewById(R.id.radio_white);
        whiteRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton blackRadioButton = (RadioButton)findViewById(R.id.radio_black);
        blackRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton autoRadioButton = (RadioButton)findViewById(R.id.radio_auto);
        autoRadioButton.setOnClickListener(radioButtonClickListener);


        if(mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");

            if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
                mCounter = "auto";
            }

            switch(mCounter){
                case "white":
                    Log.i("!!!!", mCounter);
                    whiteRadioButton.setChecked(true);
                    break;
                case "black":
                    Log.i("!!!!", mCounter);
                    blackRadioButton.setChecked(true);
                    break;
                case "auto":
                    Log.i("!!!!", mCounter);
                    autoRadioButton.setChecked(true);
                    break;
            }


        }



    }


    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();

            switch (rb.getId()) {
                case R.id.radio_white:
                    editor.putString(APP_PREFERENCES_THEME, "white");
                    break;
                case R.id.radio_black:
                    editor.putString(APP_PREFERENCES_THEME, "black");
                    break;
                case R.id.radio_auto:
                    editor.putString(APP_PREFERENCES_THEME, "auto");
                    break;
//                case R.id.radio_pink:
//                    editor.putString(APP_PREFERENCES_THEME, "pink");
//                    break;

                default:
                    break;
            }
            editor.apply();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    };

}
