package com.example.letchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyHelper extends SQLiteOpenHelper {
    public static final String friendList = "create table friendList ("
            + "_id integer primary key autoincrement," + "name varchar(20),"
            + "time integer," + "lastRecord text)";

    public MyHelper (Context context) {
        super(context, "LetChat.db",null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(friendList);
    }


    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertChatRecord(String _id, String num, String message, long time) {
        String vid = "v" + _id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num",num);
        values.put("message",message);
        values.put("time",time);
        long id = db.insert(vid,null,values);
        db.close();
    }

    public List<String> findColumn(String column) {
        List<String> col = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("friendList",null,null,null,null,null,null);
        //没有写如果数据库为空的情况
        cursor.moveToFirst();
        do{
            String str = cursor.getString(cursor.getColumnIndex(column));
            col.add(str);
        } while(cursor.moveToNext());
        db.close();
        return col;
    }

    public void addFriend(long _id, String name, long time, String lastRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id",_id);
        values.put("name",name);
        values.put("time",time);
        values.put("lastRecord",lastRecord);
        long id = db.insert("friendList",null,values);
        db.close();
    }

    public int deleteFriend(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int number = db.delete("friendList", "_id=?", new String[] {id+""});
        db.close();
        return number;
    }

    public void addChatTable(long id) {
        String _id = String.valueOf(id);
        String tablename = "v" + _id;
        SQLiteDatabase db = this.getWritableDatabase();
        String record = "create table if not exists "+ tablename +"("
                + "num varchar(10)," + "message text,"
                + "time integer)";
        db.execSQL(record);
        db.close();
    }

    //修改friendList表的lastRecord值
    public int motifyLastRecord(long id, String lastRecord) {
        String _id = String.valueOf(id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lastRecord", lastRecord);
        int number = db.update("friendList", values, "_id=?", new String[] {_id});
        db.close();
        return number;
    }
}


