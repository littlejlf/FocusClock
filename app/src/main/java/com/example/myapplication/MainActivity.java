package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mytest.testActivity;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewCountDown;      //显示剩余时间
    private Button mButtonStartPause;           //开始/结束按钮
    private SeekBar mBar;                      //进度条
    private TextView mTxt;                      //进度条下方文本
    private ImageView MusicImage;   //音乐播放器

    private CountDownTimer mCountDownTimer;  //剩余时间计时器

    private boolean mTimerRunning;              //是否在计时

//    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillis=30*60000;     //默认一开始的时间是30分钟
    private long tempTime;      //记录当时选择的学习时长
    private TextView myname;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.getDatabase();


        if(DataSupport.findFirst(StudyTime.class)==null) {//如果该数据库没有元素就新建一个，初始的学习时间为0分钟
        StudyTime a = new StudyTime();
        a.setTotalTime(0);
        a.save();
        }
        setContentView(R.layout.activity_main);
        mTextViewCountDown = findViewById(R.id.textView3);
        mButtonStartPause = findViewById(R.id.button_start_pause);
         mBar= (SeekBar) findViewById(R.id.seekBar_time);
        mTxt = (TextView) findViewById(R.id.textView4);
        MusicImage=(ImageView)findViewById(R.id.listen);
        myname=(TextView)findViewById(R.id.ttt);
        myname.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        bindViews();    //动态显示进度条函数
        upDataStudyTime();

//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        startActivity(intent);
        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        MusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyServiceRunning(MusicService.class)){
                    stopMusicService();
                }
                else startMusicService();
            }
        });

//        Button testButt=findViewById(R.id.t);
//        testButt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this, testActivity.class);
//                startActivity(intent);
//            }
//        });


        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mTimerRunning) {
//                        pauseTimer();
                        showQuitEvent();
                        startAlarmMusicService();

                    } else {
                        startTimer();
                        mBar.setEnabled(false);
                        startMyService();  //加上监测有无打开其他app
                        startMusicService();  //开始播放背景音乐
                    }
            }
        });
        updateCountDownText();
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {//判断一个service是否在运行
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void startAlarmMusicService(){
        Intent intent=new Intent(MainActivity.this, AlarmMusicService.class);
        startService(intent);
    }
    private void stopAlarmMusicService(){
        Intent intent=new Intent(MainActivity.this,AlarmMusicService.class);
        stopService(intent);
    }
    private  void startMusicService(){
        Intent intent=new Intent(MainActivity.this, MusicService.class);
        startService(intent);
    }
    private void stopMusicService(){
        Intent intent=new Intent(MainActivity.this,MusicService.class);
        stopService(intent);
    }


    private void startMyService(){
        Intent intent=new Intent(MainActivity.this,MyService.class);
        startService(intent);
    }
    private void stopMyService(){
        Intent intent=new Intent(MainActivity.this,MyService.class);
        stopService(intent);
    }

//

    private void bindViews() {//动态更新进度条函数

        mBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress==0){   //最新改变
                    mButtonStartPause.setEnabled(false);
                }else{
                    mButtonStartPause.setEnabled(true);
                }
                mTxt.setText("选取时间:" + progress+"分钟");
//                START_TIME_IN_MILLIS=progress;
                mTimeLeftInMillis=progress*60000;
                tempTime=mTimeLeftInMillis;
                mTextViewCountDown.setText(progress+":00");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//防止因为返回而停止计时
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    private void startTimer() {//开始计时
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("开始" );
                StudyTime myClock=new StudyTime();
                StudyTime preClock=new StudyTime();
                preClock=DataSupport.find(StudyTime.class,1);
                long preTime=preClock.getTotalTime();
                long total=preTime+tempTime/60000;
                myClock.setTotalTime(total);
                myClock.update(1);
                upDataStudyTime();
                //弹窗提醒
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("消息提示");
                dialog.setMessage("你已经完成了"+tempTime/60000+"分钟的学习");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopMyService();   //最新改变
                        stopMusicService();
                        mBar.setEnabled(true);
                        mTextViewCountDown.setText(tempTime/60000+":00");
                        mTimeLeftInMillis=tempTime;
                    }
                });
                dialog.show();
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("放弃");
//        mButtonReset.setVisibility(View.INVISIBLE);
//        startMyService();   //开始服务

    }

    private void pauseTimer() {//停止计时
        mCountDownTimer.cancel();
        mTimerRunning = false;
        int num=mBar.getProgress();
        mTextViewCountDown.setText(num+":00");
        mTimeLeftInMillis=num*60000;
        mButtonStartPause.setText("开始");
    }

    private void updateCountDownText() {//更新闹钟信息
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }
    private void upDataStudyTime(){//更新学习时间
        TextView myStudyTime=findViewById(R.id.myStudyTime);
        StudyTime studyTime=DataSupport.find(StudyTime.class,1);
        myStudyTime.setText("你学习的时间为:"+studyTime.getTotalTime()+"分钟");
    }

    private void showQuitEvent(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("放弃警告");
        dialog.setMessage("放弃将导致本次时间不计入时长");
        dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //无事发生，继续计时
                stopAlarmMusicService();  //警告音乐关闭
            }
        });
        dialog.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这代表你放弃了，将时间重新设置
                pauseTimer();
                mBar.setEnabled(true);
                updateCountDownText();  //新增
                stopMyService();
                stopAlarmMusicService();
                stopMusicService();

//                stopMyService();    //停止服务


            }
        });
        dialog.show();
    }

}
