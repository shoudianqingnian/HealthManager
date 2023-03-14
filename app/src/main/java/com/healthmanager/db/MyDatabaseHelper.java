package com.healthmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;//数据库版本
    private static final String DBNAME="healthmanager.db";//数据库名称
    private Context mContext;
    public MyDatabaseHelper(Context context){
        super(context,DBNAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_pwd(id integer primary key autoincrement,useraccount varchar(20),userpassword varchar(20))");
        db.execSQL("create table tb_weight(id integer primary key autoincrement,nowtime varchar(10),gender String,age integer,weight float,height float,bmi float,bodyfatrate float)");
//        db.execSQL("create table tb_case(id integer primary key autoincrement,disease varchar(10) ,hospital varchar(20),hosdepartment varchar(10),clinictime varchar(10),diamode varchar(30),diagnosis varchar(100) ,medicine varchar(30),isoperation varchar(5),opestr varchar(10),opeend varchar(10),exithostime varchar(10))" );
        db.execSQL("create table tb_yd(id integer primary key autoincrement,etime varchar(10),step integer)");
        db.execSQL("create table tb_ys(id integer primary key autoincrement,eatime varchar(10),kcal integer,breakfast integer,lunch integer,dinner integer)");
//        db.execSQL("create table tb_sm(id integer primary key autoincrement,stime varchar(10),sleeptime varchar(15),begintime varchar(20),endtime varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
