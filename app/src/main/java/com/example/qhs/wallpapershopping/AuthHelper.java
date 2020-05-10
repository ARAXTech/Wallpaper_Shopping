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

    /* Key for username in the jwt claim
     */
    private static final String JWT_KEY_USERNAME = "data";
    private static final String PREFS = "prefs";
    private static final String PREF_TOKEN = "pref_token";
    private static final String PREF_USERNAME = "pref_username";
    private static final String PREF_ID = "pref_id";
    private static final String PREF_SHAREKEY_WISHLIST = "pref_sharekey";
    private SharedPreferences mPrefs;

    //private Context context;
    private NetRequest request;
    private static AuthHelper sInstance;

    private AuthHelper(@NonNull Context c) {
        //context = c;
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

        setUserId();
        //getSharekeyWishlist();
    }



    @Nullable
    public String getIdToken() {
        return mPrefs.getString(PREF_TOKEN, null);
    }


    public String getUserId() {
        String d= mPrefs.getString(PREF_ID, null);
        return d;
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
            //return decodeUsername(getIdToken());
        }
        return null;
    }


    @Nullable
    private String decodeUsername(String token) {
        JWT jwt = new JWT(token);
        Gson gson = null;
        //  Map<String, Claim> allClaims = jwt.getClaims();



        Date date = jwt.getExpiresAt();
        date.setMonth(date.getMonth()+1);

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512

        String jws = Jwts.builder().setIssuer(jwt.getIssuer())
                .setSubject("Bob")
                .setAudience("you")
                .setExpiration(date) //a java.util.Date
                .setNotBefore(jwt.getNotBefore()) //a java.util.Date
                .setIssuedAt(jwt.getIssuedAt()) // for example, now
                //  .setId(UUID.randomUUID()) ;//just an example id
                //   .setClaims(allClaims)
                // .claim("data", gson.toJson(jwt.getClaim("data")))

                .signWith(key) // <---

                .compact();

        try {

            Claim claim = jwt.getClaim("data");//claim != null

            if ( claim != null ) {

                //JSONObject jsonObject = gson.fromJson(allClaims., JSONObject.class);jsonObject.toString()
                //Log.d("dataClaim: ", claim.asString() );//

                ArrayList<Claim> list = new ArrayList<>(claim.asList(Claim.class));

                /// JsonObject[] arrayList = claim.asArray(JsonObject.class);
                Log.d("dataClaim: ", String.valueOf(claim.asInt()));

                // List<JSONObject> dataClaim = claim.asList(JSONObject.class);
                Log.d("size of list: ", String.valueOf(list.size()));
                Gson gson1 = null;

                Log.d("NEW TOKEN: ", jws);
                return jwt.getClaim(JWT_KEY_USERNAME).asString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        mPrefs.edit().clear().commit();
    }


    public void setUserId(){

        request.JsonObjectNetRequest("GET", "wp/v2/users/me", mUserIdCallback, null);
    }

    private NetRequest.Callback<JSONObject> mUserIdCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {

            try {
                Log.d("knrlknfrl", String.valueOf(response.getInt("id")));
//                SharedPreferences.Editor editor = mPrefs.edit();
//                editor.putString(PREF_ID, String.valueOf(response.getInt("id")));
//                editor.apply();

                request.JsonArrayNetRequest("GET", "wc/v3/wishlist/get_by_user/" + String.valueOf(response.getInt("id")), mWishlistCallback, null);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {
        }
    };

    private void getSharekeyWishlist() {
        request.JsonArrayNetRequest("GET", "wc/v3/wishlist/get_by_user/" + getUserId(), mWishlistCallback, null);


    }

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