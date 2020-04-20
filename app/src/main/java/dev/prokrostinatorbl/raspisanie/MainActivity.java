package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.CoreComponentFactory;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;


import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;



import android.content.Intent;
import android.widget.Toolbar;

import org.json.*;
import org.w3c.dom.Text;


import java.io.*;
import java.util.*;


import java.util.Iterator;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Iterator;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;


// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private static String theme;
    private Toolbar toolbar;


        @Override
        protected void onRestart(){
        super.onRestart();

            int currentNightMode = getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;

            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        setTheme(R.style.Light_statusbar);
                    } else {
                        setTheme(R.style.Light);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.Dark);
                    break;
                // Night mode is active, we're
                default:
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        setTheme(R.style.Light_statusbar);
                    } else {
                        setTheme(R.style.Light);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    break;
                // We don't know what mode we're in, assume notnight
            }

//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
        setContentView(R.layout.activity_main);
            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите институт");



        }



        @Override
        protected void onCreate(Bundle savedInstanceState) {



            super.onCreate(savedInstanceState);

            int currentNightMode = getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;

            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        setTheme(R.style.Light_statusbar);
                    } else {
                        setTheme(R.style.Light);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.Dark);
                    break;
                // Night mode is active, we're
                default:
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        setTheme(R.style.Light_statusbar);
                    } else {
                        setTheme(R.style.Light);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    break;
                // We don't know what mode we're in, assume notnight
            }



            setContentView(R.layout.activity_main);

            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите институт");

            TextView ictef = (TextView) findViewById(R.id.ictef);
            ictef.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.ictef:
                Intent intent = new Intent(MainActivity.this, Ictef.class);
                startActivity(intent);
        }



    }




}