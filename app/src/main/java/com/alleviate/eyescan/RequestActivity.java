package com.alleviate.eyescan;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends AppCompatActivity {

    private APIService mAPIService;
    TextView textView, tv_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        textView = (TextView) findViewById(R.id.textView);
        tv_count = (TextView) findViewById(R.id.count);

        SharedPreferences spf = getSharedPreferences("Config", Context.MODE_PRIVATE);

        int count = spf.getInt("Attend",0);

        tv_count.setText("Total Workers today "+count);


        mAPIService = ApiUtils.getAPIService();

        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            upload_file();
        } else {
            Toast.makeText(getApplicationContext(),"Connection Failed...",Toast.LENGTH_SHORT).show();
        }
    }

    private void upload_file() {

        File file = new File(getExternalFilesDir(null), "scan_img.jpg");

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("scanner", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "user_name");

        Call<ResponseBody> req = mAPIService.upload_image(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {

                    showResponse(response.body().toString());
                    Log.i("Response", "Post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("Request", "Unable to submit post to API. :: "+t.toString());

            }
        });
    }

    public void showResponse(String response) {

        textView.setText("Success !");

        SharedPreferences spf = getSharedPreferences("Config", Context.MODE_PRIVATE);

        int count = spf.getInt("Attend",0);

        SharedPreferences.Editor spf_edit = spf.edit();
        count = count + 1;

        spf_edit.putInt("Attend", count);
        spf_edit.commit();

        tv_count.setText("Total Workers today "+count);
    }
}
