package com.example.guoyao.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class locationActivity extends AppCompatActivity {

    TextView data;
    private Button btn_show_menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        data= (TextView)findViewById(R.id.data);
        data.setText("定位与显示");

        btn_show_menu = findViewById(R.id.btn_show_menu);
        btn_show_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到TextView演示界面
                Intent intent = new Intent(locationActivity.this, location_settingActivity.class);
                startActivity(intent);
            }
        });
    }
}

