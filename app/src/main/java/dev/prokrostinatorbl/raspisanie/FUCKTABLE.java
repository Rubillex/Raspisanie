package dev.prokrostinatorbl.raspisanie;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.*;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;


import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;


import android.content.Intent;
import android.widget.Toast;
import android.widget.Toolbar;


import java.io.*;
import java.security.acl.Group;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


import androidx.sqlite.db.SupportSQLiteDatabase;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import static dev.prokrostinatorbl.raspisanie.Setting.APP_PREFERENCES_PREMIUM;


public class FUCKTABLE extends Activity {

    private List<View> allEds;

    MyDatabaseHelper dbHelper;

    public String DATE = "";
    public String link_from_start = "";

    public String link_asu_1 = "";
    public String link_asu_2 = "";

    public String group_number = "";

    public int currentNightMode;

    public static Integer NOTE_FRAGMENT_NUMBER = 1;

    public static String from = " ";

    public static String link_from = "";

    public String url_update = "";

    private Document doc;
    private Elements elements;

    private Thread Thread_work;
    private Runnable runnable_work;

    private  Thread Thread_work_2;
    private  Runnable runnable_work_2;

    private Elements elements_day;
    private Elements elements_time;
    private Element base_elements;
    private Elements base_child;

    public static class timetable {
        public String Name;
        public String Value;

        public timetable(String Name, String Value){
            this.Name = Name;
            this.Value = Value;
        }
    }

    public static ArrayList<timetable> timetable_list = new ArrayList<timetable>();

    private final int USERID = 6000;
    private int countID = 1;

    public boolean second = false;

    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема
    public static String APP_PREFERENCES_PREMIUM;

//    SharedPreferences mSettings;

    public static String instit_list;

    public static String link_from_list = "";

    public int temp = 1;


    public JSONArray group_numb;
    public JSONArray prepod_name;
    public JSONArray auditor;
    public JSONArray par_name;
    public JSONArray start_par;
    public JSONArray end_par;
    public JSONArray date_par;
    public JSONArray day_js;
    public JsonArray month_js;
    public JsonArray year_js;


    public ArrayList<String> number;
    public ArrayList<String> prepod;
    public ArrayList<String> location;
    public ArrayList<String> par_names;
    public ArrayList<String> start;
    public ArrayList<String> end;
    public ArrayList<String> date;

    public ArrayList<String> time;


    public String destFileName;
    public String src_file;
    public String json_db_name;


    public Integer day_number;

    public static Handler h;

    Dialog note_dialog;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        Saved.init(getApplicationContext());
        new Saved().load_fucktable();

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


        setContentView(R.layout.activity_fucktable);


        note_dialog = new Dialog(this);
        note_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        note_dialog.setContentView(R.layout.dialog_note);
        note_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.donate_alarm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);


        Intent intent = getIntent();
        final String group_num = intent.getStringExtra("key");
        final String institut = intent.getStringExtra("instit");
        link_from = intent.getStringExtra("link");

        link_from_start = intent.getStringExtra("startUp");

        if (link_from_start == null) link_from_start = "";

        group_number = group_num;

        if (link_from.length() < 1 || link_from == null){
            new Saved().load_fucktable();
        }

        new Saved().save_fucktable();

        link_from_list = intent.getStringExtra("link_from");

        from = intent.getStringExtra("from");

        instit_list = institut;


        dbHelper = new MyDatabaseHelper(this, group_num);


        //-----------------------------------
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(MyDatabaseHelper.TABLE_NOTE, null, null, null, null, null, null);


        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM", Locale.getDefault());
        Integer dateText = Integer.valueOf(dateFormat.format(currentDate));

        String temper = "";
        String[] temp_word;
        Integer temp1;
        Integer temp2;

        String table_date;


        if (cursor.moveToFirst()) {
            int dateIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_DATE);
            int noteIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_NOTE);

            Log.i("МЕСЯЦ", cursor.getString(dateIndex));

            do {


                temper = cursor.getString(dateIndex);
                Log.i("TEMPER", temper);
                temp_word = temper.split("\\.");

                temp1 = Integer.valueOf(temp_word[1]);
                temp2 = Math.abs(dateText - temp1);


                table_date = cursor.getString(dateIndex);

                if(temp2 > 1){
                    Log.i("TEMPER", "temp2=" + String.valueOf(temp2));
                    database.delete(MyDatabaseHelper.TABLE_NOTE, MyDatabaseHelper.KEY_DATE + " = ?", new String[]{String.valueOf(cursor.getString(dateIndex))});
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        //-------------------------------------------------------------------------------------

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (from){
                    case "list":
                        Intent intent = new Intent(getApplicationContext(), group_list.class);
                        intent.putExtra("key", instit_list);
                        intent.putExtra("link", link_from_list);
                        Log.d("MyLog", link_from_list);
                        startActivity(intent);
                        break;
                    case "favorite":
                        Intent intent2 = new Intent(getApplicationContext(), Favorite.class);
                        startActivity(intent2);
                        break;
                    case "main":
                        Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                        intent3.putExtra("back", "false");
                        startActivity(intent3);
                }
            }
        });



        destFileName = group_num + ".txt";
        json_db_name = group_num + ".json";

//        src_file = intent.getStringExtra("link");

        time = new ArrayList<String>();
        number = new ArrayList<String>();
        prepod = new ArrayList<String>();
        location = new ArrayList<String>();
        par_names = new ArrayList<String>();
        start = new ArrayList<String>();
        end = new ArrayList<String>();
        date = new ArrayList<String>();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);
            }
        });


        Context c = getApplicationContext();
        final File file = new File(c.getFilesDir(), "/files");
        final String from = "FUCKTABLE";

        String online_is = intent.getStringExtra("online");

        if (online_is.equals("true")){

            if (!hasConnection(this)){

                Log.d("MyLog2", "offline");

                init_ofline();
                toolbar_text.setText(institut + ": " + group_num + " (офлайн)");
            } else {
                Log.d("MyLog2", "online");
                init_3();
                toolbar_text.setText(institut + ": " + group_num);
            }
        }



        if (online_is.equals("false")){
            Log.d("MyLog2", "offline");

            init_ofline();


            toolbar_text.setText(institut + ": " + group_num + " (офлайн)");
        } else {

        }

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                // ждём окончание выполнения Загрузчика

                if (msg.what == 1)
                {
                        Parser_2();
                }
                if (msg.what == 2)
                {
                    Parser_2();
                }
            }
        };


    }


    private void init_ofline(){
        runnable_work_2 = new Runnable() {
            @Override
            public void run() {
                getWirk_2();
            }
        };
        Runnable target;
        Thread_work_2 = new Thread(runnable_work_2);
        Thread_work_2.start();
    }

    private void init_3(){
        runnable_work = new Runnable() {
            @Override
            public void run() {
                getWork();
            }
        };
        Thread_work = new Thread(runnable_work);
        Thread_work.start();
    }

    public boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNW = cm.getActiveNetworkInfo();
        if (activeNW != null && activeNW.isConnected()) {
            return true;
        }
        return false;
    }

    private void getWork(){
        try {


            Elements child;

            link_asu_1 = "https://www.asu.ru/timetable/students/" + link_from + link_from_start;

            doc = Jsoup.connect(link_asu_1).get();

                Log.d("MyLog2", "https://www.asu.ru/timetable/students/" + link_from + link_from_start);

                base_elements = doc.getElementsByClass("align_top schedule").first();

                if(base_elements != null){

                    child = base_elements.getAllElements();

                    elements = doc.getElementsByClass("schedule-time" + "schedule-time schedule-current");

                    elements_day = doc.getElementsByClass("schedule-date");

                    elements_time = doc.getElementsByClass("date t_small_x t_gray_light");

                    base_child = child.select("tr");

                } else {
                    Toast.makeText(this, "Пожалуйста, перезапустите приложение", Toast.LENGTH_SHORT).show();
                }


                Element chidren;
                String class_child = "";

                Log.d("MyLog", String.valueOf(base_child.size()));
//            Log.d("MyLog", base_child.text());

                String second_time = ""; //предыдущее время пары


                timetable bufer_list;

                ArrayList<String> bufer = new ArrayList<>();

                String time_paaar;
                String[] time_split;
                String name_para;
                String prepod_name;
                String loc;

                if (base_child.size() != 0) {

                    for (int i = 0; i < base_child.size(); i++){


                        chidren = base_child.get(i);

                        class_child = chidren.attr("class");

//                Log.d("MyLog", chidren.text());

                        switch (class_child){
                            case "schedule-date":


                                timetable_list.add(new timetable("Day", chidren.text()));


//
//                        hashMap.put("Day" + i, chidren.text());
                                break;
                            case "schedule-time schedule-current":
                                time_paaar = chidren.select("td").get(1).text();

                                time_split = time_paaar.split(":");

                                if (time_split.length > 1 && time_split[0] != null){
                                    second_time = time_paaar;
//                            hashMap.put("Time" + i, second_time);

                                    timetable_list.add(new timetable("Time", second_time));
                                } else{
//                            hashMap.put("Time" + i, second_time);
                                    timetable_list.add(new timetable("Time", second_time));
                                }

                                bufer.clear();

                                name_para = chidren.select("td").get(2).text();

                                bufer.add("Para");
                                bufer.add(name_para);

//                        hashMap.put("Para" + i, name_para);
                                timetable_list.add(new timetable("Para", name_para));

                                bufer.clear();
                                prepod_name = chidren.select("td").get(3).text();

                                if (!prepod_name.equals("")){
                                } else {
                                    prepod_name = " ";
                                }

                                bufer.add("Prepod");
                                bufer.add(prepod_name);

//                        hashMap.put("Prepod" + i,prepod_name);
                                timetable_list.add(new timetable("Prepod", prepod_name));

                                bufer.clear();

                                loc = chidren.select("td").get(4).text();

                                bufer.add("Location");
                                bufer.add(loc);

//                        hashMap.put("Location" + i, loc);
                                timetable_list.add(new timetable("Location", loc));
                                break;
                            case "schedule-time":
                                time_paaar = chidren.select("td").get(1).text();

                                time_split = time_paaar.split(":");

                                if (time_split.length > 1 && time_split[0] != null){
                                    second_time = time_paaar;
//                            hashMap.put("Time" + i, second_time);

                                    timetable_list.add(new timetable("Time", second_time));
                                } else{
//                            hashMap.put("Time" + i, second_time);
                                    timetable_list.add(new timetable("Time", second_time));
                                }

                                bufer.clear();

                                name_para = chidren.select("td").get(2).text();

                                bufer.add("Para");
                                bufer.add(name_para);

//                        hashMap.put("Para" + i, name_para);
                                timetable_list.add(new timetable("Para", name_para));

                                bufer.clear();
                                prepod_name = chidren.select("td").get(3).text();

                                if (!prepod_name.equals("")){
                                } else {
                                    prepod_name = " ";
                                }

                                bufer.add("Prepod");
                                bufer.add(prepod_name);

//                        hashMap.put("Prepod" + i,prepod_name);
                                timetable_list.add(new timetable("Prepod", prepod_name));

                                bufer.clear();

                                loc = chidren.select("td").get(4).text();

                                bufer.add("Location");
                                bufer.add(loc);

//                        hashMap.put("Location" + i, loc);
                                timetable_list.add(new timetable("Location", loc));
                                break;
                        }

//                Log.d("MyLog", class_child);
                        Log.d("MyLog", "Class item: " + timetable_list.get(i).Name + " " + timetable_list.get(i).Value);
                    }



                    Elements link_second;

                    link_second = doc.select("div.margin_bottom");

                    String url_second = "";

                    if (link_from_start != "") {
                        Element second = link_second.select("a").get(2);
                        url_second = second.attr("href");
                    } else {
                        Element second = link_second.select("a").get(1);
                        url_second = second.attr("href");
                    }


                    url_update = url_second.replaceAll("\\./", "");

                    link_asu_2 = "https://www.asu.ru/timetable/students/" + link_from + url_update;

                    if(hasConnection(this)){

                        doc = Jsoup.connect(link_asu_2).get();

                    } else {

                        try {
                            final Context c = getApplicationContext();
                            File in = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + group_number + "second" + ".html");
                            doc = Jsoup.parse(in, null);
                        } catch (IOException e){
                            Toast.makeText(this, "Кэш пуст", Toast.LENGTH_SHORT).show();
                        }
                    }




                    Log.d("MyLog2", "https://www.asu.ru/timetable/students/" + link_from + url_update);

                    base_elements = doc.getElementsByClass("align_top schedule").first();

                    child = base_elements.getAllElements();

                    elements = doc.getElementsByClass("schedule-time");

                    elements_day = doc.getElementsByClass("schedule-date");

                    elements_time = doc.getElementsByClass("date t_small_x t_gray_light");

                    base_child = child.select("tr");


                    for (int i = 0; i < base_child.size(); i++){

                        chidren = base_child.get(i);

                        class_child = chidren.attr("class");

//                Log.d("MyLog", chidren.text());

                        switch (class_child){
                            case "schedule-date":

                                bufer_list = new timetable("Day", chidren.text());


                                timetable_list.add(bufer_list);


//
//                        hashMap.put("Day" + i, chidren.text());
                                break;
                            case "schedule-time":
                                time_paaar = chidren.select("td").get(1).text();

                                time_split = time_paaar.split(":");

                                if (time_split.length > 1 && time_split[0] != null){
                                    second_time = time_paaar;
//                            hashMap.put("Time" + i, second_time);

                                    timetable_list.add(new timetable("Time", second_time));
                                } else{
//                            hashMap.put("Time" + i, second_time);
                                    timetable_list.add(new timetable("Time", second_time));
                                }

                                bufer.clear();

                                name_para = chidren.select("td").get(2).text();

                                bufer.add("Para");
                                bufer.add(name_para);

//                        hashMap.put("Para" + i, name_para);
                                timetable_list.add(new timetable("Para", name_para));

                                bufer.clear();
                                prepod_name = chidren.select("td").get(3).text();

                                if (!prepod_name.equals("")){
                                } else {
                                    prepod_name = " ";
                                }

                                bufer.add("Prepod");
                                bufer.add(prepod_name);

//                        hashMap.put("Prepod" + i,prepod_name);
                                timetable_list.add(new timetable("Prepod", prepod_name));

                                bufer.clear();

                                loc = chidren.select("td").get(4).text();

                                bufer.add("Location");
                                bufer.add(loc);

//                        hashMap.put("Location" + i, loc);
                                timetable_list.add(new timetable("Location", loc));
                                break;
                        }

//                Log.d("MyLog", class_child);
                        Log.d("MyLog", "Class item: " + timetable_list.get(i).Name + " " + timetable_list.get(i).Value);
                    }

                    Log.d("MyLog", String.valueOf(url_update));

                    if (hasConnection(this)){
                        Save_page saver = new Save_page();
                        saver.execute();
                    }

                    h.sendEmptyMessage(1);

                } else {
                    Toast.makeText(this, "Пожалуйста, перезапустите приложение", Toast.LENGTH_SHORT).show();
                }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void getWirk_2(){



            Elements child;

            Element chidren;

            String class_child = "";

        String time_paaar;
        String[] time_split;
        String name_para;
        String prepod_name;
        String loc;

        String second_time = ""; //предыдущее время пары


        timetable bufer_list;

        ArrayList<String> bufer = new ArrayList<>();

            link_asu_1 = "https://www.asu.ru/timetable/students/" + link_from + link_from_start;


            try {
                final Context c = getApplicationContext();
                File in = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + group_number + "first" + ".html");
                doc = Jsoup.parse(in, null);

                Log.d("MyLog2", "https://www.asu.ru/timetable/students/" + link_from + link_from_start);

                base_elements = doc.getElementsByClass("align_top schedule").first();

                child = base_elements.getAllElements();

                elements = doc.getElementsByClass("schedule-time" + "schedule-time schedule-current");

                elements_day = doc.getElementsByClass("schedule-date");

                elements_time = doc.getElementsByClass("date t_small_x t_gray_light");

                base_child = child.select("tr");




                Log.d("MyLog", String.valueOf(base_child.size()));
//            Log.d("MyLog", base_child.text());





                for (int i = 0; i < base_child.size(); i++){


                    chidren = base_child.get(i);

                    class_child = chidren.attr("class");

//                Log.d("MyLog", chidren.text());

                    switch (class_child){
                        case "schedule-date":


                            timetable_list.add(new timetable("Day", chidren.text()));


//
//                        hashMap.put("Day" + i, chidren.text());
                            break;
                        case "schedule-time schedule-current":
                            time_paaar = chidren.select("td").get(1).text();

                            time_split = time_paaar.split(":");

                            if (time_split.length > 1 && time_split[0] != null){
                                second_time = time_paaar;
//                            hashMap.put("Time" + i, second_time);

                                timetable_list.add(new timetable("Time", second_time));
                            } else{
//                            hashMap.put("Time" + i, second_time);
                                timetable_list.add(new timetable("Time", second_time));
                            }

                            bufer.clear();

                            name_para = chidren.select("td").get(2).text();

                            bufer.add("Para");
                            bufer.add(name_para);

//                        hashMap.put("Para" + i, name_para);
                            timetable_list.add(new timetable("Para", name_para));

                            bufer.clear();
                            prepod_name = chidren.select("td").get(3).text();

                            if (!prepod_name.equals("")){
                            } else {
                                prepod_name = " ";
                            }

                            bufer.add("Prepod");
                            bufer.add(prepod_name);

//                        hashMap.put("Prepod" + i,prepod_name);
                            timetable_list.add(new timetable("Prepod", prepod_name));

                            bufer.clear();

                            loc = chidren.select("td").get(4).text();

                            bufer.add("Location");
                            bufer.add(loc);

//                        hashMap.put("Location" + i, loc);
                            timetable_list.add(new timetable("Location", loc));
                            break;
                        case "schedule-time":
                            time_paaar = chidren.select("td").get(1).text();

                            time_split = time_paaar.split(":");

                            if (time_split.length > 1 && time_split[0] != null){
                                second_time = time_paaar;
//                            hashMap.put("Time" + i, second_time);

                                timetable_list.add(new timetable("Time", second_time));
                            } else{
//                            hashMap.put("Time" + i, second_time);
                                timetable_list.add(new timetable("Time", second_time));
                            }

                            bufer.clear();

                            name_para = chidren.select("td").get(2).text();

                            bufer.add("Para");
                            bufer.add(name_para);

//                        hashMap.put("Para" + i, name_para);
                            timetable_list.add(new timetable("Para", name_para));

                            bufer.clear();
                            prepod_name = chidren.select("td").get(3).text();

                            if (!prepod_name.equals("")){
                            } else {
                                prepod_name = " ";
                            }

                            bufer.add("Prepod");
                            bufer.add(prepod_name);

//                        hashMap.put("Prepod" + i,prepod_name);
                            timetable_list.add(new timetable("Prepod", prepod_name));

                            bufer.clear();

                            loc = chidren.select("td").get(4).text();

                            bufer.add("Location");
                            bufer.add(loc);

//                        hashMap.put("Location" + i, loc);
                            timetable_list.add(new timetable("Location", loc));
                            break;
                    }

//                Log.d("MyLog", class_child);
                    Log.d("MyLog", "Class item: " + timetable_list.get(i).Name + " " + timetable_list.get(i).Value);
                }



                Elements link_second;

                link_second = doc.select("div.margin_bottom");

                Element second = link_second.select("a").get(1);

                String url_second = second.attr("href");


                url_update = url_second.replaceAll("\\./", "");

                link_asu_2 = "https://www.asu.ru/timetable/students/" + link_from + url_update;




            } catch (IOException e){
//                Toast.makeText(this, "Кэш пуст", Toast.LENGTH_SHORT).show();
            }


                try {
                    final Context c = getApplicationContext();
                    File in = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + group_number + "second" + ".html");
                    doc = Jsoup.parse(in, null);



                    Log.d("MyLog2", "https://www.asu.ru/timetable/students/" + link_from + url_update);

                    base_elements = doc.getElementsByClass("align_top schedule").first();

                    child = base_elements.getAllElements();

                    elements = doc.getElementsByClass("schedule-time");

                    elements_day = doc.getElementsByClass("schedule-date");

                    elements_time = doc.getElementsByClass("date t_small_x t_gray_light");

                    base_child = child.select("tr");


                    for (int i = 0; i < base_child.size(); i++){

                        chidren = base_child.get(i);

                        class_child = chidren.attr("class");

//                Log.d("MyLog", chidren.text());

                        switch (class_child){
                            case "schedule-date":

                                bufer_list = new timetable("Day", chidren.text());


                                timetable_list.add(bufer_list);


//
//                        hashMap.put("Day" + i, chidren.text());
                                break;
                            case "schedule-time":
                                time_paaar = chidren.select("td").get(1).text();

                                time_split = time_paaar.split(":");

                                if (time_split.length > 1 && time_split[0] != null){
                                    second_time = time_paaar;
//                            hashMap.put("Time" + i, second_time);

                                    timetable_list.add(new timetable("Time", second_time));
                                } else{
//                            hashMap.put("Time" + i, second_time);
                                    timetable_list.add(new timetable("Time", second_time));
                                }

                                bufer.clear();

                                name_para = chidren.select("td").get(2).text();

                                bufer.add("Para");
                                bufer.add(name_para);

//                        hashMap.put("Para" + i, name_para);
                                timetable_list.add(new timetable("Para", name_para));

                                bufer.clear();
                                prepod_name = chidren.select("td").get(3).text();

                                if (!prepod_name.equals("")){
                                } else {
                                    prepod_name = " ";
                                }

                                bufer.add("Prepod");
                                bufer.add(prepod_name);

//                        hashMap.put("Prepod" + i,prepod_name);
                                timetable_list.add(new timetable("Prepod", prepod_name));

                                bufer.clear();

                                loc = chidren.select("td").get(4).text();

                                bufer.add("Location");
                                bufer.add(loc);

//                        hashMap.put("Location" + i, loc);
                                timetable_list.add(new timetable("Location", loc));
                                break;
                        }

//                Log.d("MyLog", class_child);
                        Log.d("MyLog", "Class item: " + timetable_list.get(i).Name + " " + timetable_list.get(i).Value);
                    }

                    Log.d("MyLog", String.valueOf(url_update));


                    h.sendEmptyMessage(1);

                    if (hasConnection(this)){
                        Save_page saver = new Save_page();
                        saver.execute();
                    }
                } catch (IOException e){
//                    Toast.makeText(this, "Кэш пуст", Toast.LENGTH_SHORT).show();
                }





    }


    private void snackbar() {


            LinearLayout activity_main = (LinearLayout) findViewById(R.id.linearLayout);

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


    @Override
    public void onBackPressed() {
        switch (from){
            case "list":
                Intent intent = new Intent(getApplicationContext(), group_list.class);
                intent.putExtra("key", instit_list);
                intent.putExtra("link", link_from_list);
                startActivity(intent);
                break;
            case "favorite":
                Intent intent2 = new Intent(getApplicationContext(), Favorite.class);
                startActivity(intent2);
                break;
            case "main":
                Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                intent3.putExtra("back", "false");
                startActivity(intent3);
        }
    }


    public void handleUncaughtException (Thread thread, Throwable e)
    {
        String versionName = BuildConfig.VERSION_NAME;
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();
        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"gubchenko.vadim@gmail.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "MyApp Crash. From -> " + from + " version:" + versionName);
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
    }

    private void Parser_2() {

        allEds = new ArrayList<View>();
        final LinearLayout first_day = (LinearLayout) findViewById(R.id.first_day);
        final LinearLayout second_day = (LinearLayout) findViewById(R.id.second_day);
        final LinearLayout day3 = (LinearLayout) findViewById(R.id.day3);
        final LinearLayout day4 = (LinearLayout) findViewById(R.id.day4);
        final LinearLayout day5 = (LinearLayout) findViewById(R.id.day5);
        final LinearLayout day6 = (LinearLayout) findViewById(R.id.day6);
        final LinearLayout day7 = (LinearLayout) findViewById(R.id.day7);
        final LinearLayout day8 = (LinearLayout) findViewById(R.id.day8);
        final LinearLayout day9 = (LinearLayout) findViewById(R.id.day9);
        final LinearLayout day10 = (LinearLayout) findViewById(R.id.day10);
        final LinearLayout day11 = (LinearLayout) findViewById(R.id.day11);
        final LinearLayout day12 = (LinearLayout) findViewById(R.id.day12);

        final LinearLayout card1 = (LinearLayout) findViewById(R.id.card1);
        final LinearLayout card2 = (LinearLayout) findViewById(R.id.card2);
        final LinearLayout card3 = (LinearLayout) findViewById(R.id.card3);
        final LinearLayout card4 = (LinearLayout) findViewById(R.id.card4);
        final LinearLayout card5 = (LinearLayout) findViewById(R.id.card5);
        final LinearLayout card6 = (LinearLayout) findViewById(R.id.card6);
        final LinearLayout card7 = (LinearLayout) findViewById(R.id.card7);
        final LinearLayout card8 = (LinearLayout) findViewById(R.id.card8);
        final LinearLayout card9 = (LinearLayout) findViewById(R.id.card9);
        final LinearLayout card10 = (LinearLayout) findViewById(R.id.card10);
        final LinearLayout card11 = (LinearLayout) findViewById(R.id.card11);
        final LinearLayout card12 = (LinearLayout) findViewById(R.id.card12);

        final TextView date_activ1 = (TextView) findViewById(R.id.date1);
        final TextView date_activ2 = (TextView) findViewById(R.id.date2);
        final TextView date_activ3 = (TextView) findViewById(R.id.date3);
        final TextView date_activ4 = (TextView) findViewById(R.id.date4);
        final TextView date_activ5 = (TextView) findViewById(R.id.date5);
        final TextView date_activ6 = (TextView) findViewById(R.id.date6);
        final TextView date_activ7 = (TextView) findViewById(R.id.date7);
        final TextView date_activ8 = (TextView) findViewById(R.id.date8);
        final TextView date_activ9 = (TextView) findViewById(R.id.date9);
        final TextView date_activ10 = (TextView) findViewById(R.id.date10);
        final TextView date_activ11 = (TextView) findViewById(R.id.date11);
        final TextView date_activ12 = (TextView) findViewById(R.id.date12);

        final TextView day_activ1 = (TextView) findViewById(R.id.daaaay1);
        final TextView day_activ2 = (TextView) findViewById(R.id.daaaay2);
        final TextView day_activ3 = (TextView) findViewById(R.id.daaaay3);
        final TextView day_activ4 = (TextView) findViewById(R.id.daaaay4);
        final TextView day_activ5 = (TextView) findViewById(R.id.daaaay5);
        final TextView day_activ6 = (TextView) findViewById(R.id.daaaay6);
        final TextView day_activ7 = (TextView) findViewById(R.id.daaaay7);
        final TextView day_activ8 = (TextView) findViewById(R.id.daaaay8);
        final TextView day_activ9 = (TextView) findViewById(R.id.daaaay9);
        final TextView day_activ10 = (TextView) findViewById(R.id.daaaay10);
        final TextView day_activ11 = (TextView) findViewById(R.id.daaaay11);
        final TextView day_activ12 = (TextView) findViewById(R.id.daaaay12);


        RelativeLayout nedel1 = (RelativeLayout) findViewById(R.id.second_nedel);
        TextView nedel1_text1 = (TextView) findViewById(R.id.second_nedel_text1);
        TextView nedel1_text2 = (TextView) findViewById(R.id.second_nedel_text2);

        RelativeLayout nedel2 = (RelativeLayout) findViewById(R.id.second_nedel2);
        TextView nedel2_text1 = (TextView) findViewById(R.id.second_nedel_text12);
        TextView nedel2_text2 = (TextView) findViewById(R.id.second_nedel_text22);

        RelativeLayout nedel3 = (RelativeLayout) findViewById(R.id.second_nedel3);
        TextView nedel3_text1 = (TextView) findViewById(R.id.second_nedel_text13);
        TextView nedel3_text2 = (TextView) findViewById(R.id.second_nedel_text23);

        RelativeLayout nedel4 = (RelativeLayout) findViewById(R.id.second_nedel4);
        TextView nedel4_text1 = (TextView) findViewById(R.id.second_nedel_text14);
        TextView nedel4_text2 = (TextView) findViewById(R.id.second_nedel_text24);

        RelativeLayout nedel5 = (RelativeLayout) findViewById(R.id.second_nedel5);
        TextView nedel5_text1 = (TextView) findViewById(R.id.second_nedel_text15);
        TextView nedel5_text2 = (TextView) findViewById(R.id.second_nedel_text25);

        RelativeLayout nedel6 = (RelativeLayout) findViewById(R.id.second_nedel6);
        TextView nedel6_text1 = (TextView) findViewById(R.id.second_nedel_text16);
        TextView nedel6_text2 = (TextView) findViewById(R.id.second_nedel_text26);

        RelativeLayout nedel7 = (RelativeLayout) findViewById(R.id.second_nedel7);
        TextView nedel7_text1 = (TextView) findViewById(R.id.second_nedel_text17);
        TextView nedel7_text2 = (TextView) findViewById(R.id.second_nedel_text27);

        RelativeLayout nedel8 = (RelativeLayout) findViewById(R.id.second_nedel8);
        TextView nedel8_text1 = (TextView) findViewById(R.id.second_nedel_text18);
        TextView nedel8_text2 = (TextView) findViewById(R.id.second_nedel_text28);

        RelativeLayout nedel9 = (RelativeLayout) findViewById(R.id.second_nedel9);
        TextView nedel9_text1 = (TextView) findViewById(R.id.second_nedel_text19);
        TextView nedel9_text2 = (TextView) findViewById(R.id.second_nedel_text29);

        RelativeLayout nedel10 = (RelativeLayout) findViewById(R.id.second_nedel10);
        TextView nedel10_text1 = (TextView) findViewById(R.id.second_nedel_text110);
        TextView nedel10_text2 = (TextView) findViewById(R.id.second_nedel_text210);

        RelativeLayout nedel11 = (RelativeLayout) findViewById(R.id.second_nedel11);
        TextView nedel11_text1 = (TextView) findViewById(R.id.second_nedel_text111);
        TextView nedel11_text2 = (TextView) findViewById(R.id.second_nedel_text211);

        RelativeLayout nedel12 = (RelativeLayout) findViewById(R.id.second_nedel12);
        TextView nedel12_text1 = (TextView) findViewById(R.id.second_nedel_text112);
        TextView nedel12_text2 = (TextView) findViewById(R.id.second_nedel_text212);

        String key_value = "";
        Integer par_4_day = 0;
        String day = "";
        String[] day_split = new String[0];
        String time = "";
        String para = "";
        String prepod_name = "";
        String loc = "";

        String DATE = "";

        String date_prev = "";

        Integer day_number = 0;
        Integer pre_day_number = 7;
        int i = 1;

        boolean second = false;

        TextView start_par = null;
        TextView name_of_par = null;
        TextView docent = null;
        TextView auditoria = null;

        View view = null;

        Log.d("MyLog", "LIST SIZE: " + String.valueOf(timetable_list.size()));

        nedel1.setVisibility(View.VISIBLE);

        for (int point = 0; point < timetable_list.size(); point++){

//            Log.d("MyLog2", timetable_list.get(point).Value);

            key_value = timetable_list.get(point).Name;


            if (key_value.startsWith("Time")){

                view = getLayoutInflater().inflate(R.layout.fragment_timetable_custom, null);


                start_par = (TextView) view.findViewById(R.id.time_para);
                name_of_par = (TextView) view.findViewById(R.id.name_of_par);
                docent = (TextView) view.findViewById(R.id.docent);
                auditoria = (TextView) view.findViewById(R.id.auditoria);
            }

            if (key_value.startsWith("Day")){

                pre_day_number = day_number;

                if (par_4_day < 3 && par_4_day > 0){

                    switch (i){
                        case 2:
                            date_activ1.setVisibility(View.GONE);
                        case 3:
                            date_activ2.setVisibility(View.GONE);
                        case 4:
                            date_activ3.setVisibility(View.GONE);
                        case 5:
                            date_activ4.setVisibility(View.GONE);
                        case 6:
                            date_activ5.setVisibility(View.GONE);
                        case 7:
                            date_activ6.setVisibility(View.GONE);
                        case 8:
                            date_activ7.setVisibility(View.GONE);
                        case 9:
                            date_activ8.setVisibility(View.GONE);
                        case 10:
                            date_activ9.setVisibility(View.GONE);
                        case 11:
                            date_activ10.setVisibility(View.GONE);
                        case 12:
                            date_activ11.setVisibility(View.GONE);
                    }
                }

                if (par_4_day >= 3){
                    switch (i){
                        case 2:
                            date_activ1.setVisibility(View.VISIBLE);
                        case 3:
                            date_activ2.setVisibility(View.VISIBLE);
                        case 4:
                            date_activ3.setVisibility(View.VISIBLE);
                        case 5:
                            date_activ4.setVisibility(View.VISIBLE);
                        case 6:
                            date_activ5.setVisibility(View.VISIBLE);
                        case 7:
                            date_activ6.setVisibility(View.VISIBLE);
                        case 8:
                            date_activ7.setVisibility(View.VISIBLE);
                        case 9:
                            date_activ8.setVisibility(View.VISIBLE);
                        case 10:
                            date_activ9.setVisibility(View.VISIBLE);
                        case 11:
                            date_activ10.setVisibility(View.VISIBLE);
                        case 12:
                            date_activ11.setVisibility(View.VISIBLE);
                    }
                }

                par_4_day = 0;



                if (i>1){
                    date_prev = day_split[1];
                }
                day = timetable_list.get(point).Value;
                day_split = day.split(" ");

                DATE = day_split[1];

                if(point == 0){
                    nedel1_text1.setText(day_split[1]);
                }

                switch (day_split[0]){
                    case "Понедельник":
                        day_number = 1;
                        break;
                    case "Вторник":
                        day_number = 2;
                        break;
                    case "Среда":
                        day_number = 3;
                        break;
                    case "Четверг":
                        day_number = 4;
                        break;
                    case "Пятница":
                        day_number = 5;
                        break;
                    case "Суббота":
                        day_number = 6;
                        break;
                }

                if (day_number < pre_day_number){
                    second = true;
                    switch (i){
                        case 1:
                            nedel1.setVisibility(View.VISIBLE);
                            nedel1_text1.setText(day_split[1]);
                            break;
                        case 2:
                            nedel2.setVisibility(View.VISIBLE);
                            nedel2_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 3:
                            nedel3.setVisibility(View.VISIBLE);
                            nedel3_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 4:
                            nedel4.setVisibility(View.VISIBLE);
                            nedel4_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 5:
                            nedel5.setVisibility(View.VISIBLE);
                            nedel5_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 6:
                            nedel6.setVisibility(View.VISIBLE);
                            nedel6_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 7:
                            nedel7.setVisibility(View.VISIBLE);
                            nedel7_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 8:
                            nedel8.setVisibility(View.VISIBLE);
                            nedel8_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 9:
                            nedel9.setVisibility(View.VISIBLE);
                            nedel9_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 10:
                            nedel10.setVisibility(View.VISIBLE);
                            nedel10_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 11:
                            nedel11.setVisibility(View.VISIBLE);
                            nedel11_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                        case 12:
                            nedel12.setVisibility(View.VISIBLE);
                            nedel12_text1.setText(day_split[1]);
                            nedel1_text2.setText(date_prev);
                            break;
                    }
                }

                Log.d("MyLog2", day_split[1]);

                switch (i){
                    case 1:
                        day_activ1.setText(day_split[0]);
                        date_activ1.setText(day_split[1]);
                        DATE = day_split[1];
                        Log.d("MyLog2","Date_activ " + (String) date_activ1.getText());
                        card1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        day_activ2.setText(day_split[0]);
                        date_activ2.setText(day_split[1]);
                        DATE = day_split[1];
                        Log.d("MyLog2", "Date_activ " +(String) date_activ2.getText());
                        card2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        day_activ3.setText(day_split[0]);
                        date_activ3.setText(day_split[1]);
                        DATE = day_split[1];
                        Log.d("MyLog2", "Date_activ " +(String) date_activ3.getText());
                        card3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        day_activ4.setText(day_split[0]);
                        date_activ4.setText(day_split[1]);
                        DATE = day_split[1];
                        card4.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        day_activ5.setText(day_split[0]);
                        date_activ5.setText(day_split[1]);
                        DATE = day_split[1];
                        card5.setVisibility(View.VISIBLE);
                        break;
                    case 6:
                        day_activ6.setText(day_split[0]);
                        date_activ6.setText(day_split[1]);
                        DATE = day_split[1];
                        card6.setVisibility(View.VISIBLE);
                        break;
                    case 7:
                        day_activ7.setText(day_split[0]);
                        date_activ7.setText(day_split[1]);
                        DATE = day_split[1];
                        card7.setVisibility(View.VISIBLE);
                        break;
                    case 8:
                        day_activ8.setText(day_split[0]);
                        date_activ8.setText(day_split[1]);
                        DATE = day_split[1];
                        card8.setVisibility(View.VISIBLE);
                        break;
                    case 9:
                        day_activ9.setText(day_split[0]);
                        date_activ9.setText(day_split[1]);
                        DATE = day_split[1];
                        card9.setVisibility(View.VISIBLE);
                        break;
                    case 10:
                        day_activ10.setText(day_split[0]);
                        date_activ10.setText(day_split[1]);
                        DATE = day_split[1];
                        card10.setVisibility(View.VISIBLE);
                        break;
                    case 11:
                        day_activ11.setText(day_split[0]);
                        date_activ11.setText(day_split[1]);
                        DATE = day_split[1];
                        card11.setVisibility(View.VISIBLE);
                        break;
                    case 12:
                        day_activ12.setText(day_split[0]);
                        date_activ12.setText(day_split[1]);
                        DATE = day_split[1];
                        card12.setVisibility(View.VISIBLE);
                        break;
                }
                    i++;
            }

            if (key_value.startsWith("Time")){
                time = timetable_list.get(point).Value;

                Log.d("MyLog", "Time");

                start_par.setText(time);

                Date currentDate = new Date();


                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);


//                Log.i("ТЕКУЩАЯ ДАТА",dateText);


                DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String curr_date = timeFormat.format(currentDate);

//                Log.i("ТЕКУЩЕЕ ВРЕМЯ", curr_date);


                char[] start_t = time.toCharArray(); //преобразования слова в массив символов

                String hh_f = new String(start_t, 0, 2); // начиная с нулевого символа(до строки) забираем 4 символа в переменную
                String mm_f = new String(start_t, 3, 2); // начиная с 4 символа забираем два символа в переменную
                String hh_s = new String(start_t, 8, 2); // начиная с нулевого символа(до строки) забираем 4 символа в переменную
                String mm_s = new String(start_t, 11, 2); // начиная с 4 символа забираем два символа в переменную

                String CURRENT_DATE[] = curr_date.split(":");

                int START_PAR_HH = Integer.parseInt(hh_f);
                int START_PAR_mm = Integer.parseInt(mm_f);

                int END_PAR_HH = Integer.parseInt(hh_s);
                int END_PAR_mm = Integer.parseInt(mm_s);

                int CURRENT_DATE_HH = Integer.parseInt(CURRENT_DATE[0]);
                int CURRENT_DATE_mm = Integer.parseInt(CURRENT_DATE[1]);

                double curr_dbl = (CURRENT_DATE_HH * 60) + CURRENT_DATE_mm;

                double start_dbl = (START_PAR_HH * 60) + START_PAR_mm;
                double end_dbl = (END_PAR_HH * 60) + END_PAR_mm;



                DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

                try {
                    String date1 = df1.parse(DATE).toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (curr_dbl >= start_dbl &&
                        curr_dbl <= end_dbl &&
                        dateText.equals(DATE)) {

                    Log.d("MyLog2", "СЕЙЧАС ПАРА ПИДОРАС");

                    final LinearLayout gb_par_fragment = (LinearLayout) view.findViewById(R.id.bg_par_fragment);
                    gb_par_fragment.setBackgroundResource(R.drawable.study_timetable_bg_on);
                    int currentNightMode = getResources().getConfiguration().uiMode
                            & Configuration.UI_MODE_NIGHT_MASK;



                    switch (APP_PREFERENCES_THEME) {
                        case "white":
                            name_of_par.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                            docent.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                            auditoria.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                            break;
                        case "black":
                            name_of_par.setTextColor(getResources().getColor(R.color.colorPrimary));
                            docent.setTextColor(getResources().getColor(R.color.colorPrimary));
                            auditoria.setTextColor(getResources().getColor(R.color.colorPrimary));
                            break;
                        case "auto":
                            switch (currentNightMode) {
                                case Configuration.UI_MODE_NIGHT_NO:
                                    name_of_par.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                                    docent.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                                    auditoria.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                                    break;
                                case Configuration.UI_MODE_NIGHT_YES:
                                    name_of_par.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    docent.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    auditoria.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    break;
                                default:
                                    name_of_par.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                                    docent.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                                    auditoria.setTextColor(getResources().getColor(R.color.colorPrimary_light));
                                    break;
                            }
                            break;
                    }
                }

            }

            if (key_value.startsWith("Para")){
                Log.d("MyLog", "Para");

                para = timetable_list.get(point).Value;
                Log.d("MyLog", "PARAAAA " + para);
                name_of_par.setText(para);
            }

            if (key_value.startsWith("Prepod")){
                Log.d("MyLog", "Prepod");
                prepod_name = timetable_list.get(point).Value;
                docent.setText(prepod_name);
            }

            if (key_value.startsWith("Location")){
                Log.d("MyLog", "Location");
                loc = timetable_list.get(point).Value;

                if (loc.length() > 0 && loc != null){

                }else{
                    loc = "";
                }
                Log.d("MyLog", loc);
                auditoria.setText(loc);
                par_4_day++;

                allEds.add(view);

                switch (i){
                    case 2:
                        first_day.addView(view);
                        break;
                    case 3:
                        second_day.addView(view);
                        break;
                    case 4:
                        day3.addView(view);
                        break;
                    case 5:
                        day4.addView(view);
                        break;
                    case 6:
                        day5.addView(view);
                        break;
                    case 7:
                        day6.addView(view);
                        break;
                    case 8:
                        day7.addView(view);
                        break;
                    case 9:
                        day8.addView(view);
                        break;
                    case 10:
                        day9.addView(view);
                        break;
                    case 11:
                        day10.addView(view);
                        break;
                    case 12:
                        day11.addView(view);
                        break;
                    case 13:
                        day12.addView(view);
                        break;
                }

            }

            switch (key_value){
                case "Day":

                    break;
                case "Time":

                    break;

                case "Para":

                    break;

                case "Prepod":

                    break;

                case "Location":

                    break;
            }
        }
        if (!second){
            nedel1_text2.setText(day_split[1]);
        }

        nedel2_text2.setText(day_split[1]);
        nedel3_text2.setText(day_split[1]);
        nedel4_text2.setText(day_split[1]);
        nedel5_text2.setText(day_split[1]);
        nedel6_text2.setText(day_split[1]);
        nedel7_text2.setText(day_split[1]);
        nedel8_text2.setText(day_split[1]);
        nedel9_text2.setText(day_split[1]);
        nedel10_text2.setText(day_split[1]);
        nedel11_text2.setText(day_split[1]);
        nedel12_text2.setText(day_split[1]);

        final TextView note_subtext = (TextView) note_dialog.findViewById(R.id.note_subtext);
        final EditText note_text = (EditText) note_dialog.findViewById(R.id.note_text);
        MaterialButton note_button = (MaterialButton) note_dialog.findViewById(R.id.note_button);

        note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                contentValues.put(MyDatabaseHelper.KEY_DATE, (String) note_subtext.getText());
                contentValues.put(MyDatabaseHelper.KEY_NOTE, note_text.getText().toString());

                Cursor cursor = database.query(MyDatabaseHelper.TABLE_NOTE, null, null, null, null, null, null);

                int updater = 0;

                if (cursor.moveToFirst()) {
                    int dateIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_DATE);
                    int noteIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_NOTE);

                    Log.i("МЕСЯЦ", cursor.getString(dateIndex));

                    do {

                        if(cursor.getString(dateIndex).equals(note_subtext.getText())){

                            String date_db = (String) note_subtext.getText();
                            Log.i("date_db",  date_db);


                            database.delete(MyDatabaseHelper.TABLE_NOTE, MyDatabaseHelper.KEY_DATE + " = ?", new String[]{String.valueOf(cursor.getString(dateIndex))});
                            database.insert(MyDatabaseHelper.TABLE_NOTE, null, contentValues);

                            if (note_text.getText().toString().equals("") ||
                                    note_text.getText().toString().equals(" ") ||
                                    note_text.getText().toString().equals(null)){

                                database.delete(MyDatabaseHelper.TABLE_NOTE, MyDatabaseHelper.KEY_DATE + " = ?", new String[]{String.valueOf(cursor.getString(dateIndex))});
                            }

                            updater = 1;
                        }

                    } while (cursor.moveToNext());
                }
                cursor.close();

                if (updater == 0){
                    database.insert(MyDatabaseHelper.TABLE_NOTE, null, contentValues);
                    if (note_text.getText().toString().equals("") ||
                            note_text.getText().toString().equals(" ") ||
                            note_text.getText().toString().equals(null)){
                        database.delete(MyDatabaseHelper.TABLE_NOTE, MyDatabaseHelper.KEY_DATE, null);
                    }
                }

                recreate();
            }
        });

        for (int g = 1; g < 13; g++){
            final View note_fragment_view = getLayoutInflater().inflate(R.layout.fragment_note, null);


            LinearLayout note_linear = (LinearLayout) note_fragment_view.findViewById(R.id.fragment_note_linear);
            TextView note = (TextView) note_fragment_view.findViewById(R.id.note_text);
            ImageView note_pen = (ImageView) note_fragment_view.findViewById(R.id.pen_icon);


            note_linear.setId(g);
            note_linear.setTag(g);
            note.setTag(g);
            note_pen.setTag(g);

            note_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int layoutID = (int) v.getTag();



                    switch (layoutID){
                        case 1:
                            note_subtext.setText(date_activ1.getText());
                            break;
                        case 2:
                            note_subtext.setText(date_activ2.getText());
                            break;
                        case 3:
                            note_subtext.setText(date_activ3.getText());
                            break;
                        case 4:
                            note_subtext.setText(date_activ4.getText());
                            break;
                        case 5:
                            note_subtext.setText(date_activ5.getText());
                            break;
                        case 6:
                            note_subtext.setText(date_activ6.getText());
                            break;
                        case 7:
                            note_subtext.setText(date_activ7.getText());
                            break;
                        case 8:
                            note_subtext.setText(date_activ8.getText());
                            break;
                        case 9:
                            note_subtext.setText(date_activ9.getText());
                            break;
                        case 10:
                            note_subtext.setText(date_activ10.getText());
                            break;
                        case 11:
                            note_subtext.setText(date_activ11.getText());
                            break;
                        case 12:
                            note_subtext.setText(date_activ12.getText());
                            break;
                    }


                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    Cursor cursor = database.query(MyDatabaseHelper.TABLE_NOTE, null, null, null, null, null, null);

                    note_text.setText("");

                    if (cursor.moveToFirst()) {
                        int dateIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_DATE);
                        int noteIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_NOTE);

                        do {

                            if(cursor.getString(dateIndex).equals(note_subtext.getText())){

                                Log.d("Я НАШЁЛ_ДИАЛОГ!!!", "name = " + cursor.getString(dateIndex) +
                                        ", email = " + cursor.getString(noteIndex));
                                note_text.setText(cursor.getString(noteIndex));

                            }

                        } while (cursor.moveToNext());
                    } else
                        Log.d("mLog","0 rows");
                    cursor.close();

                    if(!APP_PREFERENCES_PREMIUM.equals("false")){
                        note_dialog.show();
                    } else {
                        dialog.show();
                    }

                }
            });

            String SUBTEXT;

            switch (g){
                case 1:
                    SUBTEXT = (String) date_activ1.getText();
                    break;
                case 2:
                    SUBTEXT = (String) date_activ2.getText();
                    break;
                case 3:
                    SUBTEXT = (String) date_activ3.getText();
                    break;
                case 4:
                    SUBTEXT = (String) date_activ4.getText();
                    break;
                case 5:
                    SUBTEXT = (String) date_activ5.getText();
                    break;
                case 6:
                    SUBTEXT = (String) date_activ6.getText();
                    break;
                case 7:
                    SUBTEXT = (String) date_activ7.getText();
                    break;
                case 8:
                    SUBTEXT = (String) date_activ8.getText();
                    break;
                case 9:
                    SUBTEXT = (String) date_activ9.getText();
                    break;
                case 10:
                    SUBTEXT = (String) date_activ10.getText();
                    break;
                case 11:
                    SUBTEXT = (String) date_activ11.getText();
                    break;
                case 12:
                    SUBTEXT = (String) date_activ12.getText();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + g);
            }

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(MyDatabaseHelper.TABLE_NOTE, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                int dateIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_DATE);
                int noteIndex = cursor.getColumnIndex(MyDatabaseHelper.KEY_NOTE);

                do {
                    Log.d("mLog", "name = " + cursor.getString(dateIndex) +
                            ", email = " + cursor.getString(noteIndex));

                    if(cursor.getString(dateIndex).equals(SUBTEXT)
                            && !cursor.getString(dateIndex).equals("")
                            && !cursor.getString(dateIndex).equals(" ")){

                        Log.d("Я НАШЁЛ!!!", "name = " + cursor.getString(dateIndex) +
                                ", email = " + cursor.getString(noteIndex));

                        note.setText(cursor.getString(noteIndex));
                        note_pen.setVisibility(View.GONE);
                    }

                } while (cursor.moveToNext());
            } else
                Log.d("mLog","0 rows");
            cursor.close();

            switch (g){
                case 1:
                    first_day.addView(note_fragment_view);
                    break;
                case 2:
                    second_day.addView(note_fragment_view);
                    break;
                case 3:
                    day3.addView(note_fragment_view);
                    break;
                case 4:
                    day4.addView(note_fragment_view);
                    break;
                case 5:
                    day5.addView(note_fragment_view);
                    break;
                case 6:
                    day6.addView(note_fragment_view);
                    break;
                case 7:
                    day7.addView(note_fragment_view);
                    break;
                case 8:
                    day8.addView(note_fragment_view);
                    break;
                case 9:
                    day9.addView(note_fragment_view);
                    break;
                case 10:
                    day10.addView(note_fragment_view);
                    break;
                case 11:
                    day11.addView(note_fragment_view);
                    break;
                case 12:
                    day12.addView(note_fragment_view);
                    break;


            }
        }


        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        timetable_list.clear();

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
        final Connection.Response response1;
        try {

            final Context c = getApplicationContext();
            response = Jsoup.connect(link_asu_1).execute();
            response1 = Jsoup.connect(link_asu_2).execute();
            Document doc = response.parse();
            Document doc1 = response1.parse();

            File f = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + group_number + "first" + ".html");

            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

            f = new File(c.getFilesDir() + "Android/data/dev.prokrostinatorbl.raspisanie/files/" + group_number + "second" + ".html");

            FileUtils.writeStringToFile(f, doc1.outerHtml(), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}