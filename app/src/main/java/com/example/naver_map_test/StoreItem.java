package com.example.naver_map_test;

public class StoreItem {
    int resourceId;
    String title;
    String message;

    public StoreItem(int resourceId,String title, String message) {
        this.title = title;
        this.message= message;
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

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
