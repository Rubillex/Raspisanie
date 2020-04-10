package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.CoreComponentFactory;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.*;


import java.io.*;
import java.util.*;


import java.util.Iterator;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Iterator;




// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


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


        BlueFragment frag1;
        FragmentTransaction fTrans;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            frag1 = new BlueFragment();
            fTrans = getFragmentManager().beginTransaction();
            fTrans.add(R.id.frgmCont, frag1);
            Log.i("aaa","Фрагмен криейтед");

//            Button aaa = (Button) findViewById(R.id.aaa);
//            aaa.setOnClickListener(this);
//
//            Button fuck = (Button) findViewById(R.id.FUCK);
//            fuck.setOnClickListener(this);






        Cheker();
        Downloader();



    }

    @Override
    public void onClick(View view)
    {
//        switch (view.getId())
//        {
//            case R.id.aaa:
//                checkPermission(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    STORAGE_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.INTERNET,
//                        INTERNET_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        STORAGE_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        INTERNET_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.READ_PHONE_STATE,
//                        INTERNET_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.MANAGE_DOCUMENTS,
//                        INTERNET_PERMISSION_CODE);
//                Downloader();
//
//
//                break;
//            case R.id.FUCK:
//                Intent intent = new Intent(MainActivity.this, FUCKTABLE.class);
//                startActivity(intent);
//                checkPermission(
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        STORAGE_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.INTERNET,
//                        INTERNET_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        STORAGE_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        INTERNET_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.READ_PHONE_STATE,
//                        INTERNET_PERMISSION_CODE);
//                checkPermission(
//                        Manifest.permission.MANAGE_DOCUMENTS,
//                        INTERNET_PERMISSION_CODE);
//
//                Downloader();
//                break;
//        }
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
                Manifest.permission.READ_PHONE_STATE,
                INTERNET_PERMISSION_CODE);
        checkPermission(
                Manifest.permission.MANAGE_DOCUMENTS,
                INTERNET_PERMISSION_CODE);
    }

    private void Downloader(){
        String destFileName = "581-8.txt";
        String src = "https://www.asu.ru/timetable/students/24/2129437031/?file=2129437031.ics";
        Log.i("***", src);
        File dest = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/" + destFileName);
        new LoadFile(src, dest).start();

    }

    private void onDownloadComplete(boolean success) {
        // файл скачался, можно как-то реагировать
        Log.i("***", "************** " + success);
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
            } catch (IOException e) {
                e.printStackTrace();
                onDownloadComplete(false);
            }
        }
    }

    public static void Read() throws IOException {
        File dest = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/581-8.txt");
        Scanner in = new Scanner(dest);

        Log.i("***", "  " + "я читаю");

        String s;


        String group_num = "X-WR-CALNAME";
        String prep = "DESCRIPTION";
        String location = "LOCATION";
        String name = "SUMMARY";
        String start = "DTSTART;TZID=Asia/Krasnoyarsk";
        String end = "DTEND;TZID=Asia/Krasnoyarsk";

        JSONArray group_numb = new JSONArray();
        JSONArray prepod_name = new JSONArray();
        JSONArray auditor = new JSONArray();
        JSONArray par_name = new JSONArray();
        JSONArray start_par = new JSONArray();
        JSONArray end_par = new JSONArray();
        JSONArray date_par = new JSONArray();



        while(in.hasNextLine()){

            s = in.nextLine();
            String words[] = s.split(":");
            String word = words[0];

            try {
                // Create a new instance of a JSONObject
                File json_db = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/585.json");
                final JSONObject object = new JSONObject();



            if (word.equals(group_num))
            {
                String words_line[] = words[1].split(",");

                String number_group = words_line[0];
                group_numb.put(number_group); // в json добавляем номер группы
//                Log.i("!!!", "номер группы: " + words_line[0]);
//                Log.i("!!!", "Место учёбы: " + words_line[1]);
            }

            if (word.equals(prep))
            {
                if (words.length > 1 && words[1] != null) //Проверка: если в строке есть не пустые данные после двоеточия, то идём дальше
                {
                    String words_line[] = words[1].split(" ");

                    String prepodav = ""; //создаём пустую переменную, чтобы в неё скидывать нужные записи
                    for (int i = 0; i <= words_line.length - 1; i++) { //цикл который будет работать пока не дойдёт до значения "Длина строки" (хуй знает зачем минус один. По сути я хотел убрать скобки после ФИО препода которые "Доцент" и т.п., но не вышло, да и похуй)
                        prepodav += words_line[i] + " "; //в вышесозданную переменную мы скидываем встречающиеся слова и ставим между ними пробелы
                    }
//                    Log.i("!!!", "преподаватель: " + prepodav);
                    prepod_name.put(prepodav); // в json добавляем имя препода
                }
            }

            if (word.equals(location))
            {
                if (words.length > 1 && words[1] != null) { // аналогично проверяем существование данных
                    String words_line[] = words[1].split(" ");
//                    Log.i("!!!", "Аудитория: " + words_line[0]);
                    auditor.put(words_line[0]); // в json добавляем номер аудитории
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
//                    Log.i("!!!", "Пара: " + par);
                    par_name.put(par); // в json добавляем название пары
                }
            }




            if (word.equals(start))
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
                    date_par.put(date); // в json добавляем дату
//                    Log.i("!!!", "Начало пары: "  + t); // выводим время
                    start_par.put(t); // в json добавляем время начала пары
                }
            }

            if (word.equals(end))
            {
                if (words.length > 1 && words[1] != null) {
                    String end_time = words[1];
                    char[] end_t = end_time.toCharArray(); //преобразования слова в массив символов


                    String t = new String(end_t, 9, 2) + ":" + new String(end_t, 11, 2);
//                    Log.i("!!!", "Конец пары: "  + t);
                    end_par.put(t); // в json добавляем время конца пары
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



                    object.put("number", group_numb);
                    object.put("prepod", prepod_name);
                    object.put("location", auditor);
                    object.put("par_name", par_name);
                    object.put("start", start_par);
                    object.put("end", end_par);
                    object.put("date", date_par);


                try{
                    FileWriter file = new FileWriter(json_db); // сохраняем всё это в json
                    file.write(object.toString());
                    file.flush();
                    file.close();
//                    Log.i("***", "JSON создан");
                } catch (IOException ex){
                    ex.printStackTrace();
                }


            } catch (JSONException e) {
                Log.e("***", "Failed to create JSONObject", e);
            }

        }
        in.close();




    }



}