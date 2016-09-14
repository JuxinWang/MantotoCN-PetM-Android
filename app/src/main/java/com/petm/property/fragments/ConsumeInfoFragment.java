package com.petm.property.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petm.property.R;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 14:25
 * PetM
 */
public class ConsumeInfoFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_consume,null);
    }
}
