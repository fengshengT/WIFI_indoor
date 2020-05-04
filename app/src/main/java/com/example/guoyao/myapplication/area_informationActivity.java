package com.example.guoyao.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class area_informationActivity extends AppCompatActivity {

    TextView data,id,name,height,width,map,decrible,state;
    private static final int REQUEST_PICK_MAP = 1;
    //1.定义不同菜单项的标识:
    final private int RED = 110;
    final private int GREEN = 111;
    final private int BLUE = 112;
    final private int YELLOW = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_information);
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
            name.setText("区域名称："+String.valueOf(editMap_name.getText().toString()));
            height.setText("区域长度："+Float.valueOf(editMapHeight.getText().toString()));
            width.setText("区域宽度："+Float.valueOf(editMapWidth.getText().toString()));
            map.setText("区域地图："+"null");
            decrible.setText("区域描述："+"轻轨站");
            state.setText("区域状态："+"false");
            float width = Float.valueOf(editMapWidth.getText().toString());
            float height = Float.valueOf(editMapHeight.getText().toString());

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
                String width = String.valueOf(editMap_name.getText().toString());
                edit_area1();
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
    public void  edit_area1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("当前区域"+10);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_delete_area1, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMapWidth = view.findViewById(R.id.map_name);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float width = Float.valueOf(editMapWidth.getText().toString());
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
        final View view = inflater.inflate(R.layout.activity_edit_area1, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMap_name = view.findViewById(R.id.map_name);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String width = String.valueOf(editMap_name.getText().toString());
                edit_area1();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_PICK_MAP:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                } else {
                    this.finish();
                    Toast.makeText(area_informationActivity.this,"你必须选择加入地图来进行定位",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
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

}