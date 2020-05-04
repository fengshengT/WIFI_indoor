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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.guoyao.myapplication.collectorservice.Fingerprint;
import com.example.guoyao.myapplication.mapview.PinView;



import java.io.File;
import java.io.IOException;
import java.util.Locale;

/***
 * Created by Leslie Xong 2018,Oct (lesliexong@gmail.com)
 * For indoor positioning new beginners, this app is used to collect wifi/beacon fingerprint data.
 */

public class ButtonActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int DEFAULT_TRAIN_TIME = 6000;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    private RadioButton typeRadioButton;
    private Button startButton;

    private EditText strideEdit;
    private EditText xEdit;
    private EditText yEdit;
    private TextView xTextView;
    private TextView yTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_fignerprint);
        mapView = findViewById(R.id.mapImageView);

        startButton = findViewById(R.id.start_collect);
        typeRadioButton = findViewById(R.id.type);
        xEdit = findViewById(R.id.position_x);
        yEdit = findViewById(R.id.position_y);
        strideEdit = findViewById(R.id.stride_length);
        strideEdit.addTextChangedListener(textWatcher);
        xEdit.addTextChangedListener(textWatcher);
        yEdit.addTextChangedListener(textWatcher);

        xTextView = findViewById(R.id.x_label);
        yTextView = findViewById(R.id.y_label);

        typeRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            }
        });

        requestPermissionBeforeStart();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!tryLoadOldMap())
                        selectMapFromPhone();
                } else {
                    // permission denied could not use this app
                    Toast.makeText(ButtonActivity.this,"应用未获得权限",Toast.LENGTH_SHORT).show();
                    ButtonActivity.this.finish();
                }
            }
        }
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

    private void saveMapInfo(Uri uri, float width, float height) {
        SharedPreferences sharedPreferences = getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MAP_PATH, getRealPathFromURI(uri));
        editor.putFloat(MAP_WIDTH, width);
        editor.putFloat(MAP_height, height);
        editor.apply();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.hashCode() == strideEdit.getText().hashCode()) {
                if (strideEdit.getText().toString().trim().equals("")) {
                    Toast.makeText(ButtonActivity.this,"间隔不能未空",Toast.LENGTH_SHORT).show();
                } else {
                    float strideLength = Float.valueOf(strideEdit.getText().toString());
                    mapView.setStride(strideLength);
                }
            } else if (ifUserInput) {
                if (!xEdit.getText().toString().equals("") && !yEdit.getText().toString().equals("")) {
                    PointF p = new PointF(Float.valueOf(xEdit.getText().toString()),
                            Float.valueOf(yEdit.getText().toString()));
                    mapView.setCurrentTPosition(p);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_collect:
                break;
            case R.id.pick_map_button:
                selectMapFromPhone();
                break;
        }
    }


    public void selectMapFromPhone() {
        Toast.makeText(ButtonActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
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
                    setMapWidthHeight(selectedImage);
                } else {
                    this.finish();
                    Toast.makeText(ButtonActivity.this,"你必须选择一张地图来进行定位",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void setMapWidthHeight(final Uri selectedImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("地图信息");

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_map, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        final EditText editMapWidth = view.findViewById(R.id.map_width);
        final EditText editMapHeight = view.findViewById(R.id.map_height);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float width = Float.valueOf(editMapWidth.getText().toString());
                float height = Float.valueOf(editMapHeight.getText().toString());
                saveMapInfo(selectedImage, width, height);
                loadMapImage(selectedImage, width, height);
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
            xTextView.setText(String.format(Locale.ENGLISH, "X(max:%.1f)", width));
            yTextView.setText(String.format(Locale.ENGLISH, "Y(max:%.1f)", height));
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
                gestureDetector = new GestureDetector(ButtonActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mapView.isReady()) {
                            mapView.moveBySingleTap(e);
                            setTextWithoutTriggerListener();
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
    public void show_complete(int x,int y){
        Fingerprint fingerprint = new Fingerprint(x, y);
        fingerprint.x=x;
        fingerprint.y=y;
        mapView.addFingerprintPoint(fingerprint);
    }
    private boolean ifUserInput = true;

    private void setTextWithoutTriggerListener() {
        ifUserInput = false;

        xEdit.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().x));
        yEdit.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().y));
        ifUserInput = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
