package com.petm.property.volley;

import com.android.volley.VolleyError;

/**
 * Created by Mr.liu
 * On 2016/7/12
 * At 17:00
 * VolleyUtils
 */
public interface RequestJsonListener<T> {
    void requestSuccess(T result);//成功
    void requestError(VolleyError error);
}
