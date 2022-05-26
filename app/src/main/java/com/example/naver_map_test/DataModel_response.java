package com.example.naver_map_test;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DataModel_response {

    @Expose
    @SerializedName("Branch")
    public String[] Branch;

    @Expose
    @SerializedName("Location")
    public String[] Location;

    @Expose
    @SerializedName("Latitude")
    public double[] Latitude;

    @Expose
    @SerializedName("Longitude")
    public double[] Longitude;

    @Expose
    @SerializedName("Branch_name")
    private String branchName;

    public double[] getLongitude() {
        return Longitude;
    }

    public double[] getLatitude() {
        return Latitude;
    }

    public String[] getBranch() {
        return Branch;
    }

    public String[] getLocation() {
        return Location;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
