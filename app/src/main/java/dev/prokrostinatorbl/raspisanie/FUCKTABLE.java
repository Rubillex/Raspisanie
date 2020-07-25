package dev.prokrostinatorbl.raspisanie;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

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

import static java.util.Calendar.DATE;


public class FUCKTABLE extends Activity {

        private List<View> allEds;


        private final int USERID = 6000;
        private int countID = 1;


    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    public static String GROUP_LINK;
    public static String GROUP_FILE;
    public static String GROUP_JSON;

    SharedPreferences mSettings;




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


            final TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);


        Intent intent = getIntent();
        final String group_num = intent.getStringExtra("key");
        final String institut = intent.getStringExtra("instit");

        destFileName = group_num + ".txt";
        json_db_name = group_num + ".json";
        toolbar_text.setText(institut + ": " + group_num);
        src_file = intent.getStringExtra("link");


        if(src_file.equals("")){
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



        Integer JJJ = date.size();

        Log.i("размер даты", String.valueOf(JJJ));

        if(JJJ == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Расписания для этой группы нет в кэше/на сайте!", Toast.LENGTH_SHORT);
            toast.show();
        }else{

            String date_temp = date.get(0);

            Integer current_day = 1;
            Integer buffer_day = 1;

            for(int j = 0; j < date.size(); j++){


                String jj = String.valueOf(j);
                Log.i("J_from_looper", jj);

                if(date_temp.equals(date.get(j))){

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



                    if(f_num != s_num){
                        day_number++;
                    }
                }


                final View view = getLayoutInflater().inflate(R.layout.fragment1, null);

                final RelativeLayout bg_par = (RelativeLayout) view.findViewById(R.id.bg_par);

                TextView start_par = (TextView) view.findViewById(R.id.start);
                start_par.setText(start.get(j));

                TextView end_par = (TextView) view.findViewById(R.id.end);
                end_par.setText(end.get(j));

                TextView name_of_par = (TextView) view.findViewById(R.id.name_of_par);
                name_of_par.setText(par_names.get(j));

                TextView docent = (TextView) view.findViewById(R.id.docent);
                //                Log.i("Prepod_Name",prepod.get(j));

                Integer ps = prepod.size();

                if(j == ps || j > ps){
                    docent.setText(" ");
                } else{
                    if(prepod.get(j)!= null) {
                        docent.setText(prepod.get(j));
                    } else{
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

                LinearLayout dot1 = (LinearLayout) findViewById(R.id.dot1);
                LinearLayout dot2 = (LinearLayout) findViewById(R.id.dot2);
                LinearLayout dot3 = (LinearLayout) findViewById(R.id.dot3);
                LinearLayout dot4 = (LinearLayout) findViewById(R.id.dot4);
                LinearLayout dot5 = (LinearLayout) findViewById(R.id.dot5);
                LinearLayout dot6 = (LinearLayout) findViewById(R.id.dot6);
                LinearLayout dot7 = (LinearLayout) findViewById(R.id.dot7);
                LinearLayout dot8 = (LinearLayout) findViewById(R.id.dot8);
                LinearLayout dot9 = (LinearLayout) findViewById(R.id.dot9);
                LinearLayout dot10 = (LinearLayout) findViewById(R.id.dot10);
                LinearLayout dot11 = (LinearLayout) findViewById(R.id.dot11);
                LinearLayout dot12 = (LinearLayout) findViewById(R.id.dot12);






                DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

                String date1 = df1.parse(DATE).toString();






                TextView auditoria = (TextView) view.findViewById(R.id.auditoria);
                auditoria.setText(location.get(j));




                if(     curr_dbl >= start_dbl &&
                        curr_dbl <= end_dbl &&
                        dateText.equals(DATE)){

                    int currentNightMode = getResources().getConfiguration().uiMode
                            & Configuration.UI_MODE_NIGHT_MASK;

                    mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

                    if (mSettings.contains(APP_PREFERENCES_THEME)) {

                        String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "auto");

                        if(!mCounter.equals("auto") && !mCounter.equals("white") && !mCounter.equals("black")){
                            mCounter = "auto";
                        }

                        switch (mCounter) {
                            case "white":
                                bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                                break;
                            case "black":
                                bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                break;
                            case "pink":
                                break;
                            case "auto":
                                switch (currentNightMode) {
                                    case Configuration.UI_MODE_NIGHT_NO:
                                        bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                                        break;
                                    case Configuration.UI_MODE_NIGHT_YES:
                                        bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                        break;
                                    default:
                                        bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                                        break;
                                    // We don't know what mode we're in, assume notnight
                                }
                                break;
                        }
                    }

                }






                TextView nedel1 = (TextView) findViewById(R.id.second_nedel1);
                TextView nedel2 = (TextView) findViewById(R.id.second_nedel2);
                TextView nedel3 = (TextView) findViewById(R.id.second_nedel3);
                TextView nedel4 = (TextView) findViewById(R.id.second_nedel4);
                TextView nedel5 = (TextView) findViewById(R.id.second_nedel5);
                TextView nedel6 = (TextView) findViewById(R.id.second_nedel6);
                TextView nedel7 = (TextView) findViewById(R.id.second_nedel7);
                TextView nedel8 = (TextView) findViewById(R.id.second_nedel8);
                TextView nedel9 = (TextView) findViewById(R.id.second_nedel9);
                TextView nedel10 = (TextView) findViewById(R.id.second_nedel10);
                TextView nedel11 = (TextView) findViewById(R.id.second_nedel11);
                TextView nedel12 = (TextView) findViewById(R.id.second_nedel12);



                allEds.add(view);

                Integer temp_day_number = day_number - 1;

                switch (day_number) {
                    case 1:

                        TextView date_activ1 = (TextView)findViewById(R.id.date1);
                        date_activ1.setText(date.get(j));

                        TextView daaaay1 = (TextView) findViewById(R.id.daaaay1);

                        if(date1.startsWith("Mon")){
                            daaaay1.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay1.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay1.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay1.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay1.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay1.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot1.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        first_day.addView(view);

                        break;
                    case 2:

                        TextView date_activ2 = (TextView)findViewById(R.id.date2);
                        date_activ2.setText(date.get(j));

                        TextView daaaay2 = (TextView) findViewById(R.id.daaaay2);


                        if(date1.startsWith("Mon")){
                            daaaay2.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay2.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay2.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay2.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay2.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay2.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot2.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }


                        second_day.addView(view);

                        break;
                    case 3:

                        TextView date_activ3 = (TextView)findViewById(R.id.date3);
                        date_activ3.setText(date.get(j));

                        TextView daaaay3 = (TextView) findViewById(R.id.daaaay3);


                        if(date1.startsWith("Mon")){
                            daaaay3.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay3.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay3.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay3.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay3.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay3.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot3.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }


                        day3.addView(view);

                        break;
                    case 4:

                        TextView date_activ4 = (TextView)findViewById(R.id.date4);
                        date_activ4.setText(date.get(j));

                        TextView daaaay4 = (TextView) findViewById(R.id.daaaay4);


                        if(date1.startsWith("Mon")){
                            daaaay4.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay4.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay4.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay4.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay4.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay4.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot4.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day4.addView(view);

                        break;
                    case 5:
                        TextView date_activ5 = (TextView)findViewById(R.id.date5);
                        date_activ5.setText(date.get(j));

                        TextView daaaay5 = (TextView) findViewById(R.id.daaaay5);


                        if(date1.startsWith("Mon")){
                            daaaay5.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay5.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay5.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay5.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay5.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay5.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot5.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day5.addView(view);

                        break;
                    case 6:
                        TextView date_activ6 = (TextView)findViewById(R.id.date6);
                        date_activ6.setText(date.get(j));

                        TextView daaaay6 = (TextView) findViewById(R.id.daaaay6);



                        if(date1.startsWith("Mon")){
                            daaaay6.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay6.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay6.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay6.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay6.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay6.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot6.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day6.addView(view);

                        break;
                    case 7:
                        TextView date_activ7 = (TextView)findViewById(R.id.date7);
                        date_activ7.setText(date.get(j));

                        TextView daaaay7 = (TextView) findViewById(R.id.daaaay7);

                        if(date1.startsWith("Mon")){
                            daaaay7.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay7.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay7.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay7.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay7.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay7.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot7.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            Log.i("111", "неделя кончилась");
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    Log.i("111", "5 день");
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    Log.i("111", "6 день");
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    Log.i("111", "7 день");
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day7.addView(view);

                        break;
                    case 8:
                        TextView date_activ8 = (TextView)findViewById(R.id.date8);
                        date_activ8.setText(date.get(j));

                        TextView daaaay8 = (TextView) findViewById(R.id.daaaay8);

                        if(date1.startsWith("Mon")){
                            daaaay8.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay8.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay8.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay8.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay8.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay8.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot8.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day8.addView(view);

                        break;
                    case 9:
                        TextView date_activ9 = (TextView)findViewById(R.id.date9);
                        date_activ9.setText(date.get(j));


                        TextView daaaay9 = (TextView) findViewById(R.id.daaaay9);

                        if(date1.startsWith("Mon")){
                            daaaay9.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay9.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay9.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay9.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay9.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay9.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot9.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day9.addView(view);

                        break;
                    case 10:
                        TextView date_activ10 = (TextView)findViewById(R.id.date10);
                        date_activ10.setText(date.get(j));

                        TextView daaaay10 = (TextView) findViewById(R.id.daaaay10);

                        if(date1.startsWith("Mon")){
                            daaaay10.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay10.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay10.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay10.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay10.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay10.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot10.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day10.addView(view);

                        break;
                    case 11:
                        TextView date_activ11 = (TextView)findViewById(R.id.date11);
                        date_activ11.setText(date.get(j));

                        TextView daaaay11 = (TextView) findViewById(R.id.daaaay11);

                        if(date1.startsWith("Mon")){
                            daaaay11.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay11.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay11.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay11.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay11.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay11.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot11.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day11.addView(view);

                        break;
                    case 12:
                        TextView date_activ12 = (TextView)findViewById(R.id.date12);
                        date_activ12.setText(date.get(j));

                        TextView daaaay12 = (TextView) findViewById(R.id.daaaay12);

                        if(date1.startsWith("Mon")){
                            daaaay12.setText("Понедельник");
                            current_day = 1;
                        }
                        if(date1.startsWith("Tue")){
                            daaaay12.setText("Вторник");
                            current_day = 2;
                        }
                        if(date1.startsWith("Wed")){
                            daaaay12.setText("Среда");
                            current_day = 3;
                        }
                        if(date1.startsWith("Thu")){
                            daaaay12.setText("Четверг");
                            current_day = 4;
                        }
                        if(date1.startsWith("Fri")){
                            daaaay12.setText("Пятница");
                            current_day = 5;
                        }
                        if(date1.startsWith("Sat")){
                            daaaay12.setText("Суббота");
                            current_day = 6;
                        }

                        if (dateText.equals(DATE)){
                            dot12.setVisibility(View.VISIBLE);
                        }

                        if(current_day < buffer_day){
                            switch (temp_day_number){
                                case 1:
                                    nedel1.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    nedel2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    nedel3.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    nedel4.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    nedel5.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    nedel6.setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    nedel7.setVisibility(View.VISIBLE);
                                    break;
                                case 8:
                                    nedel8.setVisibility(View.VISIBLE);
                                    break;
                                case 9:
                                    nedel9.setVisibility(View.VISIBLE);
                                    break;
                                case 10:
                                    nedel10.setVisibility(View.VISIBLE);
                                    break;
                                case 11:
                                    nedel11.setVisibility(View.VISIBLE);
                                    break;
                                case 12:
                                    nedel12.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }

                        day12.addView(view);

                        break;
                }


                buffer_day = current_day;

            }

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

            progressBar.setVisibility(View.GONE);
            day12.setVisibility(View.VISIBLE);
            day11.setVisibility(View.VISIBLE);
            day10.setVisibility(View.VISIBLE);
            day9.setVisibility(View.VISIBLE);
            day8.setVisibility(View.VISIBLE);
            day7.setVisibility(View.VISIBLE);
            day6.setVisibility(View.VISIBLE);
            day5.setVisibility(View.VISIBLE);
            day4.setVisibility(View.VISIBLE);
            day3.setVisibility(View.VISIBLE);
            second_day.setVisibility(View.VISIBLE);
            first_day.setVisibility(View.VISIBLE);


            Integer number_of_day = day_number;

            TextView second_nedel = (TextView) findViewById(R.id.second_nedel1);

            if(number_of_day != 12){
                if(number_of_day == 11){
                    day12.setVisibility(View.GONE);
                }
                if(number_of_day == 10) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                }
                if(number_of_day == 9) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                }
                if(number_of_day == 8) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                }
                if(number_of_day == 7) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                }
                if(number_of_day == 6) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);
                }
                if(number_of_day == 5) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    day6.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if(number_of_day == 4) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    day6.setVisibility(View.GONE);
                    day5.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if(number_of_day == 3) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    day6.setVisibility(View.GONE);
                    day5.setVisibility(View.GONE);
                    day4.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if(number_of_day == 2) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    day6.setVisibility(View.GONE);
                    day5.setVisibility(View.GONE);
                    day4.setVisibility(View.GONE);
                    day3.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

                }
                if(number_of_day == 1) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    day6.setVisibility(View.GONE);
                    day5.setVisibility(View.GONE);
                    day4.setVisibility(View.GONE);
                    day3.setVisibility(View.GONE);
                    second_day.setVisibility(View.GONE);
                    second_nedel.setVisibility(View.GONE);

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