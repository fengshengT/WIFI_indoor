package com.example.guoyao.myapplication;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.guoyao.myapplication.RssDbHelper;


public class MainActivity<string> extends AppCompatActivity {

    private Button mBtnTextView;
    private Button mBtnButton;
    private Button mBtnEditText;
    WifiManager wifiManager;
    Handler handler;
    Runnable runnable;
    private Object Menu;
    private SQLiteDatabase sqldata;
    private EditText x_num;
    private EditText y_num;
    private RssDbHelper rssDbHelper;
    private Cursor cursor1;

    private List<ScanResult> scanResults = new ArrayList<>();
    private Button btn_show_menu; //显示大猪 小猪
    TextView data;

    int x_loc;
    int y_loc;
    int rssi;
    String mac;
    int sort;
    int name;
    String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* 建立数据库、表
        * by liu 2020-426
        */
        rssDbHelper=new RssDbHelper(this);
        sqldata=rssDbHelper.getWritableDatabase();
        initview();
        btn_show_menu = (Button) findViewById(R.id.btn_show_menu);
        btn_show_menu.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this,btn_show_menu);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.lpig:
                                Toast.makeText(MainActivity.this,"你点了小猪~",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.bpig:
                                Toast.makeText(MainActivity.this,"你点了大猪~",Toast.LENGTH_SHORT).show();
                                getwifi();
                                        try{
                                            cursor1=sqldata.rawQuery("select * from rss",null);
                                            while(cursor1.moveToNext()){
                                                x_loc=cursor1.getInt(cursor1.getColumnIndex("x_loc"));
                                                y_loc=cursor1.getInt(cursor1.getColumnIndex("y_loc"));
                                                rssi=cursor1.getInt(cursor1.getColumnIndex("rssi"));
                                                name=cursor1.getInt(cursor1.getColumnIndex("name"));
                                                mac=cursor1.getString(cursor1.getColumnIndex("mac"));
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    data.setText(x_loc+"  "+y_loc+"   "+rssi+"  "+mac+"  ");
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


        mBtnTextView = findViewById(R.id.btn_textview);
        mBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到TextView演示界面
                Intent intent = new Intent(MainActivity.this, TextViewActivity.class);
                startActivity(intent);
            }
        });
        mBtnButton = findViewById(R.id.btn_button);
        mBtnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到Button演示界面
                Intent intent = new Intent(MainActivity.this, ButtonActivity.class);
                startActivity(intent);
            }
        });
        mBtnEditText = findViewById(R.id.btn_edittext);
        mBtnEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到EditText演示界面
                Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                startActivity(intent);
            }
        });

    }
   //关联控件与行为
    public void initview() {
        x_num = (EditText) findViewById(R.id.x_loc);
        y_num = (EditText) findViewById(R.id.y_loc);
        data= findViewById(R.id.data);
    }

    //将扫描的WiFi存入sqlite
    public void getwifi() {
        List<ScanResult> scanResults1 = new ArrayList<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        scanResults1 = wifiManager.getScanResults();
        int sor = 0;
        int lev = wifiManager.calculateSignalLevel(scanResults1.get(0).level, 100);
        data.setText("\nWiFi名称:" + scanResults1.get(0).SSID +
                "\nMAC地址:" + scanResults1.get(0).BSSID +
                "\nRSSI:" + scanResults1.get(0).level + "\n");
        for (int i = 0; i < 5; i++) {
            int rss = wifiManager.calculateSignalLevel(scanResults1.get(i).level, 100);
            ContentValues values = new ContentValues();
            values.put("x_loc", Integer.valueOf(x_num.getText().toString()));
            values.put("y_loc", Integer.valueOf(y_num.getText().toString()));
            values.put("rssi", scanResults1.get(i).level);
            values.put("mac", scanResults1.get(i).BSSID);
            try {
                sqldata.insert("rss", null, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
            values.clear();
        }
    }

}