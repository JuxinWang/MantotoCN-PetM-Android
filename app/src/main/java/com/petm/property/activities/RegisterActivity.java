package com.petm.property.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.petm.property.PetMApplication;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.InfoUser;
import com.petm.property.model.VOVarify;
import com.petm.property.utils.CommonUtils;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.RSAEncryptor;
import com.petm.property.utils.ToastU;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Mr.liu
 * On 2016/9/13
 * At 13:22
 * PetM
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,View.OnFocusChangeListener{
    private static final String TAG = "RegisterActivity";
    private EditText mobile,verifyCode;
    private TextView getCode;
    private Button login;
    private String RandCode = "";
    private int i = 60;
    private LoadingFragment fragment;
    private static RSAEncryptor rsaEncryptor;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mobile = (EditText) findViewById(R.id.mobile);
        verifyCode = (EditText) findViewById(R.id.verify_code);
        getCode = (TextView) findViewById(R.id.getCode);
        login = (Button) findViewById(R.id.login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getCode:
                if (CommonUtils.isEmpty(mobile)){
                    ToastU.showShort(RegisterActivity.this,"请输入手机号");
                    return;
                }
                Random random = new Random();
                for (int i =0; i< 6;i++){
                    RandCode += random.nextInt(10);
                }
                getVerifyCode();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (;i > 0;i--){
                            handler.sendEmptyMessage(1);
                            if (i<=0){
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(2);
                    }
                }).start();
                break;
            case R.id.login:
                if (CommonUtils.isEmpty(mobile)){
                    ToastU.showShort(RegisterActivity.this,"请输入手机号");
                    return;
                }
                if (CommonUtils.isEmpty(verifyCode)){
                    ToastU.showShort(RegisterActivity.this,"请输入验证码");
                    return;
                }
                if (!RandCode.equals(verifyCode.getText().toString())) {
                    LogU.i(TAG,RandCode);
                    ToastU.showShort(RegisterActivity.this, "验证码错误");
                    return;
                }
                login();

                break;
            case R.id.top_bar_left_img:
                finish();
                break;
        }
    }

    private void login() {
        fragment = new LoadingFragment();
        fragment.setMsg("正在登录...");
        fragment.show(getSupportFragmentManager(),"Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("mobile",mobile.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(RegisterActivity.this, Constant.USER_ACTIVATE, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
                InfoUser userInfo = JsonUtils.object(json.toString(),InfoUser.class);
                if (userInfo.code == 200) {
                    Log.i(TAG,""+userInfo.data.user.username);
                    LocalStore.setIsLogin(RegisterActivity.this,true);
                    LocalStore.setKeeperid(RegisterActivity.this, userInfo.data.keeperid);
                    LocalStore.setMobile(RegisterActivity.this, userInfo.data.user.mobile);
                    LocalStore.setUserid(RegisterActivity.this,userInfo.data.userid);
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    ToastU.showShort(RegisterActivity.this,userInfo.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                //ToastU.showShort(RegisterActivity.this,error.toString());
                LogU.i(TAG,error.getMessage());
            }
        });
    }

    /**
     * 接收处理数据
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                getCode.setText("重新发送("+i+")");
                getCode.setClickable(false);
            }else if (msg.what == 2){
                getCode.setText("获取验证码");
                getCode.setClickable(true);
            }
        }
    };

    private void getVerifyCode() {
        JSONObject object = new JSONObject();
        String tokens;
        try {
            tokens =  Constant.USER_ACTIVATE+","+"com.wuxianying.gd720"+","+ CommonUtils.getTimeStamp()+","+ PetMApplication.getDeviceIMEI();
            rsaEncryptor = new RSAEncryptor(Constant.RSA_PUBLIC_KEY, Constant.PKCS8_PRIVATE_KEY);
            object.put("token",rsaEncryptor.encryptWithBase64(tokens));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            object.put("phoneNum",mobile.getText().toString());
            object.put("templateStr","SMS_5032123");
            object.put("code",RandCode);
            object.put("product","易修到家");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(RegisterActivity.this, Constant.GET_RANT_CODE, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
//                ToastU.showShort(RegisterActivity.this, json.toString());
                LogU.i(TAG, json.toString());
                VOVarify voVarify = JsonUtils.object(json.toString(),VOVarify.class);
                if (voVarify.code == 200){
                    ToastU.showShort(RegisterActivity.this, "验证码已发送");
                }else {
                    ToastU.showShort(RegisterActivity.this, voVarify.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(RegisterActivity.this,error.toString());
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.mobile:
                CommonUtils.deleteHint(mobile,hasFocus);
                break;
            case R.id.verify_code:
                CommonUtils.deleteHint(verifyCode,hasFocus);
                break;
        }
    }
}
