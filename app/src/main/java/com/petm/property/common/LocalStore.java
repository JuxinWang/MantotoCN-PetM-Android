package com.petm.property.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.petm.property.model.Keeper;

/**
 * Created by liudongdong on 2016/8/14.
 * 存储共享参数
 */
public class LocalStore {
    public final static String USER_INFO = "user_info";//存储用户登录信息
    public final static String USER_ID = "userid";
    public final static String USER_NAME = "username";
    public final static String KEEPERID="keeperid";
    public final static String USER_MOBILE="mobile";
    public static Keeper keeperInfo;//用户信息

    /**
     * 初始化UserInfo
     * @param context
     */
    public static void initUserInfo (Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO,0);
        keeperInfo = new Keeper();
        keeperInfo.userid = saving.getLong(USER_ID, 0);
        keeperInfo.user.username = saving.getString(USER_NAME, "");
        keeperInfo.keeperid = saving.getLong(KEEPERID, 0);
        keeperInfo.user.mobile = saving.getString(USER_MOBILE, "");
    }

    /**
     * 存储用户登录信息
     * @param context
     * @param user
     */
    public static void setUserInfo(Context context, Keeper keeper){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO,0);
        Log.i("Local",""+keeper.keeperid);
        saving.edit().putLong(USER_ID,keeper.userid).commit();
        saving.edit().putString(USER_NAME, keeper.user.username).commit();
        saving.edit().putLong(KEEPERID, keeper.keeperid).commit();
        saving.edit().putString(USER_MOBILE, keeper.user.mobile).commit();
//        keeperInfo.userid = keeper.userid;
//        keeperInfo.user.username = keeper.user.username;
//        keeperInfo.keeperid = keeper.keeperid;
//        keeperInfo.user.mobile = keeper.user.mobile;
    }

    /**
     * 获取用户存储信息
     * @return
     */
    public static  Keeper getUserInfo(){
        return keeperInfo;
    }

}
