package com.petm.property.activities;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.petm.property.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by Mr.liu
 * On 2016/7/13
 * At 18:51
 * My Application
 */
public abstract class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";
    /**加载动画*/
    public FrameLayout loading;
    public View load_failed,load_loading;
    private AnimationDrawable animation;
    private ImageView loadingImageView;
    private TextView loadingText;
    protected Activity mContext;
    /**
     * 加载状态 0：加载失败；1：正在加载；2加载成功*/
    protected String loadingState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContentViewResId() != 0){
            setContentView(getContentViewResId());
        }
        //设置状态栏颜色
        initSystemBar(this);
        initTopBar();
        initViews();
        initEvents();

    }

    /**
     * 获取导航栏左侧图片
     * @return
     */
    protected int getTopBarLeftImgRes(){
        return 1;
    }

    /**
     * 获取导航栏中间文字
     * @return
     */
    protected int getTopBarTextRes(){
        return 0;
    }

    /**
     * 获取导航栏右侧图片
     * @return
     */
    protected int getTopBarRightImgRes(){
       return 0;
    }

    /**
     * 获取导航栏右侧文字
     * @return
     */
    protected int getTopBarRightTextRes(){
        return 0;
    }

    /**
     * 初始化设置统一导航栏
     */
    private void initTopBar() {
        if (findViewById(R.id.top_bar) == null){
            return;
        }
        ImageView leftImg = (ImageView) findViewById(R.id.top_bar_left_img);
        TextView textTitle = (TextView) findViewById(R.id.top_bar_text);
        ImageView rightImg = (ImageView) findViewById(R.id.top_bar_right_img);
        TextView rightText = (TextView) findViewById(R.id.top_bar_right_text);
        if (getTopBarLeftImgRes() != 0){
            leftImg.setVisibility(View.VISIBLE);
        }
        if (getTopBarTextRes() != 0){
            textTitle.setText(getTopBarTextRes());
        }
        if (getTopBarRightImgRes() != 0){
            rightImg.setVisibility(View.VISIBLE);
            rightImg.setImageResource(getTopBarRightImgRes());
        }
        if (getTopBarRightTextRes() != 0){
            rightText.setVisibility(View.VISIBLE);
            rightText.setText(getTopBarRightTextRes());
        }

    }

    protected abstract int getContentViewResId();

    protected void initEvents() {
    }

    protected void loadDatas() {
        loadingState = "1";
        loading = (FrameLayout) findViewById(R.id.loading);
        load_failed = findViewById(R.id.load_loading_fail);
        if (loading != null && load_loading != null){
            /**
             * 重新加载
             * */
            load_failed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadingImageView != null && load_loading != null){
                        loading.setVisibility(View.VISIBLE);
                        load_failed.setVisibility(View.GONE);
                        load_loading.setVisibility(View.VISIBLE);
                        initAnimation();
                    }
                    loadDatas();
                }
            });
            loadingImageView = (ImageView) findViewById(R.id.image_loading);
            if (load_loading != null && loadingImageView != null){
                load_loading.setVisibility(View.VISIBLE);
                initAnimation();
            }
        }

    }

    public void initAnimation(){
        load_loading = findViewById(R.id.load_loading);
        loadingImageView.setBackgroundResource(R.drawable.loading);
        animation = (AnimationDrawable) loadingImageView.getBackground();
        animation.setOneShot(false);
        startAnimation();
    }

    public void startAnimation() {
        if (loading.getVisibility() == View.GONE){
            loading.setVisibility(View.VISIBLE);
        }
        if (load_loading.getVisibility() == View.GONE){
            load_loading.setVisibility(View.VISIBLE);
        }
        if (load_failed.getVisibility() == View.VISIBLE){
            load_failed.setVisibility(View.GONE);
        }
        if (animation != null && loadingImageView != null){
            animation.start();
        }
    }

    protected void initViews() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        //使用主色调
        tintManager.setStatusBarTintResource(R.color.main_color);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
