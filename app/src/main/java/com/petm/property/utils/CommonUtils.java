package com.petm.property.utils;

import android.widget.EditText;

/**
 * Created by liudongdong on 2016/8/4.
 */
public class CommonUtils {
    /**
     * EditText 点击EditText 编辑框删除hint预设字
     * @param editText
     * @param hasFocus
     */
    public static void deleteHint(EditText editText, boolean hasFocus) {
        String hint;
        if (hasFocus){
            hint = editText.getHint().toString();
            editText.setTag(hint);
            editText.setHint("");
        }else {
            hint = editText.getTag().toString();
            editText.setHint(hint);
        }
    }

    /**
     * 同时判断多个输入框
     * @param editText
     * @return
     */
    public static boolean isEmptys(EditText... editText){
        for (EditText et: editText) {
            if (et.getText() == null)
                return true;
            if (et.getText().toString().trim().equals(""));
                return true;
        }
        return false;
    }

    /**
     * 判断单个输入框
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText){
        if (editText.getText() == null){
            return true;
        }
        if (editText.getText().toString().trim().equals("")){
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static long getTimeStamp(){
        long timeStamp = System.currentTimeMillis();
        return timeStamp;
    }
}
