package com.petm.property.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.media.Constants;
import com.alibaba.sdk.android.media.MediaService;
import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadOptions;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.BitmapUtils;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.alibaba.sdk.android.media.utils.MediaUtils;
import com.alibaba.sdk.android.media.utils.StringUtils;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.PetMApplication;
import com.petm.property.R;
import com.petm.property.adapter.PetVaccinesAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.dialog.PromptDialog;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.fragments.ProgressFragment;
import com.petm.property.model.NetInfo;
import com.petm.property.model.VOCategory;
import com.petm.property.model.VOPetShop;
import com.petm.property.model.VOPetVaccines;
import com.petm.property.utils.AliUtils;
import com.petm.property.utils.DateHelper;
import com.petm.property.utils.DialogCommon;
import com.petm.property.utils.FileUtils;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.SelectHeadTools;
import com.petm.property.utils.ToastU;
import com.petm.property.views.OptionsPopupWindow;
import com.petm.property.views.TimePopupWindow;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 11:11
 * PetM
 */
public class AddPetActivity extends BaseActivity implements View.OnClickListener, PetVaccinesAdapter.OnDateChangedListener {
    private static final String TAG = "AddPetActivity";
    /**
     * 头像名称
     */
    private static final String IMAGE_FILE_NAME = "image.jpg";
    private View[] views;
    private LoadingFragment fragment;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private PetVaccinesAdapter mAdapter;
    private ImageView headImg;
    private Uri photoUri = null;
    /**
     * 日期、宠物种类选择
     */
    private TimePopupWindow pwTime;
    private OptionsPopupWindow pwOptions;
    private ArrayList<String> options1Items = new ArrayList<String>();
    private ArrayList<Long> categoryids = new ArrayList<>();
    private EditText petName, petBirthday, petCategory;
    private long categoryid;
    private int gender = 1;
    private long timeTemp;
    private String path = "";
    private JSONArray vaccin_array = new JSONArray();
    ;
    private String vaccinid;
    private String petvaccinid;
    private Set<String> lists = new HashSet<>();
    private VOPetVaccines petVaccines;
    private int dateTag;
    private Map<Integer, String> maps = new HashMap<>();
    private Map<Integer, String> vaccin_arrayMap = new HashMap<>();
    private Map<String, String> editMap = new HashMap<>();
    public UploadListener mListener;
    public String mTaskId;
    private String photoPath = "";
    /**
     * 编辑信息
     */
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private long petid;
    private String etImgPath;
    private String etPetName;
    private String etCategoryName;
    private String etBirthday;
    private String[] vaccinTime;
    private String[] vaccinids;
    private String[] petvaccinids;
    private boolean isEdit = false;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_pet;
    }

    @Override
    protected int getTopBarTextRes() {
        if (isEdit) {
            return R.string.edit_pet;
        }
        return R.string.add_pet;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Bundle bundle = getIntent().getExtras();
        views = new View[]{findViewById(R.id.male), findViewById(R.id.female)};
        mRecyclerView = (RecyclerView) findViewById(R.id.pet_vaccines);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        headImg = (ImageView) findViewById(R.id.head_img);
        petName = (EditText) findViewById(R.id.pet_name);
        petBirthday = (EditText) findViewById(R.id.pet_birthday);
        petCategory = (EditText) findViewById(R.id.pet_category);
        if (bundle != null) {
            isEdit = true;
            petid = bundle.getLong("petid");
            etImgPath = bundle.getString("imgpath");
            etPetName = bundle.getString("petname");
            etCategoryName = bundle.getString("categoryname");
            etBirthday = bundle.getString("birthday");
            categoryid = bundle.getLong("categoryid");
            vaccinTime = bundle.getStringArray("vaccintime");
            vaccinids = bundle.getStringArray("vaccinids");
            petvaccinids = bundle.getStringArray("petvaccinids");
            if (vaccinids!=null&&vaccinids.length > 0) {
                for (int i = 0; i < vaccinids.length; i++) {
                    LogU.i(TAG, vaccinids[i] + "----" + vaccinTime[i]);
                    editMap.put(vaccinids[i], vaccinTime[i]);
                    LogU.i(TAG, editMap.get(vaccinids[i]));
                }
            }
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                    .considerExifParams(true)
                    .build();//构建完成
            imageLoader.displayImage(etImgPath, headImg, options);
            petName.setText(etPetName);
            petBirthday.setText(DateHelper.getStringTime(etBirthday, "yyyy-MM-dd"));
            petCategory.setText(etCategoryName);
        }
        loadDatas();

        setSelected(0);
        for (int i = 0; i < views.length; i++) {
            final int temp = i;
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(temp);
                }
            });
        }

//      时间选择器
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        pwTime = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(2000, calendar.get(Calendar.YEAR));
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String add = "-add";
                if (dateTag == -1) {
                    petBirthday.setText(DateHelper.getTime(date));
                    timeTemp = date.getTime();
                } else {
                    vaccin_arrayMap.put(dateTag, vaccinid + "-" + date.getTime());
                    try {
                       if (!vaccin_array.isNull(dateTag)){
                           add = "-edit-"+petvaccinid;
                       }
                        vaccin_array.put(dateTag, vaccinid + "-" + date.getTime() / 1000+add);
                        LogU.i(TAG,"vaccin_array:"+ vaccin_array.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    maps.put(dateTag, DateHelper.getTime(date));
                    LogU.i(TAG, "maps:" + maps.toString());
                    mAdapter.notifyItemChanged(dateTag);
                }
            }
        });
        getCategories();
        //选项选择器
        pwOptions = new OptionsPopupWindow(this);
        pwOptions.setPicker(options1Items);
        pwOptions.setSelectOptions(0);
        pwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                petCategory.setText(options1Items.get(options1));
                categoryid = categoryids.get(options1);
            }
        });

        mListener = new UploadListener() {
            @Override
            public void onUploading(UploadTask uploadTask) {
                LogU.i(TAG, uploadTask.getCurrent() + "/" + uploadTask.getTotal());
            }

            @Override
            public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                UploadTask.Result result = uploadTask.getResult();
                String requestId = (null == result ? "null" : result.requestId);
                Log.e(TAG, "###########onUploadFailed:" + requestId);
                ToastU.showShort(AddPetActivity.this, "" + requestId);
            }

            @Override
            public void onUploadComplete(UploadTask uploadTask) {
                UploadTask.Result res = uploadTask.getResult();            //上传成功后，从服务端返回的结果都在这个对象中，根据自己需要获取
                Log.e(TAG, "###########onUploadComplete:" + uploadTask.getResult().requestId);
                path = res.url;
                if (path.equals("")) {
                    path = "http://img0.bdstatic.com/img/image/zhengjiuwxr.jpg";
                }
                if (isEdit){
                    editPetRequest();
                }else {
                    addPetRequest();
                }
            }

            @Override
            public void onUploadCancelled(UploadTask uploadTask) {
                ToastU.showShort(AddPetActivity.this, "上传失败");
            }
        };
    }

    private void editPetRequest() {
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(),"Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("petid",petid);
            object.put("petnick", petName.getText().toString());
            object.put("gender", gender);
            object.put("time", timeTemp);
            object.put("categoryid", categoryid);
            object.put("path", path);
            object.put("vaccin_array", vaccin_array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogU.i(TAG, "editpet:"+object.toString());
        IRequest.postJson(AddPetActivity.this, Constant.PET_EDIT, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG, json.toString());
                NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                if (netInfo.code == 200) {
                    fragment.dismiss();
                    new PromptDialog(AddPetActivity.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText("编辑成功")
                            .setContentText("编辑宠物成功")
                            .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    fragment.dismiss();
                    ToastU.showShort(AddPetActivity.this, netInfo.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(AddPetActivity.this, error.getMessage());
            }
        });
    }

    private void setSelected(int position) {
        for (int i = 0; i < views.length; i++) {
            if (i == position) {
                views[i].setSelected(true);
            } else {
                views[i].setSelected(false);
            }
        }
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("tag", "DOG");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(AddPetActivity.this, Constant.VACCIN_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
//                ToastU.showShort(MainActivity.this, json.toString());
                LogU.i(TAG, json.toString());
                petVaccines = JsonUtils.object(json.toString(), VOPetVaccines.class);
                if (petVaccines.code == 200) {
                    mAdapter = new PetVaccinesAdapter(AddPetActivity.this, petVaccines.data, maps, vaccinTime, vaccinids,petvaccinids,vaccin_array);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    ToastU.showShort(AddPetActivity.this, petVaccines.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(AddPetActivity.this, error.getMessage());
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
            case R.id.pet_birthday:
                dateTag = -1;
                pwTime.showAtLocation(petBirthday, Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.pet_category_img:
                pwOptions.showAtLocation(petCategory, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.save:
                if (photoPath.equals("") || photoPath == null) {
                    path = "http://img0.bdstatic.com/img/image/zhengjiuwxr.jpg";
                    if (isEdit) {
                        editPetRequest();
                    } else {
                        addPetRequest();
                    }
                } else {
                    uploadFile(photoPath, false);
                }
                break;
            case R.id.top_bar_left_img:
                finish();
                break;
        }
    }

    public void uploadFile(final String paths, boolean compress) {
        if (TextUtils.isEmpty(paths)) {
            ToastU.showShort(AddPetActivity.this, "您还未给您的宠物设置头像");
            return;
        }
        final String fileName = "petm_" + StringUtils.getUUID();
        final String tempFile = DateHelper.getStringTime("" + System.currentTimeMillis(), "yyyyMMdd");
        final File file = new File(paths);
        final UploadOptions options = new UploadOptions.Builder()
                .tag(String.valueOf(SystemClock.elapsedRealtime()))
                .dir("/PetM/Images/" + tempFile)
                .aliases(fileName).build();
        if (!compress) {
            mTaskId = PetMApplication.mediaService.upload(file, PetMApplication.NAMESPACE, options, mListener);
        } else {
            AliUtils.SERVICE.submit(new Runnable() {
                @Override
                public void run() {
                    byte[] data = BitmapUtils.getSmallBitmapBytes(path, 200, 200, 80);
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

    private void addPetRequest() {
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(AddPetActivity.this));
            object.put("petnick", petName.getText().toString());
            object.put("gender", gender);
            object.put("time", timeTemp);
            object.put("categoryid", categoryid);
            object.put("path", path);
            object.put("vaccin_array", vaccin_array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogU.i(TAG, object.toString());
        IRequest.postJson(AddPetActivity.this, Constant.PET_ADD, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG, json.toString());
                NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                if (netInfo.code == 200) {
                    fragment.dismiss();
                    new PromptDialog(AddPetActivity.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText("添加成功")
                            .setContentText("添加宠物成功")
                            .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    fragment.dismiss();
                    ToastU.showShort(AddPetActivity.this, netInfo.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(AddPetActivity.this, error.getMessage());
            }
        });

    }

    private void getCategories() {
        JSONObject object = new JSONObject();
        try {
            object.put("tag", "DOG");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(AddPetActivity.this, Constant.GET_PET_CATEGORY, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG, json.toString());
                VOCategory categories = JsonUtils.object(json.toString(), VOCategory.class);
                if (categories.code == 200) {
                    for (int i = 0; i < categories.data.size(); i++) {
                        options1Items.add(categories.data.get(i).petcategoryname);
                        categoryids.add(categories.data.get(i).petcategoryid);
                    }
                } else {
                    ToastU.showShort(AddPetActivity.this, categories.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(AddPetActivity.this, error.getMessage());
            }
        });
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
                    File tempFile = new File(path, IMAGE_FILE_NAME);
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
                }
                break;
        }
    }

    @Override
    public void OnDateChanged(String string, int position,JSONArray vaccin_array,Map<Integer, String> maps,String petvaccinid) {
        dateTag = position;
        pwTime.showAtLocation(petBirthday, Gravity.BOTTOM, 0, 0, new Date());
        vaccinid = string;
        this.petvaccinid = petvaccinid;
//        this.vaccin_array = vaccin_array;
        this.maps = maps;
        LogU.i(TAG,"datechange:"+vaccin_array.toString());
    }
}
