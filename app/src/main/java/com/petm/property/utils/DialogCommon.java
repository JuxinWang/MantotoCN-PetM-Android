package com.petm.property.utils;

import android.app.Activity;
import android.content.Context;

import com.petm.property.dialog.PromptDialog;

/**
 * Created by Mr.liu
 * On 2016/9/20
 * At 11:40
 * PetM
 */
public class DialogCommon {
    /**
     *
     */
    public static void showPromoteDialog(final Context mContext,String title,String content,String confirm){
            new PromptDialog(mContext)
                    .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                    .setAnimationEnable(true)
                    .setTitleText(title)
                    .setContentText(content)
                    .setPositiveListener(confirm, new PromptDialog.OnPositiveListener() {
                        @Override
                        public void onClick(PromptDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
    }
}
