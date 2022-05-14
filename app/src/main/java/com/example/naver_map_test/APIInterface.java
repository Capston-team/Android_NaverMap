package com.example.naver_map_test;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/conv/gs25_router")
    Call<DataModel_response> Test(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @POST("/capstone/capstone_router")
    Call<DataModel_response> call_request(@Body Send_request[] send_request);

//    @POST("/meal/meal_router")
//    Call<DataModel_response> getMEALData(@Body Send_request send_request);
//
//    @POST("/cafe/cafe_router")
//    Call<DataModel_response> getCAFEData(@Body Send_request send_request);



}
