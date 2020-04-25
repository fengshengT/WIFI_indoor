package com.example.guoyao.myapplication;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TextViewActivity extends AppCompatActivity {

    private TextView mTv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        mTv1= findViewById(R.id.tv_1);
        mTv1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线


    }
}
