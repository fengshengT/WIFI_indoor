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


import github.ishaan.buttonprogressbar.ButtonProgressBar;

public class area_informationActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_information);
        mapView = findViewById(R.id.mapImageView);
        requestPermissionBeforeStart();
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

    public void selectMapFromPhone() {
        Toast.makeText(area_informationActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_PICK_MAP);  //one can be replaced with any action code
    }

    //To solve some phone's cannot get the position permission, result will invoke "onRequestPermissionsResult"
    public void requestPermissionBeforeStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                return;
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        } else {
            if (!tryLoadOldMap())
                selectMapFromPhone();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        float width = 10;
        float height = 8;
        switch (requestCode) {
            case REQUEST_PICK_MAP:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    saveMapInfo(selectedImage, width, height);
                    loadMapImage(selectedImage, width, height);
                } else {
                    this.finish();
                    showToast("你必须选择一张地图来进行定位");
                }
                break;

            default:
                break;
        }
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void saveMapInfo(Uri uri, float width, float height) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MAP_PATH, getRealPathFromURI(uri));
        editor.putFloat(MAP_WIDTH, width);
        editor.putFloat(MAP_height, height);
        editor.apply();
    }

    //Pick picture from gallery is a uri not the actual file.
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
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
            //mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //显示当前位置
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
                gestureDetector = new GestureDetector(area_informationActivity.this, new GestureDetector.SimpleOnGestureListener() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}