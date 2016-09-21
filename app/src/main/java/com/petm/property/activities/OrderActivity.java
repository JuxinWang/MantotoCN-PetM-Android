package com.petm.property.activities;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.BeauticianAdapter;
import com.petm.property.adapter.ServicesAdapter;
import com.petm.property.common.Constant;
import com.petm.property.model.VOBeautician;
import com.petm.property.model.VOPetService;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.ToastU;
import com.petm.property.views.SpaceItemDecoration;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.liu
 * On 2016/9/21
 * At 10:56
 * PetM
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "OrderActivity";
    private long petshopid;
    private RecyclerView petServiceRecyclerView,beauticianRecyclerView;
    private GridLayoutManager gridLayoutManger;
    private GridLayoutManager beauticianLayoutManger;
    private ServicesAdapter serviceAdapter;
    private BeauticianAdapter beauticianAdapter;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_order_service;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.order_service;
    }

    @Override
    protected void initViews() {
        super.initViews();
        petshopid = getIntent().getExtras().getLong("petshopid");
        petServiceRecyclerView = (RecyclerView) findViewById(R.id.service_recyclerview);
        beauticianRecyclerView = (RecyclerView) findViewById(R.id.beautician);
        gridLayoutManger = new GridLayoutManager(this,3);
        beauticianLayoutManger = new GridLayoutManager(this,3);
        petServiceRecyclerView.setLayoutManager(gridLayoutManger);
        beauticianRecyclerView.setLayoutManager(beauticianLayoutManger);
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("petshopid",petshopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(OrderActivity.this, Constant.PETSERVICE_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
                VOPetService petServices = JsonUtils.object(json.toString(),VOPetService.class);
                if (petServices.code == 200){
                    serviceAdapter = new ServicesAdapter(OrderActivity.this,petServices.data);
                    petServiceRecyclerView.setAdapter(serviceAdapter);
                }else {
                    ToastU.showShort(OrderActivity.this,petServices.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(OrderActivity.this,error.getMessage());
            }
        });

        IRequest.postJson(OrderActivity.this, Constant.BEAUTICIAN_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
                VOBeautician beauticians = JsonUtils.object(json.toString(),VOBeautician.class);
                if (beauticians.code == 200){
                    beauticianAdapter = new BeauticianAdapter(OrderActivity.this,beauticians.data);
                    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
                    beauticianRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
                    beauticianRecyclerView.setAdapter(beauticianAdapter);
                }else {
                    ToastU.showShort(OrderActivity.this,beauticians.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(OrderActivity.this,error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
