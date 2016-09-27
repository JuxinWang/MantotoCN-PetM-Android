package com.petm.property.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.adapter.PetVaccinAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.model.VOPetVaccin;
import com.petm.property.utils.DateHelper;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.ToastU;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 11:21
 * PetM
 */
public class PetInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PetInfoActivity";
    private long petid;
    private LinearLayout petVaccinLL;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private PetVaccinAdapter mAdapter;
    private String imgpath;
    private String petName;
    private String categoryName;
    private String birthday;
    private TextView name,category,birth;
    private ImageView img;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_petinfo;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.pet_info;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Bundle bundle = getIntent().getExtras();
        petid = bundle.getLong("petid");
        imgpath = bundle.getString("imgpath");
        petName = bundle.getString("petname");
        categoryName = bundle.getString("categoryname");
        birthday = bundle.getString("birthday");
        petVaccinLL = (LinearLayout) findViewById(R.id.pet_vaccin_ll);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView = (RecyclerView) findViewById(R.id.petvaccin_recycler);
        name = (TextView) findViewById(R.id.pet_name);
        category = (TextView) findViewById(R.id.pet_categoryName);
        birth = (TextView) findViewById(R.id.pet_birthday);
        img = (ImageView) findViewById(R.id.pet_imgpath);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .build();//构建完成
        imageLoader.displayImage(imgpath,img,options);
        name.setText("昵称：" + petName);
        category.setText(categoryName);
        birth.setText("生日："+ DateHelper.getStringTime(birthday,"yyyy年MM月dd日"));
        mRecyclerView.setLayoutManager(layoutManager);
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("petid", petid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(PetInfoActivity.this, Constant.PET_VACCIN_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
                VOPetVaccin petVaccins = JsonUtils.object(json.toString(),VOPetVaccin.class);
                if (petVaccins.code ==200){
                    if (petVaccins.data.size()>0){
                        petVaccinLL.setVisibility(View.VISIBLE);
                        mAdapter = new PetVaccinAdapter(PetInfoActivity.this,petVaccins.data);
                        mRecyclerView.setAdapter(mAdapter);
                    }else {
                        petVaccinLL.setVisibility(View.GONE);
                    }
                }else {
                    petVaccinLL.setVisibility(View.GONE);
                    ToastU.showShort(PetInfoActivity.this,petVaccins.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(PetInfoActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pet_edit:
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("imgpath",imgpath);
                intent.putExtra("petname",petName);
                intent.putExtra("imgpath",imgpath);
                intent.putExtra("categoryname",categoryName);
                intent.putExtra("birthday",birthday);
                intent.setClass(PetInfoActivity.this, AddPetActivity.class);
                startActivity(intent);
                break;
        }
    }
}
