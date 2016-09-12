package com.petm.property.utils;

import com.google.gson.Gson;

/**
 * Created by Mr.liu
 * On 2016/7/12
 * At 17:18
 * VolleyUtils
 */
public class JsonUtils {
    private static Gson gson = new Gson();
    public static <T> T object(String json, Class<T> classOfT){
        return gson.fromJson(json,classOfT);
    }
    public static <T> String toJson(Class<T> param){
        return gson.toJson(param);
    }
}
