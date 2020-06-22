package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

public class recordActivity extends AppCompatActivity {
    private Button returnMainButton;
    private TextView giveUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        giveUpText=(TextView)findViewById(R.id.giveUp);
        StudyTime tempNum= DataSupport.find(StudyTime.class,1);
        int giveUpNum=tempNum.getGiveUpNum();
        giveUpText.setText("放弃的次数:"+giveUpNum);
        returnMainButton=(Button)findViewById(R.id.returnMain);
        returnMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(recordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
