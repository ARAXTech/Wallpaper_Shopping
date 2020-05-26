package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.qhs.wallpapershopping.network.NetRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.qhs.wallpapershopping.network.Token;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class AuthHelper {

    private static final String PREFS = "prefs";
    private static final String PREF_ID = "pref_id";
    private static final String PREF_TOKEN = "pref_token";
    private static final String PREF_USERNAME = "pref_username";
    private static final String PREF_SHAREKEY_WISHLIST = "pref_sharekey";
    private SharedPreferences mPrefs;

    private NetRequest request;
    private static AuthHelper sInstance;

    private AuthHelper(@NonNull Context c) {
        mPrefs = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sInstance = this;
        request = new NetRequest(c);
    }

    public static AuthHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new AuthHelper(context);
        }
        return sInstance;
    }

    public void setIdToken(@NonNull Token token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        Log.d("Auth_token: ", token.getIdToken());
        editor.putString(PREF_TOKEN, token.getIdToken());
        Log.d("Auth_USERNAME: ", token.getUser_display_name());
        editor.putString(PREF_USERNAME, token.getUser_display_name());
        editor.apply();

        getUserId();
    }

    @Nullable
    public String getIdToken() {
        return mPrefs.getString(PREF_TOKEN, null);
    }

    public String getIdUser() {
        return mPrefs.getString(PREF_ID, null);
    }

    public String getSharekey() {
        return mPrefs.getString(PREF_SHAREKEY_WISHLIST, null);
    }

    public boolean isLoggedIn() {
        String token = getIdToken();

        return token != null;
    }

    /* Gets the username of the signed in user
     * @return - username of the signed in user
     */
    public String getUsername() {
        if (isLoggedIn()) {
            Log.d("IDtoken: ", getIdToken());
            return mPrefs.getString(PREF_USERNAME, null);
        }
        return null;
    }


    public void clear() {
        mPrefs.edit().clear().commit();
    }


    public void getUserId(){

        Log.d("ttoken: ", getIdToken());
        request.JsonObjectNetRequest("GET", "wp/v2/users/me", mUserIdCallback, getIdToken());
    }

    private NetRequest.Callback<JSONObject> mUserIdCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {

            try {
                int id = response.getInt("id");
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString(PREF_ID, String.valueOf(id));
                editor.apply();
                //Get Sharekey Wishlist
                request.JsonArrayNetRequest("GET", "wc/v3/wishlist/get_by_user/" + id, mWishlistCallback, getIdToken());

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {
        }
    };

    private NetRequest.Callback<JSONArray> mWishlistCallback = new NetRequest.Callback<JSONArray>() {
        @Override
        public void onResponse(@NonNull JSONArray response) {

            try {
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString(PREF_SHAREKEY_WISHLIST, response.getJSONObject(0).getString("share_key"));
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {
        }

    };

}