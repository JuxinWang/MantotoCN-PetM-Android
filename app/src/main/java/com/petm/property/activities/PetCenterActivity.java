package com.petm.property.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.PetVaccinesAdapter;
import com.petm.property.adapter.PetsAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.dialog.ColorDialog;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.NetInfo;
import com.petm.property.model.VOPet;
import com.petm.property.model.VOPetShop;
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
 * At 10:05
 * PetM
 */
public class PetCenterActivity extends BaseActivity implements View.OnClickListener,PetsAdapter.OnDeletePetListener{
    private static final String TAG = "PetCenterActivity";
    private RecyclerView petsRecycler;
    private LinearLayoutManager layoutManager;
    private LoadingFragment fragment;
    private PetsAdapter mAdapter;
    private String flag = "";
    private long petshopid;
    private long petid;
    private VOPet pets;
    private boolean selected = false;
    @Override
    protected int getContentViewResId() {
        return R.layout.actvity_pet_center;
    }

    @Override
    protected int getTopBarTextRes() {
        if (flag .equals("petshop")){
            return R.string.select_pet;
        }
        return R.string.pet_center;
    }

    @Override
    protected int getTopBarRightImgRes() {
        if (flag .equals("petshop")){
            return 0;
        }
        return R.drawable.mainaddsmallf;
    }

    @Override
    protected int getTopBarRightTextRes() {
        if (flag .equals("petshop")){
            return R.string.pet_commit;
        }
        return 0;
    }

    @Override
    protected void initViews() {
        super.initViews();
        loadDatas();
        petsRecycler = (RecyclerView) findViewById(R.id.pets_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        petsRecycler.setLayoutManager(layoutManager);
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            flag = bundle.getString("flag");
            if (flag.equals("petshop")){
                petshopid = bundle.getLong("petshopid");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(PetCenterActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(PetCenterActivity.this, Constant.PET_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
//                ToastU.showShort(PetCenterActivity.this, json.toString());
                LogU.i(TAG, json.toString());
                pets = JsonUtils.object(json.toString(), VOPet.class);
                if (pets.code == 200) {
                    mAdapter = new PetsAdapter(PetCenterActivity.this, pets.data, flag);
                    petsRecycler.setAdapter(mAdapter);
                    mAdapter.setmOnItemClickListener(new PetsAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            mAdapter.selectPosition(position);
                            mAdapter.notifyDataSetChanged();
                            petid = pets.data.get(position).petid;
                            selected = true;
                        }
                    });
                } else {
                    ToastU.showShort(getApplicationContext(), pets.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(PetCenterActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()){
            case R.id.top_bar_right_img:
                intent.setClass(PetCenterActivity.this, AddPetActivity.class);
                startActivity(intent);
                break;
            case R.id.top_bar_right_text:
                if (!selected){
                    ToastU.showShort(PetCenterActivity.this,"请选择宠物");
                    return;
                }
                intent.setClass(PetCenterActivity.this,OrderActivity.class);
                intent.putExtra("petshopid",petshopid);
                intent.putExtra("petid",petid);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void OnDeletePet(final long petid) {
        ColorDialog dialog = new ColorDialog(PetCenterActivity.this);
        dialog.setTitle("删除宠物");
        dialog.setTitleTextColor(Color.BLACK);
        dialog.setContentText("您确定删除该宠物么？")
                .setColor(getResources().getColor(R.color.main_color))
                .setContentTextColor(getResources().getColor(R.color.black))
                .setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        fragment = new LoadingFragment();
                        fragment.show(getSupportFragmentManager(), "Loading");
                        JSONObject object = new JSONObject();
                        try {
                            object.put("petid", petid);
                            object.put("isvalid", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        IRequest.postJson(PetCenterActivity.this, Constant.PET_DELETE, object, new RequestListener() {
                            @Override
                            public void requestSuccess(JSONObject json) {
                                NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                                if (netInfo.code == 200) {
                                    fragment.dismiss();
                                    ToastU.showShort(PetCenterActivity.this, "删除成功");
                                    loadDatas();
                                } else {
                                    fragment.dismiss();
                                    ToastU.showShort(PetCenterActivity.this, netInfo.desc);
                                }
                            }

                            @Override
                            public void requestError(VolleyError error) {
                                fragment.dismiss();
                                ToastU.showShort(PetCenterActivity.this, error.getMessage());
                            }
                        });
                        dialog.dismiss();
                    }
                }).setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }
}
