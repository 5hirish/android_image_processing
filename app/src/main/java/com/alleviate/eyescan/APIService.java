package com.alleviate.eyescan;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Shirish Kadam on 1/4/17.
 * Logged in as felix.
 * www.shirishkadam.com
 */

public interface APIService {

    @POST("/upload/")
    @Multipart
    //@FormUrlEncoded
    Call<ResponseBody> upload_image(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @POST("/result/")
    //@FormUrlEncoded
    Call<ScanResp> req_res(@Body ScanResp request);
}
