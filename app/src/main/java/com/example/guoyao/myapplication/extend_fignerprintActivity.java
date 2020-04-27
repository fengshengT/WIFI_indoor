package com.example.guoyao.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.widget.TextView;

public class extend_fignerprintActivity extends AppCompatActivity {

    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_fignerprint);
        data= (TextView)findViewById(R.id.data);
        data.setText("位置指纹库扩充");
    }
}