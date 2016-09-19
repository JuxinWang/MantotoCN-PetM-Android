package com.petm.property.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.petm.property.R;
import com.petm.property.utils.ToastU;
import com.petm.property.views.OptionsPopupWindow;
import com.petm.property.views.TimePopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 14:24
 * PetM
 */
public class MyInfoFragment extends BaseFragment implements View.OnClickListener{
    /**
     * 日期和性别选择
     */
    private TimePopupWindow pwTime;
    private OptionsPopupWindow pwOptions;
    private ArrayList<String> options1Items = new ArrayList<String>();
    private EditText personName,personSex,personBirth;
    private Button save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_mine,null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        personName = (EditText) mView.findViewById(R.id.person_name);
        personBirth = (EditText) mView.findViewById(R.id.person_birthday);
        personSex = (EditText) mView.findViewById(R.id.person_sex);
        save = (Button) mView.findViewById(R.id.save);
        //      时间选择器
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        pwTime = new TimePopupWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setRange(1900,calendar.get(Calendar.YEAR));
        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                personBirth.setText(getTime(date));
            }
        });
        //选项选择器
        pwOptions = new OptionsPopupWindow(getActivity());
        //选项1
        options1Items.add("男");
        options1Items.add("女");
        pwOptions.setPicker(options1Items);
        pwOptions.setSelectOptions(0);
        pwOptions.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                personSex.setText(options1Items.get(options1));
            }
        });
        save.setOnClickListener(this);
        personBirth.setOnClickListener(this);
        personSex.setOnClickListener(this);
    }
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_sex:
                pwOptions.showAtLocation(personSex, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.person_birthday:
                pwTime.showAtLocation(personBirth, Gravity.BOTTOM, 0, 0, new Date());
                break;
            case R.id.save:
                ToastU.showShort(getActivity(),"提交");
                break;
        }
    }
}
