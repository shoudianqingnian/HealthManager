package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanager.db.MyDatabaseHelper;
import com.healthmanager.util.ActivityCollector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeightManagerActivity extends AppCompatActivity {
    private TextView BmiShowText,BodyFatPercentageShowText,BmiEvalBtn,SaveBmiBtn;
    private EditText WeightScan,HeightScan,AgeScan;
    private ImageView WeightReturn,BMIStatisticsBtn;
    public float bmi,bmitizhi;
    private String heightstr,weightstr,agestr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weightmanager);
        ActivityCollector.addActivity(this);
        init();
    }

    private void init(){
        BmiShowText=findViewById(R.id.tv_bmi);
        BodyFatPercentageShowText=findViewById(R.id.tv_bodyfatpercentage);
        BmiEvalBtn=findViewById(R.id.btn_bmieval); //计算BMI按钮
        SaveBmiBtn=findViewById(R.id.btn_savebmi); //存储数据按钮
        WeightScan=findViewById(R.id.et_weight);
        HeightScan=findViewById(R.id.et_height);
        AgeScan=findViewById(R.id.et_age);
        WeightReturn=findViewById(R.id.iv_weightreturn);//返回按钮
        BMIStatisticsBtn=findViewById(R.id.iv_bmistatistics);//统计BMI数据按钮
        BmiEvalBtn.setOnClickListener(o);
        SaveBmiBtn.setOnClickListener(o);
        WeightReturn.setOnClickListener(o);
        BMIStatisticsBtn.setOnClickListener(o);
    }

    View.OnClickListener o=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
//                case R.id.iv_bmistatistics: //统计BMI数据按钮
//                    Intent intentt=new Intent(WeightManagerActivity.this,WeightDataActivity.class);
//                    startActivity(intentt);
//                    finish();
//                    break;
                case R.id.btn_savebmi: //存储数据按钮
                    String bmitextsave=BmiShowText.getText().toString();
                    String bmitztextsave=BodyFatPercentageShowText.getText().toString();
                    String sep="（";
                    String[] bmitextsavestr=bmitextsave.split(sep);
                    String[] bmitztextsavestr=bmitztextsave.split(sep);
                    Spinner spinnersave=findViewById(R.id.sp_gender);
                    String gendersave=spinnersave.getSelectedItem().toString().trim();//获取用户选择的性别
                    heightstr=HeightScan.getText().toString().trim();
                    weightstr=WeightScan.getText().toString().trim();
                    agestr=AgeScan.getText().toString().trim();
                    if(bmitextsavestr.length!=2||bmitztextsavestr.length!=2)
                    {
                        Toast.makeText(WeightManagerActivity.this, "请评估之后再进行存储", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(WeightManagerActivity.this);
                        SQLiteDatabase dbwrite=dbOpenHelper.getWritableDatabase();
                        SimpleDateFormat sdf=new SimpleDateFormat();
                        sdf.applyPattern("MM-dd");
                        Date date=new Date();
                        String savedata=sdf.format(date);
                        ContentValues values=new ContentValues();
                        values.put("nowtime",savedata);
                        values.put("gender",gendersave);
                        values.put("age",Integer.valueOf(agestr));
                        values.put("weight",Float.valueOf(weightstr));
                        values.put("height",Float.valueOf(heightstr));
                        values.put("bmi",Float.valueOf(bmitextsavestr[0]));
                        values.put("bodyfatrate",Float.valueOf(bmitztextsavestr[0]));
                        dbwrite.insert("tb_weight",null,values);
                        Toast.makeText(WeightManagerActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_bmieval: //计算BMI按钮
                    heightstr=HeightScan.getText().toString().trim();//获取用户输入的身高
                    weightstr=WeightScan.getText().toString().trim();//获取用户输入的体重
                    agestr=AgeScan.getText().toString().trim();//获取用户输入的年龄
                    Spinner spinner=findViewById(R.id.sp_gender);
                    String gender=spinner.getSelectedItem().toString().trim();//获取用户选择的性别
                    int gendernum=0;
                    switch(gender)//将性别转换为性别指数
                    {
                        case "女":
                            gendernum=0;
                            break;
                        case "男":
                            gendernum=1;
                            break;
                    }
                    //判断用户输入的身高、体重、年龄是否为空
                    if(TextUtils.isEmpty(heightstr)||TextUtils.isEmpty(weightstr)||TextUtils.isEmpty(agestr))
                    {
                        Toast.makeText(WeightManagerActivity.this, "输入的数据不能为空", Toast.LENGTH_SHORT).show();
                        HeightScan.requestFocus();
                        WeightScan.requestFocus();
                        AgeScan.requestFocus();
                        return;
                    }
                    float height=Float.valueOf(heightstr);
                    float weight=Float.valueOf(weightstr);
                    float userage=Float.valueOf(agestr);
                    bmi=Bmi(height,weight);//计算用户的BMI
                    bmitizhi=Bmitizhi(bmi,userage,gendernum);//计算用户的体脂率
                    String bmitextshow=String.valueOf(bmi);
                    String bmitizhishow=String.valueOf(bmitizhi);
                    bmitextshow=bmitextshow+"\n"+BmiStr(bmi);
                    bmitizhishow="\0\0"+bmitizhishow+"\n"+BmitizhiStr(gendernum,bmitizhi);
                    BmiShowText.setText(bmitextshow);//将用户的BMI呈现于界面
                    BodyFatPercentageShowText.setText(bmitizhishow);//将用户的体脂率呈现于界面
                    break;
                case R.id.iv_weightreturn: //返回按钮
                    Intent intent=new Intent(WeightManagerActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private float Bmi(float height,float weight)//bmi计算函数
    {
        float c=height/100;
        float d=c*c;
        float bmi=weight/d;
        return bmi;
    }
    private float Bmitizhi(float bmi,float userage,int genderNum)//体脂率计算函数
    {
        float bmitz=0;
        bmitz=(float) (1.2*bmi+0.23*userage-5.4-10.8*genderNum);
        return bmitz;
    }
    private String BmiStr(float bmi)//bmi评估文字
    {
        String str=" ";
        if(bmi<18.5)
        {
            str="（偏瘦）";
        }
        else if(bmi>=18.5&&bmi<24)
        {
            str="（正常）";
        }
        else if(bmi>=24&&bmi<27)
        {
            str="（偏胖）";
        }
        else if(bmi>=27&&bmi<30)
        {
            str="（肥胖）";
        }
        else if(bmi>=30&&bmi<40)
        {
            str=" （重度肥胖）";
        }
        else if(bmi>=40)
        {
            str="（极重度肥胖）";
        }
        return str;
    }
    private String BmitizhiStr(int gendernum,float bmitizhi)//体脂率评估文字
    {
        String bmitizhistr=" ";
        if(gendernum==1)
        {
            if(bmitizhi>=0&&bmitizhi<0.15)
            {
                bmitizhistr="（脂肪含量偏低）";
            }
            else if(bmitizhi>=0.15&&bmitizhi<=0.18)
            {
                bmitizhistr="（脂肪含量正常）";
            }
            else if(bmitizhi>0.18)
            {
                bmitizhistr="（脂肪含量偏高）";
            }
        }
        else if(gendernum==0)
        {
            if(bmitizhi>=0&&bmitizhi<0.2)
            {
                bmitizhistr="（脂肪含量偏低）";
            }
            else if(bmitizhi>=0.2&&bmitizhi<=0.25)
            {
                bmitizhistr="（脂肪含量正常）";
            }
            else if(bmitizhi>0.25)
            {
                bmitizhistr="（脂肪含量过高）";
            }
        }
        return bmitizhistr;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}