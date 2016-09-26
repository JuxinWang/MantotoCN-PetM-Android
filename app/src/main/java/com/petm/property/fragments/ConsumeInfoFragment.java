package com.petm.property.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.ConsumeAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.model.VOUserGift;
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
 * At 14:25
 * PetM
 */
public class ConsumeInfoFragment extends BaseFragment {
    private static final String TAG = "ConsumeInfoFragment";
    private LinearLayoutManager layoutManager;
    private ConsumeAdapter consumeAdapter;
    private RecyclerView consumeRecycler;
    private TextView mNoConsume;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_consume,null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mNoConsume = (TextView) mContext.findViewById(R.id.no_consume);
        consumeRecycler = (RecyclerView) mContext.findViewById(R.id.consume_recyclerview);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        consumeRecycler.setLayoutManager(layoutManager);
    }

    @Override
    protected void onLoadData() {
        super.onLoadData();
        JSONObject object = new JSONObject();
        try {
            object.put("userid", LocalStore.getUserid(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(mContext, Constant.USER_GIFT_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
                VOUserGift userGifts = JsonUtils.object(json.toString(),VOUserGift.class);
                if (userGifts.code == 200){
                    if (userGifts.data.size()>0){
                        mNoConsume.setVisibility(View.GONE);
                        consumeAdapter = new ConsumeAdapter(mContext,userGifts.data);
                        consumeRecycler.setAdapter(consumeAdapter);
                    }
                }else if(userGifts.code == 201){
                    mNoConsume.setVisibility(View.VISIBLE);
                }else{
                    ToastU.showShort(mContext,userGifts.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(mContext,error.getMessage());
            }
        });
    }
}
