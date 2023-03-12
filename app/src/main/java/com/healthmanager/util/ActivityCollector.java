package com.healthmanager.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>(); //Activity收集器：收集所有的Activity
    public static void addActivity(Activity activity){ //增加Activity
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){ //删除Activity
        activities.remove(activity);
    }
    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
