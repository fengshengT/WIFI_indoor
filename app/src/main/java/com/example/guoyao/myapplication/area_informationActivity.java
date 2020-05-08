package com.example.guoyao.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.guoyao.myapplication.mapview.PinView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import github.ishaan.buttonprogressbar.ButtonProgressBar;

public class area_informationActivity extends AppCompatActivity
        implements View.OnClickListener{

    private ButtonProgressBar mLoader;
    private int progress = 0;

    TextView data,id,name,height,width,map,decrible,state;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    //1.定义不同菜单项的标识:
    final private int RED = 110;
    final private int GREEN = 111;
    final private int BLUE = 112;
    final private int YELLOW = 113;
    private PinView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_information);
        mapView = findViewById(R.id.mapImageView);
        mLoader = (ButtonProgressBar) findViewById(R.id.cl_main);
        init();

    }
    private void init() {
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
                    Toast.makeText(area_informationActivity.this, "完成", Toast.LENGTH_SHORT).show();
                }
            }
        }, 30);
    }

    public void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoader.setProgress(progress);
            }
        });
    }


    public void onClick(View v) {
        if (mLoader.getLoaderType() == ButtonProgressBar.Type.DETERMINATE) {
            callHandler();
        } else {
            mLoader.startLoader();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoader.stopLoader();
                    Toast.makeText(area_informationActivity.this, "完成", Toast.LENGTH_SHORT).show();
                }
            }, 5000);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        data= (TextView)findViewById(R.id.data);
        id= (TextView)findViewById(R.id.id);
        name= (TextView)findViewById(R.id.name);
        width= (TextView)findViewById(R.id.width);
        height= (TextView)findViewById(R.id.height);
        map= (TextView)findViewById(R.id.map);
        decrible= (TextView)findViewById(R.id.decrible);
        state= (TextView)findViewById(R.id.state);
        data.setText("区域信息管理");
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, YELLOW, 1, "添加定位采集区域");
        menu.add(1, GREEN, 2, "编辑定位采集区域");
        menu.add(1, BLUE, 3, "删除定位采集区域");
        menu.add(1, RED, 4, "上传区域地图(图片)");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case RED:
                selectMapFromPhone();
                break;
            case GREEN:
                edit_area();
                break;
            case BLUE:
                delect_area();
                break;
            case YELLOW:
               add_area();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
public void  add_area() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("添加定位采集区域 ");
    LayoutInflater inflater = this.getLayoutInflater();
    final View view = inflater.inflate(R.layout.activity_add_area, null);

    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder.setView(view);

    final EditText editMap_name = view.findViewById(R.id.map_name);
    final EditText editMapWidth = view.findViewById(R.id.map_width);
    final EditText editMapHeight = view.findViewById(R.id.map_height);

    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            id.setText("序号(id)："+1);
            name.setText("区域名称："+editMap_name.getText().toString());
            height.setText("区域长度："+Float.valueOf(editMapHeight.getText().toString()));
            width.setText("区域宽度："+Float.valueOf(editMapWidth.getText().toString()));
            map.setText("区域地图："+"null");
            decrible.setText("区域描述："+"null");
            state.setText("区域状态："+"false");
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
    public void  edit_area() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑定位区域 ");
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_edit_area1, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMap_name = view.findViewById(R.id.map_name);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String map_name=editMap_name.getText().toString();
                name.setText("区域名称："+editMap_name.getText().toString());
                edit_area1(map_name);
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
    public void  edit_area1(final String map_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前区域："+map_name);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_edit_area2, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMapWidth = view.findViewById(R.id.map_width);
        final EditText editMapHeight = view.findViewById(R.id.map_height);
        final EditText editMap_id = view.findViewById(R.id.map_id);
        final EditText editMap_map = view.findViewById(R.id.map);
        final EditText editMap_decribe = view.findViewById(R.id.map_decrible);
        final EditText editMap_state = view.findViewById(R.id.map_state);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id.setText("序号(id)："+Integer.valueOf(editMap_id.getText().toString()));
                name.setText("区域名称："+map_name);
                height.setText("区域长度："+Float.valueOf(editMapHeight.getText().toString()));
                width.setText("区域宽度："+Float.valueOf(editMapWidth.getText().toString()));
                map.setText("区域地图："+editMap_map.getText().toString());
                decrible.setText("区域描述："+editMap_decribe.getText().toString());
                state.setText("区域状态："+editMap_state.getText().toString());
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
    public void  delect_area() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除定位区域 ");
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_delete_area, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMap_name = view.findViewById(R.id.map_name);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                id.setText("序号(id)：");
                name.setText("区域名称：");
                height.setText("区域长度：");
                width.setText("区域宽度：");
                map.setText("区域地图：");
                decrible.setText("区域描述：");
                state.setText("区域状态：");

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


    private static final String MAP_INFO = "map_info";
    private static final String MAP_PATH = "map_path";
    private static final String MAP_WIDTH = "width";
    private static final String MAP_height = "height";


    public void selectMapFromPhone() {
        Toast.makeText(area_informationActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_PICK_MAP);  //one can be replaced with any action code
    }

}