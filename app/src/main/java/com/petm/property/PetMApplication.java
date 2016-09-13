package com.petm.property;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.petm.property.common.LocalStore;
import com.petm.property.utils.LogU;

/**
 * Created by Mr.liu
 * On 2016/7/13
 * At 18:30
 * My Application
 */
public class PetMApplication extends Application {
    private static Context mConText;
    private static String DeviceImei;
    @Override
    public void onCreate() {
        super.onCreate();
        mConText = getApplicationContext();
        LogU.isDebug = true;
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        DeviceImei = tm.getDeviceId();//需要读取手机权限android.permission.READ_PHONE_STATE.
//        LocalStore.initUserInfo(mConText);
    }

    public static String getDeviceIMEI() {
        return DeviceImei;
    }

    public static Context getConText(){
        return mConText;
    }


}
