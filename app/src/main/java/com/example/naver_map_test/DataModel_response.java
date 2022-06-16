package com.example.naver_map_test;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataModel_response {

    @SerializedName("Branch")
    @Expose
    private ArrayList<String> branch = null;  //지점명
    @SerializedName("Location")
    @Expose
    private ArrayList<String> location = null; // 주소
    @SerializedName("Latitude")
    @Expose
    private ArrayList<Double> latitude = null; //위도
    @SerializedName("Longitude")
    @Expose
    private ArrayList<Double> longitude = null; // 경도
    @SerializedName("Branch_name")
    @Expose
    private String branchName; //상호 ex)cu
    @SerializedName("Discount_rate")
    @Expose
    private Integer discountRate; // 할인율

    public ArrayList<String> getBranch() {
        return branch;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public ArrayList<Double> getLatitude() {
        return latitude;
    }

    public ArrayList<Double> getLongitude() {
        return longitude;
    }

    public String getBranchName() {
        return branchName;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setBranch(ArrayList<String> branch) {
        this.branch = branch;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }

    public void setLatitude(ArrayList<Double> latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(ArrayList<Double> longitude) {
        this.longitude = longitude;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }
}
