package com.example.naver_map_test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("/conv/gs25_router")
    Call<DataModel_response> Test(@Query("latitude") double latitude, @Query("longitude") double longitude);

//    @POST("/capstone/capstone_router")
//    Call<List<DataModel_response>> call_request(@Body Send_request send_request);


    @GET("/capstone/store-address")
    Call<List<DataModel_response>> call_request(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("category") String category,
            @Query("carrier") String carrier,
            @Query("rate") String rate

    );


    @GET("/capstone/event")
    Call<eventDataModel_response> event_request(
            @Query("carrier") String carrier
    );
}
