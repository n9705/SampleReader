package com.example.n9705.samplereader;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.n9705.samplereader.ViewPager.ViewPagerActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //让窗口1s后自动消失
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharePreference sp=new SharePreference(WelcomeActivity.this);
                boolean isLogin = sp.getState();
                if(!isLogin){//第一次启动
                    init();//初始化
                    Intent intent=new Intent(WelcomeActivity.this,ViewPagerActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000); //1s for release
    }

    //初始化后面所用到的按键状态
    public void init(){
        SharePreference sp=new SharePreference(WelcomeActivity.this);
        //默认黄色
        sp.setYellowTrue();
        sp.setWhiteFalse();
        sp.setGreenFalse();
        sp.setPinkFalse();
        //默认关闭夜间模式
        sp.setNightFalse();
        //默认字体大小为“中”
        sp.setSize(1);
    }
}
