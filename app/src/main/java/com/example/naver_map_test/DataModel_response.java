package com.example.naver_map_test;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataModel_response {

    @SerializedName("Branch")
    @Expose
    private List<String> branch = null;  //지점명
    @SerializedName("Location")
    @Expose
    private List<String> location = null; // 주소
    @SerializedName("Latitude")
    @Expose
    private List<Double> latitude = null; //위도
    @SerializedName("Longitude")
    @Expose
    private List<Double> longitude = null; // 경도
    @SerializedName("Branch_name")
    @Expose
    private String branchName; //상호 ex)cu
    @SerializedName("Discount_rate")
    @Expose
    private Integer discountRate; // 할인율

    public List<String> getBranch() {
        return branch;
    }

    public List<String> getLocation() {
        return location;
    }

    public List<Double> getLatitude() {
        return latitude;
    }

    public List<Double> getLongitude() {
        return longitude;
    }

    public String getBranchName() {
        return branchName;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setBranch(List<String> branch) {
        this.branch = branch;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public void setLatitude(List<Double> latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(List<Double> longitude) {
        this.longitude = longitude;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }
}
