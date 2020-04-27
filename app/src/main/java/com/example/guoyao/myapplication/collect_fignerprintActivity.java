package com.example.guoyao.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.widget.EditText;
import android.widget.TextView;

public class collect_fignerprintActivity extends AppCompatActivity {

    TextView data;
    private EditText x_num;
    private EditText y_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_fignerprint);
        x_num = (EditText) findViewById(R.id.x_loc);
        y_num = (EditText) findViewById(R.id.y_loc);
        data= (TextView)findViewById(R.id.data);
        data.setText("位置指纹数据采集");
    }
}