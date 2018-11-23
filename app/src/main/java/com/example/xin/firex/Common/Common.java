package com.example.xin.firex.Common;

import com.example.xin.firex.remote.IGoogleAPI;
import com.example.xin.firex.remote.RetrofitClient;

public class Common {
    public static final String baseURL = "https://googleapis.com";
    public static IGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getRetrofit(baseURL).create(IGoogleAPI.class);
    }
}
