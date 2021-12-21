package com.iliptam.adnetwork.api;

import com.iliptam.adnetwork.utils.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apiClient {

    public static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit==null) {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Global.API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
