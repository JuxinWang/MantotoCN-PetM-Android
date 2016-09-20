package com.petm.property;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.media.MediaService;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.petm.property.common.LocalStore;
import com.petm.property.utils.LogU;

/**
 * Created by Mr.liu
 * On 2016/7/13
 * At 18:30
 * My Application
 */
public class PetMApplication extends Application {
    private static final String TAG = "PetMApplication";
    private static Context mConText;
    private static String DeviceImei;
    public static final String NAMESPACE = "petm";
    public static MediaService mediaService;
    @Override
    public void onCreate() {
        super.onCreate();
        initAlibabaSDK();
        mConText = getApplicationContext();
        LogU.isDebug = true;
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        DeviceImei = tm.getDeviceId();//需要读取手机权限android.permission.READ_PHONE_STATE.

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(mConText)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(mConText, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);//全局初始化此配置
//        LocalStore.initUserInfo(mConText);
    }
    public void initAlibabaSDK() {
        MediaService.enableHttpDNS(); //如果用户为了避免域名劫持，可以启用HttpDNS
        MediaService.enableLog(); //在调试时，可以打印日志。正式上线前可以关闭
        AlibabaSDK.asyncInit(this, new InitResultCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "AlibabaSDK   onSuccess");
                mediaService = AlibabaSDK.getService(MediaService.class);
                Toast.makeText(PetMApplication.this, "初始化成功 " , Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.e(TAG, "AlibabaSDK onFailure  msg:" + msg + " code:" + code);
                Toast.makeText(PetMApplication.this, "初始化失败 " + msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public static String getDeviceIMEI() {
        return DeviceImei;
    }

    public static Context getConText(){
        return mConText;
    }


}
