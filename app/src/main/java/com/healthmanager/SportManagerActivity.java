package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanager.db.MyDatabaseHelper;
import com.healthmanager.db.SaveStepPreference;
import com.healthmanager.util.ActivityCollector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SportManagerActivity extends AppCompatActivity {
    private ImageView SportReturn,StepStatistics;
    private SensorManager sensorManager;//定义传感器管理
    private TextView StepShow,TagetStep,StepSug;
    private Button BTNSaveStep;
    private int userstep=0;

    public static int CURRENT_SETP;//定义步数
    public static float SENSITIVITY=8;//定义灵敏度

    private float mLastValues[]=new float[3*2];
    private float mScale[]=new float[2];//定义重力加速度数组
    private float mYOffset;//定义位移
    private static long mEnd=0;//定义运动相隔时间
    private static long mStart=0;//定义运动开始时间

    /**
     * 最后加速度方向
     * @param savedInstanceState
     */
    private float mLastDirections[]=new float[3*2];//最后的方向
    private float mLastExtremes[][]={new float[3*2],new float[3*2]};
    private float mLastDiff[]=new float[3*2];
    private int mLastMatch=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportmanager);
        init();
        ActivityCollector.addActivity(this);
    }

    private void init(){
        SportReturn=findViewById(R.id.iv_sportreturn);
        StepShow=findViewById(R.id.tv_stepshow);
        TagetStep=findViewById(R.id.tv_targetstep);
        StepSug=findViewById(R.id.tv_stepsug);
        BTNSaveStep=findViewById(R.id.btn_savestep);
        StepStatistics=findViewById(R.id.iv_stepstatistics);
        SportReturn.setOnClickListener(o);
        BTNSaveStep.setOnClickListener(o);
        StepStatistics.setOnClickListener(o);
        //获取传感器服务
        sensorManager=(SensorManager) this.getSystemService(SENSOR_SERVICE);
        //定义两个判断是否用于计步的值
        int h=480;
        mYOffset=h*0.5f;
        mScale[0]=-(h*0.5f*(1.0f/(SensorManager.STANDARD_GRAVITY*2)));//重力加速度
        mScale[1]=-(h*0.5f*(1.0f/(SensorManager.MAGNETIC_FIELD_EARTH_MAX)));//地球最大磁场

        SaveStepPreference.createSharedPreferences(this);
        //获取保存的步数
        userstep= SaveStepPreference.getIntValues("sport_steps",CURRENT_SETP);
        StepShow.setText(String.valueOf(userstep));
        TagetStep.setText("8000");
        //togicstep();
        StepSug.setText("您当前尚未完成建议步数!");
        //exerciseremind();
    }

    View.OnClickListener o=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch(view.getId())
            {
//                case R.id.iv_stepstatistics: //统计按钮
//                    intent=new Intent(SportManagerActivity.this,ExerciseDataActivity.class);
//                    startActivity(intent);
//                    finish();
                case R.id.iv_sportreturn://返回界面按钮
                    intent=new Intent(SportManagerActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_savestep://存储步数按钮
                    int stepnow=Integer.valueOf(StepShow.getText().toString());
                    MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(SportManagerActivity.this);
                    SQLiteDatabase dbwrite=dbOpenHelper.getWritableDatabase();
                    SimpleDateFormat sdf=new SimpleDateFormat();
                    sdf.applyPattern("MM-dd");
                    Date date=new Date();
                    String savedata=sdf.format(date);
                    ContentValues values=new ContentValues();
                    values.put("etime",savedata);
                    values.put("step",stepnow);
                    dbwrite.insert("tb_yd",null,values);
                    Toast.makeText(SportManagerActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        //注册传感器监听
//        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //取消传感器监听
//        sensorManager.unregisterListener(this);
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public int bmitostep(float BMI) //togicstep（）的相关函数，此函数用于根据bmi推荐步数
    {
        int bmitostep;
        if (BMI < 24) {
            bmitostep = 6000;
        }
        else if (BMI >= 24 && BMI <= 27) {
            bmitostep = 9000;
        }
        else {
            bmitostep = 11000;
        }
        return bmitostep;
    }

    private void exerciseremind()
    {
        int nowstep=Integer.parseInt(StepShow.getText().toString());
        int togicstep=Integer.parseInt(TagetStep.getText().toString());
        if(nowstep<togicstep)
        {
            StepSug.setText("您当前还未达到运动目标，继续努力啊");
        }
        else
        {
            StepSug.setText("您当前已完成达到运动目标，继续加油啊");
        }
    }
}