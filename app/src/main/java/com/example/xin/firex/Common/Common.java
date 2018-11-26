package com.example.xin.firex.Common;

import com.example.xin.firex.remote.IGoogleAPI;
import com.example.xin.firex.remote.RetrofitClient;

public class Common {

    public static final String user_location_tb1 = "Users";  // store user's location
    public static final String user_info_tb1 = "UsersInformation";  // store users's all information including name,phone,email
    public static final String date_request_tb1 = "DateRequest";


    public static final String baseURL = "https://googleapis.com";
    public static IGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getRetrofit(baseURL).create(IGoogleAPI.class);
    }
}
