package com.example.qhs.wallpapershopping.network;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.qhs.wallpapershopping.AuthHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Admin {
    private String adminToken;
    private NetRequest request;
    private static Admin sInstance;

    private Admin(Context context){

        NetworkRequest request = new NetworkRequest();
        request.doLogin("mjavadi", "mhf9129380279", mLoginCallback);
//        Map<String, String> params = new HashMap<>();
//        params.put("username", "mjavadi");
//        params.put("password", "mhf9129380279");
//        request = new NetRequest(context);
//        request.JsonObjectNetRequest("POST", "jwt-auth/v1/token?username=mjavadi&password=mhf9129380279", mAdminLoginCallback, "no_need");
//        Calendar calendar = Calendar.getInstance();
//        Date currentTime = calendar.getTime();
//        calendar.add(Calendar.MONTH, 1);
//       // String monthString  = (String) DateFormat.format("MMM",  currentTime); // Jun
//
//        JSONObject user = new JSONObject();
//        try {
//            user.put("id","1");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        HashMap<String,JSONObject> data = new HashMap<>();
//        data.put("user",user);
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
//
//        String jws = "";
//
//            jws = Jwts.builder()
//                    .setHeaderParam("typ", "JWT")
//                    .setIssuer("http://mobifytech.ir")
//                    .setIssuedAt(currentTime)
//                    .setNotBefore(currentTime)
//                    .setExpiration(calendar.getTime())
//                  //  .setId("215c40f2-19ea-46ce-aa38-5064e2fb17c6")
//                    .claim("data", data)
//                    .signWith(key,SignatureAlgorithm.HS256)
//                    //.signWith(SignatureAlgorithm.HS256, "+$t+y+}uBMEP:`~!8,[IHo]@w.pa@/ujk/]|LM_1<+ln5eT!:R8 )75U}OnUR<3K".getBytes("UTF-8"))
//                    .compact();
//
//        Log.d("Admin JWS ", jws);
    }

    public static Admin getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Admin(context);
        }
        return sInstance;
    }

    private NetworkRequest.Callback<Token> mLoginCallback = new NetworkRequest.Callback<Token>() {
        @Override
        public void onResponse(@NonNull Token response) {
            adminToken = response.getIdToken();
        }

        @Override
        public void onError(String error) {
            Log.d("LOG_ADMIN ", error);
        }

        @Override
        public Class<Token> type() {
            return Token.class;
        }

    };

    private NetRequest.Callback<JSONObject> mAdminLoginCallback = new NetRequest.Callback<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {
            try {
                adminToken = response.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(String error) {

        }
    };

    public String getAdminAuth(){
        return adminToken;
    }
}
