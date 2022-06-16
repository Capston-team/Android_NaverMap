package com.example.naver_map_test;

public class StoreItem {
    int resourceId;
    String title;
    String message;
    String distance;

    public StoreItem(int resourceId,String title, String message, String distance) {
        this.title = title;
        this.message= message;
        this.distance=distance;
        this.resourceId=resourceId;
    }
    public int getResourceId() {
        return resourceId;
    }
    public String getTitle() {
        return title;
    }
    public String getMessage() {
        return message;
    }
    public String getDistance() {
        return distance;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
}
