package com.petm.property.activities;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.AddPetshopAdapter;
import com.petm.property.adapter.PetshopAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.dialog.ColorDialog;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.NetInfo;
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
 * At 11:18
 * PetM
 */
public class AddPetshopActivity extends BaseActivity implements OnClickListener, AddPetshopAdapter.OnDeletePetshopListener {
    private static final String TAG = "AddPetshopActivity";
    private String inviteCode;
    private EditText mEditText;
    private Button commit;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManage;
    private AddPetshopAdapter petshopAdapter;
    private LoadingFragment fragment;

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
        mLayoutManage = new LinearLayoutManager(this);
        mLayoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManage);
        inviteCode = mEditText.getText().toString();
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(AddPetshopActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(AddPetshopActivity.this, Constant.PET_SHOP_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
                VOPetShop petShops = JsonUtils.object(json.toString(), VOPetShop.class);
                if (petShops.code == 200) {
                    petshopAdapter = new AddPetshopAdapter(AddPetshopActivity.this, petShops.data);
                    mRecyclerView.setAdapter(petshopAdapter);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(AddPetshopActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit:
                ColorDialog dialog = new ColorDialog(AddPetshopActivity.this);
                dialog.setTitle("添加宠物店");
                dialog.setContentText("您确定添加该宠物店么？")
                        .setColor(getResources().getColor(R.color.main_color))
                        .setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                fragment = new LoadingFragment();
                                fragment.show(getSupportFragmentManager(), "Loading");
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("keeperid", LocalStore.getKeeperid(AddPetshopActivity.this));
                                    object.put("code", "15045412899");
                                    LogU.i(TAG, inviteCode);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                IRequest.postJson(AddPetshopActivity.this, Constant.PETSHOP_RELATE, object, new RequestListener() {
                                    @Override
                                    public void requestSuccess(JSONObject json) {
                                        LogU.i(TAG, json.toString());
                                        NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                                        if (netInfo.code == 200) {
                                            ToastU.showShort(AddPetshopActivity.this, "添加成功");
                                            fragment.dismiss();
                                            loadDatas();
                                        } else {
                                            fragment.dismiss();
                                            ToastU.showShort(AddPetshopActivity.this, netInfo.desc);
                                        }
                                    }

                                    @Override
                                    public void requestError(VolleyError error) {
                                        fragment.dismiss();
                                        ToastU.showShort(AddPetshopActivity.this, error.getMessage());
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
                break;
        }
    }

    @Override
    public void OnDeletePetshop(final long petshopid) {
        ColorDialog dialog = new ColorDialog(AddPetshopActivity.this);
        dialog.setTitle("删除宠物店");
        dialog.setTitleTextColor(Color.BLACK);
        dialog.setContentText("您确定删除该宠物店么？")
                .setColor(getResources().getColor(R.color.main_color))
                .setContentTextColor(getResources().getColor(R.color.black))
                .setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        fragment = new LoadingFragment();
                        fragment.show(getSupportFragmentManager(), "Loading");
                        JSONObject object = new JSONObject();
                        try {
                            object.put("petshopid", petshopid);
                            object.put("keeperid", LocalStore.getKeeperid(AddPetshopActivity.this));
                            object.put("isshow", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        IRequest.postJson(AddPetshopActivity.this, Constant.PETSHOP_DELETE, object, new RequestListener() {
                            @Override
                            public void requestSuccess(JSONObject json) {
                                NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                                if (netInfo.code == 200) {
                                    fragment.dismiss();
                                    ToastU.showShort(AddPetshopActivity.this, "删除成功");
                                    loadDatas();
                                } else {
                                    fragment.dismiss();
                                    ToastU.showShort(AddPetshopActivity.this, netInfo.desc);
                                }
                            }

                            @Override
                            public void requestError(VolleyError error) {
                                fragment.dismiss();
                                ToastU.showShort(AddPetshopActivity.this, error.getMessage());
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
