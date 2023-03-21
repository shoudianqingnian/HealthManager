package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.healthmanager.db.MyDatabaseHelper;

public class RetrieveActivity extends AppCompatActivity {
    private ImageView RetrieveReturn;
    private EditText FuserAccount;
    private TextView Fuserpassword;
    private Button Searchpwd;
    MyDatabaseHelper dbOpenHelper=new MyDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        init();
    }

    private void init(){
        RetrieveReturn=findViewById(R.id.iv_retrievereturn); //返回按钮
        FuserAccount=findViewById(R.id.et_fuseraccount);
        Fuserpassword=findViewById(R.id.tv_fuserpassword);
        Searchpwd=findViewById(R.id.btn_searchpwd);  //查找按钮
        RetrieveReturn.setOnClickListener(o);
        Searchpwd.setOnClickListener(o);
    }

    View.OnClickListener o=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.iv_retrievereturn:  //返回按钮
                    Intent intent=new Intent(RetrieveActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_searchpwd: //查找密码按钮
                    String useraccount=FuserAccount.getText().toString().trim();
                    SQLiteDatabase dbRead=dbOpenHelper.getReadableDatabase();
                    Cursor cursor=dbRead.query("tb_pwd",null,"useraccount=?",new String[]{useraccount},null,null,null);
                    if(cursor.getCount()==0)//验证账号是否存在
                    {
                        Toast.makeText(RetrieveActivity.this, "该账号尚未注册", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        while(cursor.moveToNext())
                        {
                            @SuppressLint("Range") String password=cursor.getString(cursor.getColumnIndex("password"));
                            Fuserpassword.setText(password);
                        }
                    }
                    break;
            }
        }
    };
}