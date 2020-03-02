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
import android.widget.Toast;



import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import android.content.Intent;



import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;





import java.util.Iterator;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Iterator;


// ХЛЕБНЫЕ КРОШКИ


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public int file_number = 80572;

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


                try {
                readFromExcel();
                }
                catch (IOException e) {
                    // Do something here
                }

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



    //НЕ РАБОТАЕТ ФУНКЦИЯ

    public static void readFromExcel() throws IOException{

        int number_list = 0;
        File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/dev.prokrostinatorbl.raspisanie/files/123.xlsx");

        Log.i("***", "************** " + "я попал сюда, а дальше не могу :с");

        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));

        HSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        HSSFRow row = myExcelSheet.getRow(0);


        Iterator<Row> rowIterator = myExcelSheet.iterator();

        while (rowIterator.hasNext()) {
            Row row2 = rowIterator.next();
            // Get iterator to all cells of current row
            Iterator<Cell> cellIterator = row2.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                // Change to getCellType() if using POI 4.x
                CellType cellType = cell.getCellTypeEnum();

                switch (cellType) {
                    case _NONE:
                        Log.i("***", "************** ");
                        break;
                    case BOOLEAN:
                        Log.i("***", "************** " + cell.getBooleanCellValue());
                        System.out.print("\t");
                        break;
                    case BLANK:
                        Log.i("***", "************** ");
                        System.out.print("\t");
                        break;
                    case FORMULA:
                        // Formula
                        Log.i("***", "************** " + cell.getCellFormula());

                        System.out.print("\t");

                        FormulaEvaluator evaluator = myExcelBook.getCreationHelper().createFormulaEvaluator();
                        // Print out value evaluated by formula
                        Log.i("***", "************** " + evaluator.evaluate(cell).getNumberValue());
                        break;
                    case NUMERIC:
                        Log.i("***", "************** " + cell.getNumericCellValue());
                        System.out.print("\t");
                        break;
                    case STRING:
                        Log.i("***", "************** " + cell.getStringCellValue());

                        System.out.print("\t");
                        break;
                    case ERROR:
                        System.out.print("!");
                        System.out.print("\t");
                        break;
                }

            }
        }



    }


}