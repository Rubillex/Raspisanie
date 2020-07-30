package dev.prokrostinatorbl.raspisanie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends Activity {

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    SharedPreferences mSettings;

    RadioGroup radioTheme;

    private Toolbar toolbar;
    Dialog theme_swith;

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

        String versionName = BuildConfig.VERSION_NAME;

        TextView version = (TextView)findViewById(R.id.info_sub);
        version.setText(versionName);


        theme_swith = new Dialog(this);
        theme_swith.requestWindowFeature(Window.FEATURE_NO_TITLE);
        theme_swith.setContentView(R.layout.theme_swich);
        theme_swith.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView current_theme = (TextView)findViewById(R.id.current_theme);




        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Настройки");

        RadioButton whiteRadioButton = (RadioButton)theme_swith.findViewById(R.id.radio_white);
        whiteRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton blackRadioButton = (RadioButton)theme_swith.findViewById(R.id.radio_black);
        blackRadioButton.setOnClickListener(radioButtonClickListener);

        RadioButton autoRadioButton = (RadioButton)theme_swith.findViewById(R.id.radio_auto);
        autoRadioButton.setOnClickListener(radioButtonClickListener);



        RelativeLayout them = (RelativeLayout) findViewById(R.id.Theme_swicher);
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme_swith.show();
            }
        });

        RelativeLayout info = (RelativeLayout)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent app_info = new Intent(Setting.this, app_info.class);
                startActivity(app_info);
            }
        });


        if(mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");



            if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
                mCounter = "auto";
            }

            switch(mCounter){
                case "white":
                    Log.i("!!!!", mCounter);
                    whiteRadioButton.setChecked(true);
                    current_theme.setText("Текущая тема: светлая");
                    break;
                case "black":
                    Log.i("!!!!", mCounter);
                    blackRadioButton.setChecked(true);
                    current_theme.setText("Текущая тема: тёмная");
                    break;
                case "auto":
                    Log.i("!!!!", mCounter);
                    autoRadioButton.setChecked(true);
                    current_theme.setText("Текущая тема: авто");
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
                    theme_swith.dismiss();
                    editor.putString(APP_PREFERENCES_THEME, "white");
                    break;
                case R.id.radio_black:
                    theme_swith.dismiss();
                    editor.putString(APP_PREFERENCES_THEME, "black");
                    break;
                case R.id.radio_auto:
                    theme_swith.dismiss();
                    editor.putString(APP_PREFERENCES_THEME, "auto");
                    break;
//                case R.id.radio_pink:
//                    editor.putString(APP_PREFERENCES_THEME, "pink");
//                    break;

                default:
                    break;
            }
            editor.apply();
//            Intent i = getBaseContext().getPackageManager()
//                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(i);
            recreate();
        }
    };

}
