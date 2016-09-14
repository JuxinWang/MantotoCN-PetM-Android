package com.petm.property.activities;

import com.petm.property.R;

/**
 * Created by Mr.liu
 * On 2016/9/14
 * At 10:05
 * PetM
 */
public class PetCenterActivity extends BaseActivity {
    @Override
    protected int getContentViewResId() {
        return R.layout.actvity_pet_center;
    }

    @Override
    protected int getTopBarTextRes() {
        return R.string.pet_center;
    }

    @Override
    protected int getTopBarRightImgRes() {
        return R.drawable.mainaddsmallf;
    }
}
