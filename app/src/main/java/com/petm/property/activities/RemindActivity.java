package com.petm.property.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.RemindAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.model.VOPetWorkOrder;
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
public class RemindActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "RemindActivity";
    private LinearLayoutManager layoutManager;
    private RecyclerView remindRevyvler;
    private TextView noRmind;
    private RemindAdapter mAdapter;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_remind;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.remind;
    }

    @Override
    protected void initViews() {
        super.initViews();
        noRmind = (TextView) findViewById(R.id.no_remind);
        remindRevyvler = (RecyclerView) findViewById(R.id.remind_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        remindRevyvler.setLayoutManager(layoutManager);
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(RemindActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(RemindActivity.this, Constant.REMIND_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
                VOPetWorkOrder workOrders = JsonUtils.object(json.toString(),VOPetWorkOrder.class);
                if (workOrders.code == 200){
                    if (workOrders.data.size()>0){
                        mAdapter = new RemindAdapter(RemindActivity.this,workOrders.data);
                        remindRevyvler.setAdapter(mAdapter);
                    }
                }else if(workOrders.code == 201){
                    noRmind.setVisibility(View.VISIBLE);
                }else {
                    ToastU.showShort(RemindActivity.this,workOrders.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(RemindActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_bar_left_img:
                finish();
                break;
        }
    }
}
