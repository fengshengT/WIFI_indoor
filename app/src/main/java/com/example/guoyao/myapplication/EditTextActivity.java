package com.example.guoyao.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTextActivity extends AppCompatActivity {

    private Button mBtnStart;

    EditText mEtAP1;//AP1
    EditText mEtAP2;//AP2





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        mEtAP1= findViewById(R.id.et_1);
        mEtAP2= findViewById(R.id.et_2);

        //int AP1 = Integer.parseInt(mEtAP2.getText().toString());
        //int AP2 = Integer.parseInt(mEtAP2.getText().toString());



        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTextActivity.this,ResualtActivity.class);
                Bundle bundle= new Bundle();

                bundle.putInt("AP1", Integer.parseInt(mEtAP1.getText().toString()));
                bundle.putInt("AP2", Integer.parseInt(mEtAP2.getText().toString()));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });





    }



}
