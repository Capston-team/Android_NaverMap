package com.example.naver_map_test;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataModel_response {

    @SerializedName("Branch")
    @Expose
    private List<String> branch = null;
    @SerializedName("Location")
    @Expose
    private List<String> location = null;
    @SerializedName("Latitude")
    @Expose
    private List<Double> latitude = null;
    @SerializedName("Longitude")
    @Expose
    private List<Double> longitude = null;
    @SerializedName("Branch_name")
    @Expose
    private String branchName;
    @SerializedName("Discount_rate")
    @Expose
    private Integer discountRate;

    public List<String> getBranch() {
        return branch;
    }

    public void setBranch(List<String> branch) {
        this.branch = branch;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public List<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(List<Double> latitude) {
        this.latitude = latitude;
    }

    public List<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(List<Double> longitude) {
        this.longitude = longitude;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }
}
