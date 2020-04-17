package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.app.CoreComponentFactory;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toolbar;

import org.json.*;
import org.w3c.dom.Text;


import java.io.*;
import java.util.*;


import java.util.Iterator;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Iterator;




// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Toolbar toolbar;

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
        protected void onRestart(){
        super.onRestart();
        setContentView(R.layout.activity_main);
            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите группу");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            TextView group_561 = (TextView) findViewById(R.id.group_561);
            group_561.setOnClickListener(this);
            TextView group_563 = (TextView) findViewById(R.id.group_563);
            group_563.setOnClickListener(this);
            TextView group_565 = (TextView) findViewById(R.id.group_565);
            group_565.setOnClickListener(this);
            TextView group_566 = (TextView) findViewById(R.id.group_566);
            group_566.setOnClickListener(this);
            TextView group_567 = (TextView) findViewById(R.id.group_567);
            group_567.setOnClickListener(this);
            TextView group_568 = (TextView) findViewById(R.id.group_568);
            group_568.setOnClickListener(this);
            TextView group_571 = (TextView) findViewById(R.id.group_571);
            group_571.setOnClickListener(this);
            TextView group_571M = (TextView) findViewById(R.id.group_571M);
            group_571M.setOnClickListener(this);
            TextView group_572aM = (TextView) findViewById(R.id.group_572aM);
            group_572aM.setOnClickListener(this);
            TextView group_572bM = (TextView) findViewById(R.id.group_572bM);
            group_572bM.setOnClickListener(this);
            TextView group_573 = (TextView) findViewById(R.id.group_573);
            group_573.setOnClickListener(this);
            TextView group_573M = (TextView) findViewById(R.id.group_573M);
            group_573M.setOnClickListener(this);
            TextView group_574M = (TextView) findViewById(R.id.group_574M);
            group_574M.setOnClickListener(this);
            TextView group_575 = (TextView) findViewById(R.id.group_575);
            group_575.setOnClickListener(this);
            TextView group_576 = (TextView) findViewById(R.id.group_576);
            group_576.setOnClickListener(this);
            TextView group_577 = (TextView) findViewById(R.id.group_577);
            group_577.setOnClickListener(this);
            TextView group_578 = (TextView) findViewById(R.id.group_578);
            group_578.setOnClickListener(this);
            TextView group_581M = (TextView) findViewById(R.id.group_581M);
            group_581M.setOnClickListener(this);
            TextView group_582bM = (TextView) findViewById(R.id.group_582bM);
            group_582bM.setOnClickListener(this);
            TextView group_583 = (TextView) findViewById(R.id.group_583);
            group_583.setOnClickListener(this);
            TextView group_583M = (TextView) findViewById(R.id.group_583M);
            group_583M.setOnClickListener(this);
            TextView group_584M = (TextView) findViewById(R.id.group_584M);
            group_584M.setOnClickListener(this);
            TextView group_587 = (TextView) findViewById(R.id.group_587);
            group_587.setOnClickListener(this);
            TextView group_588 = (TextView) findViewById(R.id.group_588);
            group_588.setOnClickListener(this);
            TextView group_591 = (TextView) findViewById(R.id.group_591);
            group_591.setOnClickListener(this);
            TextView group_591M = (TextView) findViewById(R.id.group_591M);
            group_591M.setOnClickListener(this);
            TextView group_592aM = (TextView) findViewById(R.id.group_592aM);
            group_592aM.setOnClickListener(this);
            TextView group_592bM = (TextView) findViewById(R.id.group_592bM);
            group_592bM.setOnClickListener(this);
            TextView group_593 = (TextView) findViewById(R.id.group_593);
            group_593.setOnClickListener(this);
            TextView group_593M = (TextView) findViewById(R.id.group_593M);
            group_593M.setOnClickListener(this);
            TextView group_594M = (TextView) findViewById(R.id.group_594M);
            group_594M.setOnClickListener(this);
            TextView group_595 = (TextView) findViewById(R.id.group_595);
            group_595.setOnClickListener(this);
            TextView group_597 = (TextView) findViewById(R.id.group_597);
            group_597.setOnClickListener(this);
            TextView group_598 = (TextView) findViewById(R.id.group_598);
            group_598.setOnClickListener(this);

        TextView group_585 = (TextView) findViewById(R.id.group_585);
        group_585.setOnClickListener(this);
        TextView group_581 = (TextView) findViewById(R.id.group_581);
        group_581.setOnClickListener(this);

        Cheker();

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            toolbar  = (Toolbar) findViewById(R.id.my_toolbar);
            TextView toolbar_text = (TextView) findViewById(R.id.toolbar_text);
            toolbar_text.setText("Выберите группу");
//            setActionBar(toolbar);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            TextView group_585 = (TextView) findViewById(R.id.group_585);
            group_585.setOnClickListener(this);

            TextView group_581 = (TextView) findViewById(R.id.group_581);
            group_581.setOnClickListener(this);

            TextView group_561 = (TextView) findViewById(R.id.group_561);
            group_561.setOnClickListener(this);
            TextView group_563 = (TextView) findViewById(R.id.group_563);
            group_563.setOnClickListener(this);
            TextView group_565 = (TextView) findViewById(R.id.group_565);
            group_565.setOnClickListener(this);
            TextView group_566 = (TextView) findViewById(R.id.group_566);
            group_566.setOnClickListener(this);
            TextView group_567 = (TextView) findViewById(R.id.group_567);
            group_567.setOnClickListener(this);
            TextView group_568 = (TextView) findViewById(R.id.group_568);
            group_568.setOnClickListener(this);
            TextView group_571 = (TextView) findViewById(R.id.group_571);
            group_571.setOnClickListener(this);
            TextView group_571M = (TextView) findViewById(R.id.group_571M);
            group_571M.setOnClickListener(this);
            TextView group_572aM = (TextView) findViewById(R.id.group_572aM);
            group_572aM.setOnClickListener(this);
            TextView group_572bM = (TextView) findViewById(R.id.group_572bM);
            group_572bM.setOnClickListener(this);
            TextView group_573 = (TextView) findViewById(R.id.group_573);
            group_573.setOnClickListener(this);
            TextView group_573M = (TextView) findViewById(R.id.group_573M);
            group_573M.setOnClickListener(this);
            TextView group_574M = (TextView) findViewById(R.id.group_574M);
            group_574M.setOnClickListener(this);
            TextView group_575 = (TextView) findViewById(R.id.group_575);
            group_575.setOnClickListener(this);
            TextView group_576 = (TextView) findViewById(R.id.group_576);
            group_576.setOnClickListener(this);
            TextView group_577 = (TextView) findViewById(R.id.group_577);
            group_577.setOnClickListener(this);
            TextView group_578 = (TextView) findViewById(R.id.group_578);
            group_578.setOnClickListener(this);
            TextView group_581M = (TextView) findViewById(R.id.group_581M);
            group_581M.setOnClickListener(this);
            TextView group_582bM = (TextView) findViewById(R.id.group_582bM);
            group_582bM.setOnClickListener(this);
            TextView group_583 = (TextView) findViewById(R.id.group_583);
            group_583.setOnClickListener(this);
            TextView group_583M = (TextView) findViewById(R.id.group_583M);
            group_583M.setOnClickListener(this);
            TextView group_584M = (TextView) findViewById(R.id.group_584M);
            group_584M.setOnClickListener(this);
            TextView group_587 = (TextView) findViewById(R.id.group_587);
            group_587.setOnClickListener(this);
            TextView group_588 = (TextView) findViewById(R.id.group_588);
            group_588.setOnClickListener(this);
            TextView group_591 = (TextView) findViewById(R.id.group_591);
            group_591.setOnClickListener(this);
            TextView group_591M = (TextView) findViewById(R.id.group_591M);
            group_591M.setOnClickListener(this);
            TextView group_592aM = (TextView) findViewById(R.id.group_592aM);
            group_592aM.setOnClickListener(this);
            TextView group_592bM = (TextView) findViewById(R.id.group_592bM);
            group_592bM.setOnClickListener(this);
            TextView group_593 = (TextView) findViewById(R.id.group_593);
            group_593.setOnClickListener(this);
            TextView group_593M = (TextView) findViewById(R.id.group_593M);
            group_593M.setOnClickListener(this);
            TextView group_594M = (TextView) findViewById(R.id.group_594M);
            group_594M.setOnClickListener(this);
            TextView group_595 = (TextView) findViewById(R.id.group_595);
            group_595.setOnClickListener(this);
            TextView group_597 = (TextView) findViewById(R.id.group_597);
            group_597.setOnClickListener(this);
            TextView group_598 = (TextView) findViewById(R.id.group_598);
            group_598.setOnClickListener(this);


            Cheker();
//        Downloader();



    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, FUCKTABLE.class);

        switch (view.getId())
        {
            case R.id.group_585:
                intent.putExtra("key", "585");
                intent.putExtra("instit", "ИЦТЭФ");
               break;

            case R.id.group_581:
                intent.putExtra("key", "581");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_561:
                intent.putExtra("key","561");
                intent.putExtra("instit", "ИЦТЭФ");
                break;

            case R.id.group_563:
                intent.putExtra("key","563");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_565:
                intent.putExtra("key","565");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_566:
                intent.putExtra("key","566");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_567:
                intent.putExtra("key","567");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_568:
                intent.putExtra("key","568");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_571:
                intent.putExtra("key","571");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_571M:
                intent.putExtra("key","571M");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_572aM:
                intent.putExtra("key","572aM");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_572bM:
                intent.putExtra("key","572bM");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_573:
                intent.putExtra("key","573");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_573M:
                intent.putExtra("key","573M");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_574M:
                intent.putExtra("key","574M");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_575:
                intent.putExtra("key","575");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_576:
                intent.putExtra("key","576");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_577:
                intent.putExtra("key","577");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_578:
                intent.putExtra("key","578");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_581M:
                intent.putExtra("key","581M");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_582bM:
                intent.putExtra("key","582bM");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_583:
                intent.putExtra("key","583");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_583M:
                intent.putExtra("key","583M");
                intent.putExtra("instit", "ИЦТЭФ");
                break;
            case R.id.group_584M:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","584M");
                break;
            case R.id.group_587:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","587");
                break;
            case R.id.group_588:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","588");
                break;
            case R.id.group_591:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","591");
                break;
            case R.id.group_591M:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","591M");
                break;
            case R.id.group_592aM:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","592aM");
                break;
            case R.id.group_592bM:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","592bM");
                break;
            case R.id.group_593:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","593");
                break;
            case R.id.group_593M:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","593M");
                break;
            case R.id.group_594M:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","594M");
                break;
            case R.id.group_595:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","595");
                break;
            case R.id.group_597:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","597");
                break;
            case R.id.group_598:
                intent.putExtra("instit", "ИЦТЭФ");
                intent.putExtra("key","598");
                break;

        }
        startActivity(intent);
    setContentView(R.layout.loading);

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


}