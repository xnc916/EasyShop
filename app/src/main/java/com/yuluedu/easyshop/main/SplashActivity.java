package com.yuluedu.easyshop.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.model.CachePreferences;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activityUtils = new ActivityUtils(this);

        //判断用户是否登录，自动登录
        if (CachePreferences.getUser().getName() != null) {
            activityUtils.startActivity(MainActivity.class);
            finish();
            return;
        }

        //1.5秒调到主页面
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activityUtils.startActivity(MainActivity.class);
                finish();
            }
        }, 1500);




    }
}
