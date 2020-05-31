package com.example.qhs.wallpapershopping.network;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.qhs.wallpapershopping.AuthHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class NetRequest {
    private JsonObjectRequest objectRequest;
    private JsonArrayRequest arrayRequest;
    private StringRequest stringRequest;

    private RequestQueue queue;
    private AuthHelper mAuthHelper;
    private int method;
    private static final String BASE_URL = "http://mobifytech.ir/wp-json/";

    public NetRequest(Context c) {
        queue = newRequestQueue(c);
        mAuthHelper = AuthHelper.getInstance(c);
    }

    public void JsonObjectNetRequest(String method, String endpoint, final Callback callback, String token){
        if ( method == "GET" ){
            this.method = com.android.volley.Request.Method.GET;
        }
        else if ( method == "POST" ){
            this.method = Request.Method.POST;
        }

        objectRequest = new JsonObjectRequest(this.method, BASE_URL + endpoint, null, response -> {
            if (response != null & callback != null) {
                callback.onResponse(response);
            }
        }, error -> {

            // As of f605da3 the following should work
            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    JSONObject obj = new JSONObject(res);
                    if (obj != null){
                    callback.onResponse(obj);}
                } catch (UnsupportedEncodingException e1) {
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                } catch (JSONException e2) {
                    // returned data is not JSONObject?
                    e2.printStackTrace();
                }
            }else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Log.e("NoConnectionError", error.getMessage());
            } else if (error instanceof AuthFailureError) {
                Log.e("AuthFailureError", error.getMessage());
            } else if (error instanceof NetworkError) {
                Log.e("NetworkError", error.getMessage());
            } else if (error instanceof ParseError) {
                Log.e("ParseError", error.getMessage());
            }

        }){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                if (token != "no_need"){
                    if (token == null)
                        params.put("Authorization", "Bearer " + mAuthHelper.getIdToken());
                    else
                        params.put("Authorization", "Bearer " + token);
                }

                return params;
            }

        };

        objectRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(objectRequest);


    }


    public void JsonArrayNetRequest(String method, String endpoint, final Callback callback, String token){
        if (method == "GET"){
            this.method = com.android.volley.Request.Method.GET;
        }
        else if (method == "POST"){
            this.method = Request.Method.POST;
        }

        arrayRequest = new JsonArrayRequest(this.method, BASE_URL + endpoint, null, response -> {
            if (response != null) {
                callback.onResponse(response);
            } else {
                callback.onError(response.toString());
            }

        }, error -> {

            NetworkResponse response = error.networkResponse;
            if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    JSONArray obj = new JSONArray(res);
                    if (obj!=null){
                    callback.onResponse(obj);}
                } catch (UnsupportedEncodingException e1) {
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                } catch (JSONException e2) {
                    // returned data is not JSONObject?
                    e2.printStackTrace();
                }
            }else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Log.e("NoConnectionError", error.getMessage());
            } else if (error instanceof AuthFailureError) {
                Log.e("AuthFailureError", error.getMessage());
            } else if (error instanceof NetworkError) {
                Log.e("NetworkError", error.getMessage());
            } else if (error instanceof ParseError) {
                Log.e("ParseError", error.getMessage());
            }

        }){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                if (token != "no_need"){
                    if (token == null)
                        params.put("Authorization", "Bearer " + mAuthHelper.getIdToken());
                    else
                        params.put("Authorization", "Bearer " + token);
                }

                return params;
            }

        };

        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(arrayRequest);

    }

    public void JsonStringNetRequest(String method, String endpoint) {
        if (method == "POST"){
            this.method = com.android.volley.Request.Method.GET;
        }
        else if (method == "DELETE"){
            this.method = Request.Method.DELETE;
        }

        stringRequest = new StringRequest(this.method, BASE_URL + endpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Message of StringReq ", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        Log.d("delete message ", res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("NoConnectionError", error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    Log.e("AuthFailureError", error.getMessage());
                } else if (error instanceof NetworkError) {
                    Log.e("NetworkError", error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.e("ParseError", error.getMessage());
                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + mAuthHelper.getIdToken());
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    /**
     * Callback interface for network response and error
     * @param <T>
     */
    public interface Callback<T> {
        void onResponse(@NonNull T response);
        void onError(String error);
    }

}

