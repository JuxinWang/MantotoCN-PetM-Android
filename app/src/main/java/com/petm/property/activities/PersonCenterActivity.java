package com.petm.property.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewPropertyAnimator;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.fragments.ConsumeInfoFragment;
import com.petm.property.fragments.MyInfoFragment;
import com.petm.property.fragments.OrderInfoFragment;
import com.petm.property.utils.FileUtils;
import com.petm.property.utils.SelectHeadTools;
import com.petm.property.utils.ToastU;
import com.petm.property.views.OptionsPopupWindow;
import com.petm.property.views.TimePopupWindow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 10:30
 * PetM
 */
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener{
    private ImageView headImg;
    private TextView count,line;
    private TextView mine,consume,order;
    private ViewPager mViewPage;
    private ArrayList<Fragment> fragments;
    private int lineWidth;
    private Uri photoUri = null;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_person_center;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.person_center;
    }

    @Override
    protected void initViews() {
        super.initViews();
        headImg = (ImageView) findViewById(R.id.head_img);
        count = (TextView) findViewById(R.id.count);
        mine = (TextView) findViewById(R.id.mine_info);
        consume = (TextView) findViewById(R.id.consume_info);
        order = (TextView) findViewById(R.id.order_info);
        mViewPage = (ViewPager) findViewById(R.id.viewpage);
        line = (TextView) findViewById(R.id.line);
        fragments = new ArrayList<>();
        fragments.add(new MyInfoFragment());
        fragments.add(new ConsumeInfoFragment());
        fragments.add(new OrderInfoFragment());
        lineWidth = getWindowManager().getDefaultDisplay().getWidth()/fragments.size();
        line.getLayoutParams().width = lineWidth;
        line.requestLayout();
        changeState(0);
        mViewPage.setAdapter(new FragmentStatePagerAdapter(
                getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });
        mViewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                changeState(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                float tagerX = arg0 * lineWidth + arg2 / fragments.size();
                ViewPropertyAnimator.animate(line).translationX(tagerX)
                        .setDuration(0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img:
                if(!FileUtils.hasSdcard()){
                    Toast.makeText(this, "没有找到SD卡，请检查SD卡是否存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    photoUri = FileUtils.getUriByFileDirAndFileName(Constant.SAVE_DIRECTORY, Constant.SAVE_PIC_NAME);
                } catch (IOException e) {
                    Toast.makeText(this, "创建文件失败。", Toast.LENGTH_SHORT).show();
                    return;
                }
                SelectHeadTools.openDialog(this, photoUri);
                break;
            case R.id.mine_info:
                mViewPage.setCurrentItem(0);
                break;
            case R.id.consume_info:
                mViewPage.setCurrentItem(1);
                break;
            case R.id.order_info:
                mViewPage.setCurrentItem(2);
                break;
        }
    }

    /* 根据传入的值来改变状态 */
    private void changeState(int arg0) {
        if (arg0 == 0) {
            mine.setTextColor(getResources().getColor(R.color.main_color));
            order.setTextColor(getResources().getColor(R.color.main_gray_background));
            consume.setTextColor(getResources().getColor(R.color.main_gray_background));
            ViewPropertyAnimator.animate(mine).scaleX(1.2f).setDuration(200);
            ViewPropertyAnimator.animate(mine).scaleY(1.2f).setDuration(200);
            ViewPropertyAnimator.animate(consume).scaleX(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(consume).scaleY(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(order).scaleX(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(order).scaleY(1.0f)
                    .setDuration(200);
        }else if (arg0==1){
            consume.setTextColor(getResources().getColor(R.color.main_color));
            mine.setTextColor(getResources().getColor(R.color.main_gray_background));
            order.setTextColor(getResources().getColor(R.color.main_gray_background));
            ViewPropertyAnimator.animate(consume).scaleX(1.2f).setDuration(200);
            ViewPropertyAnimator.animate(consume).scaleY(1.2f).setDuration(200);
            ViewPropertyAnimator.animate(mine).scaleX(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(mine).scaleY(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(order).scaleX(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(order).scaleY(1.0f)
                    .setDuration(200);
        }else {
            order.setTextColor(getResources().getColor(R.color.main_color));
            mine.setTextColor(getResources().getColor(R.color.main_gray_background));
            consume.setTextColor(getResources().getColor(R.color.main_gray_background));
            ViewPropertyAnimator.animate(order).scaleX(1.2f).setDuration(200);
            ViewPropertyAnimator.animate(order).scaleY(1.2f).setDuration(200);
            ViewPropertyAnimator.animate(mine).scaleX(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(mine).scaleY(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(consume).scaleX(1.0f)
                    .setDuration(200);
            ViewPropertyAnimator.animate(consume).scaleY(1.0f)
                    .setDuration(200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constant.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                SelectHeadTools.startPhotoZoom(this,photoUri, 600);
                break;
            case Constant.PHOTO_REQUEST_GALLERY://相册获取
                if (data==null)
                    return;
                SelectHeadTools.startPhotoZoom(this, data.getData(), 600);
                break;
            case Constant.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果
                if (data==null)
                    return;
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(SelectHeadTools.path));
                    headImg.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
