package com.example.guoyao.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class set_areaActivity <string> extends AppCompatActivity {

    private Button mBtnTextView;
    private Button mBtnEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_area);
        mBtnTextView = findViewById(R.id.btn_textview);
        mBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到area_informationActivity演示界面
                Intent intent = new Intent(set_areaActivity.this, area_informationActivity.class);
                startActivity(intent);
            }
        });
        mBtnEditText = findViewById(R.id.btn_edittext);
        mBtnEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到EditText演示界面
                Intent intent = new Intent(set_areaActivity.this, ButtonActivity.class);
                startActivity(intent);
            }
        });

    }

}