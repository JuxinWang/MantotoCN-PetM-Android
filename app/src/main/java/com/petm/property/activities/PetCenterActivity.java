package com.petm.property.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.PetsAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.fragments.LoadingFragment;
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
public class PetCenterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PetCenterActivity";
    private RecyclerView petsRecycler;
    private LinearLayoutManager layoutManager;
    private LoadingFragment fragment;
    private PetsAdapter mAdapter;
    @Override
    protected int getContentViewResId() {
        return R.layout.actvity_pet_center;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.pet_center;
    }

    @Override
    protected int getTopBarRightImgRes() {
        return R.drawable.mainaddsmallf;
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
                VOPet pets = JsonUtils.object(json.toString(), VOPet.class);
                if (pets.code == 200) {
                    mAdapter = new PetsAdapter(PetCenterActivity.this,pets.data);
                    petsRecycler.setAdapter(mAdapter);
                }else {
                    ToastU.showShort(getApplicationContext(),pets.desc);
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
        switch (v.getId()){
            case R.id.top_bar_right_img:
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(PetCenterActivity.this,AddPetActivity.class);
                startActivity(intent);
                break;
        }
    }
}
