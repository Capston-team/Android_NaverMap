package com.example.naver_map_test;


import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class fragment_home1_InstrumentedTest {

    private final Context context = ApplicationProvider.getApplicationContext();
    private final Double latitude = 37.555945;
    private final Double longitude = 126.97231666666667;
    private final String category = "CONV";
    private final String carrier = "SKT";
    private final String rate = "VIP";

    public static final String fake_BASE_URL = "https://where-we-go.herokuapp.com/capstone/store-address/";
    private Gson gson = new GsonBuilder().setLenient().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(fake_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private APIInterfaceTest apiInterfaceTest = retrofit.create(APIInterfaceTest.class);

    @Before
    public void setUp() {
        String carrier = PreferenceUtil.getCarrierPreferences(context, "carrier");
        String rate = PreferenceUtil.getRatePreferences(context, "rate");
    }

    @Test
    public void HttpRetrofitUnitTest() {
        Call<List<DataModel_response_Test>> fake_call = apiInterfaceTest.call_request(latitude, longitude, category, carrier, rate);
        fake_call.clone().enqueue(new Callback<List<DataModel_response_Test>>() {

            @Override
            public void onResponse(Call<List<DataModel_response_Test>> call, Response<List<DataModel_response_Test>> response) {
                if(response.isSuccessful()) {
                    Log.d("fragment_home1 retrofit 테스트", response.toString());
                    assert response.body() != null;
                    Log.d("fragment_home1 retrofit 테스트", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<DataModel_response_Test>> call, Throwable t) {

            }
        });
    }


}
