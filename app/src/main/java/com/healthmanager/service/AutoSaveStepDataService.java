package com.healthmanager.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.TextView;

import com.healthmanager.R;
import com.healthmanager.db.MyDatabaseHelper;
//实现定时自动存储跑步数据
public class AutoSaveStepDataService extends Service {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase myDatabase;
    int anHour=1*60*60*1000; //1小时的毫秒数
//    private Handler handler = new Handler();
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
//            TextView textView = (TextView) findViewById(R.id.textView);
//            String text = textView.getText().toString();
//            dbHelper.insertText(text);
//            handler.postDelayed(this, INTERVAL);
//        }
//    };


    public AutoSaveStepDataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myDatabaseHelper=new MyDatabaseHelper(this);
        myDatabase=myDatabaseHelper.getWritableDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDatabase.close();
        myDatabaseHelper.close();
    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {  //每次服务启动时调用
        saveStepData(); //将步数存储到数据库
//        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE); //设置一个闹钟
//        int anHour=8*60*60*1000; //8小时的毫秒数
////        int anMinute=60*1000; //1分钟的毫秒数，用于验证
//        long triggerAtTime= SystemClock.elapsedRealtime()+anHour; //获取从设备开机后经历的时间值
//        Intent i=new Intent(this,AutoSaveStepDataService.class);
//        PendingIntent pi=PendingIntent.getService(this,0,i,0);
//        manager.cancel(pi);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi); //设置一次性闹钟：闹钟类型为主动唤醒并提示型，使用相对时间，
        return super.onStartCommand(intent, flags, startId);
    }

    private void saveStepData(){ //实现将步数存储到数据库的功能
//        TextView textView = (TextView) findViewById(R.id.textView);
//        String value = textView.getText().toString();
//
//        // Store the value into the database
//        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("value", value);
//        db.insert("my_table", null, values);
    }


}