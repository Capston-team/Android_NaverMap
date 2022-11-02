package com.example.naver_map_test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterfaceTest {

    @GET("/capstone/store-address")
    Call<List<DataModel_response_Test>> call_request(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("category") String category,
            @Query("carrier") String carrier,
            @Query("rate") String rate

    );
}
