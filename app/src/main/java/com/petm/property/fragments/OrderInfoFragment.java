package com.petm.property.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
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
import com.petm.property.adapter.OrderAdapter;
import com.petm.property.adapter.OrderAdapter.OnCancelOrderListener;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.dialog.ColorDialog;
import com.petm.property.model.NetInfo;
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
 * At 14:26
 * PetM
 */
public class OrderInfoFragment extends BaseFragment{
    private static final String TAG = "OrderInfoFragment";
    private TextView mNoOrder;
    private RecyclerView orderRecycler;
    private LinearLayoutManager layoutManager;
    private OrderAdapter mAdapter;
    private LoadingFragment loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_order,null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        loading = new LoadingFragment();
        mNoOrder = (TextView) mContext.findViewById(R.id.no_order);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecycler = (RecyclerView) mContext.findViewById(R.id.order_recyclerview);
        orderRecycler.setLayoutManager(layoutManager);
    }

    @Override
    protected void onLoadData() {
        super.onLoadData();
        JSONObject object = new JSONObject();
        try {
            object.put("keeperid", LocalStore.getKeeperid(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(mContext, Constant.REMIND_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG,json.toString());
                VOPetWorkOrder petWorkOrders = JsonUtils.object(json.toString(),VOPetWorkOrder.class);
                if (petWorkOrders.code == 200){
                    if (petWorkOrders.data.size()>0){
                        mAdapter = new OrderAdapter(mContext,petWorkOrders.data);
                        orderRecycler.setAdapter(mAdapter);
                        mAdapter.setmOnCancelOrderListener(new OnCancelOrderListener() {
                            @Override
                            public void OnCancelOrder(final long workorderid) {
                                ColorDialog dialog = new ColorDialog(mContext);
                                dialog.setTitle("取消预约");
                                dialog.setTitleTextColor(Color.BLACK);
                                dialog.setContentText("您确定取消该预约么？")
                                        .setColor(getResources().getColor(R.color.main_color))
                                        .setContentTextColor(getResources().getColor(R.color.black))
                                        .setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                            @Override
                                            public void onClick(ColorDialog dialog) {
                                                JSONObject object = new JSONObject();
                                                try {
                                                    object.put("workorderid", workorderid);
                                                    object.put("comment", "取消预约");
                                                    object.put("operatorid",LocalStore.getKeeperid(mContext));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                IRequest.postJson(mContext, Constant.PET_WORK_CANCEL, object, new RequestListener() {
                                                    @Override
                                                    public void requestSuccess(JSONObject json) {
                                                        NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                                                        if (netInfo.code == 200) {
                                                            onLoadData();
                                                            ToastU.showShort(mContext, "取消成功");
                                                        }else {
                                                            ToastU.showShort(mContext, netInfo.desc);
                                                        }
                                                    }

                                                    @Override
                                                    public void requestError(VolleyError error) {
                                                        ToastU.showShort(mContext, error.getMessage());
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
                        });
                    }else {
                        mNoOrder.setVisibility(View.VISIBLE);
                    }
                } else if(petWorkOrders.code == 201){
                    mNoOrder.setVisibility(View.VISIBLE);
                }else {
                    mNoOrder.setVisibility(View.VISIBLE);
                    ToastU.showShort(mContext,petWorkOrders.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(mContext,error.getMessage());
            }
        });
    }

    public void OnCancelOrder(final long workorderid) {
        ColorDialog dialog = new ColorDialog(mContext);
        dialog.setTitle("取消预约");
        dialog.setTitleTextColor(Color.BLACK);
        dialog.setContentText("您确定取消该预约么？")
                .setColor(getResources().getColor(R.color.main_color))
                .setContentTextColor(getResources().getColor(R.color.black))
                .setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("workorderid", workorderid);
                            object.put("comment", "取消预约");
                            object.put("operatorid",LocalStore.getKeeperid(mContext));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        IRequest.postJson(mContext, Constant.PET_WORK_CANCEL, object, new RequestListener() {
                            @Override
                            public void requestSuccess(JSONObject json) {
                                NetInfo netInfo = JsonUtils.object(json.toString(), NetInfo.class);
                                if (netInfo.code == 200) {
                                    ToastU.showShort(mContext, "取消成功");
                                } else {
                                    ToastU.showShort(mContext, netInfo.desc);
                                }
                            }

                            @Override
                            public void requestError(VolleyError error) {
                                ToastU.showShort(mContext, error.getMessage());
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
