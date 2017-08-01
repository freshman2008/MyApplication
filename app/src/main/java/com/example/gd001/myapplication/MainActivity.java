package com.example.gd001.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private SQLiteDatabase database;

    private DBAdapter adapter;
    private ListView listView;
    private List<Person> personList = new ArrayList<Person>();
    private Button insertBtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeSQLCipher();
        context = this;

        insertBtn = (Button) findViewById(R.id.btn_insert);
        listView = (ListView)findViewById(R.id.person_list);
        personList = queryData();
        //实例化DbAdapter
        adapter = new DBAdapter(getApplication(), personList);
        listView.setAdapter(adapter);

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //实例化一个ContentValues， ContentValues是以键值对的形式，键是数据库的列名，值是要插入的值
                ContentValues values = new ContentValues();
                values.put("name", "Edward");
                values.put("age", 30);
                values.put("sex", "mail");

                //调用insert插入数据库
                database.insert("person", null, values);
                updateUI();
            }
        });
    }

    private void InitializeSQLCipher() {
        SQLiteDatabase.loadLibs(this);
        File databaseFile = getDatabasePath("demo.db");
//        databaseFile.mkdirs();
//        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
        database.execSQL("CREATE TABLE IF NOT EXISTS person(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER, sex TEXT)");
        database.execSQL("insert into person(name, age, sex) values(?, ?, ?)", new Object[]{"John", 26, "male"});
    }


    public void updateUI() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //重新刷新适配器
                adapter.refresh(queryData());
            }
        });
    }

    //查询数据库，将每一行的数据封装成一个person 对象，然后将对象添加到List中
    private List<Person> queryData() {
        personList.clear();

        //调用query()获取Cursor
        Cursor c = database.query("person", null, null, null, null, null, null, null);
        while (c.moveToNext()){
            int _id = c.getInt(c.getColumnIndex("_id"));
            String name = c.getString(c.getColumnIndex("name"));
            int age = c.getInt(c.getColumnIndex("age"));
            String sex = c.getString(c.getColumnIndex("sex"));

            //用一个Person对象来封装查询出来的数据
            Person p = new Person();
            p.set_id(_id);
            p.setName(name);
            p.setAge(age);
            p.setSex(sex);

            personList.add(p);
        }
        return personList;
    }
}
