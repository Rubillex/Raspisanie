package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Favorite extends Activity implements View.OnClickListener {


    private Handler h;
    public static String APP_PREFERENCES;
    public static String APP_PREFERENCES_THEME; // выбранная тема

    public static String APP_PREFERENCES_STARTFRAME;
    public static String APP_PREFERENCES_START_UNI;
    public static String APP_PREFERENCES_START_GROUP;
    public static String APP_PREFERENCES_LINK;
    public static Boolean APP_PREFERENCES_FIRST;

    public int Current_group = 0;

    DatabaseFavorite dbHelper;

    public static String Snack_text = "";

    private LinearLayout linearLayout;

    private int USERID = 0;
    private int countID;

    private Toolbar toolbar;

    public String institut_name;

    Dialog delete;

    public ArrayList<String> number;
    public ArrayList<String> link_list;
    public ArrayList<String> institut;

    private List<View> allEds;



    public String destFileName = "list.txt";
    SharedPreferences mSettings;
    TextView delete_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Saved.init(getApplicationContext());
        new Saved().load_favorite();


        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        switch (APP_PREFERENCES_THEME) {
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

        setContentView(R.layout.favorite_group);

        //-----------------------------------------------------------------------------------------
        delete = new Dialog(this);
        delete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delete.setContentView(R.layout.dialog_delete);
        delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        delete_text = (TextView) delete.findViewById(R.id.delete_text);

        //-----------------------------------------------------------------------------------------

        delete.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.dismiss();
            }
        });

        delete.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.dismiss();

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();

                String NAME = institut.get(Current_group) + ";" + number.get(Current_group);

                Log.i("FAVORITE_DELETE", NAME);

                contentValues.put(DatabaseFavorite.KEY_NAME, NAME);

                Cursor cursor = database.query(DatabaseFavorite.TABLE_FAVORITE, null, null, null, null, null, null);


                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(DatabaseFavorite.KEY_NAME);
                    do {

                        if(cursor.getString(nameIndex).equals(NAME)){

                            database.delete(DatabaseFavorite.TABLE_FAVORITE, DatabaseFavorite.KEY_NAME + " = ?", new String[]{String.valueOf(cursor.getString(nameIndex))});
                            Log.i("FAVORITE_DELETE", "удолил");
                        }

                    } while (cursor.moveToNext());
                }
                cursor.close();

                recreate();

            }
        });

        //-----------------------------------------------------------------------------------------

        toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
        TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        toolbar_text.setText("Избранное");

        //-----------------------------------------------------------------------------------------

        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("back", "true");
                startActivity(intent);
            }
        });

        //-----------------------------------------------------------------------------------------


        new Reader().start();

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

    private class Reader extends Thread {


        Reader(){
        }

        @Override
        public void run() {
            h.sendEmptyMessage(1);
        }
    }

    public void Creator() throws FileNotFoundException {

        Context c = getApplicationContext();
        File file = new File(c.getFilesDir(), "/files");

        File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

        File dest = new File(file + destFileName);
        Scanner in = new Scanner(dest);

        String s;

        number = new ArrayList<String>();
        link_list = new ArrayList<String>();

        institut = new ArrayList<String>();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);



        allEds = new ArrayList<View>();

        dbHelper = new DatabaseFavorite(this, "favorite_list");

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DatabaseFavorite.TABLE_FAVORITE, null, null, null, null, null, null);

        ArrayList<String> favorite_group;

        favorite_group = new ArrayList<String>();


        String NAME_DATA = "";


        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseFavorite.KEY_NAME);
            do {
                favorite_group.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");
        cursor.close();

        while(in.hasNextLine()){
            s = in.nextLine();
            String words[] = s.split(";");
            String word = words[0];
            String word2 = words[1];

            NAME_DATA = word + ";" + word2;

            for (int g = 0; g < favorite_group.size(); g++){
                if (NAME_DATA.equals(favorite_group.get(g))) {
                    number.add(words[1]);
                    Log.i("WORDS SIZE", String.valueOf(words.length));
                    Log.i("WORDS 2", words[2]);
                    link_list.add(words[2]);
                    institut.add(word);

                }
            }

        }
        in.close();

        Double Row_double = Double.valueOf(number.size() / 3);
        long stop = Math.round(Row_double);

        Log.i("STEP", String.valueOf(stop));

        for (int i = 0; i < number.size(); i+=3){

            Log.i("STEP", "&&&&");

            final View view = getLayoutInflater().inflate(R.layout.fragment_group_num, null);


            Button group1 = (Button) view.findViewById(R.id.group1);
            Button group2 = (Button) view.findViewById(R.id.group2);
            Button group3 = (Button) view.findViewById(R.id.group3);

            group1.setText(number.get(i));
            group1.setTag(USERID + countID);

            group1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int resourceID = (int) v.getTag();
                    Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
                    intent.putExtra("link", link_list.get(resourceID));
                    intent.putExtra("instit", institut.get(resourceID));
                    intent.putExtra("key", number.get(resourceID));
                    intent.putExtra("from", "favorite");
                    startActivity(intent);
                }
            });

            group1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int resourceID = (int) v.getTag();
                    Current_group = resourceID;
                    delete_text.setText("Вы действительно хотите удалить " + number.get(resourceID) + " из избранного?");
                    delete.show();

                    return true;
                }
            });


            countID++;
            if (i+1 < number.size()){
                group2.setText(number.get(i+1));
                group2.setTag(USERID + countID);

                group2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resourceID = (int) v.getTag();
                        Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
                        intent.putExtra("link", link_list.get(resourceID));
                        intent.putExtra("instit", institut.get(resourceID));
                        intent.putExtra("key", number.get(resourceID));
                        intent.putExtra("from", "favorite");
                        startActivity(intent);
                    }
                });

                group2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int resourceID = (int) v.getTag();
                        Current_group = resourceID;
                        delete_text.setText("Вы действительно хотите удалить " + number.get(resourceID) + " из избранного?");
                        delete.show();
                        return true;
                    }
                });

                countID++;
            } else{
                group2.setVisibility(View.GONE);
            }
            if (i+2 < number.size()){
                group3.setText(number.get(i+2));
                group3.setTag(USERID + countID);

                group3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resourceID = (int) v.getTag();
                        Intent intent = new Intent(getApplicationContext(), FUCKTABLE.class);
                        intent.putExtra("link", link_list.get(resourceID));
                        intent.putExtra("instit", institut.get(resourceID));
                        intent.putExtra("key", number.get(resourceID));
                        intent.putExtra("from", "favorite");
                        startActivity(intent);
                    }
                });

                group3.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int resourceID = (int) v.getTag();
                        Current_group = resourceID;
                        delete_text.setText("Вы действительно хотите удалить " + number.get(resourceID) + " из избранного?");
                        delete.show();
                        return true;
                    }
                });

                countID++;
            } else{
                group3.setVisibility(View.GONE);
            }
            allEds.add(view);
            linearLayout.addView(view);
        }

    }


    @Override
    public void onClick(View v) {

    }
}