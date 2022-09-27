package com.example.naver_map_test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class eventDataModel_response {

    @SerializedName("title")
    @Expose
    private List<String> title = null;

    @SerializedName("date")
    @Expose
    private List<String> date = null;

    @SerializedName("img")
    @Expose
    private List<String> img = null;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
