package com.example.n9705.samplereader.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.n9705.samplereader.Article;

public class Articles_Dao {
    private MyOpenHelper helper;
    private Context mContext;
    private SQLiteDatabase db;
    public Articles_Dao(Context context){
        this.mContext = context;
        helper = new MyOpenHelper(context);
    }
    public void addFavorite(Article article){
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title",article.getTitle());
        values.put("Author",article.getAuthor());
        values.put("Text",article.getText());
        db.insert("Article",null,values);
        values.clear();
        db.close();
        System.out.println("储存进数据库成功！");
    }

    public void deleteFavorite (Article article) {
        db = helper.getWritableDatabase();
        db.delete("Article", "Title = ?", new String[]{article.getTitle()});
        System.out.println("删除成功！");
    }
}
