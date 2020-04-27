package com.example.guoyao.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ButtonActivity extends AppCompatActivity {

    TextView data;
    private Button mBtn3;
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
        setContentView(R.layout.activity_button);
        mBtn3 = findViewById(R.id.bt_3);
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ButtonActivity.this,"按钮3被点击了",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        data= (TextView)findViewById(R.id.data);
        data.setText("点击菜单选择颜色");
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, RED, 4, "红色");
        menu.add(1, GREEN, 2, "绿色");
        menu.add(1, BLUE, 3, "蓝色");
        menu.add(1, YELLOW, 1, "黄色");
        menu.add(1, GRAY, 5, "灰色");
        menu.add(1, CYAN, 6, "蓝绿色");
        menu.add(1, BLACK, 7, "黑色");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            case GRAY:
                data.setTextColor(Color.GRAY);
                break;
            case CYAN:
                data.setTextColor(Color.CYAN);
                break;
            case BLACK:
                data.setTextColor(Color.BLACK);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}