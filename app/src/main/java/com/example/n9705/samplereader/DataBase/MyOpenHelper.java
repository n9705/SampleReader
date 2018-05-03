package com.example.n9705.samplereader.DataBase;

/**
 * Created by 86758 on 2017/12/28 0028.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.n9705.samplereader.Bottom_Dialog;
import com.example.n9705.samplereader.MainActivity;

public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        //创建数据库
        super(context, "Articles.db", null, 1);
        // TODO Auto-generated constructor stub
        System.out.println("MyOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //创建表
        db.execSQL("create table Article(" +
                "id integer primary key autoincrement, " +
                "Title Text not null," +
                "Author Text, " +
                "Text Text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
