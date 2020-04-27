package com.example.guoyao.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class area_informationActivity extends AppCompatActivity {

    TextView data;
    //1.定义不同颜色的菜单项的标识:
    final private int RED = 110;
    final private int GREEN = 111;
    final private int BLUE = 112;
    final private int YELLOW = 113;
    final private int GRAY = 114;
    final private int CYAN = 115;
    final private int BLACK = 116;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_information);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        data= (TextView)findViewById(R.id.data);
        data.setText("区域信息管理");
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, YELLOW, 1, "添加定位采集区域");
        menu.add(1, GREEN, 2, "删除定位采集区域");
        menu.add(1, BLUE, 3, "编辑定位采集区域");
        menu.add(1, RED, 4, "上传区域地图(图片)");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case RED:
                data.setTextColor(Color.RED);
                break;
            case GREEN:
                data.setTextColor(Color.GREEN);
                break;
            case BLUE:
                data.setTextColor(Color.BLUE);
                break;
            case YELLOW:
                data.setTextColor(Color.YELLOW);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}