package dev.prokrostinatorbl.raspisanie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class institut extends Activity {

    private Handler h;
    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    public static String GROUP_LINK;
    public static String GROUP_FILE;
    public static String GROUP_JSON;
    private LinearLayout linearLayout;

    private final int USERID = 0;
    private int countID;


    public ArrayList<String> number;



    public String destFileName = "list.txt";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(APP_PREFERENCES_THEME)) {

            String mCounter = mSettings.getString(APP_PREFERENCES_THEME, "white");
            switch (mCounter) {
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

        setContentView(R.layout.fragment_institut);

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getToken();

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


    private void Downloader(){

        Log.i("!!!!!!!!!!!!", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        String src_file = "https://firebasestorage.googleapis.com/v0/b/timetable-1590248920670.appspot.com/o/list.txt?alt=media&token=710ba120-c1d6-45bc-97b2-bcbd250a0511";

//        new LoadFile().start();

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

        File dest = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/" + destFileName);
        Scanner in = new Scanner(dest);
        Log.i("!!!!!!!!!!!!", String.valueOf(dest));
        Log.i("!!!!!!!!!!!!", "CREATOR");

        String s;

        number = new ArrayList<String>();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        String buffer_name = "buffer";

        while(in.hasNextLine()){



            s = in.nextLine();
            String words[] = s.split(";");
            String word = words[0];



            if (!word.equals(buffer_name)){
                number.add(word);
                Button b = new Button(getApplicationContext());
                b.setText(word);
                b.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                b.setId(USERID + countID);
                b.setTag(USERID + countID);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resourceID = (int) v.getTag();
                        Intent intent = new Intent(institut.this, group_list.class);
                        intent.putExtra("key", number.get(resourceID));
                        startActivity(intent);
                    }
                });
                linearLayout.addView(b);
                countID++;
                buffer_name = word;
            }

        }
    }
}
