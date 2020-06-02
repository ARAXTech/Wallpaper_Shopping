package com.example.qhs.wallpapershopping.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Model.Order;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Network Request class to abstract implementation details of requests
 */
public class NetworkRequest {
    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
    private static final String BASE_URL = "http://mobifytech.ir/wp-json/";

    private Callback mCallback;

    private OkHttpClient mClient;

    public NetworkRequest() {
        mClient = new OkHttpClient();
    }

    /**
     * Sets the callback for the network request
     * @param callback
     */
    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    /**
     * Login
     *
     * @param username - username
     * @param password - password
     * @param callback - callback
     */
    public void doLogin(@NonNull String username,
                        @NonNull String password,
                        Callback callback) {
        setCallback(callback);

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        String loginUrl = BASE_URL + "jwt-auth/v1/token";
        doPostRequest(loginUrl, params, callback);
    }

    /**
     * Sign up
     *
     * @param username - username
     * @param password - password
     * @param email - email
     * @param callback - callback
     */
    public void doSignUp(@NonNull String email,
                         @NonNull String username,
                         @NonNull String password,
                         @Nullable Callback callback) {
        setCallback(callback);

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("username", username);
        params.put("password", password);

        String signUpUrl = BASE_URL + "wp/v2/users/register";
        doPostRequest(signUpUrl, params, callback);
    }

    /**
     * Get protected quote
     *
     * @param token - token
     * @param callback - callback
     */
    public void doGetProtectedQuote(@NonNull String token, @Nullable Callback callback) {
        setCallback(callback);

        String protectedQuoteUrl = BASE_URL + "wc/v3/products/categories";
        doGetRequestWithToken(protectedQuoteUrl, new HashMap<String, String>(), token, callback);
    }

    /**
     * Execute post request
     *
     * @param url
     * @param params
     * @param callback
     */
    private void doPostRequest(@NonNull String url, @NonNull Map<String, String> params,
                               @Nullable final Callback callback) {

        HttpUrl httpUrl = HttpUrl.parse(url);

        JSONObject jsonObject = new JSONObject();


        //FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (String string : params.keySet()) {
            try {
                jsonObject.put(string, params.get(string));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //bodyBuilder.addEncoded(string, params.get(string));
            //Log.d(string, params.get(string));
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); //or HS384 or HS512
//
//        String token = Jwts.builder() // (1)
//                .setSubject("register")      // (2)
//                .signWith(key)          // (3)
//                .compact();

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                //.post(bodyBuilder.build())
                .build();

        doRequest(request, callback);
    }

    private void doGetRequestWithToken(@NonNull String url, @NonNull Map<String, String> params,
                                       @Nullable String token, @Nullable Callback callback) {
        HttpUrl httpUrl = HttpUrl.parse(url);

        HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
        Log.d("paramsss", "ooo");
        for (String key : params.keySet()) {
            Log.d(key, params.get(key));
            urlBuilder.addQueryParameter(key, params.get(key));
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.build())
                .get();

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
        }

        doRequest(requestBuilder.build(), callback);
    }

    public void orderPostRequest(@NonNull String url, @NonNull Order order,
                               @Nullable final Callback callback) throws JSONException {

        HttpUrl httpUrl = HttpUrl.parse(url);

        Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject(gson.toJson(order));

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); //or HS384 or HS512
//
//        String token = Jwts.builder() // (1)
//                .setSubject("register")      // (2)
//                .signWith(key)          // (3)
//                .compact();

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                //.post(bodyBuilder.build())
                .build();

        doRequest(request, callback);
    }

    /**
     * Makes request and fires callback as at when due
     *
     * @param request
     * @param callback
     */
    private void doRequest(@NonNull Request request, final Callback callback) {
        mClient.newCall(request)
                .enqueue(new okhttp3.Callback() {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        Log.d("onFailureDoRequest: ", e.toString());
                        if (callback != null) {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError(e.toString());
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(final Call call, final Response response) {

                        if(callback != null) {
                            try {
                                final String stringResponse = response.body().string();
                                Log.d("onResponseDoRequest: ", stringResponse);
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Object res = buildObjectFromResponse(stringResponse,
                                                callback.type());
                                        if (res != null) {
                                            callback.onResponse(res);
                                        } else {
                                            callback.onError(stringResponse);
                                        }
                                    }
                                });
                            } catch (final IOException ioe) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(ioe.toString());
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private Object buildObjectFromResponse(String response, Class cls) {
        if (cls == String.class) {
            return response;
        } else {
            try {
                return new Gson().fromJson(response, cls);
            } catch (JsonSyntaxException jse) {
                return null;
            }
        }
    }

    /**
     * Callback interface for network response and error
     * @param <T>
     */
    public interface Callback<T> {
        void onResponse(@NonNull T response);
        void onError(String error);
        Class<T> type();
    }

    /**
     * ApiResponse interface
     */
    public interface ApiResponse {
        String string();
    }
}
