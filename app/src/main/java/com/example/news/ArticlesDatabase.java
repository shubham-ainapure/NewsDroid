package com.example.news;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ArticlesDatabase extends SQLiteOpenHelper {
    Context context;

    private static final String databaseName="articlesDB";
    private static final int databaseVersion=1;
    public ArticlesDatabase( Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY AUTOINCREMENT,source TEXT,title TEXT,subtitle TEXT,content TEXT,imgUrl TEXT,newsUrl TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS articles");
        onCreate(db);
    }

   public void insert(String source,String title,String subTitle,String content,String imgUrl,String newsUrl){
     SQLiteDatabase db=this.getWritableDatabase();
       ContentValues cv=new ContentValues();
       cv.put("source",source);
       cv.put("title",title);
       cv.put("subtitle",subTitle);
       cv.put("content",content);
       cv.put("imgUrl",imgUrl);
       cv.put("newsUrl",newsUrl);

       db.insert("articles",null,cv);
   }

   public ArrayList<ArticlesDbModel> fetch(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM articles",null);
        ArrayList<ArticlesDbModel> arrArticles=new ArrayList<>();

       if (!cursor.moveToNext()){

       }else {
           do {
               ArticlesDbModel model=new ArticlesDbModel();
               model.id=cursor.getInt(0);
               model.source=cursor.getString(1);
               model.title=cursor.getString(2);
               model.subtitle=cursor.getString(3);
               model.content=cursor.getString(4);
               model.imgUrl=cursor.getString(5);
               model.newsUrl=cursor.getString(6);
               arrArticles.add(model);
           }while (cursor.moveToNext());
       }
        return arrArticles;
   }

   public void delete(String newsUrl){
        SQLiteDatabase db=this.getWritableDatabase();
       String sql = "DELETE FROM articles WHERE newsUrl = ?";
       SQLiteStatement stmt = db.compileStatement(sql);
       stmt.bindString(1, newsUrl);
       stmt.executeUpdateDelete();
       db.close();
   }
}
