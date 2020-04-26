package com.example.guoyao.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TextViewActivity extends AppCompatActivity {

    private TextView mTv1;
    WifiManager wifiManager;
    TextView text;
    Handler handler=new Handler();
    List<ScanResult> list=new ArrayList<>();
    private List<ScanResult> scanResults = new ArrayList<>();
//延时函数 by liu 2020-4-23
    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            handler.postDelayed(this, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        text= findViewById(R.id.text);

        ScanResult result;
        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
            wifiManager.startScan();
            list = wifiManager.getScanResults();

            StringBuilder sb = new StringBuilder("WiFi信息\n");
            StringBuilder wifi = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                result = (ScanResult) list.get(i);
                sb.append("\nWiFi名称:" + result.SSID +
                        "\nMAC地址:" + result.BSSID +
                        "\nRSSI:" + result.level + "\n");
                wifi.append(result.level + " ");
            }
            wifi.append("\n");
            text.setText(sb.toString());
            text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

    }


}

