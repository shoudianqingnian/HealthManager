package com.healthmanager.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SaveStepPreference {
    public static SharedPreferences sharedPreferences; //定义SharedPreference对象
    private static SharedPreferences.Editor editor;//定义SharedPreference.Editor对象

    public static void createSharedPreferences(Context context) //创建SharedPreference对象
    {
        String appName=context.getPackageName();//从上下文中获取包名
        sharedPreferences=context.getSharedPreferences(appName,Context.MODE_PRIVATE);//定义
        editor=sharedPreferences.edit();//修改存储对象
    }

    public static boolean isUnCreate()  //判断SharedPreference是否被创建
    {
        boolean result=(sharedPreferences==null)?true:false;
        if(result)
        {
            Log.e("提醒","轻量级存储没有被创建");
        }
        return result;
    }


    public static boolean putIntValues(String key,int value) //向SharedPreference中保存值
    {
        if(isUnCreate()){
            return false;
        }
        editor.putInt(key, value);
        return editor.commit();
    }


    public static int getIntValues(String key,int defValue) //向SharedPreference中获取值
    {
        if(isUnCreate())
        {
            return 0;
        }
        int int_value=sharedPreferences.getInt(key, defValue);
        return int_value;
    }
}
