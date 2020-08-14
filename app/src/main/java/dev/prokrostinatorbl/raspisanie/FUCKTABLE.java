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

import com.google.android.material.button.MaterialButton;
import com.google.gson.*;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;


import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
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

    public static Integer NOTE_FRAGMENT_NUMBER = 1;


    private final int USERID = 6000;
    private int countID = 1;


    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    public static String GROUP_LINK;
    public static String GROUP_FILE;
    public static String GROUP_JSON;

    SharedPreferences mSettings;

    public static String instit_list;


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


    public String destFileName;
    public String src_file;
    public String json_db_name;

    public LinearLayout first_day;
    public LinearLayout second_day;
    public LinearLayout day3;
    public LinearLayout day4;
    public LinearLayout day5;
    public LinearLayout day6;
    public LinearLayout day7;
    public LinearLayout day8;
    public LinearLayout day9;
    public LinearLayout day10;
    public LinearLayout day11;
    public LinearLayout day12;
    public LinearLayout linear_group;

    public Integer day_number;




    private Toolbar toolbar;

    private TextView toolbar_text;

    public ArrayList<String> days;
    public ArrayList<String> months;
    public ArrayList<String> years;
    public static Handler h;

    Dialog note_dialog;

    Dialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisited = mSettings.getBoolean("hasVisited", false);
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;


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
                Intent intent = new Intent(getApplicationContext(), group_list.class);
                intent.putExtra("key", instit_list);
                startActivity(intent);
            }
        });



        destFileName = group_num + ".txt";
        json_db_name = group_num + ".json";
        toolbar_text.setText(institut + ": " + group_num);
        src_file = intent.getStringExtra("link");


        if(src_file.equals("link")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Расписания для этой группы нет на сайте!", Toast.LENGTH_SHORT);
            toast.show();
        } else {


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
            File file = new File(c.getFilesDir(), "/files");
            String from = "FUCKTABLE";

            Downloader.Download(src_file, file, destFileName, from);


            h = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    // ждём окончание выполнения Загрузчика

                    if (msg.what == 1)
                    {
                        try {
                            Parser();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Log.i("header","онлайн мод");
                    }
                    if (msg.what == 2)
                    {
                        try {
                            Parser();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        toolbar_text.setText(institut + ": " + group_num + " (офлайн)");
                        Log.i("header","офлайн мод");
                    }
                    if (msg.what == 3)
                    {
                        try {
                            Read();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

        }

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

    private void Parser() throws ParseException {

        Log.i("@@@@@", "Parser запущен");
        day_number = 1;

        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>();
        ArrayList<String> years = new ArrayList<>();

        if(date_par != null){
            int len = date_par.length();
            for (int i = 0; i < len; i++){
                try {
                    date.add(date_par.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



        ///////////////////////////////////////////////////////////////////////




        Integer previous_d;

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


        Integer JJJ = date.size();

        Log.i("размер даты", String.valueOf(JJJ));

        if(JJJ == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Расписания для этой группы нет в кэше/на сайте!", Toast.LENGTH_SHORT);
            toast.show();
        }else {

            String date_temp = date.get(0);

            Integer current_day = 1;
            Integer buffer_day = 1;
            Integer paar_for_day = 0;

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


            nedel1.setVisibility(View.VISIBLE);
            nedel1_text1.setText(date.get(0));

            for (int j = 0; j < date.size(); j++) {


                String jj = String.valueOf(j);
//                Log.i("J_from_looper", jj);

                if (date_temp.equals(date.get(j))) {

                } else {


                    String dg = date.get(j);

                    String day_temp;
                    char[] dgg = dg.toCharArray();
                    day_temp = new String(dgg, 0, 2);


                    String pr_day_temp;
                    char[] pdg = date_temp.toCharArray();
                    pr_day_temp = new String(pdg, 0, 2);

                    date_temp = date.get(j); //обновляем текущую дату

                    Integer f_num = Integer.parseInt(day_temp);
                    Integer s_num = Integer.parseInt(pr_day_temp);

                    String axx = " " + f_num;
                    //                    Log.i("f_num", axx);


                    if (f_num != s_num) {

                        if (paar_for_day < 3){

                            switch (day_number){
                                case 1:
                                    date_activ1.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    date_activ2.setVisibility(View.GONE);
                                    break;
                                case 3:
                                    date_activ3.setVisibility(View.GONE);
                                    break;
                                case 4:
                                    date_activ4.setVisibility(View.GONE);
                                    break;
                                case 5:
                                    date_activ5.setVisibility(View.GONE);
                                    break;
                                case 6:
                                    date_activ6.setVisibility(View.GONE);
                                    break;
                                case 7:
                                    date_activ7.setVisibility(View.GONE);
                                    break;
                                case 8:
                                    date_activ8.setVisibility(View.GONE);
                                    break;
                                case 9:
                                    date_activ9.setVisibility(View.GONE);
                                    break;
                                case 10:
                                    date_activ10.setVisibility(View.GONE);
                                    break;
                                case 11:
                                    date_activ11.setVisibility(View.GONE);
                                    break;
                                case 12:
                                    date_activ12.setVisibility(View.GONE);
                                    break;
                            }

                        }
                        paar_for_day = 0;

                        day_number++;
                    }
                }

                paar_for_day++;

                LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View view = layoutInflater.inflate(R.layout.fragment_timetable_custom, null);
                final LinearLayout gb_par_fragment = (LinearLayout) view.findViewById(R.id.bg_par_fragment);


                TextView start_par = (TextView) view.findViewById(R.id.start);
                start_par.setText(start.get(j));

                TextView end_par = (TextView) view.findViewById(R.id.end);
                end_par.setText(end.get(j));

                TextView name_of_par = (TextView) view.findViewById(R.id.name_of_par);
                name_of_par.setText(par_names.get(j));

                TextView docent = (TextView) view.findViewById(R.id.docent);
                //                Log.i("Prepod_Name",prepod.get(j));

                Integer ps = prepod.size();

                if (j == ps || j > ps) {
                    docent.setText(" ");
                } else {
                    if (prepod.get(j) != null) {
                        docent.setText(prepod.get(j));
                    } else {
                        docent.setText("");
                    }
                }

                String DATE = date.get(j);
                String START = start.get(j);
                String END = end.get(j);

                Date currentDate = new Date();


                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);


//                Log.i("ТЕКУЩАЯ ДАТА",dateText);


                String START_TIME[] = START.split(":");
                String END_TIME[] = END.split(":");


                DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String curr_date = timeFormat.format(currentDate);

//                Log.i("ТЕКУЩЕЕ ВРЕМЯ", curr_date);

                String CURRENT_DATE[] = curr_date.split(":");

                int START_PAR_HH = Integer.parseInt(START_TIME[0]);
                int START_PAR_mm = Integer.parseInt(START_TIME[1]);

                int END_PAR_HH = Integer.parseInt(END_TIME[0]);
                int END_PAR_mm = Integer.parseInt(END_TIME[1]);

                int CURRENT_DATE_HH = Integer.parseInt(CURRENT_DATE[0]);
                int CURRENT_DATE_mm = Integer.parseInt(CURRENT_DATE[1]);

                double curr_dbl = (CURRENT_DATE_HH * 60) + CURRENT_DATE_mm;

                double start_dbl = (START_PAR_HH * 60) + START_PAR_mm;
                double end_dbl = (END_PAR_HH * 60) + END_PAR_mm;



                DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

                String date1 = df1.parse(DATE).toString();


                TextView auditoria = (TextView) view.findViewById(R.id.auditoria);
                auditoria.setText(location.get(j));


                if (curr_dbl >= start_dbl &&
                        curr_dbl <= end_dbl &&
                        dateText.equals(DATE)) {

                    gb_par_fragment.setBackgroundResource(R.drawable.study_timetable_bg_on);
                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                    int currentNightMode = getResources().getConfiguration().uiMode
                            & Configuration.UI_MODE_NIGHT_MASK;


                    if (mSettings.contains(APP_PREFERENCES_THEME)) {

                        String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");

                        if (!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")) {
                            mCounter = "auto";
                        }

                        switch (mCounter) {
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






                allEds.add(view);

                Integer temp_day_number = day_number - 1;


                switch (day_number) {
                    case 1:



                        date_activ1.setText(date.get(j));

                        TextView daaaay1 = (TextView) findViewById(R.id.daaaay1);

                        if (date1.startsWith("Mon")) {
                            daaaay1.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay1.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay1.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay1.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay1.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay1.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot1.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        first_day.addView(view);

                        break;
                    case 2:


                        date_activ2.setText(date.get(j));

                        TextView daaaay2 = (TextView) findViewById(R.id.daaaay2);


                        if (date1.startsWith("Mon")) {
                            daaaay2.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay2.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay2.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay2.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay2.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay2.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot2.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }


                        second_day.addView(view);

                        break;
                    case 3:


                        date_activ3.setText(date.get(j));

                        TextView daaaay3 = (TextView) findViewById(R.id.daaaay3);


                        if (date1.startsWith("Mon")) {
                            daaaay3.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay3.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay3.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay3.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay3.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay3.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot3.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }


                        day3.addView(view);

                        break;
                    case 4:


                        date_activ4.setText(date.get(j));

                        TextView daaaay4 = (TextView) findViewById(R.id.daaaay4);


                        if (date1.startsWith("Mon")) {
                            daaaay4.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay4.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay4.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay4.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay4.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay4.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot4.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day4.addView(view);

                        break;
                    case 5:

                        date_activ5.setText(date.get(j));

                        TextView daaaay5 = (TextView) findViewById(R.id.daaaay5);


                        if (date1.startsWith("Mon")) {
                            daaaay5.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay5.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay5.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay5.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay5.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay5.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot5.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day5.addView(view);

                        break;
                    case 6:
                        date_activ6.setText(date.get(j));

                        TextView daaaay6 = (TextView) findViewById(R.id.daaaay6);


                        if (date1.startsWith("Mon")) {
                            daaaay6.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay6.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay6.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay6.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay6.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay6.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot6.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day6.addView(view);

                        break;
                    case 7:
                        date_activ7.setText(date.get(j));

                        TextView daaaay7 = (TextView) findViewById(R.id.daaaay7);

                        if (date1.startsWith("Mon")) {
                            daaaay7.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay7.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay7.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay7.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay7.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay7.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot7.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day7.addView(view);

                        break;
                    case 8:
                        date_activ8.setText(date.get(j));

                        TextView daaaay8 = (TextView) findViewById(R.id.daaaay8);

                        if (date1.startsWith("Mon")) {
                            daaaay8.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay8.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay8.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay8.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay8.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay8.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot8.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day8.addView(view);

                        break;
                    case 9:
                        date_activ9.setText(date.get(j));


                        TextView daaaay9 = (TextView) findViewById(R.id.daaaay9);

                        if (date1.startsWith("Mon")) {
                            daaaay9.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay9.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay9.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay9.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay9.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay9.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot9.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day9.addView(view);

                        break;
                    case 10:
                        date_activ10.setText(date.get(j));

                        TextView daaaay10 = (TextView) findViewById(R.id.daaaay10);

                        if (date1.startsWith("Mon")) {
                            daaaay10.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay10.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay10.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay10.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay10.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay10.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot10.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day10.addView(view);

                        break;
                    case 11:

                        date_activ11.setText(date.get(j));

                        TextView daaaay11 = (TextView) findViewById(R.id.daaaay11);

                        if (date1.startsWith("Mon")) {
                            daaaay11.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay11.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay11.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay11.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay11.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay11.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot11.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day11.addView(view);

                        break;
                    case 12:

                        date_activ12.setText(date.get(j));

                        TextView daaaay12 = (TextView) findViewById(R.id.daaaay12);

                        if (date1.startsWith("Mon")) {
                            daaaay12.setText("Понедельник");
                            current_day = 1;
                        }
                        if (date1.startsWith("Tue")) {
                            daaaay12.setText("Вторник");
                            current_day = 2;
                        }
                        if (date1.startsWith("Wed")) {
                            daaaay12.setText("Среда");
                            current_day = 3;
                        }
                        if (date1.startsWith("Thu")) {
                            daaaay12.setText("Четверг");
                            current_day = 4;
                        }
                        if (date1.startsWith("Fri")) {
                            daaaay12.setText("Пятница");
                            current_day = 5;
                        }
                        if (date1.startsWith("Sat")) {
                            daaaay12.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)) {
//                            dot12.setVisibility(View.VISIBLE);
                        }

                        if (current_day < buffer_day) {
                            nedel1_text2.setText(date.get(j-1));
                            switch (day_number) {
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    nedel2_text1.setText(date.get(j));
                                    nedel2_text2.setText(date.get(date.size()-1));
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    nedel3_text1.setText(date.get(j));
                                    nedel3_text2.setText(date.get(date.size()-1));
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    nedel4_text1.setText(date.get(j));
                                    nedel4_text2.setText(date.get(date.size()-1));
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    nedel5_text1.setText(date.get(j));
                                    nedel5_text2.setText(date.get(date.size()-1));
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    nedel6_text1.setText(date.get(j));
                                    nedel6_text2.setText(date.get(date.size()-1));
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    nedel7_text1.setText(date.get(j));
                                    nedel7_text2.setText(date.get(date.size()-1));
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    nedel8_text1.setText(date.get(j));
                                    nedel8_text2.setText(date.get(date.size()-1));
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    nedel9_text1.setText(date.get(j));
                                    nedel9_text2.setText(date.get(date.size()-1));
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    nedel10_text1.setText(date.get(j));
                                    nedel10_text2.setText(date.get(date.size()-1));
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    nedel11_text1.setText(date.get(j));
                                    nedel11_text2.setText(date.get(date.size()-1));
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    nedel12_text1.setText(date.get(j));
                                    nedel12_text2.setText(date.get(date.size()-1));
                                    break;
                            }
                        }

                        day12.addView(view);

                        break;
                }


                buffer_day = current_day;

            }


            if (paar_for_day < 3){

                switch (day_number){
                    case 1:
                        date_activ1.setVisibility(View.GONE);
                        break;
                    case 2:
                        date_activ2.setVisibility(View.GONE);
                        break;
                    case 3:
                        date_activ3.setVisibility(View.GONE);
                        break;
                    case 4:
                        date_activ4.setVisibility(View.GONE);
                        break;
                    case 5:
                        date_activ5.setVisibility(View.GONE);
                        break;
                    case 6:
                        date_activ6.setVisibility(View.GONE);
                        break;
                    case 7:
                        date_activ7.setVisibility(View.GONE);
                        break;
                    case 8:
                        date_activ8.setVisibility(View.GONE);
                        break;
                    case 9:
                        date_activ9.setVisibility(View.GONE);
                        break;
                    case 10:
                        date_activ10.setVisibility(View.GONE);
                        break;
                    case 11:
                        date_activ11.setVisibility(View.GONE);
                        break;
                    case 12:
                        date_activ12.setVisibility(View.GONE);
                        break;
                }

            }


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

                        String mPremium = mSettings.getString(APP_PREFERENCES_PREMIUM, "false");

                        if (!mPremium.equals("true") && !mPremium.equals("false")){
                            mPremium = "false";
                        }

                        if(!mPremium.equals("false")){
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
            card12.setVisibility(View.VISIBLE);
            card11.setVisibility(View.VISIBLE);
            card10.setVisibility(View.VISIBLE);
            card9.setVisibility(View.VISIBLE);
            card8.setVisibility(View.VISIBLE);
            card7.setVisibility(View.VISIBLE);
            card6.setVisibility(View.VISIBLE);
            card5.setVisibility(View.VISIBLE);
            card4.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);


            Integer number_of_day = day_number;

            RelativeLayout second_nedel = (RelativeLayout) findViewById(R.id.second_nedel);

            if (number_of_day != 12) {
                if (number_of_day == 11) {
                    card12.setVisibility(View.GONE);
                }
                if (number_of_day == 10) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                }
                if (number_of_day == 9) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                }
                if (number_of_day == 8) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                }
                if (number_of_day == 7) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                }
                if (number_of_day == 6) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                    card7.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);
                }
                if (number_of_day == 5) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                    card7.setVisibility(View.GONE);
                    card6.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if (number_of_day == 4) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                    card7.setVisibility(View.GONE);
                    card6.setVisibility(View.GONE);
                    card5.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if (number_of_day == 3) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                    card7.setVisibility(View.GONE);
                    card6.setVisibility(View.GONE);
                    card5.setVisibility(View.GONE);
                    card4.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if (number_of_day == 2) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                    card7.setVisibility(View.GONE);
                    card6.setVisibility(View.GONE);
                    card5.setVisibility(View.GONE);
                    card4.setVisibility(View.GONE);
                    card3.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if (number_of_day == 1) {
                    card12.setVisibility(View.GONE);
                    card11.setVisibility(View.GONE);
                    card10.setVisibility(View.GONE);
                    card9.setVisibility(View.GONE);
                    card8.setVisibility(View.GONE);
                    card7.setVisibility(View.GONE);
                    card6.setVisibility(View.GONE);
                    card5.setVisibility(View.GONE);
                    card4.setVisibility(View.GONE);
                    card3.setVisibility(View.GONE);
                    card2.setVisibility(View.GONE);
                    card1.setVisibility(View.GONE);

                }
            }


            day_number = 1;

        }





    }


    public void Read() throws IOException {
        Context c = getApplicationContext();
        File file = new File(c.getFilesDir(), "/files");

        File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

        File dest = new File(file + destFileName);
        Scanner in = new Scanner(dest);

        Log.i("***", "  " + "я читаю");

        String s;


        String group_num = "X-WR-CALNAME";
        String prep = "DESCRIPTION";
        String locat = "LOCATION";
        String name = "SUMMARY";
        String start_js = "DTSTART;TZID=Asia/Krasnoyarsk";
        String end_js = "DTEND;TZID=Asia/Krasnoyarsk";

        group_numb = new JSONArray();
        prepod_name = new JSONArray();
        auditor = new JSONArray();
        par_name = new JSONArray();
        start_par = new JSONArray();
        end_par = new JSONArray();
        date_par = new JSONArray();

        day_js = new JSONArray();
        month_js = new JsonArray();
        year_js = new JsonArray();

        while(in.hasNextLine()){

            s = in.nextLine();
            String words[] = s.split(":");
            String word = words[0];

            try {
                // Create a new instance of a JSONObject
                File json_db = new File(dir + json_db_name);
                final JSONObject object = new JSONObject();



                if (word.equals(group_num))
                {
                    String words_line[] = words[1].split(",");

                    String number_group = words_line[0];
                    number.add(number_group);

                }

                if (word.equals(prep))
                {
                    String prepodav = ""; //создаём пустую переменную, чтобы в неё скидывать нужные записи

                    if (words.length > 1 && words[1] != null) //Проверка: если в строке есть не пустые данные после двоеточия, то идём дальше
                    {
                        String words_line[] = words[1].split(" ");


                        for (int i = 0; i <= words_line.length - 1; i++) { //цикл который будет работать пока не дойдёт до значения "Длина строки" (хуй знает зачем минус один. По сути я хотел убрать скобки после ФИО препода которые "Доцент" и т.п., но не вышло, да и похуй)

                            prepodav += words_line[i] + " "; //в вышесозданную переменную мы скидываем встречающиеся слова и ставим между ними пробелы

                        }
                        prepod.add(prepodav);

                    } else {

                        prepodav += "";
                        prepod.add(prepodav);

                    }
                }

                if (word.equals(locat))
                {
                    if (words.length > 1 && words[1] != null) { // аналогично проверяем существование данных
                        String words_line[] = words[1].split(" ");

                        location.add(words_line[0]);
                    }   else {

                        String rand = "";
                        location.add(rand);

                    }
                }

                if (word.equals(name))
                {
                    if (words.length > 1 && words[2] != null) {
                        String name_par[] = words[2].split(" ");
                        String par = "";
                        for (int i = 1; i <= name_par.length - 1; i++) {
                            par += name_par[i] + " ";
                        }
                        String paar_name[] = par.split("\\(");
                        String paar;
                        paar = paar_name[0];

                        par_names.add(paar);




                    }
                }




                if (word.equals(start_js))
                {
                    if (words.length > 1 && words[1] != null) {
                        String start_time = words[1];
                        char[] start_t = start_time.toCharArray(); //преобразования слова в массив символов

                        String y = new String(start_t, 0, 4); // начиная с нулевого символа(до строки) забираем 4 символа в переменную
                        String m = new String(start_t, 4, 2); // начиная с 4 символа забираем два символа в переменную
                        String d = new String(start_t, 6, 2); // аналогично предыдущим
                        String t = new String(start_t, 9, 2) + ":" + new String(start_t, 11, 2);
                        String date = d + "." + m + "." + y;
                        //                    Log.i("!!!", "Дата: " + date); //выводим дату
                        //                        Log.i("AAAA", date);
                        String buf_date = date.toString();
                        //                        days.add(buf_date);
                        date_par.put(date); // в json добавляем дату
                        Log.i("JSON_DATE", date);
                        //                        Log.i("ЗАБРАЛ ЭТУ ДАТУ: ", d);
                        //                        month_js.add(m);
                        //                        year_js.add(y);

                        //                    Log.i("!!!", "Начало пары: "  + t); // выводим время
                        //                        start_par.put(t); // в json добавляем время начала пары
                        start.add(t);





                    }
                }

                if (word.equals(end_js))
                {
                    if (words.length > 1 && words[1] != null) {
                        String end_time = words[1];
                        char[] end_t = end_time.toCharArray(); //преобразования слова в массив символов


                        String t = new String(end_t, 9, 2) + ":" + new String(end_t, 11, 2);
                        //                    Log.i("!!!", "Конец пары: "  + t);
                        //                        end_par.put(t); // в json добавляем время конца пары
                        end.add(t);
                    }
                }

                object.put("date", date_par);


                try{
                    FileWriter js = new FileWriter(json_db); // сохраняем всё это в json
                    js.write(object.toString());
                    js.flush();
                    js.close();
                } catch (IOException ex){
                    ex.printStackTrace();
                }


            } catch (JSONException e) {
                Log.e("***", "Failed to create JSONObject", e);
            }

        }
        in.close();


        temp = 0;

        Log.i("@@@@@", "Read завершёл");



    }

}