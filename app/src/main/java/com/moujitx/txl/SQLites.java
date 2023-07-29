package com.moujitx.txl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLites extends SQLiteOpenHelper {
    public SQLites(Context context) {
        super(context, "myDB.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE information(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name VARCHAR(20), " +
                "phone VARCHAR(20)," +
                "rgroup VARCHAR(20))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}