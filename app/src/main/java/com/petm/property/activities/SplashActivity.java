package com.petm.property.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.petm.property.R;

/**
 * Created by Mr.liu
 * On 2016/9/13
 * At 13:01
 * PetM
 */
public class SplashActivity extends BaseActivity {
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://跳转
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Thread mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                    Message msg = new Message();
                    msg.what =1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }
}
