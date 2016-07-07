package com.modle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2016/5/18.
 */
class Dbhelper extends SQLiteOpenHelper{
    public Dbhelper(Context context) {
        super(context,"UserTable", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table user(id integer primary key autoincrement" +
               ",userName varchar(30)" +
               ",passWord varchar(30)" +
               ",cenghushu boolean(30))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
