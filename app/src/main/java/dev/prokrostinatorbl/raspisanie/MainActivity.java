package dev.prokrostinatorbl.raspisanie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.CoreComponentFactory;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.*;
import java.util.*;

import static dev.prokrostinatorbl.raspisanie.group_list.Snack_text;


// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    public static Handler h;
    private static String theme;
    private Toolbar toolbar;

    Dialog search_dialog;

    public static Boolean first = true;

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

    public static String search_text = "";


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
            firstrun = false;
            new Saved().save_main();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);


        }
    }

    @Override
        protected void onRestart(){
        super.onRestart();

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        Saved.init(getApplicationContext());
        new Saved().load_main();


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

        search_text = "";
        first = true;


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    boolean online = isHostReachable.isHostReachable("https://www.asu.ru/");

                    if(!online){
                        snackbar();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();




        search_dialog = new Dialog(this);
        search_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        search_dialog.setContentView(R.layout.dialog_search);
        search_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        FloatingActionButton fab = findViewById(R.id.search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });


        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Выберите институт");

        findViewById(R.id.back_arrow).setVisibility(View.GONE);


        countID = 0;
        USERID = 0;


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

            int currentNightMode = getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;

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


            search_text = "";
            first = true;


            FloatingActionButton fab = findViewById(R.id.search);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   search();
                }
            });



            search_dialog = new Dialog(this);
            search_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            search_dialog.setContentView(R.layout.dialog_search);
            search_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        boolean online = isHostReachable.isHostReachable("https://www.asu.ru/");

                        if(!online){
                            snackbar();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            Cheker();



            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите институт");

            findViewById(R.id.back_arrow).setVisibility(View.GONE);

            BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
            setSupportActionBar(bottomAppBar);

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

    private void snackbar() {

        RelativeLayout activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        Snackbar snackbar = Snackbar
                .make(activity_main, "Сайт АГУ не отвечает", Snackbar.LENGTH_LONG);
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        switch (APP_PREFERENCES_THEME) {
            case "white":
                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case "black":
                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent_light));
                break;
            case "auto":
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent_light));
                }
                break;
        }

        snackbar.show();

    }

    private void search() {

        search_dialog.show();

        final EditText search_edit_text = (EditText) search_dialog.findViewById(R.id.search_edit_text);

        search_dialog.findViewById(R.id.outlinedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_text = search_edit_text.getText().toString().toUpperCase();
                search_dialog.dismiss();

                countID = 0;
                USERID = 0;

                LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearLayout);
                linearlayout.removeAllViews();

                try {
                    Creator();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_bar, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.setting_button:
                Intent setting = new Intent(getApplicationContext(), Setting.class);
                startActivity(setting);
                return true;
            case R.id.star:
                Intent favorite = new Intent(getApplicationContext(), Favorite.class);
                startActivity(favorite);
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onClick(View view)
    {


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

        allEds = new ArrayList<View>();

        String buffer_name = "buffer";

        Integer size = 0;

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearLayout);

        while(in.hasNextLine()){

            s = in.nextLine();
            String words[] = s.split(";");
            String word = words[0];


            if (!word.equals(buffer_name)
            && word.startsWith(search_text)){



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


        if (first){
            progressBar.setVisibility(View.GONE);
            first = false;
        }

    }
}