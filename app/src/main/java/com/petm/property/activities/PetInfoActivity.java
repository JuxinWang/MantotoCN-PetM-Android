package com.petm.property.activities;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
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
public class PetInfoActivity extends BaseActivity {
    private static final String TAG = "PetInfoActivity";
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_remind;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.pet_info;
    }

    @Override
    protected void initViews() {
        super.initViews();
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(PetInfoActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(PetInfoActivity.this, Constant.REMIND_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(PetInfoActivity.this,error.getMessage());
            }
        });
    }
}
