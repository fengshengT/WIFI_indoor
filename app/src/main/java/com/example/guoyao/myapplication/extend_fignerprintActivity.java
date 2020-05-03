package com.example.guoyao.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;


import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Arrays;

public class extend_fignerprintActivity extends AppCompatActivity {


    private RadioButton typeRadioButton;
    private Button startButton;

    private EditText strideEdit;
    private EditText xEdit;
    private EditText yEdit;
    private TextView xTextView;
    private TextView yTextView;


    TextView data;
    static double interval = 1;             //格子间隔

    static double waf = 3.5;                 //墙壁衰减
    static int all_length = 10, all_wigth = 5; //区域的长宽
    static int peak1_x = 1, peak1_y = 1;         //顶点1坐标
    static int peak2_x = 3, peak2_y = 3;         //顶点2坐标
    static int all_mac = 1;                     //整个区域内用的ap的总个数
    static double A = -40;                 //单位距离处ap的信号强度

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_fignerprint);
        data = (TextView) findViewById(R.id.data);
        data.setText("位置指纹库扩充");

        Object[][] sample = {
                {1, 1, -43},
                {2, 2, -49},
                {3, 3, -52},
                {4, 4, -55},
                {5, 5, -56},
        };
        buildmap(sample, interval);
        data.setText("位置指纹库扩充");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void buildmap(Object[][] sample, double interval) {

        int length = sample.length;
        double[] d;
        double[] z;
        double[] n;
        double nn;

        d = new double[length];
        z = new double[length];
        n = new double[length];

        for (int i = 0; i < length; i++) {
            d[i] = getDistance(sample[i]);
            z[i] = getRSS(sample[i]);

            n[i] = (A - z[i]) / (10 * Math.log10(d[i]));
        }


        //计算衰减系数
        nn = Arrays.stream(n).average().orElse(Double.NaN);

        //生成RSS指纹地图
        double rss_map1[][] = new double[all_length][all_wigth];

        for (int x = 0; x < all_length; x += interval) {
            for (int y = 0; y < all_wigth; y += interval) {
                if ((((peak1_x - interval < x) && (x < peak2_x - interval)) || (peak1_x - interval > x) && (x > peak2_x - interval)) && (((peak1_y - interval < y) && (y < peak2_y - interval)) || (peak1_y - interval > y) && (y > peak2_y - interval)))
                    rss_map1[x][y] = A - 10 * nn * (double) Math.log10(Math.sqrt((x * interval) * (x * interval) + (y * interval) * (y * interval))) - waf;
                else
                    rss_map1[x][y] = A - 10 * nn * (double) Math.log10(Math.sqrt((x * interval) * (x * interval) + (y * interval) * (y * interval)));

            }
        }
        //AP位置处的RSS设置为0
        rss_map1[0][0] = 0;

        System.out.println("生成的位置指纹地图1：");
        for (double[] temp : rss_map1) {
            System.out.println(Arrays.toString(temp));
        }
        System.out.println(rss_map1[1][1]);
        System.out.println();

        System.out.println("生成位置指纹地图1的衰减系数：");
        System.out.println(nn);
    }
    public static double getRSS (Object[]Rss){
        double ps2 = (Integer) Rss[2];
        return ps2;
    }


    public static double getDistance (Object[]dis){
        double[] ps1 = {(Integer) dis[0], (Integer) dis[1]};
        return getDistance(ps1);
    }


    public static double getDistance ( double[] ps1){
        double d = 0;
        double total = 0;
        int length = ps1.length;
        for (int i = 0; i < length; i++) {
            double sub = (ps1[i] * interval) * (ps1[i] * interval);
            total = total + sub;
        }
        d = Math.sqrt(total);
        return d;
    }
}