package com.alleviate.eyescan;

/**
 * Created by Shirish Kadam on 1/4/17.
 * Logged in as felix.
 * www.shirishkadam.com
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://5hirish.pythonanywhere.com/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
