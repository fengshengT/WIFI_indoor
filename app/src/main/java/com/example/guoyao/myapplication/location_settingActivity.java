package com.example.guoyao.myapplication;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


public class location_settingActivity <string> extends AppCompatActivity {

    private Button btn_show_menu;
    TextView data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting);

        initview();

    }


    //关联控件与行为
    public void initview() {
        data= findViewById(R.id.data);
    }

}