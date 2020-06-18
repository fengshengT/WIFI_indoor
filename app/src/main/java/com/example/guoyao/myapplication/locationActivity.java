package com.example.guoyao.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.guoyao.myapplication.mapview.PinView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class locationActivity extends AppCompatActivity {

    TextView data,result;
    private Timer timer;
    private PinView mapView;
    private TextView xEdit;
    private TextView yEdit;
    private Button btn_show_menu;

    double [] x;
    double [] y;
    private double m=2,number=6.5; //标识显示的初始位置
    int k=0;
    //1.定义不同菜单项的标识:
    final private int GREEN = 111;
    final private int YELLOW = 113;
    private Handler handler = new Handler();
    private Runnable task =new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            handler.postDelayed(this,1*1500);//设置延迟时间，此处是1.5秒
                //complete(k++);//需要执行的代码
            area();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView = findViewById(R.id.mapImageView);
        xEdit = findViewById(R.id.position_x);
        yEdit = findViewById(R.id.position_y);
        data= (TextView)findViewById(R.id.data);
        result= (TextView)findViewById(R.id.result);
        tryLoadOldMap();
        x = new double[]{1.2541,2.1431,4.2131,5.2187,7.6435,8.4056,10.3124,11.6487,13.2153,14.3864,16.6213,17.5407,19.1576,20.4408,22.1468,23.3259,25.3019,26.2835,28.1581,29.3652,30.0464,33.1801,33.9834,35.5461,37.1067,38.4682,40.6184,40.4925,43.1053,44.5107}; //分步定义数组，先定义数组名，然后再为数组赋值
        y = new double[]{7.1642,7.3052,6.7642,7.2246,7.4974,6.3734,6.9275,7.1346,7.3043,6.9462,7.1037,6.846,7.4081,6.8723,7.2427,6.9734,7.0632,7.1732,7.4527,4.8104,6.0081,8.3051,6.6021,7.6118,6.9356,7.8601,7.173,8.2437,6.7305,6.5081};
        handler.postDelayed(task,3000);//延迟3s调用
        result.setText("定位结果(单位:m)为: ");

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        //=============监听延时任务=====================

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //======================设置输出坐标============================
        @Override
        public void afterTextChanged(Editable editable) {

            if (ifUserInput) {
//                for(m=2;m<40;m++) {
//                    TimerTask task = new TimerTask() {
//                        @Override
//                        public void run() {
//                            PointF p = new PointF(Float.valueOf(m), Float.valueOf(7));
//                            mapView.setCurrentTPosition(p);
//                        }
//                    };
//                    Timer timer = new Timer();
//                    timer.schedule(task, 1000);//1秒后执行TimeTask的run方法
//                }
            }
        }
//        public void afterTextChanged(Editable editable) {
//       if (ifUserInput) {
//                if (!xEdit.getText().toString().equals("") && !yEdit.getText().toString().equals("")) {
//                    PointF p = new PointF(Float.valueOf(xEdit.getText().toString()),
//                            Float.valueOf(yEdit.getText().toString()));
//                    mapView.setCurrentTPosition(p);
//                }
//            }
//        }
    };

    public void complete(int k) {
        if(k>=30)
            k=29;
        PointF p = new PointF(Float.valueOf((float) x[k]), Float.valueOf((float) y[k]));
        mapView.setCurrentTPosition(p);
        xEdit.setText("X: "+String.valueOf((float) x[k]));
        yEdit.setText("Y: "+String.valueOf((float) y[k]));
    }



    private boolean ifUserInput = true;
    private void setTextWithoutTriggerListener() {
        ifUserInput = false;

        xEdit.setText(String.format(Locale.ENGLISH, "%.2f",mapView.getCurrentTCoord().x));
        yEdit.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().y));

        ifUserInput = true;
    }

    private static final String MAP_INFO = "map_info";
    private static final String MAP_PATH = "map_path";
    private static final String MAP_WIDTH = "width";
    private static final String MAP_height = "height";


    private boolean tryLoadOldMap() {
        SharedPreferences sharedPreferences = getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        String path = sharedPreferences.getString(MAP_PATH, null);
        if (path == null)
            return false;
        else {
            float width = sharedPreferences.getFloat(MAP_WIDTH, 0);
            float height = sharedPreferences.getFloat(MAP_height, 0);
            loadMapImage(Uri.fromFile(new File(path)), width, height);
            return true;
        }
    }
    private void loadMapImage(final Uri selectedImage, float width, float height) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            mapView.setImage(ImageSource.bitmap(bitmap));
            mapView.initialCoordManager(width, height);
            mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position
            setGestureDetectorListener(true);
        }
    }

    private GestureDetector gestureDetector = null;

    private void setGestureDetectorListener(boolean enable) {
        if (!enable)
            mapView.setOnTouchListener(null);
        else {
            if (gestureDetector == null) {
                gestureDetector = new GestureDetector(locationActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mapView.isReady()) {
                            mapView.moveBySingleTap(e);
                        } else {
                            Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }

            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
        }
    }
    protected void onRestart() {
        super.onRestart();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, YELLOW, 1, "选择定位区域");
        menu.add(1, GREEN, 2, "定位设置");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case GREEN:
                select_algorithm();
                break;
            case YELLOW:
                select_area();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void select_area(){
    // 通过builder 构建器来构造
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择定位区域");

    final String items[] = { "慧园B栋14楼", "教学楼1", "教学楼2"};

    // -1代表没有条目被选中
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {

            // [1]把选择的条目给取出来
            final String item = items[which];

            Toast.makeText(getApplicationContext(), item, 1).show();
        }
    });builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // 最后一步 一定要记得 和Toast 一样 show出来
        builder.show();
    }

    public void  select_algorithm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置 ");
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_location_setting, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void  area() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("该区域内无法读取信息 ");
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout


        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(locationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.show();
    }
}

