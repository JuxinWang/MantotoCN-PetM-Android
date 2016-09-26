package com.petm.property.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.PetsAdapter;
import com.petm.property.adapter.PetsWorkOrderAdapter;
import com.petm.property.adapter.PetshopAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.VOPet;
import com.petm.property.model.VOPetShop;
import com.petm.property.model.VOPetWorkOrder;
import com.petm.property.utils.JsonUtils;
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
    private LinearLayout addPetShopLL,petshopsLL;
    private RecyclerView petShopsRecyclerView;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;
    private PetshopAdapter petshopAdapter;
    private RecyclerView petsRecycler;
    private LoadingFragment fragment;
    private PetsWorkOrderAdapter petsAdapter;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        super.initViews();
        addPetshop = (ImageView) findViewById(R.id.add_petshop_img);
        mPetshops = (RecyclerView) findViewById(R.id.petshops_recycler);
        addPetShopLL = (LinearLayout) findViewById(R.id.add_petshop);
        petshopsLL = (LinearLayout) findViewById(R.id.pet_shops);
        petShopsRecyclerView = (RecyclerView) findViewById(R.id.petshops_recycler);
        petsRecycler = (RecyclerView) findViewById(R.id.pet_work_order);
        layoutManager = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        petShopsRecyclerView.setLayoutManager(layoutManager);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        petsRecycler.setLayoutManager(layoutManager2);
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(),"Loading");
        loadDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDatas();
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(MainActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(MainActivity.this, Constant.PET_SHOP_GET, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
//                ToastU.showShort(MainActivity.this, json.toString());
                LogU.i(TAG, json.toString());
                VOPetShop petShops = JsonUtils.object(json.toString(), VOPetShop.class);
                if (petShops.code == 200) {
                    if (petShops.data.size() == 0) {
                        addPetShopLL.setVisibility(View.VISIBLE);
                    } else {
                        petshopsLL.setVisibility(View.VISIBLE);
                        petshopAdapter = new PetshopAdapter(MainActivity.this, petShops.data);
                        petShopsRecyclerView.setAdapter(petshopAdapter);
                    }
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(MainActivity.this, error.getMessage());
            }
        });

        JSONObject object2 = new JSONObject();
        try {
            object2.put("keeperid", LocalStore.getKeeperid(MainActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(MainActivity.this, Constant.PET_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                fragment.dismiss();
//                ToastU.showShort(PetCenterActivity.this, json.toString());
                LogU.i(TAG, json.toString());
                final VOPet pets = JsonUtils.object(json.toString(), VOPet.class);
                if (pets.code == 200) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("keeperid",LocalStore.getKeeperid(MainActivity.this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    IRequest.postJson(MainActivity.this, Constant.REMIND_WORKO_ORDER_GET_BY_KEEPER, obj, new RequestListener() {
                        @Override
                        public void requestSuccess(JSONObject json) {
                            LogU.i(TAG,json.toString());
                            VOPetWorkOrder workOrders = JsonUtils.object(json.toString(),VOPetWorkOrder.class);
                            if (workOrders.code == 200){
                                petsAdapter = new PetsWorkOrderAdapter(MainActivity.this, pets.data ,workOrders.data);
                                petsRecycler.setAdapter(petsAdapter);
                            }else if(workOrders.code == 201) {
                                petsAdapter = new PetsWorkOrderAdapter(MainActivity.this, pets.data ,workOrders.data);
                                petsRecycler.setAdapter(petsAdapter);
                            }else {
                                ToastU.showShort(MainActivity.this,workOrders.desc);
                            }
                        }

                        @Override
                        public void requestError(VolleyError error) {
                            ToastU.showShort(MainActivity.this,error.getMessage());
                        }
                    });
                }else {
                    ToastU.showShort(getApplicationContext(),pets.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(MainActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (v.getId()){
            case R.id.add_petshop_img:
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.add_petshop_two:
                intent.setClass(MainActivity.this, AddPetshopActivity.class);
                startActivity(intent);
                break;
            case R.id.petcenter:
                intent.setClass(MainActivity.this,PetCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.personcenter:
                intent.setClass(MainActivity.this,PersonCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.remind:
                intent.setClass(MainActivity.this,RemindActivity.class);
                startActivity(intent);
                break;
        }
    }
}
