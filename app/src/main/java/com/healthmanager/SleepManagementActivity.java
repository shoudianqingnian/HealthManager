package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.healthmanager.db.MyDatabaseHelper;
import com.healthmanager.util.ActivityCollector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SleepManagementActivity extends AppCompatActivity {
    private ImageView SleepRuturn;
    private TextView Hourshow,Minuteshow,SleepTarget,SleepSuggestion;
    private Button SelectBedtime,SelectWakingtime,SleepTimeset,SaveSleepData;
    private int Inhour,Inminute,Outhour,Outminute;//获取入睡，醒来时间
    public int hourset,minuteset;//设置睡眠时长
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepmanagement);
        ActivityCollector.addActivity(this);
        init();
    }

    private void init(){
        SleepRuturn=findViewById(R.id.iv_sleepreturn); //返回按钮
        Hourshow=findViewById(R.id.tv_hourshow);
        Minuteshow=findViewById(R.id.tv_minshow);
        SleepTarget=findViewById(R.id.tv_sleeptarget);
        SleepSuggestion=findViewById(R.id.tv_sleepsuggestion);
        SelectBedtime=findViewById(R.id.selctbedtime);  //选择入睡时间
        SelectWakingtime=findViewById(R.id.selectwakingtime); //选择起床时间
        SleepTimeset=findViewById(R.id.sleeptimeset);
        SaveSleepData=findViewById(R.id.savesleepdata);
        SleepRuturn.setOnClickListener(o);
        SelectBedtime.setOnClickListener(o);
        SelectWakingtime.setOnClickListener(o);
        SleepTimeset.setOnClickListener(o);
        SaveSleepData.setOnClickListener(o);
        SelectBedtime.setText("00:00");
        SelectWakingtime.setText("00:00");
        Hourshow.setText("0");
        // targetsleep();
        Minuteshow.setText("0");
        // sleepsuggestion();
    }

    View.OnClickListener o =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.savesleepdata://存储信息按钮
                    String insleepstr2=SelectBedtime.getText().toString().trim();//获取入睡时间
                    String outsleepstr2=SelectWakingtime.getText().toString().trim();//获取醒来时间
                    String sleeptime=Hourshow.getText().toString()+"小时"+Minuteshow.getText().toString()+"分钟";
                    MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(SleepManagementActivity.this);
                    SQLiteDatabase dbwrite=dbOpenHelper.getWritableDatabase();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat();
                    simpleDateFormat.applyPattern("MM-dd");
                    Date date=new Date();
                    String stime=simpleDateFormat.format(date);
                    ContentValues values=new ContentValues();
                    values.put("stime",stime);
                    values.put("sleeptime",sleeptime);
                    values.put("begintime",insleepstr2);
                    values.put("endtime",outsleepstr2);
                    dbwrite.insert("tb_sm",null,values);
                    Toast.makeText(SleepManagementActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sleeptimeset://录入信息按钮
                    String insleepstr1=SelectBedtime.getText().toString().trim();//获取入睡时间
                    String outsleepstr1=SelectWakingtime.getText().toString().trim();//获取醒来时间
                    String[] temp1;//用于存放分割后的入睡时间碎片的数组
                    String[] temp2;//用于存放分割后的醒来时间碎片的数组
                    String splittogic=":";
                    temp1=insleepstr1.split(splittogic);
                    temp2=outsleepstr1.split(splittogic);
                    Inhour=Integer.parseInt(temp1[0]);
                    Inminute=Integer.parseInt(temp1[1]);
                    Outhour=Integer.parseInt(temp2[0]);
                    Outminute=Integer.parseInt(temp2[1]);
                    hourset+=hourcount(Inhour,Outhour,Inminute,Outminute);
                    minuteset+=minutecount(Inminute,Outminute);
                    if(minuteset>=60)//时间逻辑判断
                    {
                        hourset+=1;
                        minuteset-=60;
                    }
                    Hourshow.setText(String.valueOf(hourset));
                    Minuteshow.setText(String.valueOf(minuteset));
                    break;
                case R.id.selctbedtime://选择入睡时间按钮
                    new TimePickerDialog(SleepManagementActivity.this, AlertDialog.THEME_HOLO_LIGHT,new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            SelectBedtime.setText(String.format("%s:%s",timeFomat(hourOfDay),timeFomat(minute)));
                        }
                    },0,0,true).show();
                    break;
                case R.id.selectwakingtime://选择醒来时间按钮
                    new TimePickerDialog(SleepManagementActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            SelectWakingtime.setText(String.format("%s:%s",timeFomat(hourOfDay),timeFomat(minute)));
                        }
                    },0,0,true).show();
                    break;
                case R.id.iv_sleepreturn://返回主界面按钮
                    Intent intent=new Intent(SleepManagementActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    public String timeFomat(int value)
    {
        return value>=10?""+value:"0"+value;
    }//将展示时间转换为正确格式

    //睡眠分钟计算，Inminute为入睡时间的分钟，Outminute为醒来时间的分钟
    private int minutecount(int Inminute,int Outminute)
    {
        int minuteset;
        if(Inminute>Outminute)
        {
            Inminute=60-Inminute;
            minuteset=Outminute+Inminute;
        }
        else {
            minuteset=Outminute-Inminute;
        }
        return minuteset;
    }

    //睡眠小时计算，Inhour为入睡时间的小时，Outhour为醒来时间的小时，Inminute为入睡时间的分钟，Outminute为醒来时间的分钟
    private int hourcount(int Inhour,int Outhour,int Inminute,int Outminute)
    {
        int hourset;
        if(Inminute>Outminute)
        {
            if(Inhour>Outhour)
            {
                Inhour=24-Inhour;
                hourset=Inhour+Outhour-1;
            }
            else
            {
                hourset=Outhour-Inhour-1;
            }
        }
        else
        {
            if(Inhour>Outhour)
            {
                Inhour=24-Inhour;
                hourset=Inhour+Outhour;
            }
            else
            {
                hourset=Outhour-Inhour;
            }
        }
        return hourset;
    }

    private void targetsleep()//睡眠推荐函数
    {
        MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(this);
        SQLiteDatabase dbRead=dbOpenHelper.getReadableDatabase();
        Cursor cursor=dbRead.query("tb_weight",
                null,
                null,
                null,
                null,
                null,
                null);
        if(cursor.getCount()==0)
        {
            Toast.makeText(this, "请先评估并存储您的体重信息", Toast.LENGTH_SHORT).show();
            SleepTarget.setText("0小时0分钟");
        }
        else
        {
            while (cursor.moveToLast())
            {
                int age=cursor.getInt(cursor.getColumnIndex("age"));
                SleepTarget.setText(agetosleep(age));
            }
        }
    }
    private String agetosleep(int age)//根据age计算建议睡眠时长
    {
        double sleeptogicnum;
        String sleeptogicstr;
        if(age>=18||age<=65)
        {
            sleeptogicnum=21-Math.pow(age,0.3)+age/10;
            sleeptogicstr=String.valueOf(roundnum(sleeptogicnum))+"小时"+String.valueOf(decimalpoint(sleeptogicnum))+"分钟";
        }
        else if(age>65)
        {
            sleeptogicnum=22-Math.pow(age,0.3)*7+age/5.6;
            sleeptogicstr=String.valueOf(roundnum(sleeptogicnum))+"小时"+String.valueOf(decimalpoint(sleeptogicnum))+"分钟";
        }
        else
        {
            sleeptogicstr="0小时0分钟";
        }
        return sleeptogicstr;
    }
    public int roundnum(double a)//返回小数点之前的数(此数可直接转换为小时单位)
    {
        int b=(int)a;
        int roundnum;
        if(b<a)
        {
            roundnum=b;
        }
        else
        {
            roundnum=b-1;
        }
        return roundnum;
    }
    public int decimalpoint(double a)//求小数点之后的数，并将其转化为分钟单位
    {
        double decimal;
        int minute;
        int b=(int)a;
        if(b<a)
        {
            decimal=a-b;
        }
        else
        {
            decimal=a+1-b;
        }
        minute=(int)decimal*60;
        return minute;
    }

    private void sleepsuggestion()
    {
        int nowhour=Integer.parseInt(Hourshow.getText().toString());
        int nowminute=Integer.parseInt(Minuteshow.getText().toString());
        String nowtogic=SleepTarget.getText().toString();
        String dep1="小时",dep2="分钟";
        String[] dep1str=nowtogic.split(dep1);
        int togichour=Integer.parseInt(dep1str[0]);
        String[] dep2str=dep1str[1].split(dep2);
        int togicminute=Integer.valueOf(dep2str[0]);
        if(nowhour>togichour)
        {
            SleepSuggestion.setText("您当前的睡眠时长已达到目标");
        }
        else if(nowhour==togichour)
        {
            if(nowminute>togicminute)
            {
                SleepSuggestion.setText("您当前的睡眠时长已达到目标");
            }
            else
            {
                SleepSuggestion.setText("您应该再睡会儿");
            }
        }
        else
        {
            SleepSuggestion.setText("您应该再睡会儿");
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }
}