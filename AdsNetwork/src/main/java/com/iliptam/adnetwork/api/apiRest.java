package com.iliptam.adnetwork.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface apiRest {

    @GET("api/get-campaign/{API_KEY}")
    Call<List<AdCampaign>> getCampaignsById(@Path("API_KEY") int apiKey);

    @FormUrlEncoded
    @POST("api/update-campaign?")
    Call<ApiResponse> updateCampaign(@Field("id") int id,
                                     @Field("impression") int ad_impression,
                                     @Field("click") int ad_click,
                                     @Field("device_info") String device_info,
                                     @Field("country") String country,
                                     @Field("language") String language);

}
