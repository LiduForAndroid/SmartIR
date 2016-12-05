package com.smart.smartir.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.smart.smartir.R;
import com.smart.smartir.utils.Constants;
import com.smart.smartir.utils.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initEvent();
    }

    private void initEvent() {
        Timer mTimer = new Timer();
        mTimer.schedule(new MyTask(), 2000);
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            boolean isSetupFinish = SPUtils.getBoolean(getApplicationContext(), Constants.ISSETUPFINISH, false);
            if (isSetupFinish) {
                //进入主界面
                Intent home = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(home);
                finish();
            } else {
                //进入设置向导界面
                Intent guide = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(guide);
                finish();
            }
        }
    }
}
