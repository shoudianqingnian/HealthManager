package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanager.db.MyDatabaseHelper;
import com.healthmanager.util.ActivityCollector;

public class RegisterActivity extends AppCompatActivity {
    private EditText RegisterAccount,RegisterPassword;//手机号密码的输入框
    private ImageView RegisterReturn;//返回按钮
    private Button Registerbtn;//注册按钮
    private TextView Loginbtn2;//登录按钮
    MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void init(){
        RegisterAccount=findViewById(R.id.register_account);
        RegisterPassword=findViewById(R.id.register_password);
        RegisterReturn=findViewById(R.id.iv_registerreturn);
        Registerbtn=findViewById(R.id.btn_register);
        Loginbtn2=findViewById(R.id.tv_entertologin);
        RegisterReturn.setOnClickListener(o);
        Loginbtn2.setOnClickListener(o);
        Registerbtn.setOnClickListener(o);
    }

    View.OnClickListener o= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.iv_registerreturn : //返回按钮
                    returnEnter() ;
                    break;
                case R.id.tv_entertologin://登录按钮
                    returnEnter() ;//回到登录界面
                    break;
                case R.id.btn_register: //注册按钮
                    register();
                    break;
            }
        }
    };

    private void register() {
        String useraccount=RegisterAccount.getText().toString().trim();   //获取用户输入的账号
        String userpassword=RegisterPassword.getText().toString().trim();//获取设置密码
        if(TextUtils.isEmpty(useraccount)){                         //验证输入的账号是否为空
            Toast.makeText(this,"账号不能为空!",Toast.LENGTH_SHORT).show();
            RegisterAccount.requestFocus() ;                        //失去焦点
            return ;
        }
        else if(useraccount.length()<6 || useraccount.length()>10){                         //验证输入的账号是否为合理
            Toast.makeText(this,"账号不合理",Toast.LENGTH_SHORT).show();
            return;
        }//验证输入的账号是否为六位
        else if(TextUtils.isEmpty(userpassword) ){              //验证输入的密码是否为空
            Toast.makeText(this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            RegisterPassword.requestFocus();
            return ;
        }
        SQLiteDatabase dbWrite=dbOpenHelper.getWritableDatabase();
        SQLiteDatabase dbRead=dbOpenHelper.getReadableDatabase();
        Cursor cursor=dbRead.query("tb_pwd",
                new String[]{"useraccount"},
                "useraccount=?",
                new String[]{useraccount},
                null,
                null,null);
        //验证账号是否已经注册
        if(cursor.getCount()==0)
        {
            ContentValues values=new ContentValues();
            values.put("useraccount",useraccount);
            values.put("userpassword",userpassword);
            dbWrite.insert("tb_pwd",null,values);
            final ProgressDialog pd=new ProgressDialog(this) ;//初始化等待条
            pd.setMessage("正在注册中...") ;
            pd.show() ;//显示等待条
            /**
             * 模拟后台请求
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();//消失等待条
                }
            }).start() ;//开启线程
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,LoginActivity.class) ;
            startActivity(intent) ;
        }
        else
        {
            Toast.makeText(this, "该账号已被注册！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void returnEnter() {
        //跳转到登录界面
        Intent intent=new Intent(this,LoginActivity.class) ;
        startActivity(intent) ;
        finish();
    }
}