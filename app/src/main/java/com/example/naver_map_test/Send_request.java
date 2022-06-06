package com.example.naver_map_test;

import java.util.HashMap;

public class Send_request {

    // 현재 위도 경도
    double latitude;
    double longitude;
    // conv, meal, cafe
    String category;
    // 통신사
    String carrier;
    // 등급
    String rate;

    public Send_request(double latitude, double longitude, String category, String carrier, String rate) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.carrier = carrier;
        this.rate = rate;
    }

}

