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

        if (token!=null)
            Log.d("token NEt ", token);

        objectRequest = new JsonObjectRequest(this.method, BASE_URL + endpoint, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    callback.onResponse(response);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        callback.onResponse(obj);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

//                error.printStackTrace();
//
//                //VolleyLog.d("Error:", error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Log.e("NoConnectionError", error.getMessage());
//                } else if (error instanceof AuthFailureError) {
//                    Log.e("AuthFailureError", error.getMessage());
//                } else if (error instanceof ServerError) {
//                    callback.onError(error.networkResponse.toString());
//                    Log.e("VOLLEY_ServerError", error.toString());
//                } else if (error instanceof NetworkError) {
//                    Log.e("NetworkError", error.getMessage());
//                } else if (error instanceof ParseError) {
//                    Log.e("ParseError", error.getMessage());
//                }

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

        arrayRequest = new JsonArrayRequest(this.method, BASE_URL + endpoint, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    callback.onResponse(response);
                } else {
                    callback.onError(response.toString());
                }

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONArray obj = new JSONArray(res);
                        callback.onResponse(obj);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }


                //VolleyLog.d("Error:", error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("NoConnectionError", error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    Log.e("AuthFailureError", error.getMessage());
                } else if (error instanceof ServerError) {
                    // Log.e("ServerError", error.getMessage());
                    callback.onError(error.toString());
                } else if (error instanceof NetworkError) {
                    Log.e("NetworkError", error.getMessage());
                } else if (error instanceof ParseError) {
                    Log.e("ParseError", error.getMessage());
                }

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
                Log.d("DELETE Message ", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("Error:", error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.e("NoConnectionError", error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    Log.e("AuthFailureError", error.getMessage());
                } else if (error instanceof ServerError) {
                    Log.e("ServerError", error.getMessage());
                    // callback.onError(error.toString());
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

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("cart_item_key", key);
//                return params;
//            }
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

    /**
     * ApiResponse interface
     */
    public interface ApiResponse {
        String string();
    }
}

