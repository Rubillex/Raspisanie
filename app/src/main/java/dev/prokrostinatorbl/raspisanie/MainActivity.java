package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.Uri;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;




public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button aaa = (Button) findViewById(R.id.aaa);
        aaa.setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {


        switch (view.getId())
        {
            case R.id.aaa:


                String destFileName = "581-8_red.xls";
                String src = "https://www.asu.ru/timetable/students/24/?file=80720.xls&file_name=%D0%98%D0%A6%D0%A2%D0%AD%D0%A4%20581%20,%20583%20,%20585%20,%20587-8.xls";
                File dest = new File(Environment.getExternalStorageDirectory() + "/Download/" + destFileName);
                new LoadFile(src, dest).start();
        }


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
            } catch (IOException e) {
                e.printStackTrace();
                onDownloadComplete(false);
            }
        }
    }

}

