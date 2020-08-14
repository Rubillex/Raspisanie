//package dev.prokrostinatorbl.raspisanie;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Scanner;
//
//import android.accessibilityservice.AccessibilityService;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Environment;
//import android.util.Log;
//
//import com.google.gson.JsonArray;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//class MyWidget extends AppWidgetProvider {
//
//    SharedPreferences mSettings;
//
//    public static String APP_PREFERENCES;
//    public static String APP_PREFERENCES_THEME;
//
//    public static String GROUP_LINK;
//    public static String GROUP_FILE;
//    public static String GROUP_JSON;
//
//    public JSONArray group_numb;
//    public JSONArray prepod_name;
//    public JSONArray auditor;
//    public JSONArray par_name;
//    public JSONArray start_par;
//    public JSONArray end_par;
//    public JSONArray date_par;
//    public JSONArray day_js;
//    public JsonArray month_js;
//    public JsonArray year_js;
//
//
//    public ArrayList<String> number;
//    public ArrayList<String> prepod;
//    public ArrayList<String> location;
//    public ArrayList<String> par_names;
//    public ArrayList<String> start;
//    public ArrayList<String> end;
//    public ArrayList<String> date;
//
//    String link = mSettings.getString(GROUP_LINK, "");
//    String file = mSettings.getString(GROUP_FILE, "");
//    String json = mSettings.getString(GROUP_JSON, "");
//
//    final String LOG_TAG = "myLogs";
//
//    @Override
//    public void onEnabled(Context context) {
//
//        super.onEnabled(context);
//
//
//    }
//
//
//
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
//                         int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
//    }
//
//    @Override
//    public void onDeleted(Context context, int[] appWidgetIds) {
//        super.onDeleted(context, appWidgetIds);
//        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        super.onDisabled(context);
//        Log.d(LOG_TAG, "onDisabled");
//    }
//
//
//    public void Read() throws IOException {
//
//        String link = mSettings.getString(GROUP_LINK, "");
//        String file = mSettings.getString(GROUP_FILE, "");
//        String json = mSettings.getString(GROUP_JSON, "");
//
//
//
//        Downloader.Download(link, file, destFileName, from);
//
//
//        Scanner in = new Scanner(file);
//
//        Log.i("***", "  " + "я читаю");
//
//        String s;
//
//
//        String group_num = "X-WR-CALNAME";
//        String prep = "DESCRIPTION";
//        String locat = "LOCATION";
//        String name = "SUMMARY";
//        String start_js = "DTSTART;TZID=Asia/Krasnoyarsk";
//        String end_js = "DTEND;TZID=Asia/Krasnoyarsk";
//
//        group_numb = new JSONArray();
//        prepod_name = new JSONArray();
//        auditor = new JSONArray();
//        par_name = new JSONArray();
//        start_par = new JSONArray();
//        end_par = new JSONArray();
//        date_par = new JSONArray();
//
//        day_js = new JSONArray();
//        month_js = new JsonArray();
//        year_js = new JsonArray();
//
//        while(in.hasNextLine()){
//
//            s = in.nextLine();
//            String words[] = s.split(":");
//            String word = words[0];
//
//            try {
//                // Create a new instance of a JSONObject
//                File json_db = new File(json);
//                final JSONObject object = new JSONObject();
//
//
//
//                if (word.equals(group_num))
//                {
//                    String words_line[] = words[1].split(",");
//
//                    String number_group = words_line[0];
//                    number.add(number_group);
//
//                }
//
//                if (word.equals(prep))
//                {
//                    String prepodav = ""; //создаём пустую переменную, чтобы в неё скидывать нужные записи
//
//                    if (words.length > 1 && words[1] != null) //Проверка: если в строке есть не пустые данные после двоеточия, то идём дальше
//                    {
//                        String words_line[] = words[1].split(" ");
//
//
//                        for (int i = 0; i <= words_line.length - 1; i++) { //цикл который будет работать пока не дойдёт до значения "Длина строки" (хуй знает зачем минус один. По сути я хотел убрать скобки после ФИО препода которые "Доцент" и т.п., но не вышло, да и похуй)
//
//                            prepodav += words_line[i] + " "; //в вышесозданную переменную мы скидываем встречающиеся слова и ставим между ними пробелы
//
//                        }
//                        prepod.add(prepodav);
//
//                    } else {
//
//                        prepodav += "";
//                        prepod.add(prepodav);
//
//                    }
//                }
//
//                if (word.equals(locat))
//                {
//                    if (words.length > 1 && words[1] != null) { // аналогично проверяем существование данных
//                        String words_line[] = words[1].split(" ");
//
//                        location.add(words_line[0]);
//                    }   else {
//
//                        String rand = "";
//                        location.add(rand);
//
//                    }
//                }
//
//                if (word.equals(name))
//                {
//                    if (words.length > 1 && words[2] != null) {
//                        String name_par[] = words[2].split(" ");
//                        String par = "";
//                        for (int i = 1; i <= name_par.length - 1; i++) {
//                            par += name_par[i] + " ";
//                        }
//                        String paar_name[] = par.split("\\(");
//                        String paar;
//                        paar = paar_name[0];
//
//                        par_names.add(paar);
//
//
//
//
//                    }
//                }
//
//
//
//
//                if (word.equals(start_js))
//                {
//                    if (words.length > 1 && words[1] != null) {
//                        String start_time = words[1];
//                        char[] start_t = start_time.toCharArray(); //преобразования слова в массив символов
//
//                        String y = new String(start_t, 0, 4); // начиная с нулевого символа(до строки) забираем 4 символа в переменную
//                        String m = new String(start_t, 4, 2); // начиная с 4 символа забираем два символа в переменную
//                        String d = new String(start_t, 6, 2); // аналогично предыдущим
//                        String t = new String(start_t, 9, 2) + ":" + new String(start_t, 11, 2);
//                        String date = d + "." + m + "." + y;
//                        //                    Log.i("!!!", "Дата: " + date); //выводим дату
//                        //                        Log.i("AAAA", date);
//                        String buf_date = date.toString();
//                        //                        days.add(buf_date);
//                        date_par.put(date); // в json добавляем дату
//                        Log.i("JSON_DATE", date);
//                        //                        Log.i("ЗАБРАЛ ЭТУ ДАТУ: ", d);
//                        //                        month_js.add(m);
//                        //                        year_js.add(y);
//
//                        //                    Log.i("!!!", "Начало пары: "  + t); // выводим время
//                        //                        start_par.put(t); // в json добавляем время начала пары
//                        start.add(t);
//
//
//
//
//
//                    }
//                }
//
//                if (word.equals(end_js))
//                {
//                    if (words.length > 1 && words[1] != null) {
//                        String end_time = words[1];
//                        char[] end_t = end_time.toCharArray(); //преобразования слова в массив символов
//
//
//                        String t = new String(end_t, 9, 2) + ":" + new String(end_t, 11, 2);
//                        //                    Log.i("!!!", "Конец пары: "  + t);
//                        //                        end_par.put(t); // в json добавляем время конца пары
//                        end.add(t);
//                    }
//                }
//
//                object.put("date", date_par);
//
//
//                try{
//                    FileWriter js = new FileWriter(json_db); // сохраняем всё это в json
//                    js.write(object.toString());
//                    js.flush();
//                    js.close();
//                } catch (IOException ex){
//                    ex.printStackTrace();
//                }
//
//
//            } catch (JSONException e) {
//                Log.e("***", "Failed to create JSONObject", e);
//            }
//
//        }
//        in.close();
//
//
//        Log.i("@@@@@", "Read завершёл");
//
//
//
//    }
//
//
//}