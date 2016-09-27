package com.petm.property.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by Mr.liu
 * On 2016/7/13
 * At 18:39
 * My Application
 */
public class Constant {
/******************************       常量定义          *******************************************/
    public static final boolean DEBUG = true;
    /***
     * 保存到本地的目录
     */
    public static final String SAVE_DIRECTORY = "/petm";
    /***
     * 保存到本地图片的名字
     */
    public static final String SAVE_PIC_NAME="temp.jpeg";
    /***
     *标记用户点击了从照相机获取图片  即拍照
     */
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    /***
     *标记用户点击了从图库中获取图片  即从相册中取
     */
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    /***
     * 返回处理后的图片
     */
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final String IMAGE_FILE_NAME = "image.jpg";
    public static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/petm/Camera");

    public static final String RSA_PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDRsvj8cEaWr1pdG7+NUjaOIeHM\n" +
            "+v07kK9/g5JlcNXDKfMSn/xTQ7bpJ0l0yprsjaGeM+RImK/PTMzzRXHO+qsetJkO\n" +
            "MhMgNVtcATxKMLxpR1SFJy0mc8gWfqUqj1z9Wkka2MfmLZNlBifl+0Rl3Yfh85IX\n" +
            "hc5CXLJNFBjevs9jxwIDAQAB";
    public static final String PKCS8_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANGy+PxwRpavWl0b\n" +
            "v41SNo4h4cz6/TuQr3+DkmVw1cMp8xKf/FNDtuknSXTKmuyNoZ4z5EiYr89MzPNF\n" +
            "cc76qx60mQ4yEyA1W1wBPEowvGlHVIUnLSZzyBZ+pSqPXP1aSRrYx+Ytk2UGJ+X7\n" +
            "RGXdh+HzkheFzkJcsk0UGN6+z2PHAgMBAAECgYBtlWGYSfQZerl2PrHzYCsyS3J8\n" +
            "dA0xu4J62H8Ak96hhqKH8SMjQBet7+HrLpWXWMxEqEl7xo7ZfgD8gCXhTA1vo/DS\n" +
            "JbQ2r6a14oOTzAvMUBHR6FRTNwGBbDSFVNT6QNn9q3Hr2Qvk5c+q7CbiOq9Ggj9i\n" +
            "LlEQQ+UkQI0Sv3KdAQJBAPMOJ+iTVZDfpZId2sY9Kf3HA8/wO+SW4uuxcGmTepAr\n" +
            "O+ZP+2xx+/CHhtVozcfWtWcETHFGNZ2iCdUK6zZKHfECQQDc3gjcrVNfyyTvBKyn\n" +
            "MjkorV0G0kSXh15oRVdovrGwMSMjJeC+63yvkzXbojLz5PN9QtT22MoIhCb6g8pq\n" +
            "4UU3AkAsXeolBzf4UQrNKEv2IdYXcAufGnAkYvKbKXGmo/gcdvfhK7puakQd/O88\n" +
            "53ugve2xF1TI3JMO6jQ3Ql37M/5RAkB0SS6vSU5xTBqJAZQJf4bapNQqyEA2653I\n" +
            "FmIcEvKoymYulj3LJ18BbedYWQCsqqmMFMjdomxUqYeANC05sptnAkAiQEWLP9y9\n" +
            "Mrzftzoy1Dr8fkqDxB7QTLjt1YjCDdkWmFR32V/juPNBl+5A+QC7JqNO2y1PIegA\n" +
            "FsqxJTUGkK6o";

/************************          以下是网络请求接口地址         *********************************/
    /**
     * 测试服务器地址
     */
//    public static final String URL = "http://dev.mantoto.com/";
    public static final String URL = "http://192.168.0.20:8080/PetM-WebApi/";
    /**
     * 通过手机号获取验证码
     */
    public static final String GET_PHONE_CODE ="http://123.56.96.68:8080/sendCode/sendCodeWithNum/";
    /**
     * 以后采用短信登录使用
     */
    public static final String GET_RANT_CODE = "http://sms.wuxianying.com/sendCode/sendSMS/";
    /**
     * 用户登录注册
     */
    public static final String USER_ACTIVATE = URL+"/UserActivate";
    /**
     * 获取用户添加店铺列表
     */
    public static final String PET_SHOP_GET = URL+"/PetShopGetByKeeper";
    /**
     * 用户通过宠物店预约电话添加宠物店
     */
    public static final String PETSHOP_RELATE = URL+"/PetShopRelate";
    /**
     * 获取用户全部提醒
     */
    public static final String REMIND_GET_ALL =URL+ "/ReminderGetAll";
    /**
     *
     */
    public static final String REMIND_GET_PET_ALL = URL +"/ReminderGetPetAll";
    /**
     * 获取用户个人信息
     */
    public static final String USERINFO_GET =URL+ "/UserInfoGet/";
    /**
     * 更新用户信息
     */
    public static final String USERINFO_UPDATE =URL+ "/UserInfoUpdate";
    /**
     * 获取用户消费信息
     */
    public static final String USER_GIFT_GET =URL+ "/UserGiftGet";
    /**
     * 获取用户全部宠物列表
     */
    public static final String PET_GET_ALL =URL+ "/PetGetAll";
    /**
     * 添加宠物
     */
    public static final String PET_ADD =URL+ "/PetAdd";
    /**
     * 获取服务列表
     */
    public static final String  PETSERVICE_GET_ALL = URL+"/PetServiceGetAll";
    /**
     * /获取美容师列表
     */
    public static final String  BEAUTICIAN_GET_ALL =URL+ "/BeauticianGetAll";
    /**
     * 删除宠物
     */
    public static final String PET_DELETE =URL+ "/PetDelete";
    /**
     * 用户删除宠物店
     */
    public static final String PETSHOP_DELETE= URL+"/PetShopDelete";
    /**
     * 获得宠物订单信息
     */
    public static final String REMIND_WORKO_ORDER_GET_BY_KEEPER = URL+"/ReminderWorkOrderGetByKeeper";
    /**
     * 获取疫苗列表
     */
    public static final String VACCIN_GET = URL+"/VaccinGet";
    /**
     * 获取单个宠物疫苗信息
     */
    public static final String PET_VACCIN_GET =URL+ "/PetVaccinGet";
    /**
     * 预约服务
     */
    public static final String PET_WORK_CREATE = URL+ "PetWorkOrderCreate";
    /**
     * 取消预约
     */
    public static final String PET_WORK_CANCEL = URL+ "/PetWorkOrderCancel";
    /**
     * 获取用户所有信息
     */
    public static final String GET_REMIND_ALL = URL+ "/ReminderGetPetAll";
    /**
     * 获取宠物种类列表
     */
    public static final String GET_PET_CATEGORY = URL+ "/PetCategoryGet";
    /**
     * 编辑宠物
     */
    public static final String PET_EDIT = URL+ "/PetEdit";
    /**
     * 更新宠物头像
     */
    public static final String PET_HEAD_SHOT_EDIT = URL+ "/PetHeadShotEdit";
    /**
     * 批量更新疫苗信息
     */
    public static final String VACCIN_EDIT = URL+ "/VaccinEdit";

}
