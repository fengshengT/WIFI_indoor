package com.example.guoyao.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;


import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.guoyao.myapplication.mapview.PinView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class locationActivity extends AppCompatActivity {

    TextView data,result;
    private Timer timer;
    private Button btn_show_menu;

    //1.定义不同菜单项的标识:
    final private int GREEN = 111;
    final private int YELLOW = 113;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView = findViewById(R.id.mapImageView);
        data= (TextView)findViewById(R.id.data);
        result= (TextView)findViewById(R.id.result);
        data.setText("定位与显示");
        result.setText("定位结果(单位:m)为: "+"("+1.1234+","+1.1234+")");
        tryLoadOldMap();
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

    private PinView mapView;
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

    final String items[] = { "二塘", "教学楼1", "教学楼2"};

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
}

