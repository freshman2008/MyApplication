package com.example.gd001.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

/**
 * Created by gd001 on 17-8-1.
 */

public class DBHelper {
    private static final String DATABASE_NAME = "test.db";
    private final static String TABLE_NAME = "person";
    private SQLiteDatabase db;
    private Context context;

    public DBHelper(Context context) {
        this.context = context;
        InitializeSQLCipher();
    }

    private void InitializeSQLCipher() {
        SQLiteDatabase.loadLibs(context);
        File databaseFile = context.getDatabasePath(DATABASE_NAME);
        databaseFile.mkdirs();
        db = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
        db.execSQL("CREATE TABLE IF NOT EXISTS person(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER, sex TEXT)");
        db.execSQL("insert into person(name, age, sex) values(?, ?, ?)", new Object[]{"John", 26, "male"});
    }

    //插入方法
    public void insert(ContentValues values){
        //插入数据库中
        db.insert(TABLE_NAME, null, values);
    }

    //查询方法
    public Cursor query(){
        //获取Cursor
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return c;

    }

    //根据唯一标识_id  来删除数据
    public void delete(int id){
        db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }


    //更新数据库的内容
    public void update(ContentValues values, String whereClause, String[]whereArgs){
        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    //关闭数据库
    public void close(){
        if(db != null){
            db.close();
        }
    }
}
