package dev.prokrostinatorbl.raspisanie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NOTE = "note";

    public static final String KEY_DATE = "date";
    public static final String KEY_NOTE = "note";

    public MyDatabaseHelper(Context context, String name)  {
        super(context, name, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String script = "CREATE TABLE " + TABLE_NOTE + "(" + KEY_DATE + " TEXT,"
                + KEY_NOTE + " TEXT)";
        db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);


        // Recreate
        onCreate(db);

    }
}
