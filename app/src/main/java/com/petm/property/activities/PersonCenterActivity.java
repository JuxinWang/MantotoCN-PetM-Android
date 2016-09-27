package com.petm.property.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.media.Constants;
import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadOptions;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.BitmapUtils;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.alibaba.sdk.android.media.utils.StringUtils;
import com.android.volley.VolleyError;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.PetMApplication;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.fragments.ConsumeInfoFragment;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.fragments.MyInfoFragment;
import com.petm.property.fragments.OrderInfoFragment;
import com.petm.property.model.InfoUser;
import com.petm.property.model.VOUser;
import com.petm.property.utils.AliUtils;
import com.petm.property.utils.DateHelper;
import com.petm.property.utils.FileUtils;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.SelectHeadTools;
import com.petm.property.utils.ToastU;
import com.petm.property.views.OptionsPopupWindow;
import com.petm.property.views.TimePopupWindow;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PersonCenterActivity";
    private ImageView headImg;
    private TextView count, line;
    private TextView mine, consume, order;
    private ViewPager mViewPage;
    private ArrayList<Fragment> fragments;
    private int lineWidth;
    private Uri photoUri = null;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private VOUser infoUser;
    private Bundle bundle;
    private LoadingFragment frament;
    private String photoPath = "";
    public UploadListener mListener;
    public String mTaskId;
    private String path = "path";
    private MyInfoFragment myInfoFragment;
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
        frament = new LoadingFragment();
        headImg = (ImageView) findViewById(R.id.head_img);
        count = (TextView) findViewById(R.id.count);
        mine = (TextView) findViewById(R.id.mine_info);
        consume = (TextView) findViewById(R.id.consume_info);
        order = (TextView) findViewById(R.id.order_info);
        mViewPage = (ViewPager) findViewById(R.id.viewpage);
        line = (TextView) findViewById(R.id.line);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .build();//构建完成
        fragments = new ArrayList<>();
        loadDatas();
        changeState(0);
        mListener = new UploadListener() {
            @Override
            public void onUploading(UploadTask uploadTask) {
                LogU.i(TAG, uploadTask.getCurrent() + "/" + uploadTask.getTotal());
                ToastU.showShort(PersonCenterActivity.this, "" + uploadTask.getCurrent());
            }

            @Override
            public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                UploadTask.Result result = uploadTask.getResult();
                String requestId = (null == result ? "null" : result.requestId);
                Log.e(TAG, "###########onUploadFailed:" + requestId);
                ToastU.showShort(PersonCenterActivity.this, "" + requestId);
            }

            @Override
            public void onUploadComplete(UploadTask uploadTask) {
                UploadTask.Result res = uploadTask.getResult();            //上传成功后，从服务端返回的结果都在这个对象中，根据自己需要获取
                Log.e(TAG, "###########onUploadComplete:" + uploadTask.getResult().requestId);
                path = res.url;
                if (path.equals("")) {
                    path = "http://img0.bdstatic.com/img/image/zhengjiuwxr.jpg";
                }
                bundle.putString("path",path);
                fragments.clear();
                loadFragments();
                mViewPage.setCurrentItem(0);
            }

            @Override
            public void onUploadCancelled(UploadTask uploadTask) {
                ToastU.showShort(PersonCenterActivity.this, "上传失败");
            }
        };
    }

    private void loadFragments() {
        myInfoFragment = new MyInfoFragment();
        myInfoFragment.setArguments(bundle);
        fragments.add(myInfoFragment);
        fragments.add(new ConsumeInfoFragment());
        fragments.add(new OrderInfoFragment());
        lineWidth = getWindowManager().getDefaultDisplay().getWidth() / fragments.size();
        line.getLayoutParams().width = lineWidth;
        line.requestLayout();
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
    protected void loadDatas() {
        super.loadDatas();
        frament.show(getSupportFragmentManager(), "Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", LocalStore.getMobile(PersonCenterActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(mContext, Constant.USERINFO_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG, json.toString());
                infoUser = JsonUtils.object(json.toString(), VOUser.class);
                if (infoUser.code == 200) {
                    frament.dismiss();
                    imageLoader.displayImage(infoUser.data.headshot, headImg, options);
                    count.setText(infoUser.data.username);
                    bundle = new Bundle();
                    bundle.putString("truename", infoUser.data.truename);
                    bundle.putString("gender", infoUser.data.gender);
                    bundle.putString("birthday", infoUser.data.birthday);
                    bundle.putString("path",path);
                    loadFragments();
                } else {
                    frament.dismiss();
                    ToastU.showShort(PersonCenterActivity.this, infoUser.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                frament.dismiss();
                ToastU.showShort(PersonCenterActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_img:
                if (!FileUtils.hasSdcard()) {
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
        } else if (arg0 == 1) {
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
        } else {
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
        switch (requestCode) {
            case Constant.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File tempFile = new File(path, Constant.IMAGE_FILE_NAME);
                    SelectHeadTools.startPhotoZoom(this, Uri.fromFile(tempFile), 300);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constant.PHOTO_REQUEST_GALLERY://相册获取
                if (data == null)
                    return;
                SelectHeadTools.startPhotoZoom(this, data.getData(), 300);
                break;
            case Constant.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    String photoName = SelectHeadTools.getPhotoFileName();
                    photoPath = SelectHeadTools.storeImageToFile(photo, photoName);
                    Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
                    headImg.setImageBitmap(bitmap);
                        uploadFile(photoPath, false);
                }
                break;
        }
    }

    public void uploadFile(final String paths, boolean compress) {
        if (TextUtils.isEmpty(paths)) {
            ToastU.showShort(PersonCenterActivity.this, "您还未给您的宠物设置头像");
            return;
        }
        final String fileName = "petm_" + StringUtils.getUUID();
        final String tempFile = DateHelper.getStringTime("" + System.currentTimeMillis(), "yyyyMMdd");
        final File file = new File(paths);
        final UploadOptions options = new UploadOptions.Builder()
                .tag(String.valueOf(SystemClock.elapsedRealtime()))
                .dir("/Headshot/Images/" + tempFile)
                .aliases(fileName).build();
        if (!compress) {
            mTaskId = PetMApplication.mediaService.upload(file, PetMApplication.NAMESPACE, options, mListener);
        } else {
            AliUtils.SERVICE.submit(new Runnable() {
                @Override
                public void run() {
                    byte[] data = BitmapUtils.getSmallBitmapBytes(paths, 200, 200, 80);
                    if (data != null) {
                        Log.e(TAG, "compress  olderSize:" + file.length()
                                / Constants.KB + " kb" + "    newSize:"
                                + data.length / Constants.KB + "  kb");
                        mTaskId = PetMApplication.mediaService.upload(data, fileName, PetMApplication.NAMESPACE, options, mListener);
                    } else {
                        Log.e(TAG, "getSmallBitmapBytes  fail");
                    }
                }
            });
        }
    }
}
