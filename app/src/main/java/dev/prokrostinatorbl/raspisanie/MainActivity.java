package dev.prokrostinatorbl.raspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;


// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public int file_number = 80500;


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
                Downloader();
        }


    }


    private void Downloader(){
        String destFileName = "581-8_red.xls";
        String src = "https://www.asu.ru/timetable/students/24/?file=" + file_number + ".xls";
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
            } catch (IOException e) {
                e.printStackTrace();
                onDownloadComplete(false);
                file_number++;
                Downloader();
            }
        }
    }

}

