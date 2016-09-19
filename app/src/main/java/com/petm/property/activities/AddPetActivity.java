package com.petm.property.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.PetVaccinesAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.VOCategory;
import com.petm.property.model.VOPetShop;
import com.petm.property.model.VOPetVaccines;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 11:11
 * PetM
 */
public class AddPetActivity extends BaseActivity implements View.OnClickListener,PetVaccinesAdapter.OnDateChangedListener {
    private static final String TAG = "AddPetActivity";
    private View[] views ;
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
    private ArrayList<Long>categoryids = new ArrayList<>();
    private EditText petName,petBirthday,petCategory;
    private long categoryid;
    private Set<String> lists = new HashSet<>();
    private VOPetVaccines petVaccines;
    private int dateTag ;
    private Map<Integer,String> maps = new HashMap<>();
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_pet;
    }

    @Override
    protected void initViews() {
        super.initViews();
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        views = new View[]{findViewById(R.id.male),findViewById(R.id.female)};
        mRecyclerView = (RecyclerView) findViewById(R.id.pet_vaccines);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        headImg = (ImageView) findViewById(R.id.head_img);
        petName = (EditText) findViewById(R.id.pet_name);
        petBirthday = (EditText) findViewById(R.id.pet_birthday);
        petCategory = (EditText) findViewById(R.id.pet_category);
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
        pwTime.setRange(2000,calendar.get(Calendar.YEAR));
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if(dateTag == -1) {
                    petBirthday.setText(getTime(date));
                }else {
                    maps.put(dateTag,getTime(date));
                    mAdapter.notifyDataSetChanged();
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
                    mAdapter = new PetVaccinesAdapter(AddPetActivity.this,petVaccines.data,maps);
                    mRecyclerView.setAdapter(mAdapter);
                }else {
                    ToastU.showShort(AddPetActivity.this,petVaccines.desc);
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
            case R.id.pet_birthday:
                dateTag = -1;
                pwTime.showAtLocation(petBirthday, Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.pet_category_img:
                pwOptions.showAtLocation(petCategory, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.save:
                JSONObject object = new JSONObject();
                try {
                    object.put("keeperid", LocalStore.getKeeperid(AddPetActivity.this));
                    object.put("petnick",petName.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
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
                LogU.i(TAG,json.toString());
                VOCategory categories = JsonUtils.object(json.toString(),VOCategory.class);
                if (categories.code==200){
                    for (int i =0;i<categories.data.size();i++){
                        options1Items.add(categories.data.get(i).petcategoryname);
                        categoryids.add(categories.data.get(i).petcategoryid);
                    }
                }else {
                    ToastU.showShort(AddPetActivity.this,categories.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(AddPetActivity.this,error.getMessage());
            }
        });
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

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void OnDateChanged(String string,int position) {
        dateTag = position;
        pwTime.showAtLocation(petBirthday, Gravity.BOTTOM, 0, 0, new Date());
    }
}
