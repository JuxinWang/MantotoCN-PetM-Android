package com.petm.property.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liudongdong on 2016/8/14.
 * 存储共享参数
 */
public class LocalStore {
    public final static String USER_INFO = "user_info";//存储用户登录信息
    public final static String USER_ID = "userid";
    public final static String USER_NAME = "username";
    public final static String USER_PROPERTYID="userpropertyid";
    public final static String USER_MOBILE="mobile";
//    public static UserInfo userInfo;//用户信息

//    /**
//     * 初始化UserInfo
//     * @param context
//     */
//    public static void initUserInfo (Context context){
//        SharedPreferences saving = context.getSharedPreferences(USER_INFO,context.MODE_PRIVATE);
//        userInfo = new UserInfo();
//        userInfo.UserID = saving.getLong(USER_ID,0);
//        userInfo.UserName = saving.getString(USER_NAME, "");
//        userInfo.PropertyID=saving.getLong(USER_PROPERTYID, 0);
//        userInfo.Mobile=saving.getString(USER_MOBILE, "");
//    }
//
//    /**
//     * 存储用户登录信息
//     * @param context
//     * @param user
//     */
//    public static void setUserInfo(Context context, UserInfo user){
//        SharedPreferences saving = context.getSharedPreferences(USER_INFO,context.MODE_PRIVATE);
//        saving.edit().putLong(USER_ID,user.UserID).commit();
//        saving.edit().putString(USER_NAME, user.UserName).commit();
//        saving.edit().putLong(USER_PROPERTYID, user.PropertyID).commit();
//        saving.edit().putString(USER_MOBILE, user.Mobile).commit();
//        userInfo.UserID = user.UserID;
//        userInfo.UserName = user.UserName;
//        userInfo.PropertyID = user.PropertyID;
//        userInfo.Mobile = user.Mobile;
//    }
//
//    /**
//     * 获取用户存储信息
//     * @return
//     */
//    public static  UserInfo getUserInfo(){
//        return userInfo;
//    }

}
