package dev.prokrostinatorbl.raspisanie;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
        public RelativeLayout bg_par;

        public Integer day_number;




        private Toolbar toolbar;

        private TextView toolbar_text;

        public ArrayList<String> days;
        public ArrayList<String> months;
        public ArrayList<String> years;
        private Handler h;





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

        setContentView(R.layout.activity_fucktable);


            final TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);


        Intent intent = getIntent();
        final String group_num = intent.getStringExtra("key");
        final String institut = intent.getStringExtra("instit");

        destFileName = group_num + ".txt";
        json_db_name = group_num + ".json";
        toolbar_text.setText(institut + ": " + group_num);

        switch (group_num) {
            case "585":
                src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                break;
            case "581":
                src_file = "https://www.asu.ru/timetable/students/24/2129437028/?file=2129437028.ics";
                break;
            case "561":
                src_file = "https://www.asu.ru/timetable/students/24/2129436560/?file=2129436560.ics";
                break;
            case "563":
                src_file = "https://www.asu.ru/timetable/students/24/2129436562/?file=2129436562.ics";
                break;
            case "565":
                src_file = "https://www.asu.ru/timetable/students/24/2129436563/?file=2129436563.ics";
                break;
            case "566":
                src_file = "https://www.asu.ru/timetable/students/24/2129437286/?file=2129437286.ics";
                break;
            case "567":
                src_file = "https://www.asu.ru/timetable/students/24/2129436564/?file=2129436564.ics";
                break;
            case "568":
                src_file = "https://www.asu.ru/timetable/students/24/2129439087/?file=2129439087.ics";
                break;
            case "571":
                src_file = "https://www.asu.ru/timetable/students/24/2129436721/?file=2129436721.ics";
                break;
            case "571M":
                src_file = "";
                break;
            case "572aM":
                src_file = "";
                break;
            case "572bM":
                src_file = "";
                break;
            case "573":
                src_file = "https://www.asu.ru/timetable/students/24/2129436723/?file=2129436723.ics";
                break;
            case "573M":
                src_file = "";
                break;
            case "574M":
                src_file = "";
                break;
            case "575":
                src_file = "https://www.asu.ru/timetable/students/24/2129436724/?file=2129436724.ics";
                break;
            case "576":
                src_file = "https://www.asu.ru/timetable/students/24/2129437577/?file=2129437577.ics";
                break;
            case "577":
                src_file = "https://www.asu.ru/timetable/students/24/2129436725/?file=2129436725.ics";
                break;
            case "578":
                src_file = "https://www.asu.ru/timetable/students/24/2129439426/?file=2129439426.ics";
                break;
            case "581M":
                src_file = "";
                break;
            case "582bM":
                src_file = "";
                break;
            case "583":
                src_file = "https://www.asu.ru/timetable/students/24/2129437030/?file=2129437030.ics";
                break;
            case "583M":
                src_file = "";
                break;
            case "584M":
                src_file = "";
                break;
            case "587":
                src_file = "https://www.asu.ru/timetable/students/24/2129437032/?file=2129437032.ics";
                break;
            case "588":
                src_file = "https://www.asu.ru/timetable/students/24/2129439738/?file=2129439738.ics";
                break;
            case "591":
                src_file = "https://www.asu.ru/timetable/students/24/2129437210/?file=2129437210.ics";
                break;
            case "591M":
                src_file = "";
                break;
            case "592aM":
                src_file = "";
                break;
            case "592bM":
                src_file = "";
                break;
            case "593":
                src_file = "https://www.asu.ru/timetable/students/24/2129437214/?file=2129437214.ics";
                break;
            case "593M":
                src_file = "";
                break;
            case "594M":
                src_file = "";
                break;
            case "595":
                src_file = "https://www.asu.ru/timetable/students/24/2129437216/?file=2129437216.ics";
                break;
            case "597":
                src_file = "https://www.asu.ru/timetable/students/24/2129437218/?file=2129437218.ics";
                break;
            case "598":
                src_file = "https://www.asu.ru/timetable/students/24/2129439985/?file=2129439985.ics";
                break;

        }


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


            Downloader();

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
                        toolbar_text.setText(institut + ": " + group_num + " (оффлайн)");
                        Log.i("header","оффлайн мод");
                    }
                }
            };

        }

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
        bg_par = (RelativeLayout) findViewById(R.id.bg_par);


        Integer JJJ = date.size();

        Log.i("размер даты", String.valueOf(JJJ));

        if(JJJ == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Расписания для этой группы нет в кэше!", Toast.LENGTH_SHORT);
            toast.show();
        }else{

            String date_temp = date.get(0);

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



                String START_TIME[] = START.split(":");
                String END_TIME[] = END.split(":");


                DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String curr_date = timeFormat.format(currentDate);

                String CURRENT_DATE[] = curr_date.split(":");

                int START_PAR_HH = Integer.parseInt(START_TIME[0]);
                int START_PAR_mm = Integer.parseInt(START_TIME[1]);

                int END_PAR_HH = Integer.parseInt(END_TIME[0]);
                int END_PAR_mm = Integer.parseInt(END_TIME[1]);

                int CURRENT_DATE_HH = Integer.parseInt(CURRENT_DATE[0]);
                int CURRENT_DATE_mm = Integer.parseInt(CURRENT_DATE[1]);


                if(     CURRENT_DATE_HH >= START_PAR_HH &&
                        CURRENT_DATE_HH <= END_PAR_HH &&
                        CURRENT_DATE_mm >= START_PAR_mm &&
                        CURRENT_DATE_mm <= END_PAR_mm &&
                dateText.equals(DATE)){

                    int currentNightMode = getResources().getConfiguration().uiMode
                            & Configuration.UI_MODE_NIGHT_MASK;


                    switch (currentNightMode) {
                        case Configuration.UI_MODE_NIGHT_NO:
                            bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                            break;
                        case Configuration.UI_MODE_NIGHT_YES:
                            bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            break;
                        // Night mode is active, we're
                        default:
                            bg_par.setBackgroundColor(getResources().getColor(R.color.colorAccent_light));
                            break;
                        // We don't know what mode we're in, assume notnight
                    }

                }







                DateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");

                String date1 = df1.parse(DATE).toString();






                TextView auditoria = (TextView) view.findViewById(R.id.auditoria);
                auditoria.setText(location.get(j));





                allEds.add(view);



                switch (day_number) {
                    case 1:

                        TextView date_activ1 = (TextView)findViewById(R.id.date1);
                        date_activ1.setText(date.get(j));

                        TextView daaaay1 = (TextView) findViewById(R.id.daaaay1);

                        if(date1.startsWith("Mon")){
                            daaaay1.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay1.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay1.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay1.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay1.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay1.setText("Суббота");
                        }

                        first_day.addView(view);

                        break;
                    case 2:

                        TextView date_activ2 = (TextView)findViewById(R.id.date2);
                        date_activ2.setText(date.get(j));

                        TextView daaaay2 = (TextView) findViewById(R.id.daaaay2);


                        if(date1.startsWith("Mon")){
                            daaaay2.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay2.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay2.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay2.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay2.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay2.setText("Суббота");
                        }


                        second_day.addView(view);

                        break;
                    case 3:

                        TextView date_activ3 = (TextView)findViewById(R.id.date3);
                        date_activ3.setText(date.get(j));

                        TextView daaaay3 = (TextView) findViewById(R.id.daaaay3);


                        if(date1.startsWith("Mon")){
                            daaaay3.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay3.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay3.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay3.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay3.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay3.setText("Суббота");
                        }


                        day3.addView(view);

                        break;
                    case 4:

                        TextView date_activ4 = (TextView)findViewById(R.id.date4);
                        date_activ4.setText(date.get(j));

                        TextView daaaay4 = (TextView) findViewById(R.id.daaaay4);


                        if(date1.startsWith("Mon")){
                            daaaay4.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay4.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay4.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay4.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay4.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay4.setText("Суббота");
                        }

                        day4.addView(view);

                        break;
                    case 5:
                        TextView date_activ5 = (TextView)findViewById(R.id.date5);
                        date_activ5.setText(date.get(j));

                        TextView daaaay5 = (TextView) findViewById(R.id.daaaay5);


                        if(date1.startsWith("Mon")){
                            daaaay5.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay5.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay5.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay5.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay5.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay5.setText("Суббота");
                        }

                        day5.addView(view);

                        break;
                    case 6:
                        TextView date_activ6 = (TextView)findViewById(R.id.date6);
                        date_activ6.setText(date.get(j));

                        TextView daaaay6 = (TextView) findViewById(R.id.daaaay6);



                        if(date1.startsWith("Mon")){
                            daaaay6.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay6.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay6.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay6.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay6.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay6.setText("Суббота");
                        }

                        day6.addView(view);

                        break;
                    case 7:
                        TextView date_activ7 = (TextView)findViewById(R.id.date7);
                        date_activ7.setText(date.get(j));

                        TextView daaaay7 = (TextView) findViewById(R.id.daaaay7);

                        if(date1.startsWith("Mon")){
                            daaaay7.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay7.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay7.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay7.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay7.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay7.setText("Суббота");
                        }

                        day7.addView(view);

                        break;
                    case 8:
                        TextView date_activ8 = (TextView)findViewById(R.id.date8);
                        date_activ8.setText(date.get(j));

                        TextView daaaay8 = (TextView) findViewById(R.id.daaaay8);

                        if(date1.startsWith("Mon")){
                            daaaay8.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay8.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay8.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay8.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay8.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay8.setText("Суббота");
                        }
                        day8.addView(view);

                        break;
                    case 9:
                        TextView date_activ9 = (TextView)findViewById(R.id.date9);
                        date_activ9.setText(date.get(j));


                        TextView daaaay9 = (TextView) findViewById(R.id.daaaay9);

                        if(date1.startsWith("Mon")){
                            daaaay9.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay9.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay9.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay9.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay9.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay9.setText("Суббота");
                        }

                        day9.addView(view);

                        break;
                    case 10:
                        TextView date_activ10 = (TextView)findViewById(R.id.date10);
                        date_activ10.setText(date.get(j));

                        TextView daaaay10 = (TextView) findViewById(R.id.daaaay10);

                        if(date1.startsWith("Mon")){
                            daaaay10.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay10.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay10.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay10.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay10.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay10.setText("Суббота");
                        }

                        day10.addView(view);

                        break;
                    case 11:
                        TextView date_activ11 = (TextView)findViewById(R.id.date11);
                        date_activ11.setText(date.get(j));

                        TextView daaaay11 = (TextView) findViewById(R.id.daaaay11);

                        if(date1.startsWith("Mon")){
                            daaaay11.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay11.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay11.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay11.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay11.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay11.setText("Суббота");
                        }

                        day11.addView(view);

                        break;
                    case 12:
                        TextView date_activ12 = (TextView)findViewById(R.id.date12);
                        date_activ12.setText(date.get(j));

                        TextView daaaay12 = (TextView) findViewById(R.id.daaaay12);

                        if(date1.startsWith("Mon")){
                            daaaay12.setText("Понедельник");
                        }
                        if(date1.startsWith("Tue")){
                            daaaay12.setText("Вторник");
                        }
                        if(date1.startsWith("Wed")){
                            daaaay12.setText("Среда");
                        }
                        if(date1.startsWith("Thu")){
                            daaaay12.setText("Четверг");
                        }
                        if(date1.startsWith("Fri")){
                            daaaay12.setText("Пятница");
                        }
                        if(date1.startsWith("Sat")){
                            daaaay12.setText("Суббота");
                        }

                        day12.addView(view);

                        break;
                }

            }

            Integer number_of_day = day_number;

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
                }
                if(number_of_day == 5) {
                    day12.setVisibility(View.GONE);
                    day11.setVisibility(View.GONE);
                    day10.setVisibility(View.GONE);
                    day9.setVisibility(View.GONE);
                    day8.setVisibility(View.GONE);
                    day7.setVisibility(View.GONE);
                    day6.setVisibility(View.GONE);
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
                }
            }




            day_number = 1;

        }





    }



    private void Downloader(){
        //        String destFileName = "585.txt";
        Log.i("***", src_file);
        File dest = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/" + destFileName);
        new LoadFile(src_file, dest).start();

    }

    private void onDownloadComplete(boolean success) {
        // файл скачался, можно как-то реагировать
        Log.i("***", "СКАЧАЛ " + success);
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
                FileUtils.copyURLToFile(new URL(src), dest);
                onDownloadComplete(true);

                Read();
                new Reader().start();


            } catch (IOException e) {
                e.printStackTrace();
                onDownloadComplete(false);


                try {
                    Log.i("header","Read() запущен в оффлайн моде");
                    Read();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                new Reader_offline().start();
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
    private class Reader_offline extends Thread {


        Reader_offline(){
        }

        @Override
        public void run() {
            h.sendEmptyMessage(2);
        }
    }

    public void Read() throws IOException {
        File dest = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/" + destFileName);
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



        //        ArrayList<String> number = new ArrayList<String>();
        //        ArrayList<String> prepod = new ArrayList<String>();
        //        ArrayList<String> location = new ArrayList<String>();
        //        ArrayList<String> par_names = new ArrayList<String>();
        //        ArrayList<String> start = new ArrayList<String>();
        //        ArrayList<String> end = new ArrayList<String>();
        //        ArrayList<String> date = new ArrayList<String>();
        //
        //        ArrayList<String> days = new ArrayList<>();
        //        ArrayList<String> months = new ArrayList<>();
        //        ArrayList<String> years = new ArrayList<>();
        //

        while(in.hasNextLine()){

            s = in.nextLine();
            String words[] = s.split(":");
            String word = words[0];

            try {
                // Create a new instance of a JSONObject
                File json_db = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/" + json_db_name);
                final JSONObject object = new JSONObject();



                if (word.equals(group_num))
                {
                    String words_line[] = words[1].split(",");

                    String number_group = words_line[0];
                    //                    group_numb.put(number_group);
                    number.add(number_group);

                    //                    group_numb.put(number_group); // в json добавляем номер группы
                    //                Log.i("!!!", "номер группы: " + words_line[0]);
                    //                Log.i("!!!", "Место учёбы: " + words_line[1]);
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
                        //                    Log.i("!!!", "преподаватель: " + prepodav);
                        //                        prepod_name.put(prepodav); // в json добавляем имя препода
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
                        //                    Log.i("!!!", "Аудитория: " + words_line[0]);
                        //                        auditor.put(words_line[0]); // в json добавляем номер аудитории

                        location.add(words_line[0]);
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
                        //                    Log.i("!!!", "Пара: " + par);
                        //                        par_name.put(paar); // в json добавляем название пары

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
                //
                //                JSONArray group_numb = new JSONArray();
                //                JSONArray prepod_name = new JSONArray();
                //                JSONArray auditor = new JSONArray();
                //                JSONArray par_name = new JSONArray();
                //                JSONArray start_par = new JSONArray();
                //                JSONArray end_par = new JSONArray();
                //                JSONArray date_par = new JSONArray();



                //                object.put("number", group_numb);
                //                object.put("prepod", prepod_name);
                //                object.put("location", auditor);
                //                object.put("par_name", par_name);
                //                object.put("start", start_par);
                //                object.put("end", end_par);
                object.put("date", date_par);


                try{
                    FileWriter file = new FileWriter(json_db); // сохраняем всё это в json
                    file.write(object.toString());
                    file.flush();
                    file.close();
//                        Log.i("***", "JSON создан");
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