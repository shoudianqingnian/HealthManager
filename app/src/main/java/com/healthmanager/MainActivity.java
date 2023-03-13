package com.healthmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.healthmanager.util.ActivityCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ImageView MainReturn;
    GridView gvInfo;
    GridView gvInfo2;
    String[] titles=new String[]{"体重管理","运动管理","饮食管理","睡眠管理","病历管理","小病自治"};
    String[] titles2=new String[]{"肺炎常识","自我防护","肺炎自测"};
    int[] images=new int[]{R.drawable.weightfig,R.drawable.sportfig ,R.drawable.dietfig,R.drawable.sleepfig,R.drawable.caseicon,R.drawable.commonsenseofmedication};
    int[] images2=new int[]{R.drawable.commonsense,R.drawable.selfprotect,R.drawable.selftest};
    List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> listItems2 = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this); //加入Activity集中控制列表中
        init();
    }

    private void init(){
        //返回按钮
        MainReturn=findViewById(R.id.iv_mainreturn);

        //GridView布局添加文件
        gvInfo=findViewById(R.id.gvInfo);
        gvInfo2=findViewById(R.id.gvInfo2);
        //将填充数据转换为hash格式
        for(int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", titles[i]);
            listItems.add(map);
        }
        for(int j = 0; j < images2.length; j++) {
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("image", images2[j]);
            map2.put("title", titles2[j]);
            listItems2.add(map2);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,
                listItems,
                R.layout.gvitem,
                new String[]{"title", "image"},
                new int[]{R.id.ItemTitle, R.id.ItemImage});
        SimpleAdapter adapter2 = new SimpleAdapter(this,
                listItems2,
                R.layout.gvitem,
                new String[]{"title", "image"},
                new int[]{R.id.ItemTitle, R.id.ItemImage});
        gvInfo.setAdapter(adapter);
        gvInfo2.setAdapter(adapter2);

        gvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=null;
                switch(i){
                    case 0:
                        intent=new Intent(MainActivity.this, WeightManagementActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        Intent intente=new Intent(MainActivity.this, SportManagementActivity.class);
                        startActivity(intente);
                        finish();
                        break;
//                    case 2:
//                        intent=new Intent(MainActivity.this,EatActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
                    case 3:
                        intent=new Intent(MainActivity.this,SleepManagementActivity.class);
                        startActivity(intent);
                        finish();
                        break;
//                    case 4:
//                        intent=new Intent(MainActivity.this,CaseActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//                    case 5:
//                        intent=new Intent(MainActivity.this,SelfTreatmentActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//                    case 6:
//                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();

    }
}