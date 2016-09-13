package com.petm.property.activities;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.utils.LogU;
import com.petm.property.utils.ToastU;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView mPetshops;
    private ImageView addPetshop;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        loadDatas();
        addPetshop = (ImageView) findViewById(R.id.add_petshop_img);
        mPetshops = (RecyclerView) findViewById(R.id.petshops_recycler);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid",15721584006722L);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(MainActivity.this, Constant.PET_SHOP_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                ToastU.showShort(MainActivity.this,json.toString());
                LogU.i(TAG,json.toString());
            }

            @Override
            public void requestError(VolleyError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_petshop_img:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
