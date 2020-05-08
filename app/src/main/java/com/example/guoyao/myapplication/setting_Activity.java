package com.example.guoyao.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import github.ishaan.buttonprogressbar.ButtonProgressBar;

public class setting_Activity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ButtonProgressBar mLoader;
    private Spinner mLoaderTypeSp;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mLoader = (ButtonProgressBar) findViewById(R.id.cl_main);
        mLoaderTypeSp = (Spinner) findViewById(R.id.sp_select_type);
        mLoaderTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            TextView tv =(TextView)view;
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            tv.setTextSize(25.0f);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        init();
    }

    private void init() {
        //mLoaderTypeSp.setOnItemSelectedListener(this);
        mLoader.setOnClickListener(this);
    }

    private void callHandler() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress <= 100) {
                    updateUI();
                    progress++;
                    callHandler();
                } else {
                    progress = 0;
                    Toast.makeText(setting_Activity.this, "完成", Toast.LENGTH_SHORT).show();
                }
            }
        }, 20);
    }

    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoader.setProgress(progress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mLoader.getLoaderType() == ButtonProgressBar.Type.DETERMINATE) {
            callHandler();
        } else {
            mLoader.startLoader();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoader.stopLoader();
                    Toast.makeText(setting_Activity.this, "完成", Toast.LENGTH_SHORT).show();
                }
            }, 5000);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mLoader.reset();
        switch (position) {
            case 0:
                mLoader.setLoaderType(ButtonProgressBar.Type.DETERMINATE);
                break;
            case 1:
                mLoader.setLoaderType(ButtonProgressBar.Type.INDETERMINATE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
