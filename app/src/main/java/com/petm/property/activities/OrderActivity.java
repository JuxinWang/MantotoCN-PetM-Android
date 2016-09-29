package com.petm.property.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.petm.property.R;
import com.petm.property.adapter.BeauticianAdapter;
import com.petm.property.adapter.OrderAdapter;
import com.petm.property.adapter.ServicesAdapter;
import com.petm.property.common.Constant;
import com.petm.property.common.LocalStore;
import com.petm.property.dialog.ColorDialog;
import com.petm.property.dialog.PromptDialog;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.model.VOBeautician;
import com.petm.property.model.VOPetService;
import com.petm.property.utils.DateHelper;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.LogU;
import com.petm.property.utils.ToastU;
import com.petm.property.views.FullyGridLayoutManager;
import com.petm.property.views.SpaceItemDecoration;
import com.petm.property.views.TimePopupWindow;
import com.petm.property.volley.IRequest;
import com.petm.property.volley.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mr.liu
 * On 2016/9/21
 * At 10:56
 * PetM
 */
public class OrderActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OrderActivity";
    private long petshopid;
    private long petid;
    private long keeperid;
    private long beauticianid = 0;
    private long servicetime = 0;
    private long serviceid = 0;
    private JSONArray workorder_array;
    private RecyclerView petServiceRecyclerView, beauticianRecyclerView;
    private FullyGridLayoutManager gridLayoutManger;
    private FullyGridLayoutManager beauticianLayoutManger;
    private ServicesAdapter serviceAdapter;
    private BeauticianAdapter beauticianAdapter;
    private TimePopupWindow pwTime;
    private TextView dateTime, week;
    private long baseTime;
    private LinearLayout mlayout;
    private boolean showTime = true;
    private TextView ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen;
    private LoadingFragment fragment;
    private String petname;
    private String petOrderTime;
    private String serviceName;

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
        fragment = new LoadingFragment();
        petshopid = getIntent().getExtras().getLong("petshopid");
        petid = getIntent().getExtras().getLong("petid");
        petname = getIntent().getExtras().getString("petname");
        workorder_array = new JSONArray();
        dateTime = (TextView) findViewById(R.id.service_datetime);
        week = (TextView) findViewById(R.id.weekends);
        mlayout = (LinearLayout) findViewById(R.id.service_show_ll);
        mlayout.setVisibility(View.GONE);
        ten = (TextView) findViewById(R.id.ten);
        eleven = (TextView) findViewById(R.id.eleven);
        twelve = (TextView) findViewById(R.id.twenlve);
        thirteen = (TextView) findViewById(R.id.thirteen);
        fourteen = (TextView) findViewById(R.id.fourteen);
        fifteen = (TextView) findViewById(R.id.fifteen);
        sixteen = (TextView) findViewById(R.id.sixteen);
        seventeen = (TextView) findViewById(R.id.seventeen);
        eighteen = (TextView) findViewById(R.id.eighteen);
        petServiceRecyclerView = (RecyclerView) findViewById(R.id.service_recyclerview);
        beauticianRecyclerView = (RecyclerView) findViewById(R.id.beautician);
        gridLayoutManger = new FullyGridLayoutManager(this, 3);
        beauticianLayoutManger = new FullyGridLayoutManager(this, 3);
        petServiceRecyclerView.setLayoutManager(gridLayoutManger);
        beauticianRecyclerView.setLayoutManager(beauticianLayoutManger);
        loadDatas();
        //      时间选择器
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        baseTime = (new Date(System.currentTimeMillis())).getTime() / 1000;
        dateTime.setText(DateHelper.getTimes(new Date(System.currentTimeMillis())));
        pwTime = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(2000, calendar.get(Calendar.YEAR));
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                week.setText(DateHelper.getWeekends(date));
                dateTime.setText(DateHelper.getTimes(date));
                baseTime = date.getTime() / 1000;
                LogU.i(TAG, "" + baseTime);
            }
        });
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        fragment = new LoadingFragment();
        fragment.show(getSupportFragmentManager(), "Loading");
        JSONObject object = new JSONObject();
        try {
            object.put("petshopid", petshopid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IRequest.postJson(OrderActivity.this, Constant.PETSERVICE_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG, json.toString());
                fragment.dismiss();
                final VOPetService petServices = JsonUtils.object(json.toString(), VOPetService.class);
                if (petServices.code == 200) {
                    if (LocalStore.getIsRemind(OrderActivity.this)) {
                        ColorDialog dialog = new ColorDialog(OrderActivity.this);
                        dialog.setTitle("预约提醒");
                        dialog.setTitleTextColor(Color.BLACK);
                        dialog.setContentTextColor(Color.BLACK);
                        dialog.setContentText(petServices.Description)
                                .setColor(getResources().getColor(R.color.main_color))
                                .setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).setNegativeListener("不在提醒", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                LocalStore.setIsRemind(OrderActivity.this,false);
                                dialog.dismiss();
                            }
                        }).show();
                    }
                    serviceAdapter = new ServicesAdapter(OrderActivity.this, petServices.data);
                    petServiceRecyclerView.setAdapter(serviceAdapter);
                    serviceAdapter.setOnItemClickLitener(new ServicesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            serviceid = petServices.data.get(position).petserviceid;
                            serviceAdapter.selectPosition(position);
                            serviceAdapter.notifyDataSetChanged();
                            serviceName = petServices.data.get(position).petservicename;
                        }
                    });
                } else {
                    ToastU.showShort(OrderActivity.this, petServices.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                fragment.dismiss();
                ToastU.showShort(OrderActivity.this, error.getMessage());
            }
        });


        IRequest.postJson(OrderActivity.this, Constant.BEAUTICIAN_GET_ALL, object, new RequestListener() {
            @Override
            public void requestSuccess(JSONObject json) {
                LogU.i(TAG, json.toString());
                final VOBeautician beauticians = JsonUtils.object(json.toString(), VOBeautician.class);
                if (beauticians.code == 200) {
                    beauticianAdapter = new BeauticianAdapter(OrderActivity.this, beauticians.data);
                    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
                    beauticianRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
                    beauticianRecyclerView.setAdapter(beauticianAdapter);
                    beauticianAdapter.setOnItemClickLitener(new BeauticianAdapter.OnItemClickLister() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            beauticianid = beauticians.data.get(position).beauticianid;
                            beauticianAdapter.selectPosition(position);
                            beauticianAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    ToastU.showShort(OrderActivity.this, beauticians.desc);
                }
            }

            @Override
            public void requestError(VolleyError error) {
                ToastU.showShort(OrderActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (serviceid == 0) {
                    ToastU.showShort(OrderActivity.this, "请选择服务项");
                    return;
                }
                if (servicetime == 0) {
                    ToastU.showShort(OrderActivity.this, "请选择预约时间");
                    return;
                }
                if (beauticianid == 0) {
                    ToastU.showShort(OrderActivity.this, "请选择美容师");
                    return;
                }
                fragment.show(getSupportFragmentManager(), "Loading");
                try {
                    workorder_array.put(0, beauticianid + "-" + servicetime + "-" + serviceid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject object = new JSONObject();
                try {
                    object.put("petshopid", petshopid);
                    object.put("petid", petid);
                    object.put("keeperid", LocalStore.getKeeperid(OrderActivity.this));
                    object.put("workorder_array", workorder_array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                IRequest.postJson(OrderActivity.this, Constant.PET_WORK_CREATE, object, new RequestListener() {
                    @Override
                    public void requestSuccess(JSONObject json) {
                        LogU.i(TAG, json.toString());
                        fragment.dismiss();
                        new PromptDialog(OrderActivity.this)
                                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                .setAnimationEnable(true)
                                .setTitleText("预约成功")
                                .setContentText("您成功为" + petname + "预约" + DateHelper.getStringTime("" + servicetime/1000, "yyyy年MM月dd日") + "的" + serviceName + "服务请您按时到宠物店！")
                                .setPositiveListener("确定", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {
                                        Intent intent = new Intent();
                                        intent.setClass(OrderActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                    @Override
                    public void requestError(VolleyError error) {
                        fragment.dismiss();
                        ToastU.showShort(OrderActivity.this, error.getMessage());
                    }
                });
                break;
            case R.id.service_date:
                pwTime.showAtLocation(dateTime, Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.show_time:
                if (showTime) {
                    mlayout.setVisibility(View.VISIBLE);
                    showTime = false;
                } else {
                    mlayout.setVisibility(View.GONE);
                    showTime = true;
                }
                break;
            case R.id.ten:
                initColor();
                ten.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 10 * 60 * 60 + baseTime;
                break;
            case R.id.eleven:
                initColor();
                eleven.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 11 * 60 * 60 + baseTime;
                break;
            case R.id.twenlve:
                initColor();
                twelve.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 12 * 60 * 60 + baseTime;
                break;
            case R.id.thirteen:
                initColor();
                thirteen.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 13 * 60 * 60 + baseTime;
                break;
            case R.id.fourteen:
                initColor();
                fourteen.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 14 * 60 * 60 + baseTime;
                break;
            case R.id.fifteen:
                initColor();
                fifteen.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 15 * 60 * 60 + baseTime;
                break;
            case R.id.sixteen:
                initColor();
                sixteen.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 16 * 60 * 60 + baseTime;
                break;
            case R.id.seventeen:
                initColor();
                seventeen.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 17 * 60 * 60 + baseTime;
                break;
            case R.id.eighteen:
                initColor();
                eighteen.setTextColor(getResources().getColor(R.color.main_color));
                servicetime = 18 * 60 * 60 + baseTime;
                break;
            case R.id.top_bar_left_img:
                finish();
                break;
        }
    }

    private void initColor() {
        ten.setTextColor(getResources().getColor(R.color.black));
        eleven.setTextColor(getResources().getColor(R.color.black));
        twelve.setTextColor(getResources().getColor(R.color.black));
        thirteen.setTextColor(getResources().getColor(R.color.black));
        fourteen.setTextColor(getResources().getColor(R.color.black));
        fifteen.setTextColor(getResources().getColor(R.color.black));
        sixteen.setTextColor(getResources().getColor(R.color.black));
        seventeen.setTextColor(getResources().getColor(R.color.black));
        eighteen.setTextColor(getResources().getColor(R.color.black));
    }

}
