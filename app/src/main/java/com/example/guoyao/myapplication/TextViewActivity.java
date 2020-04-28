package com.example.guoyao.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class TextViewActivity extends Activity {

    WifiManager wifiManager;
    List<ScanResult> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        list = wifiManager.getScanResults();

        //初始化数据
        final List<ListitemAdapter.DataHolder> dataList = new ArrayList<ListitemAdapter.DataHolder>();
        for (int i = 0; i < list.size(); i++) {
            dataList.add(new ListitemAdapter.DataHolder(list.get(i).SSID, list.get(i).BSSID+"("+list.get(i).level+")",
                    false));
        }

        //构造Adapter
        final ListitemAdapter adapter = new ListitemAdapter(TextViewActivity.this,dataList);

        //设置adapter
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                boolean checked = dataList.get(position).checked;
                if (!checked) {
                    dataList.get(position).checked = true;
                }else {
                    dataList.get(position).checked = false;
                }
                adapter.notifyDataSetChanged();
            }
        });

        //全选按钮按钮设置
        Button all_sel = (Button) findViewById(R.id.all_sel);
        Button all_unsel = (Button) findViewById(R.id.all_unsel);
        all_sel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                for (int i = 0; i < 5; i++) {

                    dataList.get(i).checked=true;
                }

                adapter.notifyDataSetChanged();
            }
        });

        //全部取消的设置
        all_unsel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).checked=false;
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

}






