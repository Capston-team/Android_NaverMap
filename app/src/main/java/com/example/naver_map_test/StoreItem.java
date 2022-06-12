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
    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
