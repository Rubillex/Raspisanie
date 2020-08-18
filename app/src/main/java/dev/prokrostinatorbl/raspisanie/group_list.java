package dev.prokrostinatorbl.raspisanie;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class group_list extends Activity implements View.OnClickListener {

    private Handler h;
    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    public static String APP_PREFERENCES_STARTFRAME;
    public static String APP_PREFERENCES_START_UNI;
    public static String APP_PREFERENCES_START_GROUP;
    public static String APP_PREFERENCES_LINK;
    public static Boolean APP_PREFERENCES_FIRST;

    public static String Snack_text = "";

    private LinearLayout linearLayout;

    private int USERID = 0;
    private int countID;

    private Toolbar toolbar;

    public String institut_name;

    public ArrayList<String> number;
    public ArrayList<String> link_list;

    private List<View> allEds;



    public String destFileName = "list.txt";
    SharedPreferences mSettings;

    Dialog firstrun;

    @Override
    protected void onRestart(){
        super.onRestart();

        Saved.init(getApplicationContext());
        new Saved().load_grouplist();

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




        Intent intent = getIntent();
        final String institut = intent.getStringExtra("key");
        institut_name = institut;
        Log.i("LOGER_AAAAAA", institut_name);
        setContentView(R.layout.group_list);

        //-----------------------------------------------------------------------------------------
        firstrun = new Dialog(this);
        firstrun.requestWindowFeature(Window.FEATURE_NO_TITLE);
        firstrun.setContentView(R.layout.dialog_ingo_group);
        firstrun.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //-----------------------------------------------------------------------------------------

        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText(institut_name);

        //-----------------------------------------------------------------------------------------
        Log.i("FIRST_RUN", String.valueOf(APP_PREFERENCES_FIRST));

        if (APP_PREFERENCES_FIRST){
            firstrun.show();
            new Saved().save_grouplist();
        }
        //-----------------------------------------------------------------------------------------


        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("back", "true");
                startActivity(intent);
            }
        });


        countID = 0;
        USERID = 0;

        try {
            Creator();
        } catch (FileNotFoundException e) {
            Downloader();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("back", "true");
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Saved.init(getApplicationContext());
        new Saved().load_grouplist();

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

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

        setContentView(R.layout.group_list);

        Intent intent = getIntent();
        final String institut = intent.getStringExtra("key");
        institut_name = institut;
        Log.i("LOGER_AAAAAA", institut_name);


        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText(institut_name);


        //-----------------------------------------------------------------------------------------
        firstrun = new Dialog(this);
        firstrun.requestWindowFeature(Window.FEATURE_NO_TITLE);
        firstrun.setContentView(R.layout.dialog_ingo_group);
        firstrun.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //-----------------------------------------------------------------------------------------
        Log.i("FIRST_RUN", String.valueOf(APP_PREFERENCES_FIRST));

        if (APP_PREFERENCES_FIRST){
            firstrun.show();
            new Saved().save_grouplist();
        }
        //-----------------------------------------------------------------------------------------

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("back", "true");
                startActivity(intent);
            }
        });



        countID = 0;
        USERID = 0;

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);
            }
        });


        Downloader();

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


    public void handleUncaughtException (Thread thread, Throwable e)
    {
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

    private void Downloader(){
        String src_file = "https://firebasestorage.googleapis.com/v0/b/timetable-1590248920670.appspot.com/o/list.txt?alt=media&token=710ba120-c1d6-45bc-97b2-bcbd250a0511";

        Context c = getApplicationContext();
        File file = new File(c.getFilesDir(), "/files");

        File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

        File dest = new File(file + destFileName);

        new LoadFile(src_file, dest).start();

    }

    private void onDownloadComplete(boolean success) {
        Log.i("***", "СКАЧАЛ " + success);
    }

    @Override
    public void onClick(View v) {

    }

    private class LoadFile extends Thread {
        private final String src;
        private final File dest;

        LoadFile(String src, File dest) {
            this.src = src;
            this.dest = dest;
        }




        @Override
        public void run() {
            try {
                onDownloadComplete(true);

                Read();
                new Reader().start();


            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Read();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                new Reader().start();
                onDownloadComplete(false);

            }
        }
    }

    private class Reader extends Thread {


        Reader(){
        }

        @Override
        public void run() {
            h.sendEmptyMessage(1);
        }
    }


    public void Read() throws IOException {

    }

    public void Creator() throws FileNotFoundException {

        Context c = getApplicationContext();
        File file = new File(c.getFilesDir(), "/files");

        File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

        File dest = new File(file + destFileName);
        Scanner in = new Scanner(dest);

        String s;

        number = new ArrayList<String>();
        link_list = new ArrayList<String>();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);



        allEds = new ArrayList<View>();

        Log.i("INSTITUT:", institut_name);

        while(in.hasNextLine()){
            s = in.nextLine();
            String words[] = s.split(";");
            String word = words[0];

            if (word.equals(institut_name)) {
                number.add(words[1]);
                Log.i("WORDS SIZE", String.valueOf(words.length));
                Log.i("WORDS 2", words[2]);
                link_list.add(words[2]);
            }
        }
        in.close();

        Double Row_double = Double.valueOf(number.size() / 3);
        long stop = Math.round(Row_double);

        Log.i("STEP", String.valueOf(stop));

        for (int i = 0; i < number.size(); i+=3){

            Log.i("STEP", "&&&&");

            final View view = getLayoutInflater().inflate(R.layout.fragment_group_num, null);


            Button group1 = (Button) view.findViewById(R.id.group1);
            Button group2 = (Button) view.findViewById(R.id.group2);
            Button group3 = (Button) view.findViewById(R.id.group3);

            group1.setText(number.get(i));
            group1.setTag(USERID + countID);

            group1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resourceID = (int) v.getTag();
                    Intent intent = new Intent(group_list.this, FUCKTABLE.class);
                    intent.putExtra("link", link_list.get(resourceID));
                    intent.putExtra("instit", institut_name);
                    intent.putExtra("key", number.get(resourceID));
                    startActivity(intent);
                }
            });

            group1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int resourceID = (int) v.getTag();
                    status_check(resourceID);
                    return true;
                }
            });

            countID++;
            if (i+1 < number.size()){
                group2.setText(number.get(i+1));
                group2.setTag(USERID + countID);

                group2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resourceID = (int) v.getTag();
                        Intent intent = new Intent(group_list.this, FUCKTABLE.class);
                        intent.putExtra("link", link_list.get(resourceID));
                        intent.putExtra("instit", institut_name);
                        intent.putExtra("key", number.get(resourceID));
                        startActivity(intent);
                    }
                });

                group2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int resourceID = (int) v.getTag();
                        status_check(resourceID);
                        return true;
                    }
                });

                countID++;
            } else{
                group2.setVisibility(View.GONE);
            }
            if (i+2 < number.size()){
                group3.setText(number.get(i+2));
                group3.setTag(USERID + countID);

                group3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resourceID = (int) v.getTag();
                        Intent intent = new Intent(group_list.this, FUCKTABLE.class);
                        intent.putExtra("link", link_list.get(resourceID));
                        intent.putExtra("instit", institut_name);
                        intent.putExtra("key", number.get(resourceID));
                        startActivity(intent);
                    }
                });

                group3.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int resourceID = (int) v.getTag();
                        status_check(resourceID);
                        return true;
                    }
                });

                countID++;
            } else{
                group3.setVisibility(View.GONE);
            }
            allEds.add(view);
            linearLayout.addView(view);
        }

    }

    private void status_check(int resourceID) {

        if (APP_PREFERENCES_LINK.equals(link_list.get(resourceID))
        && APP_PREFERENCES_START_UNI.equals(institut_name)
        && APP_PREFERENCES_START_GROUP.equals(number.get(resourceID)))
            {
                Snack_text = "Запуск по умолчанию";
                APP_PREFERENCES_STARTFRAME = "main";
                APP_PREFERENCES_START_GROUP = "standart";
                APP_PREFERENCES_START_UNI = "0";
                APP_PREFERENCES_LINK = "0";
                snackbar(false);
                new Saved().save_grouplist();
            } else {
                APP_PREFERENCES_STARTFRAME = "table";
                APP_PREFERENCES_START_GROUP = number.get(resourceID);
                APP_PREFERENCES_LINK = link_list.get(resourceID);
                APP_PREFERENCES_START_UNI = institut_name;
                new Saved().save_grouplist();
                Snack_text = "Открываю расписание " + APP_PREFERENCES_START_GROUP + " при старте";
                snackbar(true);
        }

        new Saved().load_grouplist();
        Log.i("STARTOVLA", APP_PREFERENCES_STARTFRAME + " " + APP_PREFERENCES_START_GROUP + " " + APP_PREFERENCES_LINK + " " + APP_PREFERENCES_START_UNI);




    }


    private void snackbar(boolean run){

        if (run){
            Snackbar snackbar = Snackbar
                    .make(linearLayout, Snack_text, Snackbar.LENGTH_LONG)
                    .setAction("Отмена", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            APP_PREFERENCES_STARTFRAME = "main";
                            APP_PREFERENCES_START_GROUP = "standart";
                            APP_PREFERENCES_START_UNI = "0";
                            APP_PREFERENCES_LINK = "0";
                            new Saved().save_grouplist();
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
        } else {
            Snackbar.make(
                    linearLayout,
                    Snack_text,
                    Snackbar.LENGTH_LONG
            ).show();
        }

    }

}
