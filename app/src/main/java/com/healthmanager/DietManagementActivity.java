package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanager.db.MyDatabaseHelper;
import com.healthmanager.util.ActivityCollector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DietManagementActivity extends AppCompatActivity {
    private ImageView DietReturn,CalDataStatices;
    private TextView CalShow,TargetCalShow,EatAdvice;
    private Button SelectFood1,SelectFood2,SelectFood3,RecordFood,SaveCal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietmanagement);
        ActivityCollector.addActivity(this);
        init();
    }

    private void init(){
        DietReturn=findViewById(R.id.iv_dietreturn); //返回按钮
        CalDataStatices=findViewById(R.id.iv_caldatastatices); //数据统计按钮
        CalShow=findViewById(R.id.tv_showcal);
        TargetCalShow=findViewById(R.id.tv_caltarget);
        EatAdvice=findViewById(R.id.tv_dietaryadvice);
        SelectFood1=findViewById(R.id.btn_selectfood1); //选择食物按钮
        SelectFood2=findViewById(R.id.btn_selectfood2); //选择食物按钮
        SelectFood3=findViewById(R.id.btn_selectfood3); //选择食物按钮
        RecordFood=findViewById(R.id.btn_recordfood); //录入饮食信息按钮
        SaveCal=findViewById(R.id.btn_savecal);  //保存信息按钮
        DietReturn.setOnClickListener(o);
        SelectFood1.setOnClickListener(o);
        SelectFood2.setOnClickListener(o);
        SelectFood3.setOnClickListener(o);
        RecordFood.setOnClickListener(o);
        SaveCal.setOnClickListener(o);
        CalShow.setText("0");
        //caltarget();
        TargetCalShow.setText("0");
//        Intent intentget=getIntent();
//        String nowbrecal=intentget.getStringExtra(BreakfastActivity.BRECAL);
//        SelectFood1.setText(nowbrecal);
    }

    View.OnClickListener o=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=null;
            switch (view.getId())
            {
//                case R.id.iv_caldatastatices: //数据统计按钮
//                    intent=new Intent(DietManagementActivity.this,EatDataActivity.class);
//                    startActivity(intent);
//                    finish();
//                    break;
                case R.id.btn_savecal:  //保存信息按钮
                    int calsave=Integer.parseInt(CalShow.getText().toString());
                    MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(DietManagementActivity.this);
                    SQLiteDatabase dbWrite=dbOpenHelper.getWritableDatabase();
                    SimpleDateFormat sdf=new SimpleDateFormat();
                    sdf.applyPattern("MM-dd");
                    Date date=new Date();
                    String savedata=sdf.format(date);
                    ContentValues values=new ContentValues();
                    values.put("eatime",savedata);
                    values.put("kcal",calsave);
                    dbWrite.insert("tb_ys",null,values);
                    Toast.makeText(DietManagementActivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_dietreturn:  //返回按钮
                    intent=new Intent(DietManagementActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_selectfood1:  //选择食物按钮
//                    intent=new Intent(DietManagementActivity.this,BreakfastActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
                case R.id.btn_selectfood2: //选择食物按钮
                    //intent=new Intent(EatActivity.this,FoodSelectActivity.class);
                    //startActivity(intent);
                    //finish();
                    break;
                case R.id.btn_selectfood3: //选择食物按钮
                    //intent=new Intent(EatActivity.this,FoodSelectActivity.class);
                    //startActivity(intent);
                    //finish();
                    break;
                case R.id.btn_recordfood:
                    int nowbre=Integer.parseInt(SelectFood1.getText().toString());
                    int nowlunch=Integer.parseInt(SelectFood2.getText().toString());
                    int nowdinner=Integer.parseInt(SelectFood3.getText().toString());
                    CalShow.setText(String.valueOf(nowbre+nowlunch+nowdinner));
                    break;
            }
        }
    };

    private void caltarget()//卡路里推荐函数
    {
        int togiccal;
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
            TargetCalShow.setText("0");
        }
        else
        {
            while (cursor.moveToLast())
            {
                String gender=cursor.getString(cursor.getColumnIndex("gender"));
                int age=cursor.getInt(cursor.getColumnIndex("age"));
                float weight=cursor.getFloat(cursor.getColumnIndex("weight"));
                TargetCalShow.setText(String.valueOf(weighttotarget(gender,age,weight)));
            }
        }
    }

    private int weighttotarget(String gender,int age,float weight)//卡路里推荐函数相关函数
    {
        int caltogic;
        if(gender.equals("男"))
        {
            if(age>=18||age<31)
            {
                caltogic=(int)(15.2*weight+680);
            }
            else if(age>=31&&age<60)
            {
                caltogic=(int)(11.5*weight+830);
            }
            else if(age>=61)
            {
                caltogic=(int)(13.4*weight+490);
            }
            else
            {
                caltogic=0;
            }
        }
        else
        {
            if(age>=18||age<31)
            {
                caltogic=(int)(14.6*weight+450);
            }
            else if(age>=31&&age<60)
            {
                caltogic=(int)(8.6*weight+830);
            }
            else if(age>=61)
            {
                caltogic=(int)(10.4*weight+600);
            }
            else
            {
                caltogic=0;
            }
        }
        return caltogic;
    }

    private void caladvice()//健康提醒
    {
        int nowcal=Integer.valueOf(CalShow.getText().toString());
        int nowtogiccal=Integer.valueOf(TargetCalShow.getText().toString());
        if(nowcal<nowtogiccal)
        {
            EatAdvice.setText("您当前的没有吃多，敞开吃");
        }
        else
        {
            EatAdvice.setText("您现在应该控制一下饮食了");
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }
}