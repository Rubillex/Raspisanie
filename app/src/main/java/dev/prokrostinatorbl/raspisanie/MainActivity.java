package dev.prokrostinatorbl.raspisanie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
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


    private Document doc;
    private Elements elements;

    private Thread secThread;
    private Runnable runnable;

    public ArrayList name_fac;
    public ArrayList href_fac;

    public static Handler h;
    private static String theme;
    private Toolbar toolbar;

    Dialog search_dialog;

    String file_name = "main";

    public static Boolean first = true;

    public static Boolean find = false;

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

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;

    public static String search_text = " ";

    public String link_asu = "";

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

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        if (activeNW != null && activeNW.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Saved.init(getApplicationContext());
        new Saved().load_main();

        if (firstrun) {

            File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");
            dir.mkdir();

            firstrun = false;
            new Saved().save_main();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            find = false;

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

        search_text = " ";
        first = true;

        find = false;

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



        init();


        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // ждём окончание выполнения Загрузчика

                if (msg.what == 1)
                {
                        Creator();

                }
            }
        };

        }


    @Override
    protected void onStart() {
        super.onStart();
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("MyLog2", "checkForAppUpdateAvailability: something else");
            }
        });
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (mAppUpdateManager != null){
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i("MyLog2", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("MyLog2", "onActivityResult: app download failed");
            }
        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.Coordinator),
                        "Найдено обновление!",
                        Snackbar.LENGTH_INDEFINITE);

        FloatingActionButton fab = findViewById(R.id.search);
        snackbar.setAnchorView(fab);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });

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

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    private void snackbar() {


        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.Coordinator),
                        "Сайт АГУ не отвечает или вы офлайн",
                        Snackbar.LENGTH_LONG);


        FloatingActionButton fab = findViewById(R.id.search);
        BottomAppBar bab = findViewById(R.id.bottomAppBar);
        snackbar.setAnchorView(bab);

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


    @SuppressLint("HandlerLeak")
        @Override
        protected void onCreate(Bundle savedInstanceState) {

            Saved.init(getApplicationContext());
            new Saved().load_main();

            File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

            if (!dir.exists()) {
                dir.mkdirs();
            }

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


//        WhatsNew.newInstance(
//                new WhatsNewItem("Расписание при запуске", "Теперь расписание группы будет открываться при старте приложения.", R.drawable.ic_play)
//        ).presentAutomatically(MainActivity.this);

            search_text = " ";
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


            init();

            h = new Handler() {
                @SuppressLint("ResourceType")
                public void handleMessage(Message msg) {
                    // ждём окончание выполнения Загрузчика

                    if (msg.what == 1)
                    {
                            Creator();
                    }
                }
            };

    }


    private void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb(){
        try {

            link_asu = "https://www.asu.ru/timetable/students/";

            if(hasConnection(this)){

                doc = Jsoup.connect("https://www.asu.ru/timetable/students/").get();

            } else {

                try {
                    final Context c = getApplicationContext();

                    File in = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + file_name + ".html");
                    doc = Jsoup.parse(in, null);
                } catch (IOException e){
                    Toast.makeText(this, "Кэш пуст", Toast.LENGTH_SHORT).show();
                }
            }


                elements = doc.getElementsByClass("link_ptr_left margin_bottom");


                name_fac = new ArrayList<String>();
                href_fac = new ArrayList<String>();

                Element name;
                Element link_full;
                Elements link;

                /**
                 *
                 * КОНСТРУКЦИЯ НИЖЕ ДОСТАЁТ С САЙТА ВСЕ ИМЕНА ФАКУЛЬТЕТОВ И ССЫЛКИ НА ИХ ГРУППЫ
                 * И СКЛАДЫВАЕТ В ArrayList name_fac, href_fac
                 *
                 */

                if (elements.size() != 0) {

                    for (int i = 0; i < elements.size(); i++){
                        name = elements.get(i);

                        String temp = name.text();


                        String name_skobka = temp.substring(temp.indexOf('(') + 1, temp.indexOf(')'));

                        name_fac.add(name_skobka);




                        link_full = elements.get(i);
                        link = link_full.select("div.link_ptr_left.margin_bottom > a");
                        String url = link.attr("href");

                        href_fac.add(url);

//                Log.d("MyLog", String.valueOf(name_fac.get(i)) + "   " + String.valueOf(href_fac.get(i)));

                    }


                    h.sendEmptyMessage(1);


                } else {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.Coordinator),
                                    "Сервер расписания недоступен",
                                    Snackbar.LENGTH_LONG);

                    snackbar.show();
                }


        } catch (IOException e) {
            e.printStackTrace();
        }

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

                find = true;

                Log.d("MyLog2", "тутa");

                Creator();



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
//            case R.id.star:
//                Intent favorite = new Intent(getApplicationContext(), Favorite.class);
//                startActivity(favorite);
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
        String versionName = BuildConfig.VERSION_NAME;
        Toast toast = Toast.makeText(getApplicationContext(),
                "Приложение крашнулось! Выберите способ отправки логов", Toast.LENGTH_SHORT);
        toast.show();
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"gubchenko.vadim@gmail.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "MyApp Crash log file." + " version:" + versionName);
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
    }


    public void Creator(){

        allEds = new ArrayList<View>();

        String word;

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearLayout);

        if (name_fac.size() > 0){

            for (int i = 0; i < name_fac.size(); i++){
                word = String.valueOf(name_fac.get(i));



                final View view = getLayoutInflater().inflate(R.layout.fragment_institut, null);

                Button institut = (Button) view.findViewById(R.id.institut_text);

                institut.setText(word);




                institut.setId(USERID + countID);
                institut.setTag(USERID + countID);

                institut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resourceID = (int) v.getTag();
                        Intent intent = new Intent(MainActivity.this, group_list.class);
                        intent.putExtra("key", String.valueOf(name_fac.get(resourceID)));
                        intent.putExtra("link", String.valueOf(href_fac.get(resourceID)));
                        startActivity(intent);
                    }
                });

                countID++;
                allEds.add(view);
                if (search_text.equals(" ")){
                    linearlayout.addView(view);
                }

                if (word.startsWith(search_text)){
                    linearlayout.addView(view);
                }



            }

            Log.d("MyLog2", String.valueOf(find));


            if (!find){
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            } else {
                find = false;
            }



            if (hasConnection(this)){
                Save_page saver = new Save_page();
                saver.execute();
            }

        } else {
            Toast.makeText(this, "Пожалуйста, перезапустите приложение", Toast.LENGTH_SHORT).show();
        }

    }

    class Save_page extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Save_page();
            return null;
        }
    }

    private void Save_page() {

        final Connection.Response response;
        try {

            final Context c = getApplicationContext();
            File file = new File(c.getFilesDir(), "/files");

            if (file.exists())
            {
                Log.d("A", "exists");
                Log.d("AAAAAA", String.valueOf(file));
            } else {
                Log.d("A", "not exists");
                file.mkdir();
            }

            File dir = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

            if(!dir.exists()){
                dir.mkdir();

                Log.d("MyLog2", "НЕТ ПАПКИ");
            }

            response = Jsoup.connect(link_asu).execute();
            Document doc = response.parse();

            File f = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + file_name + ".html");

            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}