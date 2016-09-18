package com.petm.property.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.PetVaccinesAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.VOPetShop;
import com.petm.property.model.VOPetVaccines;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.ToastU;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 11:11
 * PetM
 */
public class AddPetActivity extends BaseActivity {
    private static final String TAG = "AddPetActivity";
    private View[] views ;
    private LoadingFragment fragment;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private PetVaccinesAdapter mAdapter;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_pet;
    }

    @Override
    protected void initViews() {
        super.initViews();
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        views = new View[]{findViewById(R.id.male),findViewById(R.id.female)};
        mRecyclerView = (RecyclerView) findViewById(R.id.pet_vaccines);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        loadDatas();
        setSelected(0);
        for (int i = 0; i < views.length; i++) {
            final int temp = i;
            views[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(temp);
                }
            });
        }
    }

    private void setSelected(int position) {
        for (int i = 0; i < views.length; i++) {
            if (i == position) {
                views[i].setSelected(true);
            } else {
                views[i].setSelected(false);
            }
        }
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("tag", "DOG");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(AddPetActivity.this, Constant.VACCIN_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
//                ToastU.showShort(MainActivity.this, json.toString());
                LogU.i(TAG, json.toString());
                VOPetVaccines petVaccines = JsonUtils.object(json.toString(), VOPetVaccines.class);
                if (petVaccines.code == 200) {
                    mAdapter = new PetVaccinesAdapter(AddPetActivity.this,petVaccines.data);
                    mRecyclerView.setAdapter(mAdapter);
                }else {
                    ToastU.showShort(AddPetActivity.this,petVaccines.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(AddPetActivity.this, error.getMessage());
            }
        });
    }
}
