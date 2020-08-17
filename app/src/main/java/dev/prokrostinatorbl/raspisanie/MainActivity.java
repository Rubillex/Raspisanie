package dev.prokrostinatorbl.raspisanie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.CoreComponentFactory;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;


import android.content.Intent;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.*;
import java.util.*;


// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static Handler h;
    private static String theme;
    private Toolbar toolbar;


    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема
    public static Boolean firstrun;

    SharedPreferences mSettings;
    public ArrayList<String> number;
    private LinearLayout relativeLayout;
    private int USERID = 0;
    private int countID;

    private FirebaseAuth mAuth;

    public Throwable ex;
    private List<View> allEds;



    private static final int INTERNET_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;


    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[] { permission },
                            requestCode);
        }
    }

    private void Cheker(){
        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.INTERNET,
                INTERNET_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.ACCESS_NETWORK_STATE,
                INTERNET_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.MANAGE_DOCUMENTS,
                INTERNET_PERMISSION_CODE);
    }


    public String destFileName = "list.txt";

    private FirebaseFunctions mFunctions;


    @Override
    protected void onResume() {
        super.onResume();

        Saved.init(getApplicationContext());
        new Saved().load_main();

        if (firstrun) {
            // При первом запуске (или если юзер удалял все данные приложения)
            // мы попадаем сюда. Делаем что-то
            firstrun = false;
            new Saved().save_main();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            //и после действия записывам false в переменную firstrun
            //Итого при следующих запусках этот код не вызывается.
//            mSettings.edit().putBoolean("firstrun", false).commit();

        }
    }

    @Override
        protected void onRestart(){
        super.onRestart();

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        Saved.init(getApplicationContext());
        new Saved().load_main();

//        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

//        if(mSettings.contains(APP_PREFERENCES_THEME)) {
//
//            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");
//
//
//            if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
//                mCounter = "auto";
//            }
//
//
//            switch(mCounter){
//                case "white":
//                    Log.i("Theme", mCounter );
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                        setTheme(R.style.Light_statusbar);
//                    } else {
//                        setTheme(R.style.Light);
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    }
//                    break;
//                case "black":
//                    Log.i("Theme", mCounter );
//                    setTheme(R.style.Dark);
//                    break;
//                case "pink":
//                    break;
//                case "auto":
//                    Log.i("Theme", mCounter );
//                    switch (currentNightMode) {
//                        case Configuration.UI_MODE_NIGHT_NO:
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                                setTheme(R.style.Light_statusbar);
//                            } else {
//                                setTheme(R.style.Light);
//                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                            }
//                            break;
//                        case Configuration.UI_MODE_NIGHT_YES:
//                            setTheme(R.style.Dark);
//                            break;
//                        default:
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                                setTheme(R.style.Light_statusbar);
//                            } else {
//                                setTheme(R.style.Light);
//                                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                            }
//                            break;
//                        // We don't know what mode we're in, assume notnight
//                    }
//                    break;
//                default:
//                    Log.i("Theme", mCounter );
//                    break;
//            }
//        }
        switch(APP_PREFERENCES_THEME){
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

        setContentView(R.layout.activity_main);


            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите институт");

            findViewById(R.id.back_arrow).setVisibility(View.GONE);


            Button setButton = (Button) findViewById(R.id.setting_button);
            setButton.setOnClickListener(this);


        countID = 0;
        USERID = 0;

//        Cheker();

        try {
            Creator();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // ждём окончание выполнения Загрузчика

                if (msg.what == 1)
                {
                    try {
                        Creator();
                    }  catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        }





        @SuppressLint("HandlerLeak")
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            Saved.init(getApplicationContext());
            new Saved().load_main();

//            mSettings = getSharedPreferences("dev.prokrostinatorbl.raspisanie", MODE_PRIVATE);

            int currentNightMode = getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
//            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

//            if(mSettings.contains(APP_PREFERENCES_THEME)) {
//
//                String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");
//
//                if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
//                    mCounter = "auto";
//                }
//
//                switch(mCounter){
//                    case "white":
//                        Log.i("Theme", mCounter );
//
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                            setTheme(R.style.Light_statusbar);
//                        } else {
//                            setTheme(R.style.Light);
//                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                        }
//                        break;
//                    case "black":
//                        Log.i("Theme", mCounter );
//                        setTheme(R.style.Dark);
//                        break;
//                    case "pink":
//                        break;
//                    case "auto":
//                        Log.i("Theme", mCounter );
//                        switch (currentNightMode) {
//                            case Configuration.UI_MODE_NIGHT_NO:
//                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                                    setTheme(R.style.Light_statusbar);
//                                } else {
//                                    setTheme(R.style.Light);
//                                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                                }
//                                break;
//                            case Configuration.UI_MODE_NIGHT_YES:
//                                setTheme(R.style.Dark);
//                                break;
//                            default:
//                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                                    setTheme(R.style.Light_statusbar);
//                                } else {
//                                    setTheme(R.style.Light);
//                                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                                }
//                                break;
//                            // We don't know what mode we're in, assume notnight
//                        }
//                        break;
//                        default:
//                            Log.i("Theme", mCounter);
//                            break;
//                }
//            }

            switch(APP_PREFERENCES_THEME){
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

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            Cheker();



            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите институт");

            findViewById(R.id.back_arrow).setVisibility(View.GONE);


            Button setButton = (Button) findViewById(R.id.setting_button);
            setButton.setOnClickListener(this);

            FirebaseApp.initializeApp(this);
            FirebaseInstanceId.getInstance().getToken();

            mFunctions = FirebaseFunctions.getInstance();




            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {

                    ex = ex;
                    handleUncaughtException(thread, ex);
                }
            });



            String src_file = "https://raw.githubusercontent.com/Rubillex/Raspisanie/master/timetable.txt";

            final Context c = getApplicationContext();
            File file = new File(c.getFilesDir(), "/files");
            String from = "MainActivity";

            Intent intent = getIntent();
            final String back = intent.getStringExtra("back");
            String toggle = back;
//            Toast.makeText(this, toggle, Toast.LENGTH_SHORT).show();

            if (!toggle.equals("true")){
                Downloader.Download(src_file, file, destFileName, from);
            } else {
                Downloader.toggle = toggle;
                Downloader.Download(src_file, file, destFileName, from);
            }

            h = new Handler() {
                @SuppressLint("ResourceType")
                public void handleMessage(Message msg) {
                    // ждём окончание выполнения Загрузчика

                    if (msg.what == 1)
                    {
                        try {
                            Creator();
                        }  catch (FileNotFoundException e) {
                            e.printStackTrace();

                            Boolean butt = false;
                            Log.i("AAA", "НЕ СКАЧАЛ");

                            relativeLayout = (LinearLayout) findViewById(R.id.linearLayout);
                            String Logs = "отправить логи автору приложения";



                            if (!butt){
                                Button b = new Button(getApplicationContext());
                                b.setText(Logs);
                                LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 25, 0, 0);
                                b.setLayoutParams(params);
                                b.setBackgroundColor(Color.TRANSPARENT);
                                b.setId(USERID + countID);
                                b.setTag("Loger");

                                b.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);


                                int currentNightMode = getResources().getConfiguration().uiMode
                                        & Configuration.UI_MODE_NIGHT_MASK;

//                                mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//
//                                if(mSettings.contains(APP_PREFERENCES_THEME)) {
//
//                                    String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");
//
//                                    if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
//                                        mCounter = "auto";
//                                    }
//
//                                    switch(mCounter){
//                                        case "white":
//                                            b.setTextColor(getResources().getColor(R.color.primaryText_light));
//                                            break;
//                                        case "black":
//                                            b.setTextColor(getResources().getColor(R.color.primaryText));
//                                            break;
//                                        case "pink":
//                                            break;
//                                        case "auto":
//                                            switch (currentNightMode) {
//                                                case Configuration.UI_MODE_NIGHT_NO:
//                                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                                                        b.setTextColor(getResources().getColor(R.color.primaryText_light));
//                                                    } else {
//                                                        b.setTextColor(getResources().getColor(R.color.primaryText_light));
//                                                    }
//                                                    break;
//                                                case Configuration.UI_MODE_NIGHT_YES:
//                                                    b.setTextColor(getResources().getColor(R.color.primaryText));
//                                                    break;
//                                                default:
//                                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                                                        b.setTextColor(getResources().getColor(R.color.primaryText_light));
//                                                    } else {
//                                                        b.setTextColor(getResources().getColor(R.color.primaryText_light));
//                                                    }
//                                                    break;
//                                                // We don't know what mode we're in, assume notnight
//                                            }
//                                            break;
//                                    }
//                                }

                                switch(APP_PREFERENCES_THEME){
                                    case "white":
                                        b.setTextColor(getResources().getColor(R.color.primaryText_light));
                                        break;
                                    case "black":
                                        b.setTextColor(getResources().getColor(R.color.primaryText));
                                        break;
                                    case "pink":
                                        break;
                                    case "auto":
                                        switch (currentNightMode) {
                                            case Configuration.UI_MODE_NIGHT_NO:
                                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                                    b.setTextColor(getResources().getColor(R.color.primaryText_light));
                                                } else {
                                                    b.setTextColor(getResources().getColor(R.color.primaryText_light));
                                                }
                                                break;
                                            case Configuration.UI_MODE_NIGHT_YES:
                                                b.setTextColor(getResources().getColor(R.color.primaryText));
                                                break;
                                            default:
                                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                                    b.setTextColor(getResources().getColor(R.color.primaryText_light));
                                                } else {
                                                    b.setTextColor(getResources().getColor(R.color.primaryText_light));
                                                }
                                                break;
                                            // We don't know what mode we're in, assume notnight
                                        }
                                        break;
                                }

                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int resourceID = (int) v.getTag();
                                        Intent intent = new Intent(MainActivity.this, group_list.class);
                                        intent.putExtra("key", number.get(resourceID));
                                        Log.i("INSTIT_NAME", number.get(resourceID));
                                        startActivity(intent);

                                        String resourceTAG = (String) v.getTag();

                                        if(resourceTAG.equals("Loger")){
                                            Throwable e = ex;
                                            String stackTrace = Log.getStackTraceString(e);
                                            String message = e.getMessage();
                                            Intent mail = new Intent (Intent.ACTION_SEND);
                                            mail.setType("message/rfc822");
                                            mail.putExtra (Intent.EXTRA_EMAIL, new String[] {"angoran16@gmail.com"});
                                            mail.putExtra (Intent.EXTRA_SUBJECT, "Downloader crash");
                                            mail.putExtra (Intent.EXTRA_TEXT, stackTrace);
                                            mail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
                                            startActivity(mail);
                                        }

                                    }
                                });
                                relativeLayout.addView(b);
                                countID++;
                                butt = true;
                            }
                        }
                    }
                }
            };

    }




    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.setting_button:
                Intent setting = new Intent(MainActivity.this, Setting.class);
                startActivity(setting);
                break;
        }

    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Приложение крашнулось! Выберите способ отправки логов", Toast.LENGTH_SHORT);
        toast.show();
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"gubchenko.vadim@gmail.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "MyApp Crash log file");
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
    }


    public void Creator() throws FileNotFoundException {

        Context c = getApplicationContext();
        File file = new File(c.getFilesDir(), "/files");

        File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

        File dest = new File(file + destFileName);
        final Scanner in = new Scanner(dest);
        Log.i("!!!!!!!!!!!!", String.valueOf(dest));
        Log.i("!!!!!!!!!!!!", "CREATOR");

        String s;

        number = new ArrayList<String>();

//        relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);

        allEds = new ArrayList<View>();

        String buffer_name = "buffer";

        Integer size = 0;

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearLayout);

        while(in.hasNextLine()){

            s = in.nextLine();
            String words[] = s.split(";");
            String word = words[0];

            if (!word.equals(buffer_name)){

                    final View view = getLayoutInflater().inflate(R.layout.fragment_institut, null);

                    Button institut = (Button) view.findViewById(R.id.institut_text);

                    number.add(word);
                    TextView b = new TextView(getApplicationContext());
                    institut.setText(word);

                    institut.setId(USERID + countID);
                    institut.setTag(USERID + countID);


                    int currentNightMode = getResources().getConfiguration().uiMode
                            & Configuration.UI_MODE_NIGHT_MASK;

                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

                    institut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int resourceID = (int) v.getTag();
                            Intent intent = new Intent(MainActivity.this, group_list.class);
                            intent.putExtra("key", number.get(resourceID));
                            Log.i("INSTIT_NAME", number.get(resourceID));
                            startActivity(intent);
                        }
                    });
                    countID++;
                    buffer_name = word;
                    allEds.add(view);
                    linearlayout.addView(view);



                }

        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

    }
}