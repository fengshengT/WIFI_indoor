package com.example.guoyao.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class collect_fignerprintActivity extends AppCompatActivity {

    TextView data;
    TextView text;
    WifiManager wifiManager;
    private EditText x_num;
    private EditText y_num;
    private Cursor cursor1;
    private SQLiteDatabase sqldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_fignerprint);
        x_num = (EditText) findViewById(R.id.x_loc);
        y_num = (EditText) findViewById(R.id.y_loc);
        data= (TextView)findViewById(R.id.data);
        data.setText("位置指纹数据采集");
        text= (TextView)findViewById(R.id.text);

        getwifi();
//        try{
//            cursor1=sqldata.rawQuery("select * from rss",null);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if(cursor1.moveToNext())
//            do {
//                text.setText("测试" + 1);
//                int id = cursor1.getInt(cursor1.getColumnIndex("id"));
//                int rssi1 = cursor1.getInt(cursor1.getColumnIndex("rssi1"));
//                String mac1 = cursor1.getString(cursor1.getColumnIndex("mac1"));
//                int rssi2 = cursor1.getInt(cursor1.getColumnIndex("rssi2"));
//                String mac2 = cursor1.getString(cursor1.getColumnIndex("mac2"));
//                int rssi3 = cursor1.getInt(cursor1.getColumnIndex("rssi3"));
//                String mac3 = cursor1.getString(cursor1.getColumnIndex("mac3"));
//                int rssi4 = cursor1.getInt(cursor1.getColumnIndex("rssi4"));
//                String mac4 = cursor1.getString(cursor1.getColumnIndex("mac4"));
//                int rssi5 = cursor1.getInt(cursor1.getColumnIndex("rssi5"));
//                String mac5 = cursor1.getString(cursor1.getColumnIndex("mac5"));
//            }while (cursor1.moveToNext());

//        if(cursor1.moveToNext())
//        {
//            do{
//                int id=cursor1.getInt(cursor1.getColumnIndex("id"));
//                int rssi1=cursor1.getInt(cursor1.getColumnIndex("rssi1"));
//                String mac1=cursor1.getString(cursor1.getColumnIndex("mac1"));
//                int rssi2=cursor1.getInt(cursor1.getColumnIndex("rssi2"));
//                String mac2=cursor1.getString(cursor1.getColumnIndex("mac2"));
//                int rssi3=cursor1.getInt(cursor1.getColumnIndex("rssi3"));
//                String mac3=cursor1.getString(cursor1.getColumnIndex("mac3"));
//                int rssi4=cursor1.getInt(cursor1.getColumnIndex("rssi4"));
//                String mac4=cursor1.getString(cursor1.getColumnIndex("mac4"));
//                int rssi5=cursor1.getInt(cursor1.getColumnIndex("rssi5"));
//                String mac5=cursor1.getString(cursor1.getColumnIndex("mac5"));
//            }while (cursor1.moveToNext());
//        }

    }

    //将扫描的WiFi存入sqlite
    public void getwifi() {
        List<ScanResult> scanResults = new ArrayList<>();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        scanResults = wifiManager.getScanResults();
        for (int i = 0; i < 5; i++) {
            int rss = wifiManager.calculateSignalLevel(scanResults.get(i).level, 100);
            ContentValues values = new ContentValues();
//            values.put("x_loc", Integer.valueOf(x_num.getText().toString()));
//            values.put("y_loc", Integer.valueOf(y_num.getText().toString()));
            values.put("x_loc", 1);
            values.put("y_loc", 1);
            switch(scanResults.get(i).BSSID)
            {
                case "24:7e:51:56:c8:f0":
                    values.put("rssi1", scanResults.get(i).level);
                    values.put("mac1", scanResults.get(i).BSSID);
                    System.out.println(scanResults.get(i).SSID);
                    break;
                case "cc:08:fb:c7:74:f8":
                    values.put("rssi2", scanResults.get(i).level);
                    values.put("mac2", scanResults.get(i).BSSID);
                    System.out.println(scanResults.get(i).SSID);
                    break;
                case "cc:08:fb:c7:74:fa":
                    values.put("rssi3", scanResults.get(i).level);
                    values.put("mac3", scanResults.get(i).BSSID);
                    System.out.println(scanResults.get(i).SSID);
                    break;
                case "bc:46:99:e0:e2:46":
                    values.put("rssi4", scanResults.get(i).level);
                    values.put("mac4", scanResults.get(i).BSSID);
                    System.out.println(scanResults.get(i).SSID);
                    break;
                case "54:a7:03:3e:1f:af":
                    values.put("rssi5", scanResults.get(i).level);
                    values.put("mac5", scanResults.get(i).BSSID);
                    System.out.println(scanResults.get(i).SSID);
                    break;
            }
            try {
                sqldata.insert("rss", null, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
            values.clear();
        }
    }
}