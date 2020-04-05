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



import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import android.content.Intent;



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



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button aaa = (Button) findViewById(R.id.aaa);
            aaa.setOnClickListener(this);

            Button fuck = (Button) findViewById(R.id.FUCK);
        fuck.setOnClickListener(this);

        TextView first = (TextView) findViewById(R.id.first);
        TextView second = (TextView) findViewById(R.id.second);



    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.aaa:
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
                Downloader();


                break;
            case R.id.FUCK:
                Intent intent = new Intent(MainActivity.this, FUCKTABLE.class);
                startActivity(intent);
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

                Downloader();
                break;
        }
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


        while(in.hasNextLine()){

            s = in.nextLine();
            String words[] = s.split(":");
            String word = words[0];

//            Log.i("***", "НАШЁЛ ВОТ ЭТО:" + words[0]);


            if (word.equals(group_num))
            {
//                Log.i("YRAAAA", " " + "провалился внутрь");
                String words_line[] = words[1].split(",");

                Log.i("!!!", "номер группы: " + words_line[0]);
                Log.i("!!!", "Место учёбы: " + words_line[1]);
            }

            if (word.equals(prep))
            {
                if (words.length > 1 && words[1] != null) {
                    String words_line[] = words[1].split(" ");

                    String prepodav = "";
                    for (int i = 0; i <= words_line.length - 1; i++) {
                        prepodav += words_line[i] + " ";
                    }
                    Log.i("!!!", "преподаватель: " + prepodav);
                }
            }

            if (word.equals(location))
            {
                if (words.length > 1 && words[1] != null) {
                    String words_line[] = words[1].split(" ");
                    Log.i("!!!", "Аудитория: " + words_line[0]);
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
                    Log.i("!!!", "Пара: " + par);
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

                    Log.i("!!!", "Дата: " + d + "." + m + "." + y);
                    Log.i("!!!", "Начало пары: "  + t);
                }
            }

            if (word.equals(end))
            {
                if (words.length > 1 && words[1] != null) {
                    String end_time = words[1];
                    char[] end_t = end_time.toCharArray(); //преобразования слова в массив символов


                    String t = new String(end_t, 9, 2) + ":" + new String(end_t, 11, 2);
                    Log.i("!!!", "Конец пары: "  + t);
                }
            }




        }
        in.close();
    }



}