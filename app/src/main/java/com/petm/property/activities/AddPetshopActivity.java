package com.petm.property.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 11:18
 * PetM
 */
public class AddPetshopActivity extends BaseActivity implements OnClickListener{
    private static final String TAG = "AddPetshopActivity";
    private String inviteCode;
    private EditText mEditText;
    private Button commit;
    private RecyclerView mRecyclerView;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_petshop;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.add_pet_shop;
    }

    @Override
    protected void initViews() {
        super.initViews();
        commit = (Button) findViewById(R.id.commit);
        mEditText = (EditText) findViewById(R.id.invite_code);
        mRecyclerView = (RecyclerView) findViewById(R.id.petshop_recyclerview);
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(AddPetshopActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(AddPetshopActivity.this, Constant.PET_SHOP_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {

            }

            @Override
            public void requestError(VolleyError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commit:
                JSONObject object = new JSONObject();
                try {
                    object.put("keeperid",LocalStore.getKeeperid(AddPetshopActivity.this));
                    object.put("code",inviteCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                IRequest.postJson(AddPetshopActivity.this, Constant.PETSHOP_RELATE, object, new RequestListener() {
                    @Override
                    public void requestSuccess(JSONObject json) {

                    }

                    @Override
                    public void requestError(VolleyError error) {

                    }
                });
                break;
        }
    }
}
