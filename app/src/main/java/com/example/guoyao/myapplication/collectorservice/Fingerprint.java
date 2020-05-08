package com.example.guoyao.myapplication.collectorservice;

import java.util.ArrayList;

public class Fingerprint {
    public float x;
    public float y;

    public ArrayList<XWiFi> wifiData;

    public Fingerprint(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
