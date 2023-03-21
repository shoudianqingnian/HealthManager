package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

public class LoginActivity extends AppCompatActivity{

    private ImageView LoginExitBtn; //退出登录界面按钮（退出应用程序）
    private TextView GoRegisterBtn;//注册按钮
    private EditText editAccount,editPassword;//用户名密码的输入框
    private Button Loginbtn,Forgetpwd;//登录按钮
    private String useraccount,userpassword;//用于加载输入完成后的用户名和密码
    MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginin);
        ActivityCollector.addActivity(this); //加入Activity集中控制列表中
        init();//初始化控件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void init() { //绑定控件的id
        LoginExitBtn=findViewById(R.id.iv_exit);
        GoRegisterBtn =findViewById(R.id.tv_register) ;
        editAccount =findViewById(R.id.et_username);
        editPassword=findViewById(R.id.et_password) ;
        Loginbtn=findViewById(R.id.bt_common_login);
        Forgetpwd=findViewById(R.id.bn_forget_pwd);
        //触发点击事件
        LoginExitBtn.setOnClickListener(o);
        GoRegisterBtn.setOnClickListener(o);
        Loginbtn.setOnClickListener(o);
        Forgetpwd.setOnClickListener(o);
    }

    View.OnClickListener o =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()){
                case R.id.iv_exit: //退出应用按钮
                    ActivityCollector.finishAll(); //将Activity收集器里面所有的Activity都finish掉
                    break;
            case R.id.tv_register ://注册按钮：跳转到注册界面
                intent=new Intent(LoginActivity.this,RegisterActivity.class) ;
                startActivity(intent) ;
                finish();
                break;
            case R.id.bt_common_login://登录按钮：登入主界面
                login();
                break;
                case R.id.bn_forget_pwd://忘记密码按钮:跳转到忘记密码界面
                intent=new Intent(LoginActivity.this,RetrieveActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    } ;

    private void login() {
        useraccount=editAccount.getText().toString().trim();//获取账号
        userpassword=editPassword.getText().toString().trim();//获取密码
        if(TextUtils.isEmpty(useraccount)){               //判断账号是否为空
            Toast.makeText(this,"账号不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(userpassword)){       //判断密码是否为空
            Toast.makeText(this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase dbRead=dbOpenHelper.getReadableDatabase();
        Cursor cursor=dbRead.query("tb_pwd",
                null,
                "useraccount=?",
                new String[]{useraccount},
                null,null,null);
        if(cursor.getCount()==0)                  //验证账号是否已经注册
        {
            Toast.makeText(this, "该账号尚未注册", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            while (cursor.moveToNext())          //验证账号密码是否匹配
            {
                @SuppressLint("Range")String getpassword=cursor.getString(cursor.getColumnIndex("userpassword"));
                if(getpassword.equalsIgnoreCase(userpassword))//验证密码
                {
                    final ProgressDialog pd=new ProgressDialog(LoginActivity.this ) ;//等待动画
                    pd.setMessage("正在登录...");//等待信息
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
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    /**
                     *跳转到主界面
                     */
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class) ;
                    startActivity(intent) ;
                }
                else
                {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}