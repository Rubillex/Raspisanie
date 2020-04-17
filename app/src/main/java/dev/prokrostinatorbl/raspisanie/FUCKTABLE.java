package dev.prokrostinatorbl.raspisanie;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
















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
            setContentView(R.layout.activity_fucktable);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            }

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
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "561":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "563":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "565":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "566":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "567":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "568":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "571":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "571M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "572aM":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "572bM":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "573":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "573M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "574M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "575":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "576":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "577":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "578":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "581M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "582bM":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "583":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "583M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "584M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "587":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "588":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "591":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "591M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "592aM":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "592bM":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "593":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "593M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "594M":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "595":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "597":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;
                case "598":
                    src_file = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
                    break;

            }



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
                                Parser();
                                Log.i("header","онлайн мод");
                            }
                        if (msg.what == 2)
                            {
                                Parser();
                                toolbar_text.setText(institut + ": " + group_num + " (оффлайн)");
                                Log.i("header","оффлайн мод");
                            }
                        }
                    };

    }






    private void Parser(){

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

                    Integer ifka = f_num - s_num;

                    if(ifka>1 && ifka < 7){
                        day_number+= f_num - s_num - 1;
                    } else {
                        if(ifka > 7){
                            if(day_number!= 6 && day_number!=12){
                                day_number+= f_num;
                            } else{
                                day_number+= f_num - 1;
                            }
                        }
                        else {
                            day_number+= f_num - s_num;
                        }
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



                TextView auditoria = (TextView) view.findViewById(R.id.auditoria);
                auditoria.setText(location.get(j));


                allEds.add(view);



                switch (day_number) {
                    case 1:

                        TextView date_activ1 = (TextView)findViewById(R.id.date1);
                        date_activ1.setText(date.get(j));

                        first_day.addView(view);

                        break;
                    case 2:

                        TextView date_activ2 = (TextView)findViewById(R.id.date2);
                        date_activ2.setText(date.get(j));
                        second_day.addView(view);

                        break;
                    case 3:

                        TextView date_activ3 = (TextView)findViewById(R.id.date3);
                        date_activ3.setText(date.get(j));
                        day3.addView(view);

                        break;
                    case 4:

                        TextView date_activ4 = (TextView)findViewById(R.id.date4);
                        date_activ4.setText(date.get(j));
                        day4.addView(view);

                        break;
                    case 5:
                        TextView date_activ5 = (TextView)findViewById(R.id.date5);
                        date_activ5.setText(date.get(j));
                        day5.addView(view);

                        break;
                    case 6:
                        TextView date_activ6 = (TextView)findViewById(R.id.date6);
                        date_activ6.setText(date.get(j));
                        day6.addView(view);

                        break;
                    case 7:
                        TextView date_activ7 = (TextView)findViewById(R.id.date7);
                        date_activ7.setText(date.get(j));
                        day7.addView(view);

                        break;
                    case 8:
                        TextView date_activ8 = (TextView)findViewById(R.id.date8);
                        date_activ8.setText(date.get(j));
                        day8.addView(view);

                        break;
                    case 9:
                        TextView date_activ9 = (TextView)findViewById(R.id.date9);
                        date_activ9.setText(date.get(j));
                        day9.addView(view);

                        break;
                    case 10:
                        TextView date_activ10 = (TextView)findViewById(R.id.date10);
                        date_activ10.setText(date.get(j));
                        day10.addView(view);

                        break;
                    case 11:
                        TextView date_activ11 = (TextView)findViewById(R.id.date11);
                        date_activ11.setText(date.get(j));
                        day11.addView(view);

                        break;
                    case 12:
                        TextView date_activ12 = (TextView)findViewById(R.id.date12);
                        date_activ12.setText(date.get(j));
                        day12.addView(view);

                        break;
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
