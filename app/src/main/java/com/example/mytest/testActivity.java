package com.example.mytest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        AlertDialog.Builder dialog=new AlertDialog.Builder(testActivity.this);
        dialog.setTitle("警告");
        dialog.setMessage("上课期间禁止玩手机");
        dialog.setPositiveButton("明白", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(testActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();



    }
}
