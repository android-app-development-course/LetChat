package com.example.letchat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatRecord extends SQLiteOpenHelper {
    public static String tableName;

    String record = "create table if not exists" + tableName + "("
            + "num varchar(10)" + "message text,"
            + "time integer)";

    public ChatRecord (Context context, String tableName) {
        super(context, "LetChat.db",null,1);
        this.tableName = tableName;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(record);
    }


    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {}
}
