package com.petm.property.volley;

import android.support.v4.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.petm.property.PetMApplication;
import com.petm.property.common.Constant;
import com.petm.property.fragments.LoadingFragment;
import com.petm.property.utils.CommonUtils;
import com.petm.property.utils.JsonUtils;
import com.petm.property.utils.RSAEncryptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Mr.liu
 * On 2016/7/11
 * At 18:10
 * VolleyUtils
 */
public class RequestManager {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(PetMApplication.getConText());
    private static RSAEncryptor rsaEncryptor;
    public RequestManager() {
    }

    /**
     * get请求 成功返回String
     * @param url 请求地址
     * @param tag
     * @param listener
     */
    public static void get(String url, Object tag, RequestListener listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET, url, null, responseListener(listener, false, null),
                responseError(listener, false, null));
        addRequest(request,tag);
    }

    /**
     * get请求 带进度
     * @param url 请求url
     * @param tag
     * @param progressTitle
     * @param listener
     */
    public static void get(String url, Object tag, String progressTitle, RequestListener listener){
        LoadingFragment dialog = new LoadingFragment();
        dialog.show(((FragmentActivity)tag).getSupportFragmentManager(), "Loading");
        dialog.setMsg(progressTitle);
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,url,null,responseListener(listener,true,dialog),
                responseError(listener,true,dialog));
        addRequest(request,tag);
    }

    /**
     * get请求返回对象
     * @param url
     * @param tag
     * @param classofT
     * @param listener
     * @param <T>
     */
    public static <T> void get(String url, Object tag, Class<T> classofT, RequestJsonListener<T> listener){
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,url,null,responseListener(listener,classofT,false,null),
               responseError(listener,false,null));
        addRequest(request,tag);
    }

    /**
     * get 带进度 返回对象
     * @param url
     * @param tag
     * @param classOfT
     * @param progressTitle
     * @param LoadingShow
     * @param listener
     * @param <T>
     */
    public static <T> void get(String url, Object tag, Class<T> classOfT,
                               String progressTitle, boolean LoadingShow, RequestJsonListener<T> listener){
        LoadingFragment dialog = new LoadingFragment();
        if (LoadingShow){
            dialog.show(((FragmentActivity)tag).getSupportFragmentManager(),"Loading");
            dialog.setMsg(progressTitle);
        }
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,url,null,
                responseListener(listener,classOfT,LoadingShow,dialog),
                responseError(listener,LoadingShow,dialog));
        addRequest(request,tag);
    }

    /**
     * post 请求返回String
     * @param url
     * @param tag
     * @param params
     * @param listener
     */
    public static void post(String url, Object tag, RequestParams params, RequestListener listener){
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,url,params,responseListener(listener,false,null),
                responseError(listener,false,null));
        addRequest(request,tag);
    }


    public static void post2(String url, Object tag, JSONObject params, RequestListener listener){
//        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,url,params,responseListener(listener,false,null),
//                responseError(listener,false,null));
//        try {
//            String tokens;
//            rsaEncryptor = new RSAEncryptor(Constant.RSA_PUBLIC_KEY, Constant.PKCS8_PRIVATE_KEY);
//            tokens = url+","+"com.wuxianying.gd720"+","+ CommonUtils.getTimeStamp()+","+PetMApplication.getDeviceIMEI();
//            params.put("token",rsaEncryptor.encryptWithBase64(tokens));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        JsonRequest<JSONObject> request1 = new JsonObjectRequest(Request.Method.POST,url,params,responseListener2(listener,false,null),
                responseError(listener,false,null));
        addRequest(request1,tag);
    }

    /**
     * post 请求带进度  返回String
     * @param url
     * @param tag
     * @param params
     * @param progressTitle
     * @param listener
     */
    public static void post(String url, Object tag, RequestParams params,
                            String progressTitle, RequestListener listener) {
        LoadingFragment dialog = new LoadingFragment();
        dialog.show(((FragmentActivity) tag).getSupportFragmentManager(),
                "Loading");
        dialog.setMsg(progressTitle);
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, true, dialog),
                responseError(listener, true, dialog));
        addRequest(request, tag);
    }

    /**
     * 返回对象带进度
     * @param url
     * @param tag
     * @param classOfT
     * @param params
     * @param progressTitle
     * @param LoadingShow
     * @param listener
     * @param <T>
     */
    public static <T> void post(String url, Object tag, Class<T> classOfT,
                                RequestParams params, String progressTitle, boolean LoadingShow,
                                RequestJsonListener<T> listener) {
        LoadingFragment dialog = new LoadingFragment();
        if (LoadingShow) {
            dialog.show(((FragmentActivity) tag).getSupportFragmentManager(),
                    "Loading");
            dialog.setMsg(progressTitle);
        }
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params,
                responseListener(listener, classOfT, LoadingShow, dialog),
                responseError(listener, LoadingShow, dialog));
        addRequest(request, tag);
    }

    /**
     * 成功监听返回对象
     * @param l
     * @param classOfT
     * @param flag
     * @param p
     * @param <T>
     * @return
     */
    protected static <T> Response.Listener<byte[]> responseListener(final RequestJsonListener<T> l,final Class<T> classOfT,
                                                                    final boolean flag,final LoadingFragment p){
        return new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                String data = null;
                try {
                    data = new String(response,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                l.requestSuccess(JsonUtils.object(data,classOfT));
                if (flag){
                    if (p.getShowsDialog()){
                        p.dismiss();
                    }
                }
            }
        };
    }

    public static Response.Listener<JSONObject> responseListener2(final RequestListener l, final boolean flag, final LoadingFragment p){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject data = null;
                data = response;
                l.requestSuccess(data);
                if (flag) {
                    if (p.getShowsDialog()) {
                        p.dismiss();
                    }
                }
            }
        };
    }

    /**
     * 成功消息监听 返回String
     *
     * @param l
     * @param flag
     * @param p
     * @return
     */
    protected static Response.Listener<byte[]> responseListener(final RequestListener l, final boolean flag, final LoadingFragment p) {
        return new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                String data = null;
                try {
                    data = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject object = new JSONObject();
                try {
                    l.requestSuccess(object.getJSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (flag) {
                    if (p.getShowsDialog()) {
                        p.dismiss();
                    }
                }
            }
        };
    }

    /**
     * 错误返回对象
     * @param l
     * @param flag
     * @param p
     * @param <T>
     * @return
     */
    protected static <T> Response.ErrorListener responseError(final RequestJsonListener<T> l,final boolean flag,
                                                               final LoadingFragment p){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.requestError(error);
                if (flag){
                    if (p.getShowsDialog()){
                        p.dismiss();
                    }
                }
            }
        };
    }

    /**
     * String  返回错误监听
     *
     * @param l    接口
     * @param flag true 带进度条  false 不带进度条
     * @param p    进度条的对象
     * @return
     */
    protected static Response.ErrorListener responseError(final RequestListener l, final boolean flag, final LoadingFragment p) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                l.requestError(error);
                if (flag) {
                    if (p.getShowsDialog()) {
                        p.dismiss();
                    }
                }
            }
        };
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    /**
     * 当主页面调用协议，在结束该页面是调用此方法
     *
     * @param tag
     */
    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}
