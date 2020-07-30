package dev.prokrostinatorbl.raspisanie;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Downloader {

    private static String src_file;
    private static File file;
    private static String destFileName;
    private static String from;

    public static void Download(String m_src_file, File m_file, String m_destFileName, String m_from){

        Log.i("Download", m_from);

        src_file = m_src_file;
        file = m_file;
        destFileName = m_destFileName;
        from = m_from;


        File dir = new File(Environment.getExternalStorageDirectory() + "Android/data/dev.prokrostinatorbl.raspisanie/files/");

        if (file.exists())
        {
            Log.d("A", "exists");
            Log.d("AAAAAA", String.valueOf(file));
        } else {
            Log.d("A", "not exists");
            file.mkdir();
        }

        if(!dir.exists()){
            dir.mkdir();
            Log.i("A", "НЕТ ПАПКИ");
        }


        File dest = new File(file + destFileName);



        new LoadFile(src_file, dest).start();

    }


    private static void onDownloadComplete(boolean success) {
        // файл скачался, можно как-то реагировать
        Log.i("***", "СКАЧАЛ " + success);
    }

    private static class LoadFile extends Thread {
        private final String src;
        private final File dest;

        LoadFile(String src, File dest) {
            this.src = src;
            this.dest = dest;
        }

        @Override
        public void run() {
            try {
                Log.i("Download", "LOAD_FILE_DOWNLOADER.CLASS");
                Log.i("Source_file", String.valueOf(src));
                Log.i("Source_file", String.valueOf(dest));

                FileUtils.copyURLToFile(new URL(src), dest);
                onDownloadComplete(true);

                switch (from){
                    case "MainActivity":
                        MainActivity.h.sendEmptyMessage(1);
                        break;
                    case "FUCKTABLE":
                        FUCKTABLE.h.sendEmptyMessage(3);
                        FUCKTABLE.h.sendEmptyMessage(1);
                        Log.i("msg_h", "1");
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
                onDownloadComplete(false);

                switch (from){
                    case "MainActivity":
                        MainActivity.h.sendEmptyMessage(1);
                        break;
                    case "FUCKTABLE":
                        FUCKTABLE.h.sendEmptyMessage(3);
                        FUCKTABLE.h.sendEmptyMessage(2);
                        Log.i("msg_h", "2");
                        break;
                }
            }
        }
    }

}
