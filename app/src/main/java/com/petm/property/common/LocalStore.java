package com.petm.property.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.petm.property.model.Keeper;
import com.petm.property.model.User;

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
    public final static String IS_LOGIN = "is_login";//是否登录
    public final static String IS_REMIND = "is_remind";//是否提醒
    public static User user;//用户信息

    /**
     * 初始化UserInfo
     * @param context
     */
    public static void initUserInfo (Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO,0);
        user = new User();
        user.userid = saving.getLong(USER_ID, 0);
        user.username = saving.getString(USER_NAME, "");
        user.mobile = saving.getString(USER_MOBILE, "");
    }

    /**
     * 存储用户登录信息
     * @param context
     * @param user
     */
    public static void setUserInfo(Context context, User user){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO,0);
        saving.edit().putLong(USER_ID, user.userid).commit();
        saving.edit().putString(USER_NAME, user.username).commit();
        saving.edit().putString(USER_MOBILE, user.mobile).commit();
        user.userid = user.userid;
        user.username = user.username;
        user.mobile = user.mobile;
    }

    /**
     * 获取用户存储信息
     * @return
     */
    public static  User getUserInfo(){
        return user;
    }

    /**
     * 存储用户keeperid
     * @param context
     * @param keeperid
     */
    public static void setKeeperid(Context context, long keeperid){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        saving.edit().putLong(KEEPERID, keeperid).commit();
    }

    /**
     * 获取用户keeperid
     * @param context
     * @return
     */
    public static long getKeeperid(Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        return saving.getLong(KEEPERID, 0);
    }

    /**
     * 存储用户keeperid
     * @param context
     * @param userid
     */
    public static void setUserid(Context context, long userid){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        saving.edit().putLong(USER_ID, userid).commit();
    }

    /**
     * 获取用户keeperid
     * @param context
     * @return
     */
    public static long getUserid(Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        return saving.getLong(USER_ID, 0);
    }

    /**
     * 存储用户mobile
     * @param context
     * @param mobile
     */
    public static void setMobile(Context context, String mobile){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        saving.edit().putString(USER_MOBILE, mobile).commit();
    }

    /**
     * 获取用户mobile
     * @param context
     * @return
     */
    public static String getMobile(Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        return saving.getString(USER_MOBILE, "");
    }

    /**
     * 存储用户是否登录
     * @param context
     * @param isLogin
     */
    public static void setIsLogin(Context context, boolean isLogin){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        saving.edit().putBoolean(IS_LOGIN, isLogin).commit();
    }

    /**
     * 获取用户m是否登录
     * @param context
     * @return
     */
    public static boolean getIsLoginn(Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        return saving.getBoolean(IS_LOGIN, false);
    }

    /**
     * 存储用户是否登录
     * @param context
     * @param isRemind
     */
    public static void setIsRemind(Context context, boolean isRemind){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        saving.edit().putBoolean(IS_REMIND, isRemind).commit();
    }

    /**
     * 获取用户m是否登录
     * @param context
     * @return
     */
    public static boolean getIsRemind(Context context){
        SharedPreferences saving = context.getSharedPreferences(USER_INFO, 0);
        return saving.getBoolean(IS_REMIND,true);
    }


}
