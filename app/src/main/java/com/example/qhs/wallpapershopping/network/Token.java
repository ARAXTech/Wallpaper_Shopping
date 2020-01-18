package com.example.qhs.wallpapershopping.network;

import com.google.gson.annotations.SerializedName;

public class Token implements NetworkRequest.ApiResponse {
    //@SerializedName("id_token")
    private String token;
    @SerializedName("user_display_name")
    private String userName;

    public String getIdToken() {
        return token;
    }

    public String getUser_display_name() {
        return userName;
    }

    @Override
    public String string() {
        return token;
    }
}
