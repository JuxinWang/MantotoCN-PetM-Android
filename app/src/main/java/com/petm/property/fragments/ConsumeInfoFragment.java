package com.petm.property.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.utils.LogU;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 14:25
 * PetM
 */
public class ConsumeInfoFragment extends BaseFragment {
    private static final String TAG = "ConsumeInfoFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_consume,null);
    }

    @Override
    protected void onLoadData() {
        super.onLoadData();
        JSONObject object = new JSONObject();
        try {
            object.put("userid", 95619221592286L);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(mContext, Constant.USER_GIFT_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
            }

            @Override
            public void requestError(VolleyError error) {

            }
        });
    }
}
