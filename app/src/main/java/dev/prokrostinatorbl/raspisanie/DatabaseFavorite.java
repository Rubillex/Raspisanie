package dev.prokrostinatorbl.raspisanie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseFavorite extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_FAVORITE = "note";

    public static final String KEY_NAME = "name";

    public DatabaseFavorite(Context context, String name)  {
        super(context, name, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String script = "CREATE TABLE " + TABLE_FAVORITE + "(" + KEY_NAME + " TEXT)";
        db.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);


        // Recreate
        onCreate(db);

    }
}
