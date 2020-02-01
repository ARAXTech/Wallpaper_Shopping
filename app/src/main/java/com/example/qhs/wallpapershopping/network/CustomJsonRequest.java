package com.example.qhs.wallpapershopping.network;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.example.qhs.wallpapershopping.AuthHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class CustomJsonRequest<T> extends JsonRequest<JSONArray> {

    //private Map<String, String> headers;
    private JSONObject mRequestObject;
    private Response.Listener<JSONArray> mResponseListener;
    //private AuthHelper mAuthHelper;
    //private Context context;

    public CustomJsonRequest(int method, String url, JSONObject requestObject, Context c, Response.Listener<JSONArray> responseListener,  Response.ErrorListener errorListener) {
        super(method, url, (requestObject == null) ? null : requestObject.toString(), responseListener, errorListener);
        mRequestObject = requestObject;
        mResponseListener = responseListener;
        //context = c;
        //mAuthHelper = AuthHelper.getInstance(context);
    }


    @Override
    protected void deliverResponse(JSONArray response) {
        mResponseListener.onResponse(response);
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        //Map<String, String> params
//        headers = new HashMap<String, String>();
//        headers.put("Content-Type", "application/json");
//        headers.put("Authorization", "Bearer " + mAuthHelper.getIdToken());
//        return headers;
//    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            try {
                return Response.success(new JSONArray(json),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                return Response.error(new ParseError(e));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}
