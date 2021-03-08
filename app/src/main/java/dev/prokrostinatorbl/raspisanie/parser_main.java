package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parser_main extends AppCompatActivity {

    private Document doc;
    private Elements elements;

    private Elements elements_day;
    private Elements elements_time;

    private Thread secThread;
    private Runnable runnable;

    private Thread secThread_group;
    private Runnable runnable_group;

    private Thread Thread_work;
    private Runnable runnable_work;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parser_main);
        init();

        init_2();

        init_3();
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
            doc = Jsoup.connect("https://www.asu.ru/timetable/students/").get();
            elements = doc.getElementsByClass("link_ptr_left margin_bottom");


            ArrayList name_fac = new ArrayList<String>();
            ArrayList href_fac = new ArrayList<String>();

            Element name;
            Element link_full;
            Elements link;

            /**
             *
             * КОНСТРУКЦИЯ НИЖЕ ДОСТАЁТ С САЙТА ВСЕ ИМЕНА ФАКУЛЬТЕТОВ И ССЫЛКИ НА ИХ ГРУППЫ
             * И СКЛАДЫВАЕТ В ArrayList name_fac, href_fac
             *
             */


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





        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init_2(){
        runnable_group = new Runnable() {
            @Override
            public void run() {
                getGroup();
            }
        };
        secThread_group = new Thread(runnable_group);
        secThread_group.start();
    }

    private void getGroup(){
        try {
            doc = Jsoup.connect("https://www.asu.ru/timetable/students/24/").get();
            elements = doc.getElementsByClass("link_ptr_left margin_bottom");

            ArrayList name_group = new ArrayList<String>();
            ArrayList href_group = new ArrayList<String>();

            Element name;
            Element link_full;
            Elements link;

            /**
             *
             * КОНСТРУКЦИЯ НИЖЕ ДОСТАЁТ С САЙТА ВСЕ ИМЕНА ФАКУЛЬТЕТОВ И ССЫЛКИ НА ИХ ГРУППЫ
             * И СКЛАДЫВАЕТ В ArrayList name_fac, href_fac
             *
             */


            for (int i = 0; i < elements.size(); i++){
                name = elements.get(i);

                String temp = name.text();

                name_group.add(temp);




                link_full = elements.get(i);
                link = link_full.select("div.link_ptr_left.margin_bottom > a");
                String url = link.attr("href");

                href_group.add(url);

//                Log.d("MyLog", String.valueOf(name_group.get(i)) + "   " + String.valueOf(href_group.get(i)));

            }





        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void getWork(){
        try {
            ArrayList name_group = new ArrayList<String>();
            ArrayList href_group = new ArrayList<String>();

            ArrayList day = new ArrayList<String>();
            ArrayList date = new ArrayList<String>();

            ArrayList time_par = new ArrayList<String>();
            ArrayList name_par = new ArrayList<String>();
            ArrayList audit = new ArrayList<String>();
            ArrayList prepod = new ArrayList<String>();

            Element mday;
            Element mdate;
            Element mtime;
            Element mname;
            Element maudit;
            Element mprepod;

            Element name;
            Element link_full;
            Elements link;


            doc = Jsoup.connect("https://www.asu.ru/timetable/students/24/2129437031/").get();
            elements = doc.getElementsByClass("schedule-time");

            elements_day = doc.getElementsByClass("schedule-date");

            elements_time = doc.getElementsByClass("date t_small_x t_gray_light");

//            Log.d("MyLog", String.valueOf(elements_day.size()));

            int g = 0;

            Integer bufer_time = 0;


            for (int i = 0; i < elements.size(); i++){


                mname = elements.get(i);

//                Log.d("MyLog", elements_day.get(g).text());

                // ПАРА
                Element ite = mname.getElementsByAttribute("width").first();

//                Log.d("MyLog", ite.text());

                // ВРЕМЯ
                String time_paaar = mname.select("td").get(1).text();

                String[] time_split = time_paaar.split(":");

                Integer first = Integer.valueOf(time_split[0]);

//                Log.d("MyLog", String.valueOf(first));

                if (i == 0){
                    bufer_time = first;
                }

                if (first < bufer_time){
                    bufer_time = first;
                    g++;
                }

                Log.d("MyLog", ite.text() + " " + time_paaar + " " + elements_day.get(g).text());
//                Log.d("MyLog", "Time: " + time_paaar);


                bufer_time = first;
            }





        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}