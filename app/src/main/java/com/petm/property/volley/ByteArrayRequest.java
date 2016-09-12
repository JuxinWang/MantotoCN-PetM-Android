package com.petm.property.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.liu
 * On 2016/7/12
 * At 14:33
 * VolleyUtils
 */
public class ByteArrayRequest extends Request<byte[]> {
    private Response.Listener<byte[]> mListener = null;
    private Object mPostBody = null;
    private HttpEntity httpEntity = null;

    public ByteArrayRequest(int method,String url,Object postBody,Response.Listener<byte[]>listener,Response.ErrorListener errorListener){
        super(method,url,errorListener);
        this.mPostBody = postBody;
        this.mListener = listener;
        if (this.mPostBody!=null&&this.mPostBody instanceof RequestParams){
            this.httpEntity = ((RequestParams) this.mPostBody).getEntity();
        }
    };

    /**
     *  mPostBody is null or Map<String, String>, then execute this method
     * @return
     * @throws AuthFailureError
     */
    protected Map<String,String> getParams() throws AuthFailureError
    {
        if (this.httpEntity == null && this.mPostBody !=null && this.mPostBody instanceof Map<?,?>)
        {
            return (Map<String, String>) this.mPostBody;
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String>headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap()))
        {
            headers = new HashMap<String,String>();
        }
        return super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mPostBody != null && this.mPostBody instanceof String)
        {
            String postString = (String) mPostBody;
            if (postString.length() !=0)
            {
                try {
                    return postString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else
            {
                return null;
            }
        }
        if (this.httpEntity != null)//process as MultipartRequestparams
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return baos.toByteArray();
        }
        return super.getBody();//mPostBody is null or Map<String ,String>
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        this.mListener.onResponse(response);
    }
}
