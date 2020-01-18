package com.example.qhs.wallpapershopping;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.example.qhs.wallpapershopping.network.Token;


import java.util.ArrayList;
import java.util.Date;

import javax.crypto.SecretKey;

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
    private SharedPreferences mPrefs;

    private static AuthHelper sInstance;

    private AuthHelper(@NonNull Context context) {
        mPrefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sInstance = this;
    }

    public static AuthHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new AuthHelper(context);
        }
        return sInstance;
    }

    public void setIdToken(@NonNull Token token) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_TOKEN, token.getIdToken());
        editor.putString(PREF_USERNAME, token.getUser_display_name());
        // Log.d("USERNAMEEEE: ", token.getUser_display_name());
        editor.apply();
    }

    @Nullable
    public String getIdToken() {
        return mPrefs.getString(PREF_TOKEN, null);
    }

    public boolean isLoggedIn() {
        String token = getIdToken();

        // Log.d("token: ", token);
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

        String jws = Jwts.builder()

                .setIssuer(jwt.getIssuer())
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
}
