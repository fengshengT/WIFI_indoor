package com.example.guoyao.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by xiacheng on 2018/10/17.
 */

public class RssDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "database.db";

    public static final int DB_VERSION =9;

    public static final String TABLE_NAME = "rss";
    public Context mcontext;
    //创建 srecord 表的 sql 语句
    private static final String STUDENTS_CREATE_TABLE_SQL = "create table " + TABLE_NAME + "("
            +"id integer primary key autoincrement, "
            +"x_loc integer,"
            +"y_loc intrger,"
            +"rssi integer, "
            +"mac varchar(30), "
            + ");";
    public RssDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建 students 表
        db.execSQL(STUDENTS_CREATE_TABLE_SQL);
        Toast.makeText(mcontext,"创建成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本号变更会调用 onUpgrade 函数，在这根据版本号进行升级数据库
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // 启动外键
            db.execSQL("PRAGMA foreign_keys = 1;");

            //或者这样写
            String query = String.format("PRAGMA foreign_keys = %s", "ON");
            db.execSQL(query);
        }
    }

}

